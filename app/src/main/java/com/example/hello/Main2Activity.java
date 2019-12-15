package com.example.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import android.content.ClipboardManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Main2Activity extends AppCompatActivity {
    ArrayList<String> saveLink;
    WebView webView;
    String linkget,titleget,imgget;
    FloatingActionButton fabBtn;
    DatabaseHandler dataFavourite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        dataFavourite = new DatabaseHandler(this);
        saveLink = new ArrayList<String>();

        fabBtn = (FloatingActionButton)findViewById(R.id.fabBtn);
        fabBtn.hide();

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        Intent intent = getIntent();
        linkget = intent.getStringExtra("link");
        titleget = intent.getStringExtra("title");
        imgget = intent.getStringExtra("img");

        List<dataNews> temp = new ArrayList<dataNews>();
        temp = dataFavourite.getAlldataNewss();
        for (int i=0; i<temp.size(); i++){
            if(temp.get(i).linkrss.compareTo(linkget)==0){
                fabBtn.show();
            }
        }
        webView.loadUrl(linkget);
        webView.setWebViewClient(new WebViewClient());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemback:
                this.finish();
                return true;
            case R.id.itemcopy:{
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Link", linkget);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(this, " Link copied", Toast.LENGTH_SHORT).show();
            }
                return true;
            case  R.id.itemsave:{
                boolean check = false;
                List<dataNews> temp = new ArrayList<dataNews>();
                temp= dataFavourite.getAlldataNewss();
                for(int i=0; i<temp.size(); i++){
                    if(temp.get(i).linkrss.compareTo(linkget)==0){
                        check = true;
                        temp.remove(i);
                        dataFavourite.deletedataNews1(linkget);
                        break;
                    }
                }
                if(check == true){
                    fabBtn.hide();
                }else{
                    dataFavourite.adddataNews(new dataNews(1,titleget,imgget, linkget));
                    fabBtn.show();
                }
            }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
