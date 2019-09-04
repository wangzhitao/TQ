package online.rh.simpleweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import online.rh.simpleweather.R;
import online.rh.simpleweather.model.ListAdapter;
import online.rh.simpleweather.work.WorkerClass;

import static online.rh.simpleweather.util.TimeTool.getTimeNew;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ListAdapter mListAdapter;
    private List<String> mDatas;
    HashMap<String, String> mMap;
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {

        @Override

        public void run() {

//这里写入要作的事情
            initView();

            handler.postDelayed(this, 60000);

        }

    };


    @BindView(R.id.ic_0)
    ImageView ic0;
    @BindView(R.id.type_0)
    TextView type0;
    @BindView(R.id.low_0)
    TextView low0;
    @BindView(R.id.time_0)
    TextView time0;
    @BindView(R.id.day_0)
    TextView day0;
    @BindView(R.id.daytime_0)
    LinearLayout daytime0;
    @BindView(R.id.gungao_0)
    LinearLayout gungao0;
    @BindView(R.id.tianqi_0)
    LinearLayout tianqi0;
    @BindView(R.id.applist)
    LinearLayout applist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme1);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = this.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            window.setAttributes(attributes);
        }


        ButterKnife.bind(this);

        final PeriodicWorkRequest periodicWorkRequest
                = new PeriodicWorkRequest.Builder(WorkerClass.class, 2, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance().enqueue(periodicWorkRequest);
        initView();


        handler.postDelayed(runnable, 60000);

        initData();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mListAdapter = new ListAdapter(this, mDatas);
        mListAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Click" + mDatas.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mListAdapter.remove(position); //remove the item
                Toast.makeText(MainActivity.this, "LongClick" + mDatas.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mListAdapter);
        LinearLayoutManager nn = new LinearLayoutManager(this);
        nn.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(nn);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }


    protected void initView() {
        SharedPreferences prefs = getSharedPreferences("武汉", MODE_PRIVATE);
        low0.setText(prefs.getString("low_0", "32  /  20"));
        type0.setText(prefs.getString("type_0", "晴到多云"));

        mMap = getTimeNew();
        Log.d("wztbs", "week = " + mMap.get("week"));
        String mMinute = mMap.get("minute");
        if (mMinute.length() == 1) {
            mMinute = "0" + mMinute;
        }
        // time0.setText("15 : 50");
        time0.setText(mMap.get("hour") + " : " + mMinute);
        day0.setText(mMap.get("year") + "/" + mMap.get("month") + "/" + mMap.get("day") + "            星期" + mMap.get("week"));


    }

    protected void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 0; i < 21; i++) {
            mDatas.add(String.valueOf(i));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tianqi_0:
                Intent intent_choose_theme = new Intent(this, WeatherActivity.class);
                startActivity(intent_choose_theme);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

}
