package com.framgia.project1.demodiagramongooglemap.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nguyenxuantung on 24/06/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME="distance_element";
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_POINT ="point";
    public static final String TABLE_POINT_MANAGE ="point_manage";
    public static final String TABLE_EDGE="edge";

    public static final String CREATE_POINT=" create table "
        +TABLE_POINT+ " ("
        +" id integer primary key autoincrement, latitude double,   longtitude double,  id_other_point integer) ";
    public static final String CREATE_DISTANCE="create table "
        +TABLE_EDGE+"(" +"  id_start integer,  id_end integer,  edge float)";
    public static final String CREATE_POINT_MANAGE="create table "
        +TABLE_POINT_MANAGE+"("
        +"  id_start integer,  id_end integer)";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DISTANCE);
        db.execSQL(CREATE_POINT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
