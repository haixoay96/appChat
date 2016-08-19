package com.example.duclinh.appchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.adapter.AdapterListMessageChat;
import com.example.duclinh.appchat.adapter.AdapterUsersOnline;
import com.example.duclinh.appchat.fragment.ScreenChatFragment;
import com.example.duclinh.appchat.orther.DividerItemDecoration;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsersOnlineActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView listUsers;
    private AdapterUsersOnline adapterUsersOnline;
    private JSONArray listUsersOnline;
    private SwipeRefreshLayout swipeRefresh;

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
                       FragmentManager fragmentManager = getSupportFragmentManager();
                       ScreenChatFragment screenChatFragment = ScreenChatFragment.newInstance("Tin nhắn");
                       screenChatFragment.show(fragmentManager, "Tin nhắn");
                   }
               });
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
                    JSONObject object = new JSONObject();
                    object.put("account", ((JSONObject)listUsersOnline.get(position)).getString("account"));
                    object.put("message", "Hello");
                    MyApplication.socket.emit("sendMessage",object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
