package com.example.guru2_book

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class HomeFragment : Fragment() {

    // 위젯 변수
    lateinit var textGoal: TextView     // 목표 텍스트뷰
    lateinit var imgCharacter: ImageView     // 캐릭터 이미지뷰
    lateinit var btnCharacter: ImageButton  // 캐릭터 도감 보기 버튼
    lateinit var textRead: TextView     // 읽은 책 수 텍스트
    lateinit var textDibs: TextView     // 찜한 책 수 텍스트뷰

    // activity, context 변수
    var activity : MainActivity? = null
    lateinit var fContext : Context

    // 캐릭터 이미지 변수
    var mainCharacters = arrayOf(
        R.drawable.home_chick0, R.drawable.home_chick1, R.drawable.home_chick2, R.drawable.home_chick3,
        R.drawable.home_bab0, R.drawable.home_bab1, R.drawable.home_bab2, R.drawable.home_bab3,
        R.drawable.home_rabbit0, R.drawable.home_rabbit1, R.drawable.home_rabbit2, R.drawable.home_rabbit3,
        R.drawable.home_polarbear0, R.drawable.home_polarbear1, R.drawable.home_polarbear2, R.drawable.home_polarbear3
    )

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

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 위젯 연결
        textGoal = view.findViewById(R.id.textGoal)
        imgCharacter = view.findViewById(R.id.imgCharacter)
        btnCharacter = view.findViewById(R.id.btnCharacter)
        textRead = view.findViewById(R.id.textRead)
        textDibs = view.findViewById(R.id.textDibs)

        // 리스너 연결
        btnCharacter.setOnClickListener { // 캐릭터 도감 보기 버튼
            activity?.fragmentChangeInFragment(CharacterFragment()) // 캐릭터 도감 프래그먼트로 변경
        }

        return view
    }
}