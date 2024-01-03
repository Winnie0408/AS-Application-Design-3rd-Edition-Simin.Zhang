package com.hwinzniej.exp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class Adapter extends BaseAdapter {
    private final ArrayList<Map<String, String>> data;
    private final LayoutInflater layoutInflater;
    public Adapter(Context context, ArrayList<Map<String, String>> data) {
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.list_item, null);
        TextView name = convertView.findViewById(R.id.item_name);
        TextView sub = convertView.findViewById(R.id.item_sub);
        TextView price = convertView.findViewById(R.id.item_price);
        name.setText(data.get(position).get("name"));
        sub.setText("单价：￥" + data.get(position).get("price") + "/" + data.get(position).get("unit") + "，数量：" + data.get(position).get("amount"));
        price.setText("￥" + String.format("%.2f", Double.parseDouble(data.get(position).get("total"))));
        return convertView;
    }
}
