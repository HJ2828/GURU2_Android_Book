package com.example.guru2_book

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast

class AsmrFragment : Fragment() {

    lateinit var btnPlay1: ImageButton
    lateinit var btnPlay2: ImageButton
    lateinit var btnPlay3: ImageButton
    lateinit var btnPlay4: ImageButton
    lateinit var btnPlay5: ImageButton

    lateinit var seekBarVolume1: SeekBar
    lateinit var seekBarVolume2: SeekBar
    lateinit var seekBarVolume3: SeekBar
    lateinit var seekBarVolume4: SeekBar
    lateinit var seekBarVolume5: SeekBar

    // 미디어 플레이어 객체 생성을 위한 변수
    lateinit var asmr1: MediaPlayer
    lateinit var asmr2: MediaPlayer
    lateinit var asmr3: MediaPlayer
    lateinit var asmr4: MediaPlayer
    lateinit var asmr5: MediaPlayer

    // 재생 중인지 아닌지 확인하기 위한 변수
    private var isPlaying: Array<Boolean> = arrayOf(false, false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_asmr, container, false)

        // 위젯 연결
        btnPlay1 = view.findViewById(R.id.btnPlay1)
        btnPlay2 = view.findViewById(R.id.btnPlay2)
        btnPlay3 = view.findViewById(R.id.btnPlay3)
        btnPlay4 = view.findViewById(R.id.btnPlay4)
        btnPlay5 = view.findViewById(R.id.btnPlay5)

        seekBarVolume1 = view.findViewById(R.id.seekBarVolume1)
        seekBarVolume2 = view.findViewById(R.id.seekBarVolume2)
        seekBarVolume3 = view.findViewById(R.id.seekBarVolume3)
        seekBarVolume4 = view.findViewById(R.id.seekBarVolume4)
        seekBarVolume5 = view.findViewById(R.id.seekBarVolume5)

        // 재생, 일시정지 버튼
        btnPlay1.setOnClickListener {
            isPlaying[0] = !isPlaying[0]

            if (isPlaying[0]) {    // 재생 중
                btnPlay1.setImageResource(R.drawable.asmr_pause)
                asmr1 = MediaPlayer.create(context, R.raw.rain)
                asmr1.isLooping = true
                asmr1.start()
            }
            else {      // 재생 멈춤
                btnPlay1.setImageResource(R.drawable.asmr_play)
                asmr1.stop()
            }
        }

        btnPlay2.setOnClickListener {
            isPlaying[1] = !isPlaying[1]

            if (isPlaying[1]) {    // 재생 중
                btnPlay2.setImageResource(R.drawable.asmr_pause)
                asmr2 = MediaPlayer.create(context, R.raw.firewood)
                asmr2.isLooping = true
                asmr2.start()
            }
            else {      // 재생 멈춤
                btnPlay2.setImageResource(R.drawable.asmr_play)
                asmr2.stop()

            }
        }

        btnPlay3.setOnClickListener {
            isPlaying[2] = !isPlaying[2]

            if (isPlaying[2]) {    // 재생 중
                btnPlay3.setImageResource(R.drawable.asmr_pause)
                asmr3 = MediaPlayer.create(context, R.raw.cafe)
                asmr3.isLooping = true
                asmr3.start()
            }
            else {      // 재생 멈춤
                btnPlay3.setImageResource(R.drawable.asmr_play)
                asmr3.stop()
            }
        }

        btnPlay4.setOnClickListener {
            isPlaying[3] = !isPlaying[3]

            if (isPlaying[3]) {    // 재생 중
                btnPlay4.setImageResource(R.drawable.asmr_pause)
                asmr4 = MediaPlayer.create(context, R.raw.wave)
                asmr4.isLooping = true
                asmr4.start()
            }
            else {      // 재생 멈춤
                btnPlay4.setImageResource(R.drawable.asmr_play)
                asmr4.stop()
            }
        }

        btnPlay5.setOnClickListener {
            isPlaying[4] = !isPlaying[4]

            if (isPlaying[4]) {    // 재생 중
                btnPlay5.setImageResource(R.drawable.asmr_pause)
                asmr5 = MediaPlayer.create(context, R.raw.wind)
                asmr5.isLooping = true
                asmr5.start()
            }
            else {      // 재생 멈춤
                btnPlay5.setImageResource(R.drawable.asmr_play)
                asmr5.stop()
            }
        }

        // 볼륨 조절 시크바
        seekBarVolume1.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr1.setVolume(0.05f * p1, 0.05f * p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {   // 시크바 시작
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {    // 시크바 끝
            }
        })

        seekBarVolume2.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr2.setVolume(0.05f * p1, 0.05f * p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {   // 시크바 시작
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {    // 시크바 끝
            }
        })

        seekBarVolume3.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr3.setVolume(0.05f * p1, 0.05f * p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {   // 시크바 시작
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {    // 시크바 끝
            }
        })

        seekBarVolume4.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr4.setVolume(0.05f * p1, 0.05f * p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {   // 시크바 시작
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {    // 시크바 끝
            }
        })

        seekBarVolume5.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr5.setVolume(0.05f * p1, 0.05f * p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {   // 시크바 시작
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {    // 시크바 끝
            }
        })

        return view
    }

    // asmr 프래그먼트를 벗어나면 소리 멈춤
    override fun onStop() {
        super.onStop()
        for (i in isPlaying.indices) {
            if(isPlaying[i]) {
                when (i) {
                    0 -> asmr1.pause()
                    1 -> asmr2.pause()
                    2 -> asmr3.pause()
                    3 -> asmr4.pause()
                    4 -> asmr5.pause()
                }
            }
        }
        Toast.makeText(context, "asmr이 중지되었습니다", Toast.LENGTH_SHORT).show()
    }

    // 소리 재생 중일 때, 휴대폰 홈으로 갔다가 다시 앱으로 돌아올 경우 소리 재생
    override fun onResume() {
        super.onResume()
        for (i in isPlaying.indices) {
            if(isPlaying[i]) {  // 재생 중
                when (i) {
                    0 -> asmr1.start()
                    1 -> asmr2.start()
                    2 -> asmr3.start()
                    3 -> asmr4.start()
                    4 -> asmr5.start()
                }
            }
        }
    }


}