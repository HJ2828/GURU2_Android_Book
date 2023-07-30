package com.example.guru2_book

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import de.hdodenhof.circleimageview.CircleImageView

class CharacterFragment : Fragment() {

    // 기본 정보 변수
    private var userEmail : String? = null // 사용자 이메일
    private var profileNum : Int = 0 // 사용자 프로필 번호
    private var curCheckNum : Int = 0 // 현재 장착한 캐릭터 번호
    private var curGoalNum : Int = 0 // 현재 목표 번호

    // 위젯 변수
    lateinit var textCharacterCount: TextView   // 모은 캐릭터 수 텍스트뷰
    lateinit var btnBack: ImageButton   // 뒤로가기 버튼
    lateinit var btnEquip: ImageButton  // 장착하기 버튼
    var imageChaArray : ArrayList<ImageView> = ArrayList<ImageView>(16) // 캐릭터 이미지뷰
    var imageCheckArray : ArrayList<ImageView> = ArrayList<ImageView>(16) // 캐릭터 체크 이미지뷰
    val imagechaIds = arrayOf( // 캐릭터 이미지 아이디
        R.id.imageCha00, R.id.imageCha01, R.id.imageCha02, R.id.imageCha03,
        R.id.imageCha04, R.id.imageCha05, R.id.imageCha06, R.id.imageCha07,
        R.id.imageCha08, R.id.imageCha09, R.id.imageCha10, R.id.imageCha11,
        R.id.imageCha12, R.id.imageCha13, R.id.imageCha14, R.id.imageCha15 )
    val imageCheckIds = arrayOf( // 캐릭터 체크 이미지뷰
        R.id.imageCheck00, R.id.imageCheck01, R.id.imageCheck02, R.id.imageCheck03,
        R.id.imageCheck04, R.id.imageCheck05, R.id.imageCheck06, R.id.imageCheck07,
        R.id.imageCheck08, R.id.imageCheck09, R.id.imageCheck10, R.id.imageCheck11,
        R.id.imageCheck12, R.id.imageCheck13, R.id.imageCheck14, R.id.imageCheck15 )
    val imageCheckable = Array(16){ false }

    // 캐릭터 이미지 id
    val characters = arrayOf(
        R.drawable.character_chick0, R.drawable.character_chick1, R.drawable.character_chick2, R.drawable.character_chick3,
        R.drawable.character_bab0, R.drawable.character_bab1, R.drawable.character_bab2, R.drawable.character_bab3,
        R.drawable.character_rabbit0, R.drawable.character_rabbit1, R.drawable.character_rabbit2, R.drawable.character_rabbit3,
        R.drawable.character_polarbear0, R.drawable.character_polarbear1, R.drawable.character_polarbear2, R.drawable.character_polarbear3 )

    // activity, context 변수
    var activity : MainActivity? = null
    lateinit var fContext : Context

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
        val view = inflater.inflate(R.layout.fragment_character, container, false)

        dbManager = DBManager(activity, "BookDB", null, 1) // SQLiteOpenHelper 객체 생성

        // 프로필 번호, 현재 장착한 캐릭터 번호, 현재 목표 번호 가져오기
        bookDB = dbManager.readableDatabase
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT ACurrentProfile FROM Account WHERE AEmail = '$userEmail';", null)
        if(cursor.moveToNext()){
            profileNum = cursor.getInt(0)
        }
        cursor = bookDB.rawQuery("SELECT PMainImgNum, GoalNum FROM Profile WHERE PEmail = '$userEmail' AND PNum = $profileNum;", null)
        if(cursor.moveToNext()){
            curCheckNum = cursor.getInt(0)
            curGoalNum = cursor.getInt(1)
        }

        cursor.close()
        bookDB.close()

        // 위젯 연결 및 리스너 연결
        textCharacterCount = view.findViewById<TextView>(R.id.textCharacterCount)
        btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnEquip = view.findViewById<ImageButton>(R.id.btnEquip)
        for(index in 0..15){
            imageCheckArray.add(view.findViewById<ImageView>(imageCheckIds[index])) // 캐릭터 체크 이미지뷰 연결
            imageChaArray.add(view.findViewById<ImageView>(imagechaIds[index])) // 캐릭터 이미지뷰 연결
            imageChaArray.get(index).setOnClickListener { // 캐릭터 클릭 시
                if(imageCheckable[index]){ // 얻은 캐릭터일 경우
                    imageCheckArray.get(index).visibility = View.VISIBLE // 장착
                    imageCheckArray.get(curCheckNum).visibility = View.INVISIBLE // 기존 장착 해제
                    curCheckNum = index
                }
            }
        }

        getUserInfo()

        // 리스너 연결
        btnBack.setOnClickListener { // 뒤로가기 버튼
            activity?.fragmentChangeInFragment(HomeFragment.newInstance(userEmail)) // 홈 프래그먼트로 변경
        }

        btnEquip.setOnClickListener { // 장착하기 버튼
            bookDB = dbManager.writableDatabase
            bookDB.execSQL("UPDATE Profile SET PMainImgNum = '$curCheckNum' WHERE PEmail = '$userEmail' AND PNum = $profileNum ;")
            bookDB.close()
            Toast.makeText(fContext, "캐릭터를 장착했습니다.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    // 사용자 데이터 화면 적용
    fun getUserInfo() {
        if(curGoalNum != 0) { // 현재 목표가 0이 아닌 경우
            for(index in 0 .. curGoalNum - 1){
                imageCheckable[index] = true
                imageChaArray.get(index).setImageResource(characters[index])
            }
            imageCheckArray.get(curCheckNum).visibility = View.VISIBLE // 현재 장착하고 있는 캐릭터 표시
            textCharacterCount.text = curGoalNum.toString()
        } else {
            textCharacterCount.text = "0"
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(email : String?) =
            CharacterFragment().apply {
                arguments = Bundle().apply {
                    putString("USEREMAIL", email)
                }
            }
    }


}