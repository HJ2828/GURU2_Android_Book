# GURU2_Book
2023학년도 GURU2 Android

### 소개
- 안드로이드 코틀린을 이용한 독서 기록 어플 제작
- 독서 기록 어플: 책메이트
- 대상: 아이, 아이가 있는 부모

## 팀 구성
- 개발 2명, 디자인 1명
- 개발

## 기능
- 로그인, 회원가입
- 멀티프로필 (최대 4개), 프로필 추가&수정&삭제
- 캐릭터 도감(책 읽은 권 수에 따라 16개의 캐릭터를 얻을 수 있음)
- ASMR (비, 천둥, 카페, 파도, 바람 소리)
- 책 검색(네이버 책 api를 이용하여 제목, 저자, 출판일, 출판사, 책소개, isbn 정보를 얻을 수 있음)
- 책 스캔(책 바코드로 isbn 정보를 얻어올 수 있음)
- 책 찜, 완독(책장에서 확인 가능)
- 독후감, 별점 작성&수정&삭제

## 개발 환경
- Android Kotlin
- sdk 33(API 33: Android 13.0(Tiramisu))
- jdk jbr-17

```
implementation 'de.hdodenhof:circleimageview:3.1.0' //CircleImageView

implementation 'androidx.core:core-ktx:1.8.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.5.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
testImplementation 'junit:junit:4.13.2'
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

implementation 'com.github.bumptech.glide:glide:4.12.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'

// 그리드뷰
implementation 'androidx.gridlayout:gridlayout:1.0.0'

// 리사이큘러뷰
implementation("androidx.recyclerview:recyclerview:1.2.1")
implementation("androidx.recyclerview:recyclerview-selection:1.1.0")

// api 통신
implementation 'com.google.code.gson:gson:2.8.8'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// 스캔
implementation 'com.journeyapps:zxing-android-embedded:4.3.0'
```
