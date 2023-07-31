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

    // 위젯 변수
    lateinit var btnPlay1: ImageButton  // 재생 버튼1
    lateinit var btnPlay2: ImageButton  // 재생 버튼2
    lateinit var btnPlay3: ImageButton  // 재생 버튼3
    lateinit var btnPlay4: ImageButton  // 재생 버튼4
    lateinit var btnPlay5: ImageButton  // 재생 버튼5
    lateinit var seekBarVolume1: SeekBar    // 볼륨 조절 시크바1
    lateinit var seekBarVolume2: SeekBar    // 볼륨 조절 시크바2
    lateinit var seekBarVolume3: SeekBar    // 볼륨 조절 시크바3
    lateinit var seekBarVolume4: SeekBar    // 볼륨 조절 시크바4
    lateinit var seekBarVolume5: SeekBar    // 볼륨 조절 시크바5

    // 미디어 플레이어 객체 생성을 위한 변수
    lateinit var asmr1: MediaPlayer
    lateinit var asmr2: MediaPlayer
    lateinit var asmr3: MediaPlayer
    lateinit var asmr4: MediaPlayer
    lateinit var asmr5: MediaPlayer

    // 재생 중인지 아닌지 확인하기 위한 변수
    private var isPlaying: Array<Boolean> = arrayOf(false, false, false, false, false)

    // 화면 이동 시 토스트 메시지 띄움 여부 확인 변수
    private var isToast: Boolean = false

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

        // 초기 시크바 움직이지 못하게 설정
        seekBarVolume1.isEnabled = false
        seekBarVolume2.isEnabled = false
        seekBarVolume3.isEnabled = false
        seekBarVolume4.isEnabled = false
        seekBarVolume5.isEnabled = false

        // 리스너 연결
        btnPlay1.setOnClickListener {// 첫번째(비소리) 재생, 일시정지 버튼
            isPlaying[0] = !isPlaying[0]
            seekBarVolume1.isEnabled = isPlaying[0]     // 시크바 활성화

            if (isPlaying[0]) {    // 재생 중
                btnPlay1.setImageResource(R.drawable.asmr_pause)    // 멈춤 이미지로 변경
                asmr1 = MediaPlayer.create(context, R.raw.rain)
                asmr1.isLooping = true  // 반복재생
                asmr1.start()   // 재생
            }
            else {      // 재생 멈춤
                btnPlay1.setImageResource(R.drawable.asmr_play)    // 재생 이미지로 변경
                asmr1.stop()    // 멈춤
            }
        }

        btnPlay2.setOnClickListener {// 두번째(천둥 소리) 재생, 일시정지 버튼
            isPlaying[1] = !isPlaying[1]
            seekBarVolume2.isEnabled = isPlaying[1]     // 시크바 활성화

            if (isPlaying[1]) {    // 재생 중
                btnPlay2.setImageResource(R.drawable.asmr_pause)    // 멈춤 이미지로 변경
                asmr2 = MediaPlayer.create(context, R.raw.thunderstorm)
                asmr2.isLooping = true  // 반복재생
                asmr2.start()   // 재생
            }
            else {      // 재생 멈춤
                btnPlay2.setImageResource(R.drawable.asmr_play)    // 재생 이미지로 변경
                asmr2.stop()    // 멈춤
            }
        }

        btnPlay3.setOnClickListener {// 세번째(카페 소리) 재생, 일시정지 버튼
            isPlaying[2] = !isPlaying[2]
            seekBarVolume3.isEnabled = isPlaying[2]     // 시크바 활성화

            if (isPlaying[2]) {    // 재생 중
                btnPlay3.setImageResource(R.drawable.asmr_pause)    // 멈춤 이미지로 변경
                asmr3 = MediaPlayer.create(context, R.raw.cafe)
                asmr3.isLooping = true  // 반복재생
                asmr3.start()   // 재생
            }
            else {      // 재생 멈춤
                btnPlay3.setImageResource(R.drawable.asmr_play)    // 재생 이미지로 변경
                asmr3.stop()    // 멈춤
            }
        }

        btnPlay4.setOnClickListener {// 네번째(파도 소리) 재생, 일시정지 버튼
            isPlaying[3] = !isPlaying[3]
            seekBarVolume4.isEnabled = isPlaying[3]     // 시크바 활성화

            if (isPlaying[3]) {    // 재생 중
                btnPlay4.setImageResource(R.drawable.asmr_pause)    // 멈춤 이미지로 변경
                asmr4 = MediaPlayer.create(context, R.raw.wave)
                asmr4.isLooping = true  // 반복재생
                asmr4.start()   // 재생
            }
            else {      // 재생 멈춤
                btnPlay4.setImageResource(R.drawable.asmr_play)    // 재생 이미지로 변경
                asmr4.stop()    // 멈춤
            }
        }

        btnPlay5.setOnClickListener {// 다섯번째(바다 소리) 재생, 일시정지 버튼
            isPlaying[4] = !isPlaying[4]
            seekBarVolume5.isEnabled = isPlaying[4]     // 시크바 활성화

            if (isPlaying[4]) {    // 재생 중
                btnPlay5.setImageResource(R.drawable.asmr_pause)    // 멈춤 이미지로 변경
                asmr5 = MediaPlayer.create(context, R.raw.wind)
                asmr5.isLooping = true  // 반복재생
                asmr5.start()   // 재생
            }
            else {      // 재생 멈춤
                btnPlay5.setImageResource(R.drawable.asmr_play)    // 재생 이미지로 변경
                asmr5.stop()    // 멈춤
            }
        }

        // 볼륨 조절 시크바
        seekBarVolume1.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr1.setVolume(0.05f * p1, 0.05f * p1) // 볼륨 조절
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {   // 시크바 시작
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {    // 시크바 끝
            }
        })

        seekBarVolume2.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr2.setVolume(0.05f * p1, 0.05f * p1) // 볼륨 조절
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {   // 시크바 시작
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {    // 시크바 끝
            }
        })

        seekBarVolume3.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr3.setVolume(0.05f * p1, 0.05f * p1) // 볼륨 조절
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {   // 시크바 시작
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {    // 시크바 끝
            }
        })

        seekBarVolume4.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr4.setVolume(0.05f * p1, 0.05f * p1) // 볼륨 조절
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {   // 시크바 시작
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {    // 시크바 끝
            }
        })

        seekBarVolume5.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {    // 시크바 변할 때
                asmr5.setVolume(0.05f * p1, 0.05f * p1) // 볼륨 조절
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
            if(isPlaying[i]) {  // 재생 중일 경우
                when (i) {      // 소리 멈춤
                    0 -> asmr1.pause()
                    1 -> asmr2.pause()
                    2 -> asmr3.pause()
                    3 -> asmr4.pause()
                    4 -> asmr5.pause()
                }
                isToast = true
            }
        }
        if (isToast) {
            Toast.makeText(context, "asmr이 중지되었습니다.", Toast.LENGTH_SHORT).show() // 소리 재생 중지 토스트
        }
    }

    // 소리 재생 중일 때, 휴대폰 홈으로 갔다가 다시 앱으로 돌아올 경우 소리 재생
    override fun onResume() {
        super.onResume()
        for (i in isPlaying.indices) {
            if(isPlaying[i]) {  // 재생 중
                when (i) {      // 소리 재생
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