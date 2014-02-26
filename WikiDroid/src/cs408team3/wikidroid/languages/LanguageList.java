package cs408team3.wikidroid.languages;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

public class LanguageList extends AsyncTask<String, Integer, List<String>> {

    private Languages languages;
    private Listener  listener;

    public LanguageList(Languages languages, Listener listener) {
        this.languages = languages;
        this.listener = listener;
    }

    public interface Listener {

        public void onResponse(List<String> result);

    }

    @Override
    protected List<String> doInBackground(String... url) {
        ArrayList<String> available = languages.getAvailableLanguages();
        ArrayList<String> names = languages.getLanguageNames(available);

        return names;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(List<String> result) {
        listener.onResponse(result);
    }
}
