package com.example.guru2_book

import android.content.Context
import android.database.sqlite.SQLiteDatabase
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



        return view
    }

    // 책 표지 imageView 생성 함수
    fun makeImageBook(){
        var imageBook : ImageView = ImageView(fContext)// 책 표지 imageView
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, resources.getDimension(R.dimen.book_height).toInt()) // 크기 설정
        // imageBook 속성 설정
        imageBook.layoutParams = layoutParams
        imageBook.scaleType = ImageView.ScaleType.FIT_START
        imageBook.setImageResource(R.drawable.login_bg) // 임시
        linearRead.addView(imageBook) // 임시
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