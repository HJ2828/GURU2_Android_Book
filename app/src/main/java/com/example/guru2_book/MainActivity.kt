package com.example.guru2_book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // 기본 정보 변수
    private var userEmail : String? = null // 사용자 이메일

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userEmail = intent.getStringExtra("USEREMAIL").toString()

        // 바텀 네비게이션 이동 구현
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setItemIconTintList(null);    // 아이콘 이미지 색상 보여줌

        // 네비게이션바 버튼에 따른 프래그먼트 이동
        bottomNav.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.asmr -> {      // asmr
                        changeFragment(AsmrFragment())
                    }
                    R.id.search -> {    // 책 검색
                        changeFragment(BookSearchFragment())
                    }
                    R.id.home -> {      // 캐릭터(홈)
                        changeFragment(HomeFragment.newInstance(userEmail))
                    }
                    R.id.shelf -> {     // 책장
                        changeFragment(BookShelfFragment())
                    }
                    R.id.mypage -> {    // 마이페이지
                        changeFragment(MyPageFragment.newInstance(userEmail))
                    }
                }
                true
            }
            selectedItemId = R.id.home      // 기본 선택: 캐릭터(홈)
        }
    }

    // 프래그먼트에서 다른 프래그먼트로 이동하는 함수
    public fun fragmentChangeInFragment(goFragment : Fragment){ // 매개변수에 프래그넌트 넣으면 이동합니다.
        changeFragment(goFragment)
    }

    // 프래그먼트 이동 함수
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}