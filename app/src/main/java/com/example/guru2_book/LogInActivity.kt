package com.example.guru2_book

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlin.math.log

class LogInActivity : AppCompatActivity() {

    // 데이터베이스 변수
    lateinit var dbManager: DBManager
    lateinit var bookDB : SQLiteDatabase

    // 위젯 변수
    lateinit var edtEmail : EditText // 이메일 입력 에디트텍스트
    lateinit var edtPassword : EditText //비밀번호 입력 에디트텍스트
    lateinit var btnBack : Button // 뒤로가기 버튼
    lateinit var btnOkay : Button // 로그인하기 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        dbManager = DBManager(this, "BookDB", null, 1) // SQLiteOpenHelper 객체 생성

        // 위젯 연결
        edtEmail = findViewById<EditText>(R.id.edtEmail)
        edtPassword = findViewById<EditText>(R.id.edtPassword)
        btnBack = findViewById<Button>(R.id.btnBack)
        btnOkay = findViewById<Button>(R.id.btnOkay)

        // 리스너 연결
        btnBack.setOnClickListener { // 뒤로가기 버튼
            finish() // 현재 액티비티 종료
        }
        btnOkay.setOnClickListener { // 로그인하기 버튼
            var email : String = edtEmail.text.toString()
            var password : String = edtPassword.text.toString()

            if(email.trim().isEmpty() || password.trim().isEmpty()){ // 입력 칸이 비어있을 경우
                Toast.makeText(applicationContext, "이메일 또는 비밀번호를 입력해주십시오.", Toast.LENGTH_SHORT).show()
            } else {
                checkAccount(email, password) // 계정 확인
            }
        }
    }

    // 계정 확인 함수
    fun checkAccount(email : String, password : String){
        bookDB = dbManager.readableDatabase // 데이터베이스 불러오기

        // 매개변수로 얻어온 이메일에 해당하는 계정 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT APassword FROM Account WHERE AEmail = '$email';", null)

        if (cursor.moveToNext() && (cursor.getString(0).toString().equals(password))){ // 계정이 존재하고 비밀번호가 올바른 경우
            // AEmail 가지고 메인 화면으로 이동
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USEREMAIL", email)
            startActivity(intent)

            cursor.close()
            bookDB.close()
            dbManager.close()
            finish()
        } else { // 계정이 존재하지 않거나 비밀번호가 틀렸을 경우
            // 이메일 또는 비밀번호가 잘못되었다는 대화상자
            var dlg = AlertDialog.Builder(this)
            dlg.setMessage("이메일 주소 또는 비밀번호가 잘못되었습니다.\n다시 입력해주십시오.")
            dlg.setPositiveButton("확인", null)
            dlg.show()

            cursor.close()
            bookDB.close()

        }
    }
}