package com.example.android.codelabs.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private FeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article item = adapter.getItem(position);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(item.getLink()));
                startActivity(i);
            }
        });


        String url = "https://www.wired.com/feed/rss";
        final Parser parser = new Parser();
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                adapter = new FeedAdapter(getBaseContext(), R.layout.layout_item_feed, list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onError() {
                new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(Message message) {
                        Toast.makeText(MainActivity.this, "En estos momentos no podemos cargar la informacion solicitada, intente mas tarde.", Toast.LENGTH_SHORT).show();
                    }
                };
            }
        });
        parser.execute(url);
    }

    private class FeedAdapter extends ArrayAdapter<Article>{

        private int resource;

        public FeedAdapter(Context context, int resource, List<Article> objects) {
            super(context, resource, objects);
            this.resource = resource;
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            Article article = getItem(position);
            if(convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(this.resource, parent, false);
            ((TextView)convertView.findViewById(R.id.txt_title)).setText(article.getTitle());
            return convertView;
        }
    }
}
