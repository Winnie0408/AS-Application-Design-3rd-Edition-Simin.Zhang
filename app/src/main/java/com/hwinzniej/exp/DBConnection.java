package com.hwinzniej.exp;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBConnection extends SQLiteOpenHelper {
    private static final String Database_name = "market.db";  //数据库名
    private static final int Database_Version = 1;
    SQLiteDatabase db;
    public int id_this;
    Cursor cursor;
    //定义数据库名称及结构
    static String TABLE_NAME = "Goods";          //数据表名
    static String ID = "_id";                    //ID
    static String NAME = "name";       //商品名
    static String AMOUNT = "amount";           //数量
    static String PRICE = "price";       //单价
    static String UNIT = "unit"; //单位
    DBConnection(Context ctx) {
        super(ctx, Database_name, null, Database_Version);     //创建数据库
    }
    public void onCreate(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER primary key autoincrement, "
                + NAME + " text not null, "
                + AMOUNT + " text not null, "
                + PRICE + " text not null, "
                + UNIT + " text not null " + ");";
        database.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
