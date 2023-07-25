package com.example.guru2_book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class AsmrFragment : Fragment() {

    // 예시
    // lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 프래그먼트 코드 작성법!!
        // 필요없는 코드들은 지워도 괜찮다고 했습니다.

        // 아래 val view 이렇게 해서 사용해야합니다. 보라색 글씨는 프래그먼트 xml 파일 이름 입니다. 복붙해서 쓰실 때 보라 글씩만 변경해주세요.
        val view = inflater.inflate(R.layout.fragment_asmr, container, false)

        // 위젯 연결해서 사용하는 예시
        // tv = view.findViewById(R.id.tvTitle)

//        btnClick.setOnClickListener {
//            tvTitle.setText("하이")
//        }

        // return 값 기존과 다르니 수정해야합니다. 복붙해 사용하세요.
        return view
    }


}