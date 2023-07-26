package com.example.guru2_book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginSignupActivity : AppCompatActivity() {

    lateinit var btnLogIn : Button // 로그인 화면으로 이동하는 버튼
    lateinit var btnSignUp : Button // 회원가입 화면으로 이동하는 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        // 위젯 연결
        btnLogIn = findViewById<Button>(R.id.btnLogIn)
        btnSignUp = findViewById<Button>(R.id.btnSignUP)

        // 리스너 연결
        btnLogIn.setOnClickListener { // 로그인 버튼
            // 로그인 화면으로 이동
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener { // 회원가입 버튼
            // 회원가입 화면으로 이동
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}