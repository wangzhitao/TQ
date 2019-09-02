package online.laoliang.simpleweather.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import online.laoliang.simpleweather.R;
import online.laoliang.simpleweather.model.CityList;
import online.laoliang.simpleweather.model.CityListAdapter;
import online.laoliang.simpleweather.service.AutoUpdateService;
import online.laoliang.simpleweather.util.HttpCallbackListener;
import online.laoliang.simpleweather.util.HttpUtil;
import online.laoliang.simpleweather.util.ScreenShotUtils;
import online.laoliang.simpleweather.util.ShareUtils;
import online.laoliang.simpleweather.util.ToastUtil;
import online.laoliang.simpleweather.util.Utility;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class WeatherActivityNew extends Activity implements SwipeRefreshLayout.OnRefreshListener, OnClickListener {

    private Retrofit retrofit;

    //定义当前活动的一个实例，用于在其他活动类中调用
    protected static Activity instance = null;

    // 滑动刷新
    private SwipeRefreshLayout swipe_container;

    // 进度对话框
    private ProgressDialog progressDialog;

    // 右侧菜单
    private Button share_weather;

    // 侧滑菜单
    private DrawerLayout drawer_layout;
    private View menu_list;
    private ListView city_list;
    private Button menu_left;
    private Button add_city;
    private Button choose_theme;
    private Button setting;
    private Button about;

    //首页天气信息页面视图
    private LinearLayout weather_info;

    // 已选城市列表
    private HashMap<String, String> cityName_weatherCode = new HashMap<String, String>(10);
    private ArrayList<CityList> cities = new ArrayList<CityList>(10);

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    // 用于显示城市名
    private TextView city_name_tv;
    private TextView nonce_city_name;

    // 用于显示当前时间
    private TextView current_date_tv;

    // 用于显示当前温度
    private TextView wendu_tv;

    // 用于显示天气信息对应的图片
    private ImageView ic_00_iv;
    private ImageView ic_0_iv;
    private ImageView ic_1_iv;
    private ImageView ic_2_iv;
    private ImageView ic_3_iv;
    private ImageView ic_4_iv;
    private ImageView ic_000_iv;

    // 其他信息
    private TextView high_00_tv;
    private TextView low_00_tv;
    private TextView date_00_tv;
    private TextView type_00_tv;
    private TextView fengli_00_tv;
    private TextView divide_00;

    private TextView high_0_tv;
    private TextView low_0_tv;
    private TextView date_0_tv;
    private TextView type_0_tv;
    private TextView fengli_0_tv;
    private TextView du_0;
    private TextView divide_0;
    private TextView type_000_tv;
    private TextView high_000_tv;
    private TextView low_000_tv;
    private TextView divide_000;

    private TextView high_1_tv;
    private TextView low_1_tv;
    private TextView date_1_tv;
    private TextView type_1_tv;
    private TextView fengli_1_tv;
    private TextView divide_1;

    private TextView high_2_tv;
    private TextView low_2_tv;
    private TextView date_2_tv;
    private TextView type_2_tv;
    private TextView fengli_2_tv;
    private TextView divide_2;

    private TextView high_3_tv;
    private TextView low_3_tv;
    private TextView date_3_tv;
    private TextView type_3_tv;
    private TextView fengli_3_tv;
    private TextView divide_3;

    private TextView high_4_tv;
    private TextView low_4_tv;
    private TextView date_4_tv;
    private TextView type_4_tv;
    private TextView fengli_4_tv;
    private TextView divide_4;

    private void findView() {

        instance = this;

        swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(this);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu_list = findViewById(R.id.menu_list);
        city_list = (ListView) findViewById(R.id.city_list);


        share_weather = (Button) findViewById(R.id.share_weather);
        share_weather.setOnClickListener(this);
        menu_left = (Button) findViewById(R.id.menu_left);
        menu_left.setOnClickListener(this);
        add_city = (Button) findViewById(R.id.add_city);
        add_city.setOnClickListener(this);
        choose_theme = (Button) findViewById(R.id.choose_theme);
        choose_theme.setOnClickListener(this);
        setting = (Button) findViewById(R.id.setting);
        setting.setOnClickListener(this);
        about = (Button) findViewById(R.id.about);
        about.setOnClickListener(this);

        city_name_tv = (TextView) findViewById(R.id.city_name);
        nonce_city_name = (TextView) findViewById(R.id.nonce_city_name);
        current_date_tv = (TextView) findViewById(R.id.current_date);
        wendu_tv = (TextView) findViewById(R.id.wendu);

        ic_00_iv = (ImageView) findViewById(R.id.ic_00);
        ic_0_iv = (ImageView) findViewById(R.id.ic_0);
        ic_1_iv = (ImageView) findViewById(R.id.ic_1);
        ic_2_iv = (ImageView) findViewById(R.id.ic_2);
        ic_3_iv = (ImageView) findViewById(R.id.ic_3);
        ic_4_iv = (ImageView) findViewById(R.id.ic_4);
        ic_000_iv = (ImageView) findViewById(R.id.ic_000);

        high_00_tv = (TextView) findViewById(R.id.high_00);
        low_00_tv = (TextView) findViewById(R.id.low_00);
        date_00_tv = (TextView) findViewById(R.id.date_00);
        type_00_tv = (TextView) findViewById(R.id.type_00);
        fengli_00_tv = (TextView) findViewById(R.id.fengli_00);
        divide_00 = (TextView) findViewById(R.id.divide_00);

        high_0_tv = (TextView) findViewById(R.id.high_0);
        low_0_tv = (TextView) findViewById(R.id.low_0);
        date_0_tv = (TextView) findViewById(R.id.date_0);
        type_0_tv = (TextView) findViewById(R.id.type_0);
        fengli_0_tv = (TextView) findViewById(R.id.fengli_0);
        du_0 = (TextView) findViewById(R.id.du_0);
        divide_0 = (TextView) findViewById(R.id.divide_0);
        type_000_tv = (TextView) findViewById(R.id.type_000);
        high_000_tv = (TextView) findViewById(R.id.high_000);
        low_000_tv = (TextView) findViewById(R.id.low_000);
        divide_000 = (TextView) findViewById(R.id.divide_000);

        high_1_tv = (TextView) findViewById(R.id.high_1);
        low_1_tv = (TextView) findViewById(R.id.low_1);
        date_1_tv = (TextView) findViewById(R.id.date_1);
        type_1_tv = (TextView) findViewById(R.id.type_1);
        fengli_1_tv = (TextView) findViewById(R.id.fengli_1);
        divide_1 = (TextView) findViewById(R.id.divide_1);

        high_2_tv = (TextView) findViewById(R.id.high_2);
        low_2_tv = (TextView) findViewById(R.id.low_2);
        date_2_tv = (TextView) findViewById(R.id.date_2);
        type_2_tv = (TextView) findViewById(R.id.type_2);
        fengli_2_tv = (TextView) findViewById(R.id.fengli_2);
        divide_2 = (TextView) findViewById(R.id.divide_2);

        high_3_tv = (TextView) findViewById(R.id.high_3);
        low_3_tv = (TextView) findViewById(R.id.low_3);
        date_3_tv = (TextView) findViewById(R.id.date_3);
        type_3_tv = (TextView) findViewById(R.id.type_3);
        fengli_3_tv = (TextView) findViewById(R.id.fengli_3);
        divide_3 = (TextView) findViewById(R.id.divide_3);

        high_4_tv = (TextView) findViewById(R.id.high_4);
        low_4_tv = (TextView) findViewById(R.id.low_4);
        date_4_tv = (TextView) findViewById(R.id.date_4);
        type_4_tv = (TextView) findViewById(R.id.type_4);
        fengli_4_tv = (TextView) findViewById(R.id.fengli_4);
        divide_4 = (TextView) findViewById(R.id.divide_4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        retrofit = new Retrofit.Builder().baseUrl(
                "http://wthrcdn.etouch.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        OnClick();

    }


    public interface RequestServices {

        @GET
        Observable<ResponseBody> getString(@Url String url);

        @FormUrlEncoded
        @POST
        Observable<ResponseBody> post(@FieldMap Map<String, String> params);
    }

    public void OnClick() {

        //  OnlineRecongnize = false;
        Long mb1 = System.currentTimeMillis();

        final RequestServices requestServices = retrofit.create(RequestServices.class);
        final HashMap<String, Integer> pamars1 = new HashMap<>();
        pamars1.put("page", 1);


        requestServices.getString("http://wthrcdn.etouch.cn/weather_mini?citykey=101200101")//这里我们即可获得一个Observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {


                        try {

                            String response = responseBody.string();
                            JSONObject jsonObject = null;


                            final String city = Utility.handleWeatherResponse(WeatherActivityNew.this, response);
//                            try {
//                                jsonObject = new JSONObject(response);
//
//                                JSONObject data = jsonObject.getJSONObject("data");
//                                city = data.getString("city");
//
//                                Log.d("sb", "city = " + city);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(Throwable e) {

                        Log.d("rxjavawzto", "onError(Throwable e ");

                    }

                    @Override
                    public void onComplete() {

                        Log.d("rxjavawztsb", "onComplete() ");
                        SharedPreferences prefs = getSharedPreferences("武汉", MODE_PRIVATE);


                        String high_0 = prefs.getString("high_0", null);
                       // high_0_tv.setText(high_0);
                        Log.d("rxjavawztsb", "onComplete() high_0 = " + high_0);
//                        low_0_tv.setText(prefs.getString("low_0", null));
//                        date_0_tv.setText(prefs.getString("date_0", null));
//                        type_0_tv.setText(prefs.getString("type_0", null));
//                        fengli_0_tv.setText(prefs.getString("fengli_0", null));

                    }
                });
    }


    /**
     * 查询县级代号所对应的天气代号
     *
     * @author 梁鹏宇 2016-7-29 下午2:31:00
     */
    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode", null);
    }

    /**
     * 查询天气代号所对应的天气
     *
     * @author 梁鹏宇 2016-7-29 下午2:31:17
     */
    private void queryWeatherInfo(String weatherCode) {
        String address = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + weatherCode;
        Log.d("wzy", "weatherCode = " + weatherCode);
        queryFromServer(address, "weatherCode", weatherCode);
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
     *
     * @author 梁鹏宇 2016-7-29 下午2:31:40
     */
    private void queryFromServer(final String address, final String type, final String weatherCode) {
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

            @Override
            public void onFinish(String response) {
                Log.d("wzy", "response =" + response);
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        // 从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    // 处理服务器返回的天气信息
                    final String city = Utility.handleWeatherResponse(WeatherActivityNew.this, response);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // 将得到的城市名与城市天气代号对应存储起来，刷新天气的时候会用到
                            SharedPreferences.Editor editor = getSharedPreferences("data_city", MODE_PRIVATE).edit();
                            editor.putString(city, weatherCode);
                            editor.commit();
                            // 更新城市列表并展示
                            //  updateCityList(city, "add");
                            closeProgressDialog();
                            ToastUtil.showToast(WeatherActivityNew.this, "天气已是最新  \\(^o^)/~", Toast.LENGTH_SHORT);

                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        closeProgressDialog();
                        ToastUtil.showToast(WeatherActivityNew.this, "Duang~ 没网了", Toast.LENGTH_SHORT);
                    }
                });
            }
        });
    }


    /**
     * 根据得到的天气type，返回对应的天气图片Id
     *
     * @author 梁鹏宇 2016-7-30 下午8:26:35
     */
    public static int selectImage(String type) {
        int icId;
        switch (type) {
            case "阴":
                icId = R.mipmap.weather_cloudy_day;
                break;
            case "多云":
                icId = R.mipmap.weather_cloudy_weather;
                break;
            case "雾":
                icId = R.mipmap.weather_fog;
                break;
            case "霾":
                icId = R.mipmap.weather_haze;
                break;
            case "大雨":
                icId = R.mipmap.weather_rain_heavy;
                break;
            case "小雨":
                icId = R.mipmap.weather_rain_light;
                break;
            case "中雨":
                icId = R.mipmap.weather_rain_moderate;
                break;
            case "小到中雨":
                icId = R.mipmap.weather_rain_light_moderate;
                break;
            case "中到大雨":
                icId = R.mipmap.weather_rain_moderate_heavy;
                break;
            case "阵雨":
                icId = R.mipmap.weather_rain_shower;
                break;
            case "雷阵雨":
                icId = R.mipmap.weather_rain_thunderstorms;
                break;
            case "暴雨":
                icId = R.mipmap.weather_rain_torrential;
                break;
            case "雨夹雪":
                icId = R.mipmap.weather_sleet;
                break;
            case "大雪":
                icId = R.mipmap.weather_snow_heavy;
                break;
            case "小雪":
                icId = R.mipmap.weather_snow_light;
                break;
            case "中雪":
                icId = R.mipmap.weather_snow_moderate;
                break;
            case "阵雪":
                icId = R.mipmap.weather_snow_shower;
                break;
            case "暴雪":
                icId = R.mipmap.weather_snow_torrential;
                break;
            case "晴":
                icId = R.mipmap.weather_sunny_day;
                break;
            default:
                icId = R.mipmap.not_applicable;
                break;
        }
        return icId;
    }

    /**
     * 触发滑动刷新后执行的操作
     */
    @Override
    public void onRefresh() {
        SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
        String cityName = prefs.getString("nonce_city", null);
        prefs = getSharedPreferences("data_city", MODE_PRIVATE);
        final String weatherCode = prefs.getString(cityName, null);
        if (!TextUtils.isEmpty(weatherCode)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    queryWeatherInfo(weatherCode);
                    swipe_container.setRefreshing(false);
                }
            }, 0); // 0秒后发送消息，停止刷新
        } else {
            ToastUtil.showToast(WeatherActivityNew.this, "☜ 亲！先添加一个城市吧", Toast.LENGTH_SHORT);
            swipe_container.setRefreshing(false);
        }
    }

    /**
     * 为按键注册监听事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_weather:
                String fileName = "简约天气-分享.jpeg";
                SharedPreferences prefs = getSharedPreferences("data_setting", MODE_PRIVATE);
                String cityName = prefs.getString("nonce_city", null);
                prefs = getSharedPreferences("data_city", MODE_PRIVATE);
                final String weatherCode = prefs.getString(cityName, null);
                if (TextUtils.isEmpty(weatherCode)) {
                    ToastUtil.showToast(WeatherActivityNew.this, "☜ 亲！先添加一个城市吧", Toast.LENGTH_SHORT);
                } else if (ScreenShotUtils.shotBitmap(WeatherActivityNew.this, getExternalCacheDir() + File.separator + fileName)) {
                    ToastUtil.showToast(this, "分享天气给朋友", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(WeatherActivityNew.this, WeatherActivity.class);
                    startActivity(intent);
                    finish();
                    ShareUtils.share(getExternalCacheDir() + File.separator + fileName, "来自简约天气的分享", WeatherActivityNew.this);
                } else {
                    ToastUtil.showToast(WeatherActivityNew.this, "        一键截图分享失败！\n\n请尝试打开存储空间权限哦", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.menu_left:
                drawer_layout.openDrawer(menu_list);
                break;
            case R.id.add_city:
                Intent intent_add_city = new Intent(this, ChooseAreaActivity.class);
                intent_add_city.putExtra("from_weather_activity", true);
                startActivity(intent_add_city);
                finish();
                break;
            case R.id.choose_theme:
                Intent intent_choose_theme = new Intent(this, ChooseThemeActivity.class);
                drawer_layout.closeDrawers();
                startActivity(intent_choose_theme);
                break;
            case R.id.setting:
                Intent intent_setting = new Intent(this, SettingActivity.class);
                drawer_layout.closeDrawers();
                startActivity(intent_setting);
                break;
            case R.id.about:
                Intent intent_about = new Intent(this, AboutActivity.class);
                drawer_layout.closeDrawers();
                startActivity(intent_about);
                break;
            default:
                break;
        }
    }


    /**
     * 判断Back按键，根据当前所在页面，决定返回到哪里
     */
    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(menu_list)) {
            drawer_layout.closeDrawers();
        } else {
            finish();
        }
    }

    /**
     * 显示进度对话框
     *
     * @author 梁鹏宇 2016-7-21 下午11:51:42
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     *
     * @author 梁鹏宇 2016-7-21 下午11:51:58
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
