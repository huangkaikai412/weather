package pku.ss.kitty.app;

import android.app.Application;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pku.ss.kitty.bean.City;
import pku.ss.kitty.db.CityDB;

/**
 * Created by HuangKaiKai on 2015/10/20.
 */
public class MyApplication extends Application{
    private static final String TAG = "MyApp";

    private static MyApplication mApp;
    public List<City> mCityList;
    public CityDB mCityDB;

    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases";
                //+ File.separator + CityDB.CITY_DB_NAME;
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();
        File db = new File(path + File.separator + CityDB.CITY_DB_NAME);
        //db.delete();
        Log.d(TAG,path);
        if (!db.exists()) {
            Log.i(TAG,"db is not exists");
            //将asserts下的db文件拷贝到手机相应位置
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer,0,len);
                    fos.flush();
                }
                long size = db.getTotalSpace();
                Log.d(TAG,Long.toString(size));
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        Log.i(TAG,"db exists");
        return new CityDB(this,path);
    }

    private boolean prepareCityList() {
        mCityList = mCityDB.getAllCity();
        for (City city:mCityList) {
            String cityname = city.getCity();
            Log.d(TAG,cityname);
        }
        return true;
    }

    private void initCityList() {
        mCityList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "myapp->onCreate");
        mApp = this;

        mCityDB = openCityDB();
        initCityList();
    }

    public List<City> getmCityList() {
        return mCityList;
    }

    public static Application getInstance() {
        return mApp;
    }
}
