package com.example.guru2_book

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout


class BookShelfFragment : Fragment() {

    // 기본 정보 변수
    private var userEmail : String? = null // 사용자 이메일
    private var profileNum : Int = 0 // 사용자 프로필 번호

    // activity, context 변수
    var activity : MainActivity? = null
    lateinit var fContext : Context

    // 위젯 변수
    lateinit var linearRead : LinearLayout
    lateinit var linearWant : LinearLayout
    lateinit var linearRecord : LinearLayout

    // 데이터베이스 변수
    lateinit var dbManager: DBManager
    lateinit var bookDB : SQLiteDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainActivity // MainActivity 가져오기
        fContext = context // content 가져오기
    }

    override fun onDetach() {
        super.onDetach()
        activity = null // activity null로 설정
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmail = it.getString("USEREMAIL") // 사용자 이메일 받기
            profileNum = it.getInt("PROFILENUM") // 프로필 번호 받기
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_shelf, container, false)

        dbManager = DBManager(activity, "BookDB", null, 1) // SQLiteOpenHelper 객체 생성

        // 위젯 연결
        linearRead = view.findViewById<LinearLayout>(R.id.linearRead)
        linearWant = view.findViewById<LinearLayout>(R.id.linearWant)
        linearRecord = view.findViewById<LinearLayout>(R.id.linearRecord)

        getReadBooks() // 읽은 책들을 가져옴
        getWantBooks() // 찜한 책들을 가져옴
        getRecordBooks() // 독후감 작성한 책들을 가져옴

        return view
    }

    // 읽은 책 리스트를 가져옴
    fun getReadBooks(){

        var rISBNArrayList : ArrayList<Int> = ArrayList<Int>() // isbn 리스트
        bookDB = dbManager.readableDatabase

        // 읽은 책 리스트를 읽은 날짜 순으로 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT ISBN, Bimage, RReadDate FROM Read R, Book B WHERE R.RISBN = B.ISBN AND R.REmail = '$userEmail' AND R.RNum = $profileNum ORDER BY R.RReadDate ASC;);", null)

        while(cursor.moveToNext()){
            var isbn : Int = cursor.getInt(0)
            rISBNArrayList.add(isbn)
            var uri : Uri = Uri.parse(cursor.getString(1).toString())

            makeImageBook(isbn, linearRead, uri) // 책 ISBN과 이미지 uri을 넘겨 이미지뷰 생성
        }

        cursor.close()
        bookDB.close()
    }

    // 찜한 책 리스트를 가져옴
    fun getWantBooks(){
        bookDB = dbManager.readableDatabase

        // 찜한 책 리스트를 찜한 날짜 순으로 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT ISBN, Bimage, WWantDate FROM Want W, Book B WHERE W.WISBN = B.ISBN AND W.WEmail = '$userEmail' AND W.WNum = $profileNum ORDER BY W.WWantDate ASC;", null)

        while(cursor.moveToNext()){
            var isbn : Int = cursor.getInt(0)
            var uri : Uri = Uri.parse(cursor.getString(1).toString())

            makeImageBook(isbn, linearWant, uri) // 책 ISBN과 이미지 uri을 넘겨 이미지뷰 생성
        }

        cursor.close()
        bookDB.close()
    }

    // 독후감을 작성한 책 리스트를 가져옴
    fun getRecordBooks(){
        bookDB = dbManager.readableDatabase

        // 독후감을 작성한 책 리스트를 작성한 날짜 순으로 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery(
            "SELECT ISBN, Bimage, RReportDate FROM Read R, Book B WHERE (R.RISBN = B.ISBN AND R.REmail = '$userEmail' AND R.RNum = $profileNum) AND (R.RReport IS NOT NULL || R.RRating IS NOT NULL) ORDER BY R.RReportDate ASC;", null)

        while(cursor.moveToNext()){
            var isbn : Int = cursor.getInt(0)
            var uri : Uri = Uri.parse(cursor.getString(1).toString())

            makeImageBook(isbn, linearRecord, uri) // 책 ISBN과 이미지 uri을 넘겨 이미지뷰 생성
        }

        cursor.close()
        bookDB.close()
    }

    // 책 표지 imageView 생성 함수
    fun makeImageBook(isbn : Int, layout: LinearLayout, uri : Uri){
        var imageBook : ImageView = ImageView(fContext)// 책 표지 imageView
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, resources.getDimension(R.dimen.book_height) as Int) // 크기 설정
        imageBook.layoutParams = layoutParams
        imageBook.scaleType = ImageView.ScaleType.FIT_START
        imageBook.setImageURI(uri)
        when(layout.id){
            R.id.linearRead, R.id.linearWant -> {
                goBookInfo(imageBook, isbn, userEmail, profileNum)
            }
            R.id.linearRecord -> {
                goRecord(imageBook, isbn, userEmail, profileNum)
            }
        }

        layout.addView(imageBook, 0)
    }

    // 책 표지 클릭 시 책 정보로 이동
    fun goBookInfo(imageBook : ImageView, isbn: Int, email: String?, pNum : Int){
        imageBook.setOnClickListener { // 리스너 연결
            //activity?.fragmentChangeInFragment(BookInfoFragment.newInstance(isbn, email, pNum)) // 책 정보 프래그넌트로 이동
        }
    }

    // 책 표시 클릭 시 독후감 보기로 이동
    fun goRecord(imageBook : ImageView, isbn: Int, email: String?, pNum : Int){
        /*imageBook.setOnClickListener { // 리스너 연결
            val intent = Intent(fContext, RecordActivity::class.java)
            intent.putExtra("BOOKISBN", isbn)
            intent.putExtra("USEREMAIL", email)
            intent.putExtra("PROFILENUM", pNum)
            intent.putExtra("ISLOOKING", true)
            startActivity(intent)
        }*/
    }

    companion object {

        @JvmStatic
        fun newInstance(email : String?, num : Int) =
            BookShelfFragment().apply {
                arguments = Bundle().apply {
                    putString("USEREMAIL", email)
                    putInt("PROFILENUM", num)
                }
            }
    }
}