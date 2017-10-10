package com.example.healthhelper.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthhelper.function.Function;
import com.example.healthhelper.R;
import com.example.healthhelper.component.RoundProgressBar;
import com.example.healthhelper.adapter.FunctionAdapter;
import com.example.healthhelper.function.WaterCounter;
import com.example.healthhelper.service.TimeService;
import com.example.healthhelper.utils.PreUtils;
import com.example.healthhelper.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private RoundProgressBar roundProgressBar;
    private final Context context = this;

    //服务相关
    private TimeService.TimeBinder tBinder;
    private boolean isBind = false;

    private Function[] functions = {
                new Function("药物列表", R.drawable.medicine),
    };

    private List<Function> functionList = new ArrayList<>();

    private FunctionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //启动服务
        setupService();

        //设置字体,修改喝水的数量
        final TextView leftWaterText = (TextView) findViewById(R.id.left_water);
        leftWaterText.setTypeface(Typeface.createFromAsset(getAssets(),"font/AdobeKaitiStd-Regular.otf"));
        leftWaterText.getPaint().setFakeBoldText(true);
        leftWaterText.setText("今天我还需要喝：\n"+WaterCounter.remainWater()+"ml");

        //设置圆形进度条
        roundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
        roundProgressBar.setProgress(WaterCounter.drinkedPercent());
        roundProgressBar.setCricleProgressColor(Color.parseColor("#32989A"));
        //设置进度条长按事件
        roundProgressBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setTitle("提示")
                        .setMessage("是否清空已喝水量?")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                             public void onClick(DialogInterface dialog,int which){
                                PreUtils.putInt("user_data","drink_amount",0);
                                leftWaterText.setText("今天我还需要喝：\n"+WaterCounter.remainWater()+"ml");
                                tBinder.refreshDrink();
                                roundProgressBar.setProgress(WaterCounter.drinkedPercent());
                            }
                        })
                        .setNegativeButton("取消",null).show();
                return false;
            }
        });

        //将系统状态栏设置为透明
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.parseColor("#303F9F"));
        decorViewGroup.addView(statusBarView);



        //设置滑动菜单的图标
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //设置navigationView中的点击事件
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return true;
            }
        });

        //设置floating action bar 的点击事件
        //加50ml
        com.getbase.floatingactionbutton.FloatingActionButton add_50ml=
                (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_50ml);
        add_50ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaterCounter.drinkedWater(50);
                leftWaterText.setText("今天我还需要喝：\n"+WaterCounter.remainWater()+"ml");
                tBinder.refreshDrink();
                roundProgressBar.setProgress(WaterCounter.drinkedPercent());
            }
        });
        //加120ml
        com.getbase.floatingactionbutton.FloatingActionButton add_120ml=
                (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_120ml);
        add_120ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaterCounter.drinkedWater(120);
                leftWaterText.setText("今天我还需要喝：\n"+WaterCounter.remainWater()+"ml");
                tBinder.refreshDrink();
                roundProgressBar.setProgress(WaterCounter.drinkedPercent());
            }
        });

        /*
        ****自定义喝水量
         */
        com.getbase.floatingactionbutton.FloatingActionButton add_more =
                (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_more);
        add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View add_water_view = LayoutInflater.from(context).inflate(R.layout.add_water,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(add_water_view);
                final EditText edit_water_amount = (EditText) add_water_view.findViewById(R.id.add_water_amount);

                alertDialogBuilder.setCancelable(true)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //int age = Integer.parseInt(et_age.getText().toString());
                                int add_water_amount = Integer.parseInt(
                                        edit_water_amount.getText().toString());
                                WaterCounter.drinkedWater(add_water_amount);
                                leftWaterText.setText("今天我还需要喝：\n"+WaterCounter.remainWater()+"ml");
                                tBinder.refreshDrink();
                                roundProgressBar.setProgress(WaterCounter.drinkedPercent());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog add_water_dialog = alertDialogBuilder.create();
                add_water_dialog.show();
            }
        });

        //设置主界面菜单
        initFunctions();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FunctionAdapter(functionList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        final TextView leftWaterText = (TextView) findViewById(R.id.left_water);
        leftWaterText.setText("今天我还需要喝：\n"+WaterCounter.remainWater()+"ml");
        roundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
        roundProgressBar.setProgress(WaterCounter.drinkedPercent());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(isBind) this.unbindService(conn);
        PreUtils.putInt("user_data","day", TimeUtil.getCurrentDay());
    }

    //为toolbar加载菜单栏
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    //响应toolbar的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.settings:
                //Toast.makeText(this,"You clicked Settings",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void initFunctions(){
        functionList.clear();
        functionList.add(functions[0]);
    }

    //获取状态栏的高度
    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    //开启服务，通知水量，监测时间，进行清零
    private void setupService(){
        Intent intent = new Intent(this, TimeService.class);
        isBind = bindService(intent,conn,Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private TimerTask timerTask;
    private Timer timer;

   private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            tBinder = (TimeService.TimeBinder)service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
