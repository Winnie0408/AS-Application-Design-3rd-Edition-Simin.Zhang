package com.hwinzniej.exp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBCreate extends SQLiteOpenHelper {
    static String Database_name = "PhoneBook.db";  //数据库名
    //定义数据库名称及结构
    String TABLE_NAME = "Users";          //数据表名
    String ID = "_id";                    //ID
    String USER_NAME = "user_name";       //用户名
    String ADDRESS = "address";           //地址
    String TELEPHONE = "telephone";       //联系电话
    String MAIL_ADDRESS = "mail_address"; //电子邮箱
    static int Database_Version = 1;

    DBCreate(Context ctx) {
        super(ctx, Database_name, null, Database_Version);     //创建数据库
    }
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER primary key autoincrement, "
                + USER_NAME + " text not null, "
                + TELEPHONE + " text not null, "
                + ADDRESS + " text not null, "
                + MAIL_ADDRESS + " text not null " + ");";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
