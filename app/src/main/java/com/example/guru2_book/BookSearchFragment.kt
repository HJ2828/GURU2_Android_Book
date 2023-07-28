package com.example.guru2_book

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookSearchFragment : Fragment() {

    // 네이버 api 통신을 위한 변수 (애플리케이션 정보 소유자: HJ2828)
    private val clientId = "viMu_JjgHzyCvzj2a3g0"   // 클라이언트 id
    private val clientSecret = "gD7Mh7zaV4"         // 클라이언트 시크릿
    private val baseUrl = "https://openapi.naver.com/"  // 네이버 기본 api url

    // 위젯 변수
    lateinit var edtSearch: EditText
    lateinit var btnSearch: ImageButton
    lateinit var imgViewText: ImageView
    lateinit var recyclerViewBook: RecyclerView

    // 리사이클러뷰 어댑터 변수
    lateinit var bookAdapter: BookSearchAdapter

    // 받아온 정보 저장할 리스트 변수
    val bookList: MutableList<NaverBookItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_search, container, false)

        // 위젯 연결
        edtSearch = view.findViewById<EditText>(R.id.edtSearch)
        btnSearch = view.findViewById<ImageButton>(R.id.btnSearch)
        imgViewText = view.findViewById<ImageView>(R.id.imgViewText)
        recyclerViewBook = view.findViewById<RecyclerView>(R.id.recyclerViewBook)

        // RecyclerView 어댑터 생성 및 설정
        bookAdapter = BookSearchAdapter(emptyList())
        recyclerViewBook.layoutManager = LinearLayoutManager(context)      //LinearLayoutManager: 아이템들을 수직 방향으로 나열
        recyclerViewBook.adapter = bookAdapter
        recyclerViewBook.setHasFixedSize(true)      // setHasFixedSize(true): 리사이클러뷰 크기 고정

        // 리스너 연결
        btnSearch.setOnClickListener {  // 검색 버튼
            imgViewText.setImageResource(R.drawable.booksearch_resulttext)    // '검색 결과' 이미지로 변경
            val query = edtSearch.text.toString().trim()    // editText에 검색한 검색어
            if (query.isNotEmpty()) {  // 검색하지 않고 버튼 클릭(검색어가 비어있음)
                searchBooks(query)     // api를 통해 책 정보 얻는 함수 호출
            }
        }

        edtSearch.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {    // 키보드 done 버튼 또는 엔터키 누를 경우
                // 키패드 내리기
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(edtSearch.windowToken, 0)
                true
            } else {
                false
            }
        }

        return view
    }

    // 네이버 책 api 사용해 정보를 얻기 위한 함수
    fun searchBooks(query: String) {

        // Retrofi 객체 생성 (객체 이용하여 api 인터페이스를 생성, 해당 인터페이스를 사용해 api 호출)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val naverBookApi = retrofit.create(NaverBookApi::class.java)    // NaverBookApi 인터페이스를 사용해 api 인터페이스 생성
        val call = naverBookApi.searchBooks(clientId, clientSecret, query, 100, 1, "sim")     // query(검색어)를 기반으로 api 호출

        call.enqueue(object : Callback<NaverBookResponse> {     // api 응답 처리
            override fun onResponse(call: Call<NaverBookResponse>, response: Response<NaverBookResponse>) {     // api 응답 성공
                if (response.isSuccessful) {    // 응답 성공할 경우
                    val bookResponse = response.body()  // 응답 데이터 가져오기
                    val items = bookResponse?.items     // 가져온 데이터 아이템
                    if(items != null) {     // 아이템이 비어있지 않을 경우
                        bookAdapter.updateData(items)   // RecyclerView의 데이터를 업데이트
                    }
                } else {    // 실패할 경우
                    println("API call failed: ${response.code()}")  // 응답 실패 코드
                }
            }

            override fun onFailure(call: Call<NaverBookResponse>, t: Throwable) {       // api 응답 실패
                println("API call failed: ${t.message}")
            }
        })
    }
}