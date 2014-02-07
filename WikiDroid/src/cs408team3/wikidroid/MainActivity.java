package cs408team3.wikidroid;

import java.util.Random;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import cs408team3.wikidroid.blur.Blur;
import cs408team3.wikidroid.blur.BlurTask;
import cs408team3.wikidroid.blur.Utils;

public class MainActivity extends Activity {

	// TODO: remove
	private final String[] mListTitles = new String[] { "Hello", "Yay" };
	private final String[] mButtonTitles = new String[] {"LOL", "Click me again!", "LOLOL", "jajaja"};
	private final int[] mButtonColors = new int[] {Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN};

	// TODO: remove
	private Button mTestButton;
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ImageView mBlurImage;
	private FrameLayout mContentFrame;

	private ActionBarDrawerToggle mDrawerToggle;

	// private CharSequence mDrawerTitle;
	// private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// TODO: remove
		mTestButton = (Button) findViewById(R.id.test_button);
		mTestButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Random r = new Random();
				String title = mButtonTitles[r.nextInt(mButtonTitles.length)];
				int color = mButtonColors[r.nextInt(mButtonColors.length)];
				
				mTestButton.setText(title);
				mTestButton.setBackgroundColor(color);
			}

		});

		// mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mBlurImage = (ImageView) findViewById(R.id.blur_image);
		mContentFrame = (FrameLayout) findViewById(R.id.content_frame);

		mDrawerToggle = new WikiDroidActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.drawer_list_item_text, mListTitles));
		// Set the list's click listener
		// mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Disable Drawer Scrim Color
		mDrawerLayout.setScrimColor(Color.TRANSPARENT);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
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
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

		return super.onOptionsItemSelected(item);
	}

	private class WikiDroidActionBarDrawerToggle extends ActionBarDrawerToggle {

		private Bitmap scaled;
		private BlurTask blurTask;

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
			// getActionBar().setTitle(mTitle);
			clearBlurImage(); // Clear background blur
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}

		// Called when a drawer has settled in a completely open state.
		@Override
		public void onDrawerOpened(View drawerView) {
			super.onDrawerOpened(drawerView);
			// getActionBar().setTitle(mDrawerTitle);
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
			blurTask = new BlurTask(mContentFrame.getContext(), null, scaled);

			mBlurImage.setImageBitmap(scaled);
		}

		private void clearBlurImage() {
			mBlurImage.setVisibility(View.GONE);
			mBlurImage.setImageBitmap(null);
		}

	}

}
