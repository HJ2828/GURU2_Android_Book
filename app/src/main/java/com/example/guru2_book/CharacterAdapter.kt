package com.example.guru2_book

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class CharacterAdapter(var context: Context, var characterList: ArrayList<CharacterItem>) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return characterList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return characterList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View = View.inflate(context, R.layout.item_character, null)

        // 위젯 연결
        var icons: ImageView = view.findViewById(R.id.characterIcon)

        var characterItem: CharacterItem = characterList.get(position)        // 리스트 positon 번째 캐릭터 아이템

        icons.setImageResource(characterItem.icons!!)   // 캐릭터 아이템 이미지 변경

        return view
    }
}