package cs408team3.wikidroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import cs408team3.wikidroid.blur.Blur;
import cs408team3.wikidroid.blur.BlurTask;
import cs408team3.wikidroid.languages.LanguageList;
import cs408team3.wikidroid.languages.Languages;
import cs408team3.wikidroid.languages.UrlList;
import cs408team3.wikidroid.listArticles.ListSaveArticles;
import cs408team3.wikidroid.search.SearchArticle;
import cs408team3.wikidroid.tab.TabListAdapter;
import cs408team3.wikidroid.tab.TabManager;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String   TAG                    = "MainActivity";

    private static final String   STATE_OPEN_TAB_LINKS   = "open_tab_links";

    private static final int      ACTIONBAR_NORMAL_TITLE = 0x1;
    private static final int      ACTIONBAR_DRAWER_TITLE = 0x2;

    private final Context         mContext               = this;

    private TabManager            mTabManager;
    private WebViewClient         mWebViewClient;
    private WebChromeClient       mWebChromeClient;
    private Languages             mLanguages;

    private DrawerLayout          mDrawerLayout;

    private TabListAdapter        mDrawerListAdapter;
    private ListView              mDrawerList;
    private ImageView             mBlurImage;
    private FrameLayout           mContentFrame;
    private WebView               mWebPage;
    private MenuItem              mSearchMenuItem;
    private ProgressBar           mWebProgressBar;
    private Toast                 mToast;

    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence          mDrawerTitle;
    private CharSequence          mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebViewClient = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Page " + url + " loaded");

                if (mTabManager.isForeground(view)) {
                    setTitle(mTabManager.getTitle(view), ACTIONBAR_NORMAL_TITLE);
                }
                // Refresh drawer list
                mDrawerListAdapter.notifyDataSetChanged();
            }
        };
        mWebChromeClient = new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                Log.v(TAG, "Page load progress " + progress);

                if (mTabManager.isForeground(view)) {
                    if (progress < 100) {
                        startWebProgressBar();
                    }

                    if (progress == 100) {
                        stopWebProgressBar();
                    }
                } else {
                    // Force progress bar stop
                    stopWebProgressBar();
                }
            }
        };

        mTabManager = new TabManager(this, mWebViewClient, mWebChromeClient);
        if (mTabManager.size() == 0) {
            mTabManager.newTab();
        }
        mLanguages = new Languages();

        mDrawerTitle = getTitle();
        mWebPage = mTabManager.displayTab(0);
        setTitle(mTabManager.getTitle(mWebPage), ACTIONBAR_NORMAL_TITLE);

        mWebProgressBar = (ProgressBar) findViewById(R.id.content_progress);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mBlurImage = (ImageView) findViewById(R.id.blur_image);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);

        mContentFrame.addView(mWebPage, 0);

        mDrawerToggle = new WikiDroidActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Set the adapter for the list view
        mDrawerListAdapter = new TabListAdapter(this, R.layout.drawer_list_item, R.id.drawer_list_item_text, mTabManager);
        mDrawerListAdapter.setOnTabRemoveListener(new TabListAdapter.OnTabRemoveListener() {

            @Override
            public void onTabRemove(int position) {
                boolean removed = mTabManager.removeTab(position);

                if (removed) {
                    int displayPosition = position - 1 >= 0 ? position - 1 : 0;
                    displayWebView(displayPosition);
                    mDrawerListAdapter.notifyDataSetChanged();
                } else {
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(mContext, R.string.error_remove_tab_failed, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });
        mDrawerList.setAdapter(mDrawerListAdapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(this);

        // Disable Drawer Scrim Color
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        persistOpenTabs(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // TODO: NOT WORKING YET.
        restoreOpenTabs(savedInstanceState);
        // mWebPage = mTabManager.displayTab(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebPage.canGoBack()) {
            mWebPage.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // Called whenever we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, show / hide action items related to the
        // content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        // Show Add tab
        menu.findItem(R.id.action_add_tab).setVisible(drawerOpen);
        // Hide search
        menu.findItem(R.id.search).setVisible(!drawerOpen);
        // Hide save article
        menu.findItem(R.id.saveArticle).setVisible(!drawerOpen);
        // Hide languages
        menu.findItem(R.id.languages).setVisible(!drawerOpen);
        // Hide share
        menu.findItem(R.id.action_share_article).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchMenuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText(mContext, "Teste",
                // Toast.LENGTH_LONG).show();
                boolean haveNet = Utils.isNetworkAvailable(mContext);
                if (haveNet == false) {
                    Toast t = Toast.makeText(mContext, "Sorry, No internet connection", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 5, 5);
                    t.show();
                    return false;
                } else {
                    if (!Utils.verifySearchString(query, TAG)) {
                        Toast t = Toast.makeText(mContext, "Sorry, invalid input. Try again", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER, 5, 5);
                        t.show();
                        return false;
                    }

                    if (mSearchMenuItem != null) {
                        mSearchMenuItem.collapseActionView();
                    }

                    SearchArticle search = new SearchArticle(mContext, mWebPage);
                    search.execute(query);

                    return true;
                }

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // add a button on menu to save the article

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        Log.i(TAG, "item id: " + item.getItemId());
        switch (item.getItemId()) {

        case R.id.action_add_tab:
            if (!mTabManager.newTab()) {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(this, R.string.error_max_tab_reached, Toast.LENGTH_SHORT);
                mToast.show();
            } else {
                mDrawerListAdapter.notifyDataSetChanged();
            }

            return true;

        case R.id.languages:
            showLanguagesDialog();
            return true;

        case R.id.saveArticle:
            if (mTabManager.size() == 0)
                return false; // possible future bug?. trying to save no page.
            String title = mWebPage.getTitle();
            Utils.saveArchive(mWebPage, title);
            Toast t = Toast.makeText(this, "Article Saved", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 5, 5);
            t.show();
            return true;

        case R.id.action_share_article:
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "Wikipedia article - " +
                            Utils.trimWikipediaTitle(mWebPage.getTitle()));
            shareIntent.putExtra(Intent.EXTRA_TEXT, mWebPage.getUrl());
            Log.i(TAG, "textos : \t " + mWebPage.getUrl() + "\t" + mWebPage.getTitle());
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;

        case R.id.action_show_saved_articles:
            Intent intent = new Intent(this, ListSaveArticles.class);
            startActivity(intent);
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        displayWebView(position);

        // Stop progress bar if current tab is changed
        if (parent.getSelectedItemPosition() != position) {
            stopWebProgressBar();
        }

        mDrawerLayout.closeDrawers();
    }

    private void showLanguagesDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getString(R.string.dialog_language_loading));
        progressDialog.show();

        LanguageList langList = new LanguageList(mLanguages, mWebPage.getUrl(), new LanguageList.Listener() {

            @Override
            public void onResponse(List<String> languageOptions) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.action_languages);

                if (languageOptions.size() > 0) {
                    ListAdapter stringListAdapter = new ArrayAdapter<String>(mContext, R.layout.languages_list_item, languageOptions);

                    builder.setSingleChoiceItems(stringListAdapter, 0, null)
                            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    final int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                    // getCheckedItemPosition will return -1 if
                                    // no selection made, 0 if current language
                                    // is selected
                                    if (selectedPosition == -1 || selectedPosition == 0) {
                                        // Do nothing
                                    }
                                    // Use position to find the proper URL
                                    else {
                                        UrlList urlList = new UrlList(mLanguages, mWebPage.getUrl(), new UrlList.Listener() {

                                            @Override
                                            public void onResponse(List<String> urlOptions) {
                                                mWebPage.loadUrl(urlOptions.get(selectedPosition));
                                            }
                                        });
                                        urlList.execute();
                                    }
                                    Log.i(TAG, "Selected language dialog index " + selectedPosition);
                                }
                            });
                } else {
                    builder.setMessage(R.string.dialog_language_empty);
                    builder.setNeutralButton(R.string.dialog_ok, null);
                }

                progressDialog.dismiss();
                builder.create().show();
            }
        });
        langList.execute();
    }

    private class WikiDroidActionBarDrawerToggle extends ActionBarDrawerToggle implements BlurTask.Listener {

        private Bitmap scaled;

        // private BlurTask blurTask;

        public WikiDroidActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            if (slideOffset > 0.0f) {
                setBlurAlpha(slideOffset);
            } else {
                clearBlurImage();
            }
        }

        // Called when a drawer has settled in a completely closed state.
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            toggleTitle(ACTIONBAR_NORMAL_TITLE);
            clearBlurImage(); // Clear background blur
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        // Called when a drawer has settled in a completely open state.
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            toggleTitle(ACTIONBAR_DRAWER_TITLE);
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        private void setBlurAlpha(float slideOffset) {
            if (mBlurImage.getVisibility() != View.VISIBLE) {
                setBlurImage();
            }

            mBlurImage.setAlpha(slideOffset);
        }

        private void setBlurImage() {
            mBlurImage.setImageBitmap(null);
            mBlurImage.setVisibility(View.VISIBLE);

            scaled = Utils.drawViewToBitmap(scaled, mContentFrame, mContentFrame.getWidth(), mContentFrame.getHeight(), Blur.DEFAULT_DOWNSAMPLING);
            // blurTask = new BlurTask(mContentFrame.getContext(), null,
            // scaled);
            new BlurTask(mContentFrame.getContext(), null, scaled);

            mBlurImage.setImageBitmap(scaled);
            Log.v(TAG, "BlurImage set");
        }

        private void clearBlurImage() {
            mBlurImage.setVisibility(View.GONE);
            mBlurImage.setImageBitmap(null);
            // blurTask = null;
        }

        @Override
        public void onBlurOperationFinished() {
            mBlurImage.invalidate();
        }

    }

    private void displayWebView(int position) {
        mContentFrame.removeView(mWebPage);
        mWebPage = mTabManager.displayTab(position);
        mContentFrame.addView(mWebPage, 0);
        setTitle(mTabManager.getTitle(mWebPage), ACTIONBAR_NORMAL_TITLE);
    }

    private void setTitle(String title, int status) {
        switch (status) {
        case ACTIONBAR_NORMAL_TITLE:
            if (title != null)
                mTitle = title;
            getActionBar().setTitle(mTitle);
            return;
        case ACTIONBAR_DRAWER_TITLE:
            if (title != null)
                mDrawerTitle = title;
            getActionBar().setTitle(mDrawerTitle);
            return;
        }
    }

    private void toggleTitle(int status) {
        setTitle(null, status);
    }

    private void startWebProgressBar() {
        if (mWebProgressBar.getVisibility() != View.VISIBLE) {
            mWebProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void stopWebProgressBar() {
        if (mWebProgressBar.getVisibility() != View.GONE) {
            mWebProgressBar.setVisibility(View.GONE);
        }
    }

    private void persistOpenTabs(Bundle outState) {
        if (outState == null) {
            return;
        }

        ArrayList<String> links = mTabManager.getAllLinks();

        if (links != null && links.size() > 0) {
            outState.putStringArrayList(STATE_OPEN_TAB_LINKS, links);

            Log.d(TAG, "Open tabs saved: " + links);
        }
    }

    private void restoreOpenTabs(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        ArrayList<String> links = savedInstanceState.getStringArrayList(STATE_OPEN_TAB_LINKS);

        if (links != null) {
            ArrayList<String> currentLinks = mTabManager.getAllLinks();
            if (currentLinks != null && currentLinks.equals(links)) {
                return;
            }

            mTabManager.restoreAllLinks(links);

            Log.d(TAG, "Open tabs restored: " + links);
        }
    }

}
