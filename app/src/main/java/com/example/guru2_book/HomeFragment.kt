package com.example.guru2_book

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class HomeFragment : Fragment() {

    // 기본 정보 변수
    private var userEmail : String? = null // 사용자 이메일
    private var profileNum : Int = 0 // 사용자 프로필 번호
    private var curChaNum :Int = -1 // 현재 캐릭터 이미지 번호

    // activity, context 변수
    var activity : MainActivity? = null
    lateinit var fContext : Context

    // 위젯 변수
    lateinit var textGoal: TextView     // 목표 텍스트뷰
    lateinit var imgCharacter: ImageView     // 캐릭터 이미지뷰
    lateinit var btnCharacter: ImageButton  // 캐릭터 도감 보기 버튼
    lateinit var textRead: TextView     // 읽은 책 수 텍스트뷰
    lateinit var textDibs: TextView     // 찜한 책 수 텍스트뷰

    // 캐릭터 이미지 변수
    var mainCharacters = arrayOf(
        R.drawable.home_chick1, R.drawable.home_chick0, R.drawable.home_chick2, R.drawable.home_chick3,
        R.drawable.home_bab0, R.drawable.home_bab1, R.drawable.home_bab2, R.drawable.home_bab3,
        R.drawable.home_rabbit0, R.drawable.home_rabbit1, R.drawable.home_rabbit2, R.drawable.home_rabbit3,
        R.drawable.home_polarbear0, R.drawable.home_polarbear1, R.drawable.home_polarbear2, R.drawable.home_polarbear3
    )

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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        dbManager = DBManager(activity, "BookDB", null, 1) // SQLiteOpenHelper 객체 생성

        // 위젯 연결
        textGoal = view.findViewById(R.id.textGoal)
        imgCharacter = view.findViewById(R.id.imgCharacter)
        btnCharacter = view.findViewById(R.id.btnCharacter)
        textRead = view.findViewById(R.id.textRead)
        textDibs = view.findViewById(R.id.textDibs)

        getUserInfo() // 데이터 가져오기

        // 리스너 연결
        btnCharacter.setOnClickListener { // 캐릭터 도감 보기 버튼
            activity?.fragmentChangeInFragment(CharacterFragment.newInstance(userEmail)) // 캐릭터 도감 프래그먼트로 변경
        }

        return view
    }

    // 데이터 가져오는 함수
    fun getUserInfo(){
        bookDB = dbManager.readableDatabase // 데이터베이스 가져오기

        // 개인정보 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT ACurrentProfile FROM Account WHERE AEmail = '$userEmail';", null)

        // 현재 프로필 번호 가져오기
        if(cursor.moveToNext()){
            profileNum = cursor.getInt(0)
        }

        // 목표 번호, 현재 사용하고 있는 캐릭터 번호 가져오기
        cursor = bookDB.rawQuery("SELECT GoalNum, PMainImgNum FROM Profile WHERE PEmail = '$userEmail' AND PNum = $profileNum ;", null)
        var goalNum : Int = 0 // 목표 번호
        if(cursor.moveToNext()){
            goalNum = cursor.getInt(0)
            curChaNum = cursor.getInt(1)
        }

        // 캐릭터 이미지 넣기
        if(curChaNum != -1){ // 얻은 캐릭터가 있을 경우
            imgCharacter.setImageResource(mainCharacters[curChaNum])
        } else { // 얻은 캐릭터가 없을 경우
            imgCharacter.setImageResource(R.drawable.home_egg_shadow)
        }

        // 목표 설정, 이미지 넣기
        cursor = bookDB.rawQuery("SELECT GCount FROM Goal WHERE GNum = $goalNum ;", null)
        var bCount : Int = 1; // 읽어야 할 책 개수
        if(cursor.moveToNext()){
            bCount = cursor.getInt(0)
        }
        textGoal.text = "책 $bCount 권 읽기"

        // 현재 읽은 책 수 가져오기 및 책 수 넣기
        var readCount : Int = 0 // 읽은 책 수
        cursor = bookDB.rawQuery("SELECT * FROM Read R, Book B WHERE R.RISBN = B.ISBN AND R.REmail = '$userEmail' AND R.RNum = $profileNum;", null)
        readCount = cursor.count
        textRead.text = "읽은 책 수: $readCount 권"

        // 현재 찜한 책 수 가져오기 및 책 수 넣기
        var wantCount : Int = 0 // 찜한 책 수
        cursor = bookDB.rawQuery("SELECT * FROM Want W, Book B WHERE W.WISBN = B.ISBN AND W.WEmail = '$userEmail' AND W.WNum = $profileNum ;", null)
        wantCount = cursor.count
        if(goalNum == 16){
            textDibs.text = "모든 목표를 달성하셨습니다."
        } else {
            textDibs.text = "찜한 책 수: $wantCount 권"
        }

        cursor.close()
        bookDB.close()
    }

    companion object {

        @JvmStatic
        fun newInstance(email : String?) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("USEREMAIL", email)
                }
            }
    }
}