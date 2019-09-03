package online.rh.simpleweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.rh.simpleweather.R;

public class MainActivity extends AppCompatActivity {
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
        ButterKnife.bind(this);
        SharedPreferences prefs = getSharedPreferences("武汉", MODE_PRIVATE);
        low0.setText(prefs.getString("low_0", "32  /  20"));
        type0.setText(prefs.getString("type_0", "晴到多云"));

        time0.setText("15 : 50");
        day0.setText("2019/9/3   星期一");


    }
}
