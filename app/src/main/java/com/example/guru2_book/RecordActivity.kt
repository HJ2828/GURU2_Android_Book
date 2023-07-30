package com.example.guru2_book

import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout

class RecordActivity : AppCompatActivity() {

    // 기본 정보 변수
    private var userEmail : String? = null // 사용자 이메일
    private var profileNum : Int = 0 // 사용자 프로필 번호
    private var bookISBN : String? = null // 책 ISBN
    private var isLooking : Boolean = false // 현재 버전 확인(보기/작성)

    // 위젯 변수
    lateinit var lookTitle : ImageView // 독후감 보기 타이틀
    lateinit var writeTitle : ImageView // 독후감 작성 타이틀
    lateinit var frameLook : ConstraintLayout // 독후감 보기 버튼 프레임
    lateinit var frameWrite : ConstraintLayout // 독후감 작성 버튼 프레임
    lateinit var textName : TextView // 책 제목 텍스트
    lateinit var ratingBook : RatingBar // 책 별점
    lateinit var edtRecord : EditText // 독후감 작성 에디트텍스트
    lateinit var btnBack : Button // 뒤로가기 버튼
    lateinit var btnEdit : Button // 수정 버튼
    lateinit var btnDel : Button // 삭제 버튼
    lateinit var btnOkay : Button // 작성 완료 버튼

    // 데이터베이스 변수
    lateinit var dbManager: DBManager
    lateinit var bookDB : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        dbManager = DBManager(this, "BookDB", null, 1) // SQLiteOpenHelper 객체 생성

        // 위젯 연결
        lookTitle = findViewById<ImageView>(R.id.lookTitle)
        writeTitle = findViewById<ImageView>(R.id.writeTitle)
        frameLook = findViewById<ConstraintLayout>(R.id.frameLook)
        frameWrite = findViewById<ConstraintLayout>(R.id.frameWrite)
        textName = findViewById<TextView>(R.id.textName)
        ratingBook = findViewById<RatingBar>(R.id.ratingBook)
        edtRecord = findViewById<EditText>(R.id.edtRecord)
        btnBack = findViewById<Button>(R.id.btnBack)
        btnEdit = findViewById<Button>(R.id.btnEdit)
        btnDel= findViewById<Button>(R.id.btnDel)
        btnOkay = findViewById<Button>(R.id.btnOkay)

        // intent를 받아 현재 버전, 기본 정보 변수 초기화
        userEmail = intent.getStringExtra("USEREMAIL").toString()
        profileNum = intent.getIntExtra("PROFILENUM", 0)
        bookISBN = intent.getStringExtra("BOOKISBN").toString()
        isLooking = intent.getBooleanExtra("ISLOOKING", false)

        changeVersion() // 화면 활성화

        // 데이터베이스 별점 디폴트 0으로 수정하기
        getRecordData() // 데이터 가져오기

        // 리스너 연결
        btnBack.setOnClickListener { // 뒤로가기 버튼
            if(isLooking) { // 독후감 보기일 경우
                finish()
            } else { // 독후감 작성일 경우
                // 현재 내용 저장할 것인지 대화상자 나타내기
                var dlg = AlertDialog.Builder(this)
                dlg.setMessage("현재 내용을 저장하시겠습니까?")
                dlg.setPositiveButton("저장", DialogInterface.OnClickListener { dialogInterface, i ->
                    btnOkay.callOnClick()
                })
                dlg.setNegativeButton("저장안함", DialogInterface.OnClickListener { dialogInterface, i ->
                    finish()
                })

                dlg.show()
            }
        }

        btnEdit.setOnClickListener { // 수정 버튼
            changeIntoWrite() // 독후감 작성 버전으로 바꾸기
        }

        btnDel.setOnClickListener { // 삭제 버튼
            var dlg = AlertDialog.Builder(this) // 독후감을 정말 삭제할 것인지 대화상자
            dlg.setMessage("독후감을 삭제하시겠습니까?")
            dlg.setPositiveButton("삭제", DialogInterface.OnClickListener { dialogInterface, i ->

                // 별점, 독후감 지우기
                ratingBook.rating = 0f
                edtRecord.text.clear()

                // 해당 책 독후감, 별점 데이터 삭제하기
                bookDB = dbManager.writableDatabase // 데이터베이스 불러오기
                bookDB.execSQL("UPDATE Read SET RReport = NULL, RRating = 0 WHERE RISBN = '$bookISBN' AND REmail = '$userEmail' AND RNum = $profileNum;")

                bookDB.close()
                finish()

            })
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }

