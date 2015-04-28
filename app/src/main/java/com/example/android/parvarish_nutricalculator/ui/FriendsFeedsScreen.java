package com.example.android.parvarish_nutricalculator.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.android.parvarish_nutricalculator.R;
import com.example.android.parvarish_nutricalculator.custom.CustomDialog;

public class FriendsFeedsScreen extends ActionBarActivity {

    private ListView listFeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_feed);
        listFeeds = (ListView)findViewById(R.id.listFeeds);

      /*  View headerView = ((LayoutInflater) FriendsScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.profile_list_header_item, null, false);
        View footerView = ((LayoutInflater) FriendsScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.profile_list_footer_item, null, false);
        profileList.addHeaderView(headerView);
        profileList.addFooterView(footerView);
        CustomAdapter adp = new CustomAdapter(FriendsScreen.this);
        profileList.setAdapter(adp);*/

        View headerView = ((LayoutInflater) FriendsFeedsScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.friends_feed_item_view_header, null, false);
        listFeeds.addHeaderView(headerView);

        CustomAdapter adp = new CustomAdapter(FriendsFeedsScreen.this);
        listFeeds.setAdapter(adp);
        listFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(FriendsFeedsScreen.this, ViewFriendsRecipe.class);
                startActivity(i);
            }
        });
    }

    class CustomAdapter extends BaseAdapter{
        LayoutInflater layoutInflator;
        private Context ctx;
        public CustomAdapter(Context ctx){
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = convertView;
            view = layoutInflator.inflate(R.layout.friend_feed_item_view, parent, false);
            return view;
        }
    }
}
