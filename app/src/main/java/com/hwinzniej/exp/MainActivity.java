package com.hwinzniej.exp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static EditText nameInput;
    static EditText amountInput;
    static EditText priceInput;
    static EditText unitInput;
    Cursor cursor;
    Button createBtn, openBtn, upBtn, downBtn;
    Button addBtn, updateBtn, deleteBtn, sumBtn;
    TextView goodsList, priceTotal;
    ListView listView;
    SQLiteDatabase db;
    DBConnection helper;
    public int id_this;
    //定义数据库名称及结构
    static String TABLE_NAME = "Goods";          //数据表名
    static String ID = "_id";                    //ID
    static String NAME = "name";       //商品名
    static String AMOUNT = "amount";           //数量
    static String PRICE = "price";       //单价
    static String UNIT = "unit"; //单位
    Double totalTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.EditText01);
        amountInput = findViewById(R.id.EditText02);
        priceInput = findViewById(R.id.EditText03);
        unitInput = findViewById(R.id.EditText04);

        goodsList = findViewById(R.id.goodsList);
        priceTotal = findViewById(R.id.priceTotal);
        listView = findViewById(R.id.listView1);

        createBtn = findViewById(R.id.createDatabase1);
        createBtn.setOnClickListener(new ClickEvent());
        openBtn = findViewById(R.id.openDatabase1);
        openBtn.setOnClickListener(new ClickEvent());

        upBtn = findViewById(R.id.up1);
        upBtn.setOnClickListener(new ClickEvent());
        downBtn = findViewById(R.id.down1);
        downBtn.setOnClickListener(new ClickEvent());
        addBtn = findViewById(R.id.add1);
        addBtn.setOnClickListener(new ClickEvent());
        updateBtn = findViewById(R.id.save1);
        updateBtn.setOnClickListener(new ClickEvent());
        deleteBtn = findViewById(R.id.delete1);
        deleteBtn.setOnClickListener(new ClickEvent());
        sumBtn = findViewById(R.id.sum1);
        sumBtn.setOnClickListener(new ClickEvent());
    }

    public ArrayList<Map<String, String>> getData() {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        SQLiteDatabase db1 = helper.getReadableDatabase();
        Cursor cursor = db1.query("Goods", null, null, null, null, null, null);
        totalTotal = 0.0;
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("amount", cursor.getString(cursor.getColumnIndex("amount")));
            map.put("price", cursor.getString(cursor.getColumnIndex("price")));
            map.put("unit", cursor.getString(cursor.getColumnIndex("unit")));
            Double total = Double.parseDouble(cursor.getString(cursor.getColumnIndex("amount")))
                    * Double.parseDouble(cursor.getString(cursor.getColumnIndex("price")));
            totalTotal += total;
            map.put("total", String.valueOf(total));
            list.add(map);
        }
        cursor.close();
        db1.close();
        return list;
    }

    class ClickEvent implements WebView.OnClickListener {
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.createDatabase1) {
                helper = new DBConnection(MainActivity.this);
                db = helper.getWritableDatabase();
            } else if (id == R.id.openDatabase1) {
                SQLiteDatabase db1;
                db1 = openOrCreateDatabase("market.db", Context.MODE_PRIVATE, null);
                //查询Users数据表
                cursor = db1.query("Goods", null, null, null, null, null, null);
                cursor.moveToNext();
                upBtn.setClickable(true);
                downBtn.setClickable(true);
                deleteBtn.setClickable(true);
                updateBtn.setClickable(true);
                helper = new DBConnection(MainActivity.this);
                db = helper.getWritableDatabase();
            } else if (id == R.id.up1) {
                if (cursor == null)
                    refreshCursor();
                if (!cursor.isFirst()) {
                    cursor.moveToPrevious();
                }
                datashow();
            } else if (id == R.id.down1) {
                if (cursor == null)
                    refreshCursor();
                if (!cursor.isLast())
                    cursor.moveToNext();
                datashow();
            } else if (id == R.id.add1) {
                nameInput.setText("");
                amountInput.setText("");
                priceInput.setText("");
                unitInput.setText("");
            } else if (id == R.id.save1) {
                save();
                recreate();
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.delete1) {
                delete();
                recreate();
            } else if (id == R.id.sum1) {
                ArrayList<Map<String, String>> data = getData();
                Adapter adapter = new Adapter(MainActivity.this, data);
                listView.setAdapter(adapter);

                goodsList.setVisibility(View.VISIBLE);
                priceTotal.setVisibility(View.VISIBLE);
                priceTotal.setText(String.format("总计：￥%.2f", totalTotal));

            }
        }
    }

    void refreshCursor() {
        if (cursor != null) {
            cursor.close();
        }
        SQLiteDatabase db1;
        db1 = openOrCreateDatabase("market.db", Context.MODE_PRIVATE, null);
        //查询Users数据表
        cursor = db1.query("Goods", null, null, null, null, null, null);
        cursor.moveToFirst();
    }

    //显示记录
    void datashow() {
        id_this = Integer.parseInt(cursor.getString(0));
        String user_name_this = cursor.getString(1);
        String telephone_this = cursor.getString(2);
        String address_this = cursor.getString(3);
        String mail_address_this = cursor.getString(4);

        nameInput.setText(user_name_this);
        amountInput.setText(telephone_this);
        priceInput.setText(address_this);
        unitInput.setText(mail_address_this);
    }

    //添加记录
    void add() {
        ContentValues values1 = new ContentValues();
        values1.put(NAME, nameInput.getText().toString());
        values1.put(AMOUNT, amountInput.getText().toString());
        values1.put(PRICE, priceInput.getText().toString());
        values1.put(UNIT, unitInput.getText().toString());
        SQLiteDatabase db2 = helper.getWritableDatabase();
        db2.insert(TABLE_NAME, null, values1);
        db2.close();

    }

    //保存记录
    void save() {
        SQLiteDatabase db1 = helper.getReadableDatabase();
        Cursor cursor1 = db1.query(TABLE_NAME, null, NAME + "='" + nameInput.getText() + "'", null, null, null, null);
        if (!cursor1.moveToFirst()) {
            add();
            cursor1.close();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(NAME, nameInput.getText().toString());
        values.put(AMOUNT, amountInput.getText().toString());
        values.put(PRICE, priceInput.getText().toString());
        values.put(UNIT, unitInput.getText().toString());
        String where1 = ID + " = " + id_this;
        db1.update(TABLE_NAME, values, where1, null);
        db1.close();

    }

    //删除
    void delete() {
        String where = ID + " = " + id_this;
        System.out.print(id_this);
        Toast.makeText(MainActivity.this, cursor.getString(1) + " 删除成功", Toast.LENGTH_SHORT).show();
        cursor.moveToNext();
        nameInput.setText(cursor.getString(1));
        amountInput.setText(cursor.getString(2));
        priceInput.setText(cursor.getString(3));
        unitInput.setText(cursor.getString(4));
        db.delete(TABLE_NAME, where, null);
        db = helper.getWritableDatabase();
        db.close();
    }
}
