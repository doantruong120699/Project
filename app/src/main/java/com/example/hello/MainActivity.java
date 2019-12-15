package com.example.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public ListView listView;
    Customadapter customadapter;
    ArrayList<News> mangdocbao;
    FloatingActionButton fab;
    DatabaseHandler data;
    String[] listItems; //for dialog choice menu/item tuychon
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    int currentNew = 1;

    //left menu
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    Button btnHome, btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //left menu
        drawerLayout = findViewById(R.id.activity_main_drawer);
        listView = (ListView)findViewById(R.id.listview);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        btnHome = findViewById(R.id.btn_menu_home);
        btnFavorite = findViewById(R.id.btn_menu_favorite);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //dialog menu choice item TUY CHON
        listItems = getResources().getStringArray(R.array.type_item);
        checkedItems = new boolean[listItems.length];

        //database
        data = new DatabaseHandler(this);
        List<dataNews> temp = new ArrayList<dataNews>();
        temp = data.getAlldataNewss();
        if(temp.size()==0) // test database when startup app
            addData(data);
        //fab button go to top
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelectionAfterHeaderView();
            }
        });

        mangdocbao = new ArrayList<News>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getAllData(mangdocbao, data, 25,32,checkedItems);
            }
        });

        //listview click item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("link", mangdocbao.get(position).link);
                intent.putExtra("title", mangdocbao.get(position).title);
                intent.putExtra("img", mangdocbao.get(position).image);
                startActivity(intent);
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
       
    }

    class Readdata extends AsyncTask<String , String, String>{
        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            String title = "";
            String link = "";
            String date = "";
            String image = "";
            String source = "";
            //get data
            for(int i=0; i<nodeList.getLength(); i++){
                Element element = (Element) nodeList.item(i);
                NodeList temp1 = element.getElementsByTagName("title");
                NodeList temp2 = element.getElementsByTagName("link");
                NodeList temp3 = element.getElementsByTagName("pubDate");
                NodeList temp4 = element.getElementsByTagName("description");
                title = temp1.item(0).getTextContent().trim();
                link = temp2.item(0).getTextContent().trim();
                date = temp3.item(0).getTextContent().trim();
                String string = temp4.item(0).getTextContent();

                NodeList nodeList1;
                if(document.getElementsByTagName("copyright").getLength()==0){
                    nodeList1  =document.getElementsByTagName("generator");
                }else{
                    nodeList1  =document.getElementsByTagName("copyright");
                }
                source = nodeList1.item(0).getTextContent().trim();

                String CutPoint = "src";
                String f1 = "></";
                String f2 = "/><";
                String f3 ="ali";
                String f4 = "alt";
                int FinalPointLength = f1.length();
                int CutPointLenth = CutPoint.length();
                try {
                    for(int j=0; j<string.length(); j++){
                        String temp = string.substring(j, j+CutPointLenth);
                        if(temp.compareTo(CutPoint)==0)
                        {
                            for(int m = j+6; m<string.length(); m++){
                                String tempp = string.substring(m, m+FinalPointLength);
                                if((tempp.compareTo(f1)==0 || tempp.compareTo(f2)==0) || (tempp.compareTo(f3)==0 || tempp.compareTo(f4)==0)){
                                    image =  string.substring(j+5, m-2);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }catch (Exception e){
                    break;
                }
                Log.d("full info "+i+":", title+"\n"+date+"\n"+link+"\n"+image);
                Log.d("k=a", s);
                mangdocbao.add(new News(title.trim(), link.trim(), image.trim(),date.trim(), source.trim()));
            }
            Collections.shuffle(mangdocbao);
            customadapter = new Customadapter(MainActivity.this, android.R.layout.simple_list_item_1, mangdocbao);
            listView.setAdapter(customadapter);
            super.onPostExecute(s);
        }
    }

    public String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try{
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choice_menu, menu);
        return true;
    }
    //left menu
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    //end left menu
    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){
            case R.id.item1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       getAllData(mangdocbao, data, 9,16, checkedItems);
                    }
                });
                return true;
            case R.id.item2:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getAllData(mangdocbao, data, 1,8, checkedItems);
                    }
                });
                return true;
            case R.id.item3:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getAllData(mangdocbao, data, 17,24,checkedItems);
                    }
                });
                return true;
            case R.id.item4:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getAllData(mangdocbao, data, 25,32,checkedItems);
                    }
                });
                return true;
            case R.id.item5:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                        mBuilder.setTitle("Tuỳ chọn");
                        mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if(isChecked){
                                    if(! mUserItems.contains(position)){
                                        mUserItems.add(position);
                                    }
                                }else if(mUserItems.contains(position)){
                                    mUserItems.remove(mUserItems.indexOf(position));
                                }
                            }
                        });
                        mBuilder.setCancelable(false);
                        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String item = "";
                                for(int i=0; i<checkedItems.length; i++){
                                    item = item+checkedItems[i];
                                }
                                getAllData(mangdocbao, data, currentNew,currentNew+7,checkedItems);
                            }
                        });
                        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String item = "";
                                for (int i=0; i<checkedItems.length; i++){
                                    checkedItems[i] = false;
                                    mUserItems.clear();
                                }
                                getAllData(mangdocbao, data, currentNew,currentNew+7,checkedItems);
                            }
                        });

                        AlertDialog mdiDialog = mBuilder.create();
                        mdiDialog.show();
                    }
                });
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    void getAllData(ArrayList<News> arr, DatabaseHandler data, int x, int y, boolean[] checkItem){
        currentNew = x;
        arr.clear();
        int dem = 0;
        for (int i=x; i<y; i++){
            if(checkItem[i-x]==true){
                new Readdata().execute(data.getdataNews(i).linkrss);
                dem++;
            }
        }
        if(dem==0){
            for (int i=x; i<y; i++){
                new Readdata().execute(data.getdataNews(i).linkrss);
            }
        }
    }
    public void addData(DatabaseHandler data){
        data.adddataNews(new dataNews(1,"Tuổi trẻ", "Thời sự", "https://tuoitre.vn/rss/thoi-su.rss"));
        data.adddataNews(new dataNews(2,"Tuổi trẻ", "Thế giới", "https://tuoitre.vn/rss/the-gioi.rss"));
        data.adddataNews(new dataNews(3,"Tuổi trẻ", "Pháp luật", "https://tuoitre.vn/rss/phap-luat.rss"));
        data.adddataNews(new dataNews(4,"Tuổi trẻ", "Kinh doanh", "https://tuoitre.vn/rss/kinh-doanh.rss"));
        data.adddataNews(new dataNews(5,"Tuổi trẻ", "Thể thao", "https://tuoitre.vn/rss/the-thao.rss"));
        data.adddataNews(new dataNews(6,"Tuổi trẻ", "Công nghệ", "https://tuoitre.vn/rss/nhip-song-so.rss"));
        data.adddataNews(new dataNews(7,"Tuổi trẻ", "Giáo dục", "https://tuoitre.vn/rss/giao-duc.rss"));
        data.adddataNews(new dataNews(8,"Tuổi trẻ", "Sức khoẻ-đời sống", "https://tuoitre.vn/rss/suc-khoe.rss"));
        data.adddataNews(new dataNews(9,"Thanh niên", "Thời sự", "https://thanhnien.vn/rss/viet-nam.rss"));
        data.adddataNews(new dataNews(10,"Thanh niên", "Thế giới", "https://thanhnien.vn/rss/the-gioi.rss"));
        data.adddataNews(new dataNews(11,"Thanh niên", "Pháp luật", "https://thanhnien.vn/rss/viet-nam/phap-luat.rss"));
        data.adddataNews(new dataNews(12,"Thanh niên", "Kinh doanh", "https://thanhnien.vn/rss/kinh-doanh.rss"));
        data.adddataNews(new dataNews(13,"Thanh niên", "Thể thao", "https://thethao.thanhnien.vn/rss/home.rss"));
        data.adddataNews(new dataNews(14,"Thanh niên", "Công nghệ", "https://thanhnien.vn/rss/cong-nghe-thong-tin.rss"));
        data.adddataNews(new dataNews(15,"Thanh niên", "Giáo dục", "https://thanhnien.vn/rss/giao-duc.rss"));
        data.adddataNews(new dataNews(16,"Thanh niên", "Sức khoẻ-đời sống", "https://thanhnien.vn/rss/doi-song.rss"));
        data.adddataNews(new dataNews(17,"Người lao động", "Thời sự", "https://nld.com.vn/thoi-su.rss"));
        data.adddataNews(new dataNews(18,"Người lao động", "Thế giới", "https://nld.com.vn/thoi-su-quoc-te.rss"));
        data.adddataNews(new dataNews(19,"Người lao động", "Pháp luật", "https://nld.com.vn/phap-luat.rss"));
        data.adddataNews(new dataNews(20,"Người lao động", "Kinh doanh", "https://nld.com.vn/kinh-te.rss"));
        data.adddataNews(new dataNews(21,"Người lao động", "Thể thao", "https://nld.com.vn/the-thao.rss"));
        data.adddataNews(new dataNews(22,"Người lao động", "Công nghệ", "https://nld.com.vn/cong-nghe.rss"));
        data.adddataNews(new dataNews(23,"Người lao động", "Giáo dục", "https://nld.com.vn/giao-duc-khoa-hoc.rss"));
        data.adddataNews(new dataNews(24,"Người lao động", "Sức khoẻ-đời sống", "https://nld.com.vn/suc-khoe.rss"));
        data.adddataNews(new dataNews(25,"24H", "Thời sự", "https://cdn.24h.com.vn/upload/rss/tintuctrongngay.rss"));
        data.adddataNews(new dataNews(26,"24H", "Thế giới", "https://cdn.24h.com.vn/upload/rss/tintuctrongngay.rss"));
        data.adddataNews(new dataNews(27,"24H", "Pháp luật", "https://cdn.24h.com.vn/upload/rss/anninhhinhsu.rss"));
        data.adddataNews(new dataNews(28,"24H", "Kinh doanh", "https://cdn.24h.com.vn/upload/rss/taichinhbatdongsan.rss"));
        data.adddataNews(new dataNews(29,"24H", "Thể thao", "https://cdn.24h.com.vn/upload/rss/thethao.rss"));
        data.adddataNews(new dataNews(30,"24H", "Công nghệ", "https://cdn.24h.com.vn/upload/rss/congnghethongtin.rss"));
        data.adddataNews(new dataNews(31,"24H", "Giáo dục", "https://cdn.24h.com.vn/upload/rss/giaoducduhoc.rss"));
        data.adddataNews(new dataNews(32,"24H", "Sức khoẻ-đời sống", "https://cdn.24h.com.vn/upload/rss/suckhoedoisong.rss"));
    }
}
