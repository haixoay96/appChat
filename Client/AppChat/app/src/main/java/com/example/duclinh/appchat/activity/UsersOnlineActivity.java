package com.example.duclinh.appchat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.adapter.AdapterUsersOnline;
import com.example.duclinh.appchat.data.ItemUsersOnline;
import com.example.duclinh.appchat.orther.DividerItemDecoration;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class UsersOnlineActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView listUsers;
    private AdapterUsersOnline adapterUsersOnline;
    private ArrayList<ItemUsersOnline> listUsersOnline;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_online);
        controlView();
        handleLogic();
        controlEvent();
    }

    private void handleLogic() {
        setSupportActionBar(toolbar);
        listUsersOnline = new ArrayList<ItemUsersOnline>();
        prepareData();
        adapterUsersOnline = new AdapterUsersOnline(listUsersOnline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listUsers.setLayoutManager(layoutManager);
        listUsers.setItemAnimator(new DefaultItemAnimator());
        listUsers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listUsers.setAdapter(adapterUsersOnline);
        adapterUsersOnline.notifyDataSetChanged();

    }

    private void controlEvent() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                swipeRefresh.setRefreshing(false);
                Toast.makeText(UsersOnlineActivity.this, "Updated !", Toast.LENGTH_SHORT).show();
            }
        });
        listUsers.addOnItemTouchListener(new RecyclerTouchListener(this, listUsers, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(UsersOnlineActivity.this, position +"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void controlView() {

        listUsers = (RecyclerView) findViewById(R.id.activity_users_online_listusers);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.activity_users_online_swiperefresh);
        toolbar = (Toolbar) findViewById(R.id.activity_users_online_toolbar);
    }
    private void prepareData(){
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a1, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a2, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a3, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a4, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a5, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a6, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a7, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a8, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a9, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a10, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a11, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a12, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a13, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a14, "Đức Linh", "Vui quá các bạn ơi!"));
        listUsersOnline.add(new ItemUsersOnline(R.drawable.a15, "Đức Linh", "Vui quá các bạn ơi!"));
    }


    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private UsersOnlineActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final UsersOnlineActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
