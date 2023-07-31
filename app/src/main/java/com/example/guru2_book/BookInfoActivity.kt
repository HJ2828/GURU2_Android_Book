package com.example.guru2_book

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BookInfoActivity : AppCompatActivity() {

    // 기본 정보 변수
    private var userEmail : String? = null // 사용자 이메일
    private var profileNum : Int = 0 // 사용자 프로필 번호
    private var curGoalNum : Int = 0 // 현재 목표 번호
    private var goalBookCount : Int = 0 // 완독해야 하는 책 수
    private var curBookCount : Int = 0 // 현재 완독한 책 수

    // 위젯 변수
    lateinit var btnBackBook: ImageButton // 뒤로가기 버튼
    lateinit var btnDibs: ImageButton // 찜 버튼
    lateinit var imgBook: ImageView // 표지
    lateinit var textTitle: TextView
    lateinit var textAuthor: TextView
    lateinit var textPublisher: TextView
    lateinit var textPubDate: TextView
    lateinit var textStory: TextView
    lateinit var btnFinish: ImageButton // 완독 버튼
    lateinit var btnWrite: ImageButton // 독후감 작성
    lateinit var btnRead: ImageButton // 독후감 보기

    // 책 정보 변수
    var title : String? = null  // 책 제목
    var author : String? = null // 작가
    var publisher : String? = null // 출판사
    var pubDate : String? = null // 출간일
    var imgUrl : String? = null // 표지
    var story : String? = null // 줄거리
    var isbn : String? = null // isbn

    // 데이터베이스 변수
    lateinit var dbManager: DBManager
    lateinit var bookDB : SQLiteDatabase

    // 기타 변수
    var isDibs : Boolean = false // 찜 상태
    var isFinished : Boolean = false // 완독 상태
    var fromShelf : Boolean = false // 책장에서 넘어옴


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_info)

        dbManager = DBManager(this, "BookDB", null, 1) // SQLiteOpenHelper 객체 생성

        // 위젯 연결
        btnBackBook = findViewById(R.id.btnBackBook)
        btnDibs = findViewById(R.id.btnDibs)
        imgBook = findViewById(R.id.imgBook)
        textTitle = findViewById(R.id.textTitle)
        textAuthor = findViewById(R.id.textAuthor)
        textPublisher = findViewById(R.id.textPublisher)
        textPubDate = findViewById(R.id.textPubDate)
        textStory = findViewById(R.id.textStory)
        btnFinish = findViewById(R.id.btnFinish)
        btnWrite = findViewById(R.id.btnWrite)
        btnRead = findViewById(R.id.btnRead)

        // 사용자 이메일 및 책 정보, 사용자 현재 목표 번호 받아오기
        bookDB = dbManager.readableDatabase
        var cursor : Cursor
        userEmail = intent.getStringExtra("USEREMAIL")
        if(intent.hasExtra("bookTitle")){
            title = intent.getStringExtra("bookTitle")
            author = intent.getStringExtra("bookAuthor")
            publisher = intent.getStringExtra("bookPublisher")
            pubDate = intent.getStringExtra("bookPubDate")
            imgUrl = intent.getStringExtra("bookImgUrl")
            story = intent.getStringExtra("bookStory")
            isbn = intent.getStringExtra("bookISBN")
        } else if(intent.hasExtra("BOOKISBN")){
            isbn = intent.getStringExtra("BOOKISBN")
            fromShelf = intent.getBooleanExtra("FROMSHELF", false)
            cursor = bookDB.rawQuery("SELECT BName, BWriter, BPublisher, BPubdate, Bimage, BDescription FROM Book WHERE ISBN = '$isbn';", null)
            if(cursor.moveToNext()){
                title = cursor.getString(0).toString()
                author = cursor.getString(1).toString()
                publisher = cursor.getString(2).toString()
                pubDate = cursor.getString(3).toString()
                imgUrl = cursor.getString(4).toString()
                story = cursor.getString(5).toString()
            }
        }

        cursor = bookDB.rawQuery("SELECT ACurrentProfile FROM Account WHERE AEmail = '$userEmail';", null) // 사용자 프로필 정보
        if(cursor.moveToNext()){
            profileNum = cursor.getInt(0)
        }

        cursor = bookDB.rawQuery("SELECT GoalNum FROM Profile WHERE PEmail = '$userEmail' AND PNum = $profileNum;", null) // 목표 번호
        if(cursor.moveToNext()){
            curGoalNum = cursor.getInt(0)
        }
        cursor = bookDB.rawQuery("SELECT GCount FROM Goal WHERE GNum = $curGoalNum;", null) // 목표 책 수
        if(cursor.moveToNext()){
            goalBookCount = cursor.getInt(0)
        }

        cursor = bookDB.rawQuery("SELECT * FROM Read WHERE REmail = '$userEmail' AND RNum = $profileNum;", null) // 완독한 책 수
        curBookCount = cursor.count

        cursor.close()
        bookDB.close()

        getUserInfo() // 찜 상태, 완독 상태 가져오기

        // 찜 상태, 완독 상태 설정
        if(isDibs){
            btnDibs.setImageResource(R.drawable.bookinfo_btndibs) // 찜 상태 이미지
        } else {
            btnDibs.setImageResource(R.drawable.bookinfo_btnnotdibs) // 찜 해제 이미지
        }

        if(isFinished){
            btnFinish.setImageResource(R.drawable.bookinfo_btnfinish) // 완독 이미지
        } else {
            btnFinish.setImageResource(R.drawable.bookinfo_btnnotfinish) // 완독 해제 이미지
        }

        // 받아온 값을 위젯에 설정
        Glide.with(this).load(imgUrl).into(imgBook)
        textTitle.text = title
        textAuthor.text = author
        textPublisher.text = publisher
        textPubDate.text = pubDate
        textStory.text = story

        setQuoteData() // 쌍따옴표, 따옴표 처리

        // 리스너 설정
        btnBackBook.setOnClickListener { // 뒤로가기 버튼
            if(fromShelf){ // 책장에서 넘어온 경우 인텐트로 이동
                val shelfIntent = Intent(this, MainActivity::class.java)
                shelfIntent.putExtra("FROMSHELF", true)
                shelfIntent.putExtra("USEREMAIL", userEmail)
                startActivity(shelfIntent)
            }
            finish()
        }

        btnDibs.setOnClickListener { // 찜 버튼
            if(isDibs){ // 찜 상태
                if(checkBookTable() <= 1) { // 1명 이하(본인만 있는 경우)
                    bookDB.execSQL("DELETE FROM Book WHERE ISBN = '$isbn';")
                }
                // 찜 목록에서 해당 사용자의 정보 삭제
                bookDB.execSQL("DELETE FROM Want WHERE WISBN = '$isbn' AND WEmail = '$userEmail' AND WNum = $profileNum;")

                btnDibs.setImageResource(R.drawable.bookinfo_btnnotdibs) // 찜 해제 이미지
                isDibs = false
            } else { // 찜 상태 아님
                if(checkBookTable() <= 0) { // 아무도 없을 때
                    bookDB.execSQL("INSERT INTO Book VALUES ('$isbn', '$title', '$author','$publisher', '$imgUrl', '$story', '$pubDate');")
                }

                // 찜 목록에서 해당 사용자의 정보 추가
                var date : Long = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()).toLong()
                bookDB.execSQL("INSERT INTO Want VALUES ('$isbn', '$userEmail', $profileNum, $date);")

                btnDibs.setImageResource(R.drawable.bookinfo_btndibs) // 찜 상태 이미지
                isDibs = true
            }
            bookDB.close()
        }

        btnFinish.setOnClickListener { // 완독 버튼
            if(isFinished) { // 완독 상태
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("완독을 취소하시겠습니까?")
                dlg.setMessage("완독을 취소하시면 기존에 작성하신 독후감이 삭제됩니다.")
                dlg.setPositiveButton("완독취소", DialogInterface.OnClickListener { dialogInterface, i ->
                    if(checkBookTable() <= 1) { // 1명 이하(본인만 있는 경우)
                        bookDB.execSQL("DELETE FROM Book WHERE ISBN = '$isbn';")
                    }
                    // 읽은 책 목록에서 해당 사용자의 정보 삭제
                    bookDB.execSQL("DELETE FROM Read WHERE RISBN = '$isbn' AND REmail = '$userEmail' AND RNum = $profileNum;")

                    btnFinish.setImageResource(R.drawable.bookinfo_btnnotfinish) // 완독 해제 이미지
                    isFinished = false
                })
                dlg.setNegativeButton("완독유지", null)
                dlg.show()

            } else { // 완독 상태 아님
                if(checkBookTable() <= 0) { // 아무도 없을 때
                    bookDB.execSQL("INSERT INTO Book VALUES ('$isbn', '$title', '$author','$publisher', '$imgUrl', '$story', '$pubDate');")
                }

                // 읽은 책 목록에서 해당 사용자의 정보 추가
                var date : Long = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()).toLong()
                bookDB.execSQL("INSERT INTO Read(RISBN, REmail, RNum, RReadDate) VALUES ('$isbn', '$userEmail', $profileNum, $date);")

                btnFinish.setImageResource(R.drawable.bookinfo_btnfinish) // 완독 이미지
                isFinished = true

                // 완독한 책 수 목표를 채웠을 경우
                if(curBookCount + 1 == goalBookCount) {
                    curGoalNum++
                    bookDB.execSQL("UPDATE Profile SET GoalNum = $curGoalNum WHERE PEmail = '$userEmail' AND PNum = $profileNum ;") // 목표 올리기
                    if(curGoalNum == 16){ // 마지막 목표일 경우
                        Toast.makeText(this, "캐릭터를 모두 얻었습니다.", Toast.LENGTH_SHORT).show() // 캐릭터를 모두 얻었다고 안내
                    } else if(curGoalNum == 1){ // 첫 목표를 달성했을 경우
                        bookDB.execSQL("UPDATE Profile SET PMainImgNum = 0 WHERE PEmail = '$userEmail' AND PNum = $profileNum ;")
                        Toast.makeText(this, "새로운 캐릭터를 얻었습니다.", Toast.LENGTH_SHORT).show() // 새로운 캐릭터를 얻었다고 안내
                    }
                    else {
                        Toast.makeText(this, "새로운 캐릭터를 얻었습니다.", Toast.LENGTH_SHORT).show() // 새로운 캐릭터를 얻었다고 안내
                    }
                }

            }

            bookDB.close()
        }

        btnWrite.setOnClickListener { // 독서록 쓰기 버튼
            if(isFinished){
                val intent = Intent(this, RecordActivity::class.java)
                intent.putExtra("BOOKISBN", isbn)
                intent.putExtra("USEREMAIL", userEmail)
                intent.putExtra("PROFILENUM", profileNum)
                intent.putExtra("ISLOOKING", false)
                startActivity(intent)
            } else {
                Toast.makeText(this, "완독 버튼을 먼저 눌러주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        btnRead.setOnClickListener { // 독서록 읽기 버튼
            if(isFinished){
                val intent = Intent(this, RecordActivity::class.java)
                intent.putExtra("BOOKISBN", isbn)
                intent.putExtra("USEREMAIL", userEmail)
                intent.putExtra("PROFILENUM", profileNum)
                intent.putExtra("ISLOOKING", true)
                startActivity(intent)
            } else {
                Toast.makeText(this, "완독 버튼을 먼저 눌러주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 데이터 가져오는 함수
    fun getUserInfo(){
        // 찜 상태 가져오기
        bookDB = dbManager.readableDatabase // 데이터베이스 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT * FROM Want WHERE WISBN = '$isbn' AND WEmail = '$userEmail' AND WNum = $profileNum;", null)
        if(cursor.moveToNext()){
            isDibs = true
        } else {
            isDibs = false
        }

        // 완독 상태 가져오기
        cursor = bookDB.rawQuery("SELECT * FROM Read WHERE RISBN = '$isbn' AND REmail = '$userEmail' AND RNum = $profileNum;", null)
        if(cursor.moveToNext()){
            isFinished = true
        } else {
            isFinished = false
        }

        cursor.close()
        bookDB.close()
    }

    // 현재 책을 다른 누구가 사용하고 있는지 체크하는 함수
    fun checkBookTable() : Int {
        var count : Int = 0 // 사용자 수
        bookDB = dbManager.readableDatabase // 데이터베이스 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT * FROM Want WHERE WISBN = '$isbn';", null) // 찜한 사람
        count = cursor.count
        cursor = bookDB.rawQuery("SELECT * FROM Read WHERE RISBN = '$isbn';", null) // 완독한 사람
        count += cursor.count
        cursor.close()
        bookDB.close()
        bookDB = dbManager.writableDatabase // 데이터베이스 불러오기
        return count
    }

    // 쌍따옴표, 따옴표 처리
    fun setQuoteData(){
        // 따옴표 처리
        title = title!!.replace("\'", "\'\'")
        author = author!!.replace("\'", "\'\'")
        publisher = publisher!!.replace("\'", "\'\'")
        story = story!!.replace("\'", "\'\'")
        // 쌍따옴표 처리
        title = title!!.replace("\"", "\"\"")
        author = author!!.replace("\"", "\"\"")
        publisher = publisher!!.replace("\"", "\"\"")
        story = story!!.replace("\"", "\"\"")
    }
}