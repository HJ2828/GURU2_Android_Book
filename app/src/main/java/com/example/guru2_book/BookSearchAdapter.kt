package com.example.guru2_book

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookSearchAdapter(private var bookList: List<NaverBookItem>) : RecyclerView.Adapter<BookSearchAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]       // position번째 리스트 정보를 book 변수에 전달
        holder.bind(book)       // 리사이큘러뷰에 출력하기 위한 함수 호출

        holder.itemView.setOnClickListener {    // 아이템 클릭 이벤트
            val intent = Intent(holder.itemView.context, BookInfoActivity::class.java)
            intent.putExtra("bookTitle", bookList[position].title)
            intent.putExtra("bookAuthor", bookList[position].author)
            intent.putExtra("bookPublisher", bookList[position].publisher)
            intent.putExtra("bookPubDate", holder.formatDate(bookList[position].pubdate))
            intent.putExtra("bookImgUrl", bookList[position].image)
            intent.putExtra("bookStory", bookList[position].description)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int = bookList.size

    // 새로운 데이터로 기존 bookList를 갱신하고 리사이클러뷰를 갱신하는 함수
    fun updateData(newList: List<NaverBookItem>) {
        bookList = newList
        notifyDataSetChanged()   // 어댑터에 데이터 변경 알림
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 위젯 연결
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val authorTextView: TextView = itemView.findViewById(R.id.textViewAuthor)
        private val publisherTextView: TextView = itemView.findViewById(R.id.textViewPublisher)
        private val publishedDateTextView: TextView = itemView.findViewById(R.id.textViewPublishedDate)
        private val bookImageView: ImageView = itemView.findViewById(R.id.imageViewBook)

        // 리사이큘러뷰에 출력
        fun bind(book: NaverBookItem) {
            titleTextView.text = book.title                                      // 제목
            authorTextView.text = "저자 | " + book.author                         // 저자
            publisherTextView.text = "출판사 | " + book.publisher                 // 츨판사
            publishedDateTextView.text = "출간일 | " + formatDate(book.pubdate)   // 출간일

            // 섬네일 이미지 URL
            Glide.with(itemView.context)
                .load(book.image)
                .into(bookImageView)
        }

        // 출간일 변경 함수 (ex. 20230728 -> 0223년 07월 28일)
        fun formatDate(dateStr: String): String {
            val year = dateStr.substring(0, 4)
            val month = dateStr.substring(4, 6)
            val day = dateStr.substring(6, 8)

            return "${year}년 ${month}월 ${day}일"
        }
    }
}