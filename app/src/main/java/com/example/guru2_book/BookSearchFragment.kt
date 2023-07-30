package com.example.guru2_book

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookSearchFragment : Fragment() {

    // 네이버 api 통신을 위한 변수 (네이버 오픈 소스에 애플리케이션을 등록하여 얻은 클라이언트 id와 시크릿)
    private val clientId = "viMu_JjgHzyCvzj2a3g0"   // 클라이언트 id
    private val clientSecret = "gD7Mh7zaV4"         // 클라이언트 시크릿
    private val baseUrl = "https://openapi.naver.com/"  // 네이버 기본 api url

    // 위젯 변수
    lateinit var edtSearch: EditText    // 검색 에디트텍스트
    lateinit var btnSearch: ImageButton // 검색 버튼
    lateinit var imgViewText: ImageView // '검색 예' 또는 '검색 결과' 이미지뷰
    lateinit var recyclerViewBook: RecyclerView // 책 정보 리사이큘러뷰
    lateinit var btnScan: ImageButton   // 책 스캔 버튼

    // 리사이클러뷰 어댑터 변수
    lateinit var bookAdapter: BookSearchAdapter

    // 받아온 정보 저장할 리스트 변수
    val bookList: MutableList<NaverBookItem> = mutableListOf()

    // 기본 정보 변수
    private var userEmail : String? = null // 사용자 이메일

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmail = it.getString("USEREMAIL") // 사용자 이메일 받기
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
        btnScan = view.findViewById<ImageButton>(R.id.btnScan)

        // RecyclerView 어댑터 생성 및 설정
        bookAdapter = BookSearchAdapter(mutableListOf(), userEmail)
        recyclerViewBook.layoutManager = LinearLayoutManager(context)      //LinearLayoutManager: 아이템들을 수직 방향으로 나열
        recyclerViewBook.adapter = bookAdapter
        recyclerViewBook.setHasFixedSize(true)      // setHasFixedSize(true): 리사이클러뷰 크기 고정

        // 리스너 연결
        btnScan.setOnClickListener { // 책 스캔 버튼
            val options = ScanOptions()

            options.setPrompt("책 바코드를 스캔하세요")
            options.setCameraId(0) // 후면 카메라 사용 (1: 전면 카메라)
            options.setBeepEnabled(false)   // 스캔 시 삑 소리 유무

            barcodeLauncher.launch(options) // 바코드 스캔 실행
        }

        btnSearch.setOnClickListener {  // 검색 버튼
            imgViewText.setImageResource(R.drawable.booksearch_resulttext)    // '검색 결과' 이미지로 변경
            val query = edtSearch.text.toString().trim()    // editText에 검색한 검색어
            if (query.isNotEmpty()) {  // 검색 버튼 클릭
                searchBooks(query)     // api를 통해 책 정보 얻는 함수 호출
            } else {  // 검색하지 않고 버튼 클릭(검색어가 비어있음)
                bookAdapter.clearData()
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

    companion object {

        @JvmStatic
        fun newInstance(email : String?) =
            BookSearchFragment().apply {
                arguments = Bundle().apply {
                    putString("USEREMAIL", email)
                }
            }
    }

    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>( // 바코드 스캔(ISBN 스캔)
        ScanContract()
    ) {
            result: ScanIntentResult ->
        if (result.contents == null) {  // 스캔하여 얻은 값이 없을 경우
            Toast.makeText(context, "스캔이 취소되었습니다", Toast.LENGTH_SHORT).show()
        } else {    // 스캔하여 값을 얻었을 경우
            imgViewText.setImageResource(R.drawable.booksearch_resulttext)    // '검색 결과' 이미지로 변경
            edtSearch.setText(result.contents)  // 값(ISBN)을 검색 에디트 텍스트에 표시
            searchBooks(result.contents)    // 값(ISBN)을 넣어 책 정보 얻기
        }
    }
}