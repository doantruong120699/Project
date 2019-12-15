package com.example.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    DatabaseHandler dataFavourite;
    ArrayList<News> mangdocbao;
    ListView lv;
    List<dataNews> temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        dataFavourite = new DatabaseHandler(this);
        mangdocbao = new ArrayList<>();

        temp = new ArrayList<dataNews>();
        temp = dataFavourite.getAlldataNewss();

        if(temp.size()>=32){
            for(int i=32;i<temp.size();i++){
                mangdocbao.add(new News(temp.get(i).getName(), temp.get(i).getlinkrss(),temp.get(i).gettype(),"",""));
            }
        }
        Customadapter adapter = new Customadapter(Main3Activity.this, android.R.layout.simple_list_item_1,mangdocbao);
        lv = findViewById(R.id.listviewFavorite);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
                intent.putExtra("link", mangdocbao.get(position).link);
                intent.putExtra("title", mangdocbao.get(position).title);
                intent.putExtra("img", mangdocbao.get(position).image);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu_fav_layout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemback:
                this.finish();
                return true;
            case R.id.itemreload:{
                List<dataNews> lists = new ArrayList<dataNews>();
                lists = dataFavourite.getAlldataNewss();
                mangdocbao.clear();
                if(lists.size()>=32){
                    for(int i=32;i<lists.size();i++){
                        mangdocbao.add(new News(lists.get(i).getName(), lists.get(i).getlinkrss(),lists.get(i).gettype(),"",""));
                    }
                }
                Customadapter adapter = new Customadapter(Main3Activity.this, android.R.layout.simple_list_item_1,mangdocbao);
                lv.setAdapter(adapter);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
