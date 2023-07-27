package com.example.guru2_book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var pref = this.getSharedPreferences("Login_info", 0)

        Handler().postDelayed({
            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
            val intent : Intent

            if(pref.contains("UEMAIL")){
                intent = Intent(this,MainActivity::class.java)
                intent.putExtra("USEREMAIL", pref.getString("UEMAIL", null).toString())
            } else {
                intent = Intent(this,LoginSignupActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)    // 스플래쉬 화면 3초 후 다음 화면(로그인화면)으로
    }

}