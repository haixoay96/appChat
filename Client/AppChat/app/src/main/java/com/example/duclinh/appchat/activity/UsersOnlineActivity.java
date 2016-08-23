package com.example.duclinh.appchat.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.adapter.AdapterListMessageChat;
import com.example.duclinh.appchat.adapter.AdapterUsersOnline;
import com.example.duclinh.appchat.designnotifi.CenterManagerMessage;
import com.example.duclinh.appchat.fragment.ScreenChatFragment;
import com.example.duclinh.appchat.orther.DividerItemDecoration;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsersOnlineActivity extends AppCompatActivity {
    private CenterManagerMessage centerManagerMessage;
    private Toolbar toolbar;
    private RecyclerView listUsers;
    private AdapterUsersOnline adapterUsersOnline;
    private JSONArray listUsersOnline;
    private SwipeRefreshLayout swipeRefresh;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_online);
        controlView();
        handleLogic();
        controlEvent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.socket.disconnect();
        finish();
    }

    private void handleLogic() {
        // init center notifi message
        centerManagerMessage = new CenterManagerMessage();
        // get data users online
        try {
            listUsersOnline = new JSONArray(getIntent().getStringExtra("listUsersOnline"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolbar);
        adapterUsersOnline = new AdapterUsersOnline(listUsersOnline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listUsers.setLayoutManager(layoutManager);
        listUsers.setItemAnimator(new DefaultItemAnimator());
        listUsers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listUsers.setAdapter(adapterUsersOnline);
        adapterUsersOnline.notifyDataSetChanged();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();


        MyApplication.socket.off("addUser");
        MyApplication.socket.on("addUser", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                listUsersOnline.put(args[0]);
                UsersOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UsersOnlineActivity.this, args[0].toString(), Toast.LENGTH_SHORT).show();
                        adapterUsersOnline.notifyDataSetChanged();
                    }
                });

            }
        });

        MyApplication.socket.off("removeUser");
        MyApplication.socket.on("removeUser", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                int index = -1;
                for(int i = 0 ; i<listUsersOnline.length(); i++ ){
                    try {
                        JSONObject jsonObject = (JSONObject) listUsersOnline.get(i);
                        if(object.getString("account").equals(jsonObject.getString("account"))){
                            index = i;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listUsersOnline.remove(index);
                UsersOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterUsersOnline.notifyDataSetChanged();
                    }
                });
            }
        });

        MyApplication.socket.off("receiveMessage");
        MyApplication.socket.on("receiveMessage", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
               UsersOnlineActivity.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(UsersOnlineActivity.this)
                               .setSmallIcon(R.drawable.small)
                               .setContentTitle("message")
                               .setContentText("Thong bao");
                       NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                       notificationManager.notify(3, builder.build());
                       JSONObject object = (JSONObject) args[0];
                       String account = null;
                       String message = null;
                       try {
                           account =object.getString("account");
                           message= object.getString("message");
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                       centerManagerMessage.notifiAllClient(account, message);
                   }
               });
            }
        });

        MyApplication.socket.off("connect");
        MyApplication.socket.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // re login when disconnect
            }
        });

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
                try {
                    String account = ((JSONObject)listUsersOnline.get(position)).getString("account");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    ScreenChatFragment screenChatFragment = ScreenChatFragment.newInstance(account,centerManagerMessage);
                    screenChatFragment.show(fragmentManager, account);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });


    }

    private void controlView() {

        listUsers = (RecyclerView) findViewById(R.id.activity_users_online_listusers);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.activity_users_online_swiperefresh);
        toolbar = (Toolbar) findViewById(R.id.activity_users_online_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_users_online_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.activity_users_online_navigation);
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
