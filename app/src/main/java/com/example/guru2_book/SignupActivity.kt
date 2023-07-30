package com.example.guru2_book

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignupActivity : AppCompatActivity() {

    // 데이터베이스 변수
    lateinit var dbManager: DBManager
    lateinit var bookDB : SQLiteDatabase

    // 위젯 변수
    lateinit var edtName : EditText // 이름 입력 에디트텍스트
    lateinit var edtEmail : EditText // 이메일 입력 에디트텍스트
    lateinit var edtPassword : EditText // 비밀번호 입력 에디트텍스트
    lateinit var edtPasswordCheck : EditText // 비밀번호 확인 에디트텍스트
    lateinit var btnBack : Button // 뒤로가기 버튼
    lateinit var btnDup : Button // 이메일 중복 확인 버튼
    lateinit var btnOkay : Button // 회원가입 완료 버튼

    // 기타 변수
    var isNotDup : Boolean = false // 이메일 중복 확인 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        dbManager = DBManager(this, "BookDB", null, 1) // SQLiteOpenHelper 객체 생성

        // 위젯 연결
        edtName = findViewById<EditText>(R.id.edtName)
        edtEmail = findViewById<EditText>(R.id.edtEmail)
        edtPassword = findViewById<EditText>(R.id.edtPassword)
        edtPasswordCheck = findViewById<EditText>(R.id.edtPasswordCheck)
        btnBack = findViewById<Button>(R.id.btnBack)
        btnDup = findViewById<Button>(R.id.btnDup)
        btnOkay = findViewById<Button>(R.id.btnOkay)

        // 리스너 연결
        btnBack.setOnClickListener { // 뒤로가기 버튼
            finish() // 현재 액티비티 종료
        }

        btnDup.setOnClickListener { // 이메일 중복 확인 버튼
            var email : String = edtEmail.text.toString()
            if(email.trim().isEmpty()){ // 이메일 칸이 빈칸일 경우
                Toast.makeText(applicationContext, "이메일을 입력해주십시오", Toast.LENGTH_SHORT).show() // 이메일을 입력해달라는 달라는 토스트
            } else {
                checkDupEmail(email) // 중복 확인 변수
            }
        }

        btnOkay.setOnClickListener {
            var name : String = edtName.text.toString()
            var email : String = edtEmail.text.toString()
            var password : String = edtPassword.text.toString()
            var passwordCheck : String = edtPasswordCheck.text.toString()

            if(name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || passwordCheck.trim().isEmpty()) { // 빈칸이 있을 경우
                Toast.makeText(applicationContext, "입력 칸을 전부 채워주십시오.", Toast.LENGTH_SHORT).show() // 입력 칸을 전부 채워달라는 토스트
            } else if(!isNotDup){ // 이메일 중복 확인을 하지 않았을 경우
                Toast.makeText(applicationContext, "이메일 중복 확인을 해주십시오.", Toast.LENGTH_SHORT).show() // 이메일 중복을 해달라는 토스트
            } else if(!password.equals(passwordCheck)) { // 비밀번호와 비밀번호 확인이 일치하지 않을 경우
                Toast.makeText(applicationContext, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show() // 비밀번호와 비밀번호 확인을 일치시켜 달라는 토스트
            } else { // 모든 조건을 갖추었을 경우
                // 로그인 화면으로 이동
                createAccount(email, name, password) // 계정 생성 함수
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // 이메일 중복 확인 함수
    fun checkDupEmail(email : String) {
        bookDB = dbManager.readableDatabase // 데이터베이스 불러오기

        // 매개변수로 얻어온 이메일에 해당하는 계정 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT * FROM Profile WHERE PEmail = '$email';", null)

        if (!cursor.moveToNext()){ // 계정이 존재하지 않을 경우
            Toast.makeText(applicationContext, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show() // 사용 가능한 이메일이라는 토스트
            btnDup.visibility = View.INVISIBLE // 중복확인 버튼 안보이도록 처리

            isNotDup = true // 이메일 중복되지 않음(사용가능한 이메일)
            cursor.close()
            bookDB.close()
            dbManager.close()
        } else { // 계정이 존재할 경우
            // 이미 사용 중인 이메일이라는 토스트
            Toast.makeText(applicationContext, "이미 사용 중인 이메일입니다.", Toast.LENGTH_SHORT).show()

            isNotDup = false // 이메일 중복 됨(이미 사용하고 있는 이메일)
            cursor.close()
            bookDB.close()
        }
    }

    // 계정 생성 함수
    fun createAccount(email: String, name : String, password : String) {
        bookDB = dbManager.writableDatabase
        bookDB.execSQL("INSERT INTO Account(AEmail, APassword, ACurrentProfile) VALUES ('$email', '$password', 0);")
        bookDB.execSQL("INSERT INTO Profile(PEmail, PNum, PName, GoalNum) VALUES ('$email', 0, '$name', 0);")
        bookDB.close()
        dbManager.close()
    }
}