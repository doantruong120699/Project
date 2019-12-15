package com.example.hello;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class noteAdapter extends ArrayAdapter<note> {

    public noteAdapter(Context context, int resource, List<note> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.item_layout_note, null);
        }
        note p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView txtngay = (TextView) view.findViewById(R.id.txtngay);
            txtngay.setText(p.ngay);
            TextView txtnoidung = (TextView)view.findViewById(R.id.txtnoidung);
            txtnoidung.setText(p.noidung);
        }
        return view;
    }
}
