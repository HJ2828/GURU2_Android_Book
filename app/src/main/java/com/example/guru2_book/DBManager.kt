package com.example.guru2_book

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// SQLiteOpenHelper 클래스
class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(p0: SQLiteDatabase?) {
        // 책 테이블
        p0!!.execSQL("CREATE TABLE Book (ISBN INTEGER NOT NULL, BName text, BWriter text, BPublisher text, Bimage text, BDescription text, BPubdate DATETIME, PRIMARY KEY(ISBN));")
        // 목표 테이블
        p0!!.execSQL("CREATE TABLE Goal (GNum INTEGER NOT NULL, GCount INTEGER, GCharacter text, PRIMARY KEY(GNum));")

        // 목표 데이터 삽입
        p0!!.execSQL("INSERT INTO Goal VALUES (0, 10, 'image1');")
        p0!!.execSQL("INSERT INTO Goal VALUES (1, 20, 'image2');")
        p0!!.execSQL("INSERT INTO Goal VALUES (2, 30, 'image3');")
        p0!!.execSQL("INSERT INTO Goal VALUES (3, 40, 'image4');")
        p0!!.execSQL("INSERT INTO Goal VALUES (4, 50, 'image5');")
        p0!!.execSQL("INSERT INTO Goal VALUES (5, 60, 'image6');")
        p0!!.execSQL("INSERT INTO Goal VALUES (6, 70, 'image7');")
        p0!!.execSQL("INSERT INTO Goal VALUES (7, 80, 'image8');")
        p0!!.execSQL("INSERT INTO Goal VALUES (8, 90, 'image9');")
        p0!!.execSQL("INSERT INTO Goal VALUES (9, 100, 'image10');")
        p0!!.execSQL("INSERT INTO Goal VALUES (10, 110, 'image11');")
        p0!!.execSQL("INSERT INTO Goal VALUES (11, 120, 'image12');")
        p0!!.execSQL("INSERT INTO Goal VALUES (12, 130, 'image13');")
        p0!!.execSQL("INSERT INTO Goal VALUES (13, 140, 'image14');")
        p0!!.execSQL("INSERT INTO Goal VALUES (14, 150, 'image15');")
        p0!!.execSQL("INSERT INTO Goal VALUES (15, 160, 'image16');")

        // 계정 테이블
        p0!!.execSQL("CREATE TABLE Account (AEmail text NOT NULL, APassword text, ACurrentProfile INTEGER, PRIMARY KEY(AEmail));")
        // 읽은 책 테이블
        p0!!.execSQL("CREATE TABLE Read (RISBN INTEGER NOT NULL, REmail text NOT NULL, RReport text, RRating REAL, PRIMARY KEY(RISBN, REmail), FOREIGN KEY(RISBN) REFERENCES Book(ISBN), FOREIGN KEY(REmail) REFERENCES Profile(PEmail));")
        // 찜한 책 테이블
        p0!!.execSQL("CREATE TABLE Want (WISBN INTEGER NOT NULL, WEmail text NOT NULL, PRIMARY KEY(WISBN, WEmail), FOREIGN KEY(WISBN) REFERENCES Book(ISBN), FOREIGN KEY(WEmail) REFERENCES Profile(PEmail));")
        // 프로필 테이블
        p0!!.execSQL("CREATE TABLE Profile (PEmail text NOT NULL, PNum INTEGER NOT NULL, PName text, PImage text, GoalNum INTEGER DEFAULT 0, PRIMARY KEY(PEmail, PNum), FOREIGN KEY(PEmail) REFERENCES Account(AEmail),FOREIGN KEY(GoalNum) REFERENCES Goal(GNum) ON DELETE SET NULL);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}