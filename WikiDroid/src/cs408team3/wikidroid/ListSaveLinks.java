package cs408team3.wikidroid;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListSaveLinks extends Activity {

    private ListView listView;
    private Map<String, ?> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_save_links);

        listView = (ListView) findViewById(R.id.listView1);

        list = getSavedLinkList();
        ArrayList<String> content = new ArrayList<String>(list.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, content);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                String link = (String) list.get(arg0.getSelectedItem());

                if (link == null)
                    return; // just in case it does not found the key. I should
                            // never happen in theory

                // send the link to somewhere to show the saved link.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_save_links, menu);
        return true;
    }

    public Map<String, ?> getSavedLinkList() {

        SharedPreferences sharedPref = this.getSharedPreferences(Utils.LINKS, Context.MODE_PRIVATE);
        Map<String, ?> links = sharedPref.getAll();

        return links;
    }
}
