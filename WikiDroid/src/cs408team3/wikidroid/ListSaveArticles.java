package cs408team3.wikidroid;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListSaveArticles extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_save_links);

        listView = (ListView) findViewById(R.id.listView1);

        ArrayList<String> files = getSavedArticlesList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, files);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                String filename = (String) arg0.getSelectedItem();

                // send the name of the article to somewhere to show the saved
                // link.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_save_links, menu);
        return true;
    }

    ArrayList<String> getSavedArticlesList() {

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/WikiDroid/");

        File[] filesOnDir = dir.listFiles();
        ArrayList<String> fileNames = new ArrayList<String>(filesOnDir.length);
        for (File f : filesOnDir) {
            fileNames.add(f.getName());
        }

        return fileNames;
    }
}
