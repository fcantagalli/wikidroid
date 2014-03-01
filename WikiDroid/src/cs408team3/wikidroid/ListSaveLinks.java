package cs408team3.wikidroid;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cs408team3.wikidroid.listArticles.LoadSavedStuffs;

public class ListSaveLinks extends Activity {

    private ListView listView;
    private Map<String, ?> list;
    private ArrayList<String> listLinks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_save_links);

        listView = (ListView) findViewById(R.id.listView1);

        list = getSavedLinkList();
        Log.i("oii", "oiiiii" + list);
        listLinks = new ArrayList<String>(list.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listLinks);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Log.i("oii", "entrou no onclick");
                String name = listLinks.get(arg2);
                Log.i("oii", "name : " + name);
                String link = (String) list.get(name);
                Log.i("oii", "link: " + link);
                if (link == null)
                    return; // just in case it does not found the key. I should
                            // never happen in theory
                Intent intent = new Intent(getApplicationContext(), LoadSavedStuffs.class);
                intent.putExtra("url", link);
                startActivity(intent);

                // send the link to somewhere to show the saved link.
            }
        });
    }

    public Map<String, ?> getSavedLinkList() {

        SharedPreferences sharedPref = this.getSharedPreferences(Utils.LINKS, Context.MODE_PRIVATE);
        Map<String, ?> links = sharedPref.getAll();

        return links;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_save_links, menu);
        return true;
    }


}
