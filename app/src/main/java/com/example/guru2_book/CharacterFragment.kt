package com.example.guru2_book

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView

class CharacterFragment : Fragment(), AdapterView.OnItemClickListener {

    // 위젯 변수
    lateinit var textCharacterCount: TextView   // 모은 캐릭터 수 텍스트뷰
    lateinit var gridViewCharacter: GridView    // 캐릭터 그리드뷰
    lateinit var btnBack: ImageButton   // 뒤로가기 버튼
    lateinit var btnEquip: ImageButton  // 장착하기 버튼

    // gridView 어댑터 사용을 위한 변수
    lateinit var arrayList: ArrayList<CharacterItem>
    lateinit var characterAdapter: CharacterAdapter

    // activity, context 변수
    var activity : MainActivity? = null
    lateinit var fContext : Context

    // 캐릭터 이미지 변수
    var characterShadow = arrayOf(
        R.drawable.character_egg_shadow, R.drawable.character_chick_shadow, R.drawable.character_bab_shadow,
        R.drawable.character_rabbit_shadow, R.drawable.character_polarbear_shadow
    )
    var characters = arrayOf(
        R.drawable.character_chick0, R.drawable.character_chick1, R.drawable.character_chick2, R.drawable.character_chick3,
        R.drawable.character_bab0, R.drawable.character_bab1, R.drawable.character_bab2, R.drawable.character_bab3,
        R.drawable.character_rabbit0, R.drawable.character_rabbit1, R.drawable.character_rabbit2, R.drawable.character_rabbit3,
        R.drawable.character_polarbear0, R.drawable.character_polarbear1, R.drawable.character_polarbear2, R.drawable.character_polarbear3
    )

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

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_character, container, false)

        // 위젯 연결
        textCharacterCount = view.findViewById<TextView>(R.id.textCharacterCount)
        gridViewCharacter = view.findViewById<GridView>(R.id.gridViewCharacter)
        btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnEquip = view.findViewById<ImageButton>(R.id.btnEquip)

        // gridView 어댑터 연결
        arrayList = ArrayList()
        arrayList = setDataList()
        characterAdapter = CharacterAdapter(requireContext(), arrayList!!)
        gridViewCharacter.adapter = characterAdapter
        gridViewCharacter.onItemClickListener = this

        // 리스너 연결
        btnBack.setOnClickListener { // 뒤로가기 버튼
            activity?.fragmentChangeInFragment(HomeFragment()) // 홈 프래그먼트로 변경
        }

        btnEquip.setOnClickListener { // 장착하기 버튼

        }

        return view
    }

    // 그리드뷰의 이미지를 캐릭터 기본 그림(그림자)으로 세팅해주는 함수
    private fun setDataList(): ArrayList<CharacterItem> {
        var arrayList: ArrayList<CharacterItem> = ArrayList()

        // 알닭 -> 0, 4, 8, 12 / 밥 -> 1, 5, 9, 13 / 토끼 -> 2, 6, 10, 14 / 곰 -> 3, 7, 11, 15 번째 데이터

        arrayList.add(CharacterItem(characterShadow[0]))    // chick0
        arrayList.add(CharacterItem(characterShadow[2]))    // bab0
        arrayList.add(CharacterItem(characterShadow[3]))    // rabbit0
        arrayList.add(CharacterItem(characterShadow[4]))    // polarbear0

        arrayList.add(CharacterItem(characterShadow[0]))    // chick1
        arrayList.add(CharacterItem(characterShadow[2]))    // bab1
        arrayList.add(CharacterItem(characterShadow[3]))    // rabbit1
        arrayList.add(CharacterItem(characterShadow[4]))    // polarbear1

        arrayList.add(CharacterItem(characterShadow[1]))    // chick2
        arrayList.add(CharacterItem(characterShadow[2]))    // bab2
        arrayList.add(CharacterItem(characterShadow[3]))    // rabbit2
        arrayList.add(CharacterItem(characterShadow[4]))    // polarbear2

        arrayList.add(CharacterItem(characterShadow[1]))    // chick3
        arrayList.add(CharacterItem(characterShadow[2]))    // bab3
        arrayList.add(CharacterItem(characterShadow[3]))    // rabbit3
        arrayList.add(CharacterItem(characterShadow[4]))    // polarbear3

        return arrayList
    }

    // 그리드뷰의 캐릭터 아이템 클릭했을 때 발생하는 함수
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var characterItem: CharacterItem = arrayList!!.get(position)

        // 캐릭터 클릭 했을 때 모은 캐릭터가 아니라면 이벤트 일어나지 않음

    }
}