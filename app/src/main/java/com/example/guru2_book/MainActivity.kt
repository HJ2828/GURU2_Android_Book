package com.example.guru2_book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                        changeFragment(HomeFragment())
                    }
                    R.id.shelf -> {     // 책장
                        changeFragment(BookShelfFragment())
                    }
                    R.id.mypage -> {    // 마이페이지
                        changeFragment(MyPageFragment())
                    }
                }
                true
            }
            selectedItemId = R.id.home      // 기본 선택: 캐릭터(홈)
        }
    }

    // 프래그먼트 이동 함수
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}