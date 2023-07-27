package com.example.guru2_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2_book.BookAdapter
import com.example.guru2_book.BookItem
import kotlinx.coroutines.*



/**
 * A simple [Fragment] subclass.
 * Use the [BookSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookSearchFragment : Fragment() {

    private lateinit var recyclerViewBooks: RecyclerView
    private lateinit var edtSearchBook: EditText
    private lateinit var bookAdapter: BookAdapter
    private var bookList = emptyList<BookItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_search, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookSearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookSearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}