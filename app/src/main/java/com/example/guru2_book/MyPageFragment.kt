package com.example.guru2_book

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import de.hdodenhof.circleimageview.CircleImageView


class MyPageFragment : Fragment() {

    // 기본 정보 변수
    private var userEmail : String? = null // 사용자 이메일
    private var profileCount : Int = 1 // 사용자 프로필 개수

    // activity, context 변수
    var activity : MainActivity? = null
    lateinit var fContext : Context

    // 위젯 변수
    var profileLinearArray : ArrayList<LinearLayout> = ArrayList<LinearLayout>(4) // 프로필 레이아웃 ArrayList
    var profileImageArray : ArrayList<CircleImageView> = ArrayList<CircleImageView>(4) // 프로필 사진 위젯 ArrayList
    var profileNameTextArray : ArrayList<TextView> = ArrayList<TextView>(4) // 프로필 이름 텍스트 뷰 ArrayList
    var profileLinearIds = arrayOf(R.id.LinearProfile00, R.id.LinearProfile01, R.id.LinearProfile02, R.id.LinearProfile03) // 프로필 레이아웃 아이디 Array
    val profileImageIds = arrayOf(R.id.profile00, R.id.profile01, R.id.profile02, R.id.profile03) // 프로필 사진 아이디 Array
    val profileNameIds = arrayOf(R.id.nameText00, R.id.nameText01, R.id.nameText02, R.id.nameText03) // 프로필 이름 아이디 Array
    lateinit var btnAdd : Button // 프로필 추가 버튼
    lateinit var btnLogOut : Button // 로그아웃 버튼

    // 대화상자 변수
    val profileDlgArray = arrayOf("프로필 선택", "프로필 수정")

    // 데이터베이스 변수
    lateinit var dbManager: DBManager
    lateinit var bookDB : SQLiteDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainActivity // MainActivity 가져오기
        fContext = context // content 가져오기
    }

    override fun onDetach() {
        super.onDetach()
        activity = null // activity null로 설정
    }

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
        val view = inflater.inflate(R.layout.fragment_my_page, container, false) // xml 인플레이트

        dbManager = DBManager(activity, "BookDB", null, 1) // SQLiteOpenHelper 객체 생성

        profileCount = checkProfileCount() // 사용자 프로필 개수

        // 위젯 연결 및 리스너 연결
        btnAdd = view.findViewById<Button>(R.id.btnAdd)
        btnLogOut = view.findViewById<Button>(R.id.btnLogOut)
        for(index in 0..3){
            profileLinearArray.add(view.findViewById<LinearLayout>(profileLinearIds[index])) // 프로필 레이아웃 연결
            profileImageArray.add(view.findViewById<CircleImageView>(profileImageIds[index])) // 프로필 이미지 연결
            profileImageArray.get(index).setOnClickListener { // 프로필 이미지 리스너 연결
                var dlg = AlertDialog.Builder(fContext)
                dlg.setItems(profileDlgArray){ dialogInterface: DialogInterface, i: Int ->
                    when(i){
                        0 -> { // 프로필 선택을 선택했을 경우
                            changeProfile(index, profileCount) // 프로필 전환
                        }
                        1 -> { // 프로필 수정을 선택했을 경우
                            activity?.fragmentChangeInFragment(ChangeProfileFragment.newInstance(userEmail, index, profileCount)) // 프로필 수정 프래그먼트로 변경
                        }
                    }
                }
                dlg.setPositiveButton("취소", null)
                dlg.show()
            }
            profileNameTextArray.add(view.findViewById<TextView>(profileNameIds[index])) // 프로필 이름 텍스트뷰 연결
        }

        btnAdd.setOnClickListener { //  프로필 추가 버튼
            activity?.fragmentChangeInFragment(AddProfileFragment.newInstance(userEmail, profileCount)) // 프로필 추가 프래그넌트로 이동
        }

        btnLogOut.setOnClickListener { // 로그아웃 버튼
            var pref = activity?.getSharedPreferences("Login_info", 0)
            var editor = pref?.edit()
            editor?.clear()?.apply()

            val intent = Intent(fContext, LoginSignupActivity::class.java)
            startActivity(intent)
        }

        activeProfile() // 프로필 활성화 함수

        return view
    }

    // 프로필 개수 반환 함수
    private fun checkProfileCount() : Int {
        var count : Int = 0 // 현재 계정이 가지고 있는 프로필 개수

        bookDB = dbManager.readableDatabase // 데이터베이스 불러오기

        // 이메일에 해당하는 프로필들 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT * FROM Profile WHERE PEmail = '$userEmail';", null)

        count = cursor.count // 현재 계정이 가지고 있는 프로필 개수

        cursor.close()
        bookDB.close()

        return count // 개수 반환
    }

    // 프로필 전환 함수
    private fun changeProfile(pNum : Int, pCount : Int){
        for(index in 0 .. pCount - 1){
            if(index == pNum){ // 선택한 프로필일 경우
                profileImageArray.get(index).borderWidth = 10
            } else { // 선택한 프로필이 아닐 경우
                profileImageArray.get(index).borderWidth = 0
            }
        }

        // 현재 사용하는 프로필 변경
        bookDB = dbManager.writableDatabase // 데이터베이스 불러오기
        bookDB.execSQL("UPDATE Account SET ACurrentProfile = $pNum WHERE AEmail = '$userEmail';") // 계정 테이블의 현재 사용하고 있는 프로필 번호 바꾸기
        bookDB.close()
    }

    // 프로필 활성화 함수
    private fun activeProfile(){
        bookDB = dbManager.readableDatabase // 데이터베이스 불러오기

        // 이메일에 해당하는 프로필들 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT PName, PImage FROM Profile WHERE PEmail = '$userEmail' ORDER BY PNum ASC;", null)

        var i : Int = 0

        // 프로필 활성화
        while(cursor.moveToNext()){
            if(cursor.getString(1) == null){
                profileImageArray.get(i).setImageResource(R.drawable.mypage_circle)
            } else {
                profileImageArray.get(i).setImageURI(Uri.parse(cursor.getString(1).toString()))
            }

            profileNameTextArray.get(i).text = cursor.getString(0).toString()
            profileLinearArray.get(i).visibility = View.VISIBLE
            i++
        }

        cursor = bookDB.rawQuery("SELECT ACurrentProfile FROM Account WHERE AEmail = '$userEmail';", null)

        if(cursor.moveToNext()){
            changeProfile(cursor.getInt(0), profileCount) // 현재 사용하고 있는 프로필 표시
        }

        cursor.close()
        bookDB.close()

        if(profileCount != 4){
            btnAdd.visibility = View.VISIBLE
        } else {
            btnAdd.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(email : String?) =
            MyPageFragment().apply {
                arguments = Bundle().apply {
                    putString("USEREMAIL", email)
                }
            }
    }
}