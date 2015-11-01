package pku.ss.kitty.myweather;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pku.ss.kitty.app.MyApplication;
import pku.ss.kitty.bean.City;


public class SelectCity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SelectCity";

    private ImageView BackBtn;
    private TextView tieleTV;
    private EditText selectcityET;
    private ListView citylist;
    private SimpleAdapter adapter;
    private List<City> cl;
    private Intent intent = new Intent();
    //private Application app = MyApplication.getInstance();

    //搜索城市
    private void search(List<HashMap<String, Object>> data,String key) {
        for (City i:cl) {
            if (i.getCity().contains(key)) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("city", i.getCity());
                item.put("id", i.getNumber());
                data.add(item);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        //返回按钮
        BackBtn = (ImageView) findViewById(R.id.title_back);
        BackBtn.setOnClickListener(this);

        //界面title
        tieleTV = (TextView) findViewById(R.id.title_name);
        Intent in = this.getIntent();
        tieleTV.setText("当前城市：" + in.getExtras().getString("city"));

        //设置城市列表内容
        citylist = (ListView) findViewById(R.id.list_view);
        cl = ((MyApplication)getApplicationContext()).getmCityList();
        final List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
        for (City i:cl) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("city", i.getCity());
            item.put("id", i.getNumber());
            data.add(item);
        }
        adapter = new SimpleAdapter(this,data,R.layout.select_item,
                new String[]{"city","id"},new int[]{R.id.city_name,R.id.city_id});
        citylist.setAdapter(adapter);

        //设置城市列表中每个item点击事件
        citylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = data.get(i).get("city").toString();
                String id = data.get(i).get("id").toString();
                intent.putExtra("name", name);
                intent.putExtra("id", id);
                //Log.d(name,id);
                tieleTV.setText("当前选择城市：" + name);
                //startActivityForResult(intent,0);
                //设置返回数据
                SelectCity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                SelectCity.this.finish();
            }
        });

        //搜索编辑框
        selectcityET = (EditText) findViewById(R.id.search_edit);
        selectcityET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //编辑框内容改变后，动态更新城市列表内容
                if (s.length() != 0) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String key = selectcityET.getText().toString();
                            data.clear();
                            search(data, key);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                SelectCity.this.setResult(RESULT_OK, intent);
                SelectCity.this.finish();
                break;
            default:break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_city, menu);
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
}