        btnOkay.setOnClickListener { // 작성 완료 버튼
            var bookRecord : String = edtRecord.text.toString()
            var bRating : Float = ratingBook.rating
            if(bRating == 0.0f && bookRecord.trim().isEmpty()){ // 아무것도 작성하지 않았을 경우
                var dlg = AlertDialog.Builder(this) // 독후감을 작성하지 않을지에 대화상자
                dlg.setMessage("독후감을 작성하지 않고 나가시겠습니까?")
                dlg.setPositiveButton("나가기", DialogInterface.OnClickListener { dialogInterface, i ->
                    finish()
                })
                dlg.setNegativeButton("작성하기", null)
                dlg.show()
            } else {
                // 독후감, 별점 기록을 데이터베이스에 저장
                bookDB = dbManager.writableDatabase // 데이터베이스 불러오기
                var date : Long = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()).toLong()

                if(bookRecord.trim().isEmpty()){ // 별점만 입력했을 경우
                    bookDB.execSQL("UPDATE Read SET RReport = NULL, RRating = $bRating, RReportDate = $date WHERE RISBN = '$bookISBN' AND REmail = '$userEmail' AND RNum = $profileNum;")
                } else{
                    bookDB.execSQL("UPDATE Read SET RReport = '$bookRecord', RRating = $bRating, RReportDate = $date WHERE RISBN = '$bookISBN' AND REmail = '$userEmail' AND RNum = $profileNum;")
                }

                bookDB.close()
                Toast.makeText(applicationContext, "독후감이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                changeIntoLook()
            }
        }
    }

    // 현재 버전에 알맞은 화면 활성화
    fun changeVersion(){
        if(isLooking){ // 독후감 보기
            changeIntoLook() // 독후감 보기 버전으로 바꾸기

        } else { // 독후감 작성
            changeIntoWrite() // 독후감 작성 버전으로 바꾸기
        }
    }

    // 독후감 보기 버전으로 바꾸기
    fun changeIntoLook(){
        isLooking = true
        // 독후감 보기 타이틀 보이도록 설정
        lookTitle.visibility = View.VISIBLE
        writeTitle.visibility = View.INVISIBLE
        // 수정, 삭제 버튼 보이도록 설정
        frameLook.visibility = View.VISIBLE
        frameWrite.visibility = View.INVISIBLE
        // 별점 변경 불가
        ratingBook.setIsIndicator(true)
        // 독후감 작성 불가
        edtRecord.isEnabled = false
    }

    // 독후감 작성 버전으로 바꾸기
    fun changeIntoWrite(){
        isLooking = false
        // 독후감 작성 타이틀 보이도록 설정
        lookTitle.visibility = View.INVISIBLE
        writeTitle.visibility = View.VISIBLE
        // 작성완료 버튼 보이도록 설정
        frameLook.visibility = View.INVISIBLE
        frameWrite.visibility = View.VISIBLE
        // 별점 변경 가능
        ratingBook.setIsIndicator(false)
        // 독후감 작성 가능
        edtRecord.isEnabled = true
    }

    // 기존 데이터 불러오는 함수
    fun getRecordData(){
        bookDB = dbManager.readableDatabase

        // 책 제목 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT BName FROM Book WHERE ISBN = '$bookISBN';", null)
        if(cursor.moveToNext()){
            var bName = cursor.getString(0).toString()
            textName.text = bName
        }

        // 독후감, 별점 가져오기
        cursor = bookDB.rawQuery("SELECT RReport, RRating FROM Read WHERE RISBN = '$bookISBN' AND REmail = '$userEmail' AND RNum = $profileNum;", null)
        if(cursor.moveToNext()){
            if(cursor.getString(0) == null){
                edtRecord.text.clear()
            } else {
                edtRecord.setText(cursor.getString(0).toString())
            }
            ratingBook.rating = cursor.getFloat(1)
        }

        cursor.close()
        bookDB.close()
    }

}