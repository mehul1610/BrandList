package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.myapplication.adapter.AdapterBrand;
import com.example.myapplication.bean.BeanBrand;
import com.example.myapplication.bean.BeanBrandList;
import com.example.myapplication.database.Brand;
import com.example.myapplication.dialog.RegisterDialog;
import com.example.myapplication.interfaces.CallBack;
import com.example.myapplication.network.JsonResponse;
import com.example.myapplication.network.Network;
import com.example.myapplication.network.NetworkStateChecker;
import com.example.myapplication.network.Request;
import com.example.myapplication.services.FetchService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CallBack, SwipeRefreshLayout.OnRefreshListener {
    private AdapterBrand mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterDialog dialog = new RegisterDialog(MainActivity.this);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        onListUpdate();
                    }
                });
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterBrand(getBrands());
        mRecyclerView.setAdapter(mAdapter);
        onListUpdate();

        swipeRefreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swipe_refresh));
        swipeRefreshLayout.setOnRefreshListener(this);

        Intent intent = new Intent(this, FetchService.class);
        boolean isWorking = PendingIntent.getService(this, 101, intent, PendingIntent.FLAG_NO_CREATE) != null;
        if (!isWorking) {
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 101, intent, 0);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30000, 1000 * 3600, pendingIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRecyclerView.getAdapter() != null)
            mAdapter.setBrands(getBrands());
        else {
            mAdapter = new AdapterBrand(getBrands());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private ArrayList<BeanBrand> getBrands() {
        return Brand.getBrand(this).getAllRecords();
    }

    @Override
    public void onListUpdate() {
        Request request = new Request("http://appsdata2.cloudapp.net/demo/androidApi/list.php");

        new JsonResponse(request, BeanBrandList.class, new Network.Response() {
            @Override
            public void onComplete(Object in, String method) {
                if (in == null)
                    return;
                BeanBrandList list = (BeanBrandList) in;
                if (list.getErrorCode() == 1 && list.getBrandList() != null) {
                    Brand.getBrand(MainActivity.this).addBrands(((BeanBrandList) in).getBrandList());
                    if (mRecyclerView.getAdapter() != null)
                        mAdapter.setBrands(getBrands());
                    else {
                        mAdapter = new AdapterBrand(getBrands());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }).execute();
    }

    @Override
    public void onRefresh() {
        onListUpdate();
    }
}
