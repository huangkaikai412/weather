package pku.ss.kitty.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import pku.ss.kitty.bean.TodayWeather;
import pku.ss.kitty.util.NetUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MyWeather";

    private TextView titleCityTv;
    private ImageView UpdateBtn, CityselectBtn;
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
                     temperatureTv, climateTv, windTv;
    private  ImageView weatherImg, pmImg;

    private static final int UPDATE_TODAY_WEATHER = 1;
    //主线程接受消息，执行相应操作
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather)msg.obj);
                    break;
                default:break;
            }
        }
    };

    //初始化界面
    void initView() {
        titleCityTv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city_name);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm_quality);
        pmImg = (ImageView) findViewById(R.id.pm_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "mainAct->onCreate");

        setContentView(R.layout.weather_info);
        UpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        UpdateBtn.setOnClickListener(this);
        CityselectBtn = (ImageView) findViewById(R.id.title_city_manager);
        CityselectBtn.setOnClickListener(this);

        initView();
        //UpdateBtn.performClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //更新天气信息
        UpdateBtn.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Intent intent = this.getIntent();
        String name = data.getStringExtra("name");
        String id = data.getStringExtra("id");
        if (id != null) {
            SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("main_city_code",id);
            editor.commit();
            Log.d(name, id);
        }
        //UpdateBtn.performClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //解析函数
    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayweather = null;
        try{
            int fengxiangCount = 0;
            int fengliCount = 0;
            int dateCount = 0;
            int highCount = 0;
            int lowCount = 0;
            int typeCount = 0;
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser par = fac.newPullParser();
            par.setInput(new StringReader(xmldata));
            int eventType = par.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:break;
                    case XmlPullParser.START_TAG:
                        if (par.getName().equals("resp"))
                            todayweather = new TodayWeather();

                        if (todayweather != null) {
                            if (par.getName().equals("city")) {
                                eventType = par.next();
                                todayweather.setCity(par.getText());
                            } else if (par.getName().equals("updatetime")) {
                                eventType = par.next();
                                todayweather.setUpdatetime(par.getText());
                            } else if (par.getName().equals("shidu")) {
                                eventType = par.next();
                                todayweather.setShidu(par.getText());
                            } else if (par.getName().equals("wendu")) {
                                eventType = par.next();
                                todayweather.setWendu(par.getText());
                            } else if (par.getName().equals("pm25")) {
                                eventType = par.next();
                                todayweather.setPm25(par.getText());
                            } else if (par.getName().equals("quality")) {
                                eventType = par.next();
                                todayweather.setQuality(par.getText());
                            } else if (par.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = par.next();
                                todayweather.setFengxiang(par.getText());
                                fengxiangCount++;
                            } else if (par.getName().equals("fengli") && fengliCount == 0) {
                                eventType = par.next();
                                todayweather.setFengli(par.getText());
                                fengliCount++;
                            } else if (par.getName().equals("date") && dateCount == 0) {
                                eventType = par.next();
                                todayweather.setDate(par.getText());
                                dateCount++;
                            } else if (par.getName().equals("high") && highCount == 0) {
                                eventType = par.next();
                                todayweather.setHigh(par.getText());
                                highCount++;
                            } else if (par.getName().equals("low") && lowCount == 0) {
                                eventType = par.next();
                                todayweather.setLow(par.getText());
                                lowCount++;
                            } else if (par.getName().equals("type") && typeCount == 0) {
                                eventType = par.next();
                                todayweather.setType(par.getText());
                                typeCount++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:break;
                }
                eventType = par.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return todayweather;
    }

    //根据城市ID查询对应天气信息
    private void queryWeatherCode(String CityCode) {
        //拼接地址
        final String address = " http://wthrcdn.etouch.cn/WeatherApi?citykey=" + CityCode;
        Log.d(TAG, address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    URLConnection UrlCon = url.openConnection();
                    InputStream is = UrlCon.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder stringbuilder = new StringBuilder();
                    String str;
                    //一行为单位读取内容
                    while ((str = reader.readLine()) != null)
                        stringbuilder.append(str);
                    String sbstr = stringbuilder.toString();
                    Log.d(TAG,sbstr);
                    TodayWeather TW = parseXML(sbstr);
                    if (TW != null) {
                        Log.d(TAG, TW.toString());
                        //发送消息给主线程，更新UI
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = TW;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void updateTodayWeather(TodayWeather tw) {
        //更新文字部分
        titleCityTv.setText(tw.getCity() + "天气");
        cityTv.setText(tw.getCity());
        timeTv.setText(tw.getUpdatetime() + "发布");
        humidityTv.setText("湿度" + tw.getShidu());
        weekTv.setText(tw.getDate());
        pmDataTv.setText(tw.getPm25());
        pmQualityTv.setText(tw.getQuality());
        temperatureTv.setText(tw.getLow() + "~" + tw.getHigh());
        climateTv.setText(tw.getType());
        windTv.setText("风力" + tw.getFengli());

        //更新天气图片
        switch (tw.getType()) {
            case "暴雪":weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);break;
            case "暴雨":weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);break;
            case "大暴雨":weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);break;
            case "大雪":weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);break;
            case "大雨":weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);break;
            case "多云":weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);break;
            case "雷阵雨":weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);break;
            case "雷阵雨冰雹":weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);break;
            case "晴":weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);break;
            case "沙尘暴":weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);break;
            case "特大暴雨":weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);break;
            case "小雪":weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);break;
            case "小雨":weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);break;
            case "阴":weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);break;
            case "雨夹雪":weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);break;
            case "阵雪":weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);break;
            case "阵雨":weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);break;
            case "中雪":weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);break;
            case "中雨":weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);break;
            default:weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);break;
        }

        //更新PM2.5图片
        if (tw.getPm25() != null) {
            int pmdata = Integer.parseInt(tw.getPm25());
            if (pmdata>=0 && pmdata<=50)
                pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
            else if (pmdata>50 && pmdata<=100)
                pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
            else if (pmdata>100 && pmdata<=150)
                pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
            else if (pmdata>150 && pmdata<=200)
                pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
            else if (pmdata>200 && pmdata<=300)
                pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
            else
                pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }

        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_LONG).show();
        UpdateBtn.setImageResource(R.drawable.title_update);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_update_btn:
                //设置按钮的旋转效果
                UpdateBtn.setImageResource(R.drawable.upate_rotate);
                //获取城市ID
                SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
                String CityCode = SP.getString("main_city_code", "101010100");
//            queryWeatherCode(CityCode);
                //检测网络状态
                if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                    Log.d(TAG, "网络OK");
                    queryWeatherCode(CityCode);
                } else {
                    Log.d(TAG, "网络挂了");
                    Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.title_city_manager:
                //跳转到城市列表界面并得到返回数据
                Intent i = new Intent(this, SelectCity.class);
                i.putExtra("city",cityTv.getText());
                startActivityForResult(i, 0);
                break;
            default:break;
        }
    }
}
