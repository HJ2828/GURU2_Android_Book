package com.example.guru2_book

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverBookApi {
    @GET("v1/search/book.json")
    fun searchBooks(
        @Header("X-Naver-Client-Id") clientId: String,          // 네이버 책 api 클라이언트 id
        @Header("X-Naver-Client-Secret") clientSecret: String,  // 네이버 책 api 클라이언트 시크릿
        @Query("query") query: String,      // 검색어. UTF-8로 인코딩되어야함
        @Query("display") display: Int,     // 한 번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
        @Query("start") start: Int,         // 검색 시작 위치(기본값: 1, 최댓값: 1000)
        @Query("sort") sort: String         // 검색 결과 정렬 방법(sim: 정확도순으로 내림차순 정렬(기본값),date: 출간일순으로 내림차순 정렬)
    ): Call<NaverBookResponse>
}