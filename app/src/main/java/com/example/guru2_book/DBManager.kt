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
        p0!!.execSQL("CREATE TABLE Book (ISBN text NOT NULL, BName text, BWriter text, BPublisher text, Bimage text, BDescription text, BPubdate text, PRIMARY KEY(ISBN));")
        // 목표 테이블
        p0!!.execSQL("CREATE TABLE Goal (GNum INTEGER NOT NULL, GCount INTEGER, PRIMARY KEY(GNum));")

        // 목표 데이터 삽입
        p0!!.execSQL("INSERT INTO Goal VALUES (0, 1);")
        p0!!.execSQL("INSERT INTO Goal VALUES (1, 10);")
        p0!!.execSQL("INSERT INTO Goal VALUES (2, 20);")
        p0!!.execSQL("INSERT INTO Goal VALUES (3, 30);")
        p0!!.execSQL("INSERT INTO Goal VALUES (4, 40);")
        p0!!.execSQL("INSERT INTO Goal VALUES (5, 50);")
        p0!!.execSQL("INSERT INTO Goal VALUES (6, 60);")
        p0!!.execSQL("INSERT INTO Goal VALUES (7, 70);")
        p0!!.execSQL("INSERT INTO Goal VALUES (8, 80);")
        p0!!.execSQL("INSERT INTO Goal VALUES (9, 90);")
        p0!!.execSQL("INSERT INTO Goal VALUES (10, 100);")
        p0!!.execSQL("INSERT INTO Goal VALUES (11, 110);")
        p0!!.execSQL("INSERT INTO Goal VALUES (12, 120);")
        p0!!.execSQL("INSERT INTO Goal VALUES (13, 130);")
        p0!!.execSQL("INSERT INTO Goal VALUES (14, 140);")
        p0!!.execSQL("INSERT INTO Goal VALUES (15, 150);")
        p0!!.execSQL("INSERT INTO Goal VALUES (16, 0);")

        // 계정 테이블
        p0!!.execSQL("CREATE TABLE Account (AEmail text NOT NULL, APassword text, ACurrentProfile INTEGER, PRIMARY KEY(AEmail));")
        // 읽은 책 테이블
        p0!!.execSQL("CREATE TABLE Read (RISBN text NOT NULL, REmail text NOT NULL, RNum INTEGER NOT NULL, RReadDate NUMERIC , RReportDate NUMERIC DEFAULT NULL, RReport text DEFAULT NULL, RRating REAL DEFAULT 0, PRIMARY KEY(RISBN, REmail, RNum), FOREIGN KEY(RISBN) REFERENCES Book(ISBN), FOREIGN KEY(REmail) REFERENCES Profile(PEmail), FOREIGN KEY(RNum) REFERENCES Profile(PNum));")
        // 찜한 책 테이블
        p0!!.execSQL("CREATE TABLE Want (WISBN text NOT NULL, WEmail text NOT NULL, WNum INTEGER NOT NULL, WWantDate NUMERIC , PRIMARY KEY(WISBN, WEmail, WNum), FOREIGN KEY(WISBN) REFERENCES Book(ISBN), FOREIGN KEY(WEmail) REFERENCES Profile(PEmail), FOREIGN KEY(WNum) REFERENCES Profile(PNum));")
        // 프로필 테이블
        p0!!.execSQL("CREATE TABLE Profile (PEmail text NOT NULL, PNum INTEGER NOT NULL, PName text, PImage text DEFAULT NULL, GoalNum INTEGER DEFAULT 0, PMainImgNum INTEGER DEFAULT -1, PRIMARY KEY(PEmail, PNum), FOREIGN KEY(PEmail) REFERENCES Account(AEmail),FOREIGN KEY(GoalNum) REFERENCES Goal(GNum) ON DELETE SET NULL);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}