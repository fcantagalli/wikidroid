package cs408team3.wikidroid.tab;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import cs408team3.wikidroid.R;
import cs408team3.wikidroid.Utils;

public class TabListAdapter extends BaseAdapter {

    private static final String TAG = "TabManager.ListAdapter";

    private TabManager          mTabManager;
    private OnTabRemoveListener mOnTabRemoveListener;

    private LayoutInflater      mInflater;
    private int                 mResource;
    private int                 mFieldId;

    public interface OnTabRemoveListener {

        public void onTabRemove(int position);

    }

    public TabListAdapter(Context context, int resource, int textViewResourceId, TabManager tabManager) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = resource;
        mFieldId = textViewResourceId;
        mTabManager = tabManager;
    }

    public void setOnTabRemoveListener(OnTabRemoveListener onTabRemoveListener) {
        mOnTabRemoveListener = onTabRemoveListener;
    }

    @Override
    public int getCount() {
        return mTabManager.size();
    }

    @Override
    public WebView getItem(int position) {
        return mTabManager.getTab(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);

        } else {
            view = convertView;
        }

        CheckBox favorite = (CheckBox)
                view.findViewById(R.id.drawer_fav_link);
        ImageButton removeTab = (ImageButton)
                view.findViewById(R.id.drawer_remove_tab);

        favorite.setOnClickListener(new View.OnClickListener() {

            final int pos = position;

            @Override
            public void onClick(View v) {
                CheckBox fav = (CheckBox) v;
                if (!fav.isChecked()) { // is not checked, remove the
                                        // website from the list
                    if (getItem(position).getTitle() != null) {
                        Log.d("favLink", "is now checked");
                        Log.d("favLink", "oioi" + getItem(position).getTitle() + "kkk");
                        Utils.DeleteLink(v.getContext(),
                                getItem(pos).getTitle());
                    } else {
                        fav.setChecked(false);
                    }
                } else { // if it is checked, put the website on the list
                    WebView w = getItem(position);
                    if (w.getTitle() != null && w.getUrl() != null) {
                        Log.d("favLink", "is now unchecked");
                        Log.d("favLink", "oioi " + w.getTitle());
                        Log.d("favLink", w.getUrl());
                        Utils.SaveLink(v.getContext(), w.getTitle(),
                                w.getUrl());
                    }

                }
            }

        });
        removeTab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnTabRemoveListener != null) {
                    mOnTabRemoveListener.onTabRemove(position);
                }
            }

        });

        try {
            text = (TextView) view.findViewById(mFieldId);

        } catch (ClassCastException e) {
            Log.e(TAG, "You must supply a resource ID for a TextView");
            throw new IllegalStateException(TAG + " requires the resource ID to be a TextView", e);
        }

        WebView webView = getItem(position);
        String webViewTitle = mTabManager.getTitle(webView);
        text.setText(webViewTitle);

        return view;
    }

}
