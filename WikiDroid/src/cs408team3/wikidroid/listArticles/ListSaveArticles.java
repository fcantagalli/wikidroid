package cs408team3.wikidroid.listArticles;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cs408team3.wikidroid.R;
import cs408team3.wikidroid.Utils;

public class ListSaveArticles extends Activity {

    private ListView listView;
    private ArrayList<String> listArticles;
    private Map<String, ?>    mapLinks;    // SharedPreferences returns a
                                            // generic type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_save_links);

        listView = (ListView) findViewById(R.id.listView1);

        mapLinks = getSavedLinkList();

        // return a list with the name of the files saved. the name of the file
        // is the title of the article.
        ArrayList<String> articles = getSavedArticlesList();
        // return a list with the name of the files saved. the name of the file
        // is the title of the article.

        // initialize listArticles
        listArticles = new ArrayList<String>();

        // if there is internet available, show the saved links.
        if (Utils.isNetworkAvailable(this)) {
            Set<String> keys = mapLinks.keySet();

            for (String s : keys) {
                listArticles.add(s);
            }
        }

        for (String s : articles) {
            listArticles.add(Utils.trimWikipediaTitle(s));
        }

        // put, the name of the articles saved on the Map
        for (String s : articles) {
            mapLinks.put(s, null); // null to know they are name files and not
                                   // links
        }

        // TODO - Fix this part with a custom arrayAdpater
        // ListAdapter adapter = new ListAdapter(this,)
        // listView.setAdapter(adapter);


        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                String filename = listArticles.get(arg2);

                // send the name of the article to somewhere to show the saved
                // link.
                Intent intent = new Intent(getApplicationContext(), LoadSavedStuffs.class);
                intent.putExtra("filename", filename);
                startActivity(intent);

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
        Log.i("oii", "length : " + filesOnDir.length);
        ArrayList<String> fileNames = new ArrayList<String>(filesOnDir.length);
        for (File f : filesOnDir) {
            fileNames.add(f.getName());
        }
        Log.i("oii", "" + fileNames);
        return fileNames;
    }

    public Map<String, ?> getSavedLinkList() {

        SharedPreferences sharedPref = this.getSharedPreferences(Utils.LINKS, Context.MODE_PRIVATE);
        Map<String, ?> links = sharedPref.getAll();

        return links;
    }

    class ListAdapter extends BaseAdapter {

        private static final String TAG = "ListArticlesAdapter";

        private LayoutInflater      mInflater;
        private int                 mResource;
        private int                 mFieldId;
        private int                 mImgFieldId;

        public ListAdapter(Context context, int resource, int textViewResourceId, int ImageViewResourceId) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mResource = resource;
            mFieldId = textViewResourceId;
            mImgFieldId = ImageViewResourceId;
        }

        @Override
        public int getCount() {
            return listArticles.size();
        }

        @Override
        public String getItem(int position) {
            return listArticles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            TextView text;

            if (convertView == null) {
                view = mInflater.inflate(mResource, parent, false);

            } else {
                view = convertView;
            }

            try {
                text = (TextView) view.findViewById(mFieldId);

            } catch (ClassCastException e) {
                Log.e(TAG, "You must supply a resource ID for a TextView");
                throw new IllegalStateException(TAG + " requires the resource ID to be a TextView", e);
            }

            return view;
        }

    }
}
