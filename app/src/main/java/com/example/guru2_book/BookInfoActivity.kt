package com.example.guru2_book

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class BookInfoActivity : AppCompatActivity() {

    // 위젯 변수
    lateinit var btnBackBook: ImageButton
    lateinit var btnDibs: ImageButton
    lateinit var imgBook: ImageView
    lateinit var textTitle: TextView
    lateinit var textAuthor: TextView
    lateinit var textPublisher: TextView
    lateinit var textPubDate: TextView
    lateinit var textStory: TextView
    lateinit var btnFinish: ImageButton
    lateinit var btnWrite: ImageButton
    lateinit var btnRead: ImageButton

    // activity, context 변수
    var activity : MainActivity? = null
    lateinit var fContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_info)

        // 위젯 연결
        btnBackBook = findViewById(R.id.btnBackBook)
        btnDibs = findViewById(R.id.btnDibs)
        imgBook = findViewById(R.id.imgBook)
        textTitle = findViewById(R.id.textTitle)
        textAuthor = findViewById(R.id.textAuthor)
        textPublisher = findViewById(R.id.textPublisher)
        textPubDate = findViewById(R.id.textPubDate)
        textStory = findViewById(R.id.textStory)
        btnFinish = findViewById(R.id.btnFinish)
        btnWrite = findViewById(R.id.btnWrite)
        btnRead = findViewById(R.id.btnRead)

        // 이전 액티비티에서 값 받아오기
        val title = intent.getStringExtra("bookTitle")
        val author = intent.getStringExtra("bookAuthor")
        val publisher = intent.getStringExtra("bookPublisher")
        val pubDate = intent.getStringExtra("bookPubDate")
        val imgUrl = intent.getStringExtra("bookImgUrl")
        val story = intent.getStringExtra("bookStory")

        // 받아온 값을 위젯에 설정
        Glide.with(this).load(imgUrl).into(imgBook)
        textTitle.text = title
        textAuthor.text = author
        textPublisher.text = publisher
        textPubDate.text = pubDate
        textStory.text = story

        btnBackBook.setOnClickListener { // 뒤로가기 버튼
            finish()
        }

        btnDibs.setOnClickListener { // 찜 버튼

        }

        btnFinish.setOnClickListener { // 완독 버튼

        }

        btnWrite.setOnClickListener { // 독서록 쓰기 버튼

        }

        btnRead.setOnClickListener { // 독서록 읽기 버튼

        }
    }
}