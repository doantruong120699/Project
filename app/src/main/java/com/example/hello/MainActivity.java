package com.example.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Customadapter customadapter;
    ArrayList<News> mangdocbao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listview);

        mangdocbao = new ArrayList<News>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Readdata().execute("https://vnexpress.net/rss/suc-khoe.rss");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("link", mangdocbao.get(position).link);
                startActivity(intent);
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
            NodeList nodeList1 = document.getElementsByTagName("description");
            String title = "";
            String link = "";
            String date = "";
            String image = "";
            String CutPoint = "src=";
            String FinalPoint = "></";
            int FinalPointLength = FinalPoint.length();
            int CutPointLenth = CutPoint.length();

            for(int i=0; i<nodeList.getLength(); i++){
                Element element = (Element) nodeList.item(i);
                title =parser.getValue(element, "title");
                link = parser.getValue(element, "link");
                date = parser.getValue(element, "pubDate");
                //image
                String cdata = nodeList1.item(i+1).getTextContent();
                for(int j=0; j<cdata.length(); j++){
                    String temp = cdata.substring(j, j+CutPointLenth);
                    if(temp.compareTo(CutPoint)==0)
                    {
                        for(int m = j+6; m<cdata.length(); m++){
                            String temp1 = cdata.substring(m, m+FinalPointLength);
                            if(temp1.compareTo(FinalPoint)==0){
                                image = cdata.substring(j+5, m-2);
                                break;
                            }
                        }
                        break;
                    }
                }
                //final
                Log.d("result", title+"\n"+date+"\n"+image);
                mangdocbao.add(new News(title, link, image,date));
            }
            customadapter = new Customadapter(MainActivity.this, android.R.layout.simple_spinner_item, mangdocbao);
            listView.setAdapter(customadapter);
            super.onPostExecute(s);
        }
    }
    private String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Readdata().execute("https://vnexpress.net/rss/thoi-su.rss");
                    }
                });
                return true;
            case R.id.item2:
                Toast.makeText(this, "item2", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "item3", Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
