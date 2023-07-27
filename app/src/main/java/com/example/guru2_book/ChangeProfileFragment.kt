package com.example.guru2_book

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView


class ChangeProfileFragment : Fragment() {

    // 기본 정보 변수
    private var userEmail : String? = null // 사용자 이메일
    private var profileNum : Int = 0 // 사용자 프로필 번호
    private var profileCount : Int = 0 // 사용자 프로필 개수

    // activity, context 변수
    var activity : MainActivity? = null
    lateinit var fContext : Context

    // 위젯 변수
    lateinit var edtName : EditText // 프로필 이름 에디트텍스트
    lateinit var edtPassword : EditText // 비밀번호 에디트텍스트
    lateinit var edtPasswordCheck : EditText // 비밀번호 확인 에디트텍스트
    lateinit var profileImage : CircleImageView // 프로필 사진 CircleImageView
    lateinit var btnBack : Button // 뒤로가기 버튼
    lateinit var btnOkay : Button // 수정 버튼
    lateinit var btnDel : Button // 프로필 삭제 버튼
    lateinit var linearPassword : LinearLayout // 변경할 비밀번호 레이아웃
    lateinit var linearPasswordCheck : LinearLayout // 변경할 비밀번호 확인 레이아웃

    // 데이터베이스 변수
    lateinit var dbManager: DBManager
    lateinit var bookDB : SQLiteDatabase

    // 기타 변수
    var imageUri: Uri? = null // 프로필 이미지 uri

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
            profileNum = it.getInt("PROFILENUM") // 프로필 번호 받기
            profileCount = it.getInt("PROFILECOUNT")  // 프로필 개수 받기
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_profile, container, false) // xml 인플레이트

        dbManager = DBManager(activity, "BookDB", null, 1) // SQLiteOpenHelper 객체 생성

        // 위젯 연결
        edtName = view.findViewById<EditText>(R.id.edtName)
        edtPassword = view.findViewById<EditText>(R.id.edtPassword)
        edtPasswordCheck = view.findViewById<EditText>(R.id.edtPasswordCheck)
        profileImage = view.findViewById<CircleImageView>(R.id.profileImage)
        btnBack = view.findViewById<Button>(R.id.btnBack) // 프로필 관련 백 버튼 아이디 추가
        btnOkay = view.findViewById<Button>(R.id.btnOkay)
        btnDel = view.findViewById<Button>(R.id.btnDel)
        linearPassword = view.findViewById<LinearLayout>(R.id.linearPassword)
        linearPasswordCheck = view.findViewById<LinearLayout>(R.id.linearPasswordCheck)

        // 메인 계정일 경우 비밀번호 변경 활성화 및 프로필 삭제 버튼 비활성화
        if(profileNum == 0){
            linearPassword.visibility = View.VISIBLE
            linearPasswordCheck.visibility = View.VISIBLE
            btnDel.visibility = View.GONE
        } else { // 메인 계정이 아닐 경우
            linearPassword.visibility = View.GONE
            linearPasswordCheck.visibility = View.GONE
            btnDel.visibility = View.VISIBLE
        }

        // 기존 데이터 가져오기
        bookDB = dbManager.readableDatabase // 데이터베이스 불러오기

        // 이메일과 프로필 번호에 해당하는 프로필들 가져오기
        var cursor : Cursor
        cursor = bookDB.rawQuery("SELECT PNum, PName, PImage FROM Profile WHERE PEmail = '$userEmail' AND PNum = $profileNum;", null)

        if(cursor.moveToNext()){
            // 프로필 이름, 사진 가져오기
            edtName.setText(cursor.getString(1).toString())
            if(cursor.getString(2).equals(null)){
                profileImage.setImageResource(R.drawable.mypage_circle)
            } else {
                imageUri = Uri.parse(cursor.getString(2).toString())
                profileImage.setImageURI(imageUri)
            }
        }

        cursor.close()
        bookDB.close()

        // 리스너 연결
        btnBack.setOnClickListener { // 뒤로가기 버튼
            activity?.fragmentChangeInFragment(MyPageFragment.newInstance(userEmail)) // 마이페이지 프래그먼트로 변경
        }
        btnDel.setOnClickListener { // 프로필 삭제 버튼
            bookDB = dbManager.writableDatabase // 데이터베이스 불러오기
            bookDB.execSQL("DELETE FROM Profile WHERE PEmail = '$userEmail' AND PNum = num;") // 해당 프로필 데이터 삭제

            // 프로필 번호 당겨오기
            if(profileNum + 1 < profileCount){
                for(i in profileNum + 1  .. profileCount - 1){
                    bookDB.execSQL("UPDATE Profile SET PNum = ${i - 1} WHERE PEmail = '$userEmail' AND PNum = $i ;")
                }
            }

            bookDB.execSQL("UPDATE Account SET ACurrentProfile = 0 WHERE AEmail = '$userEmail';") // 사용자가 사용하고 있는 프로필을 메인 프로필로 수정

            cursor.close()
            bookDB.close()

            activity?.fragmentChangeInFragment(MyPageFragment.newInstance(userEmail)) // 마이페이지 프래그먼트로 변경
        }
        btnOkay.setOnClickListener { // 수정 버튼
            var pName : String = edtName.text.toString()
            var pPassword : String = edtPassword.text.toString()
            var pPasswordCheck : String = edtPasswordCheck.text.toString()
            var pImage : String? = null

            if(pName.trim().isEmpty() || pPassword.trim().isEmpty() || pPasswordCheck.trim().isEmpty()){ // 빈 입력칸이 있을 경우
                // 입력 칸을 모두 채워달라는 토스트
                Toast.makeText(fContext, "입력 칸을 모두 채워주십시오.", Toast.LENGTH_SHORT).show() // 입력 칸을 모두 채워달라는 토스트
            } else if (!pPassword.equals(pPasswordCheck)){ // 비밀번호와 비밀번호 확인이 일치하지 않는 경우
                // 비밀번호와 비밀번호 확인이 일치하지 않다는 토스트
                Toast.makeText(fContext, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show() // 비밀번호와 비밀번호 확인이 일치하지 않다는 토스트
            } else {
                // 이미지 변경을 했을 경우
                if (imageUri != null) {
                    pImage = "'" + imageUri.toString() + "'"
                }

                bookDB = dbManager.writableDatabase // 데이터베이스 불러오기
                bookDB.execSQL("UPDATE Profile SET PEmail = '$pName', PImage = $pImage WHERE PEmail = '$userEmail' AND PNum = $profileNum ;") // 사용자가 프로필 데이터 수정

                // 메인 프로필일 경우
                if(profileNum == 0){
                    bookDB.execSQL("UPDATE Account SET APassword = '$pPassword' WHERE AEmail = '$userEmail';")
                }
            }

            bookDB.close()
        }
        profileImage.setOnClickListener {
            openGallery()
        }

        return view
    }

    // 갤러리 열기 함수
    fun openGallery(){
        // 갤러리 열기
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        activityResultLauncher.launch(intent);

    }

    // activityResultLauncher로 받은 값 처리(갤러리로부터 선택한 이미지 uri 가져오기)
    var activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) { // 이미지를 제대로 가져올 경우
            val intent = result.data
            imageUri = intent!!.data
            profileImage.setImageURI(imageUri) // 프로필 이미지 설정
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(email : String?, num : Int, count : Int) =
            ChangeProfileFragment().apply {
                arguments = Bundle().apply {
                    putString("USEREMAIL", email)
                    putInt("PROFILENUM", num)
                    putInt("PROFILECOUNT", count)
                }
            }
    }
}