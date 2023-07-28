package com.example.guru2_book

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class HomeFragment : Fragment() {

    // 위젯 변수
    lateinit var btnCharacter: ImageButton

    // activity, context 변수
    var activity : MainActivity? = null
    lateinit var fContext : Context

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
        btnCharacter = view.findViewById(R.id.btnCharacter)

        // 리스너 연결
        btnCharacter.setOnClickListener { // 뒤로가기 버튼
            activity?.fragmentChangeInFragment(CharacterFragment()) // 캐릭터 도감 프래그먼트로 변경
        }

        return view
    }
}