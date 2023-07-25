package com.example.guru2_book

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

        bottomNav.setItemIconTintList(null);

        bottomNav.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.asmr -> {
                        changeFragment(AsmrFragment())
                    }
                    R.id.search -> {
                        changeFragment(BookSearchFragment())
                    }
                    R.id.home -> {
                        changeFragment(HomeFragment())
                    }
                    R.id.shelf -> {
                        changeFragment(BookShelfFragment())
                    }
                    R.id.mypage -> {
                        changeFragment(MyPageFragment())
                    }
                }
                true
            }
            selectedItemId = R.id.home
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