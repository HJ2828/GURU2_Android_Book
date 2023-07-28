package com.example.guru2_book

data class NaverBookItem(
    val title: String,              // 제목
    val author: String,             // 저자
    val publisher: String,          // 츨판사
    val pubdate: String,            // 출간일
    val image: String,              // 섬네일 이미지 URL
    val description: String         // 책 소개
    // ISBN
)
