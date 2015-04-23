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

public class FriendsScreen extends ActionBarActivity {

    private ListView friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freinds_screen);

        friendList = (ListView)findViewById(R.id.friendList);


      /*  View headerView = ((LayoutInflater) FriendsScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.profile_list_header_item, null, false);
        View footerView = ((LayoutInflater) FriendsScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.profile_list_footer_item, null, false);
        profileList.addHeaderView(headerView);
        profileList.addFooterView(footerView);
        CustomAdapter adp = new CustomAdapter(FriendsScreen.this);
        profileList.setAdapter(adp);*/

        CustomAdapter adp = new CustomAdapter(FriendsScreen.this);
        friendList.setAdapter(adp);

        friendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                CustomDialog customDialog=new CustomDialog(FriendsScreen.this,"See Friend's Feed","Unfriend",android.R.style.Theme_Translucent_NoTitleBar);
                customDialog.show();
                customDialog.setResponse(new CustomDialog.CustomDialogInterface() {
                    @Override
                    public void topButton() {
                        Intent i = new Intent(FriendsScreen.this,FriendsFeedsScreen.class);
                        startActivity(i);
                    }

                    @Override
                    public void bottomButton() {

                    }
                });
                return false;
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
            view = layoutInflator.inflate(R.layout.friend_list_item_with_delete, parent, false);
            return view;
        }
    }
}
