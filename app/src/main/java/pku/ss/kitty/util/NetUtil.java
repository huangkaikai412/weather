package pku.ss.kitty.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * Created by HuangKaiKai on 2015/10/19.
 * 检查网络状态
 */
public class NetUtil {
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager CM = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //Wifi
        State state = CM.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == State.CONNECTED || state == State.CONNECTING)
            return NETWORN_WIFI;
        //Mobile
        state = CM.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == State.CONNECTED || state == State.CONNECTING)
            return NETWORN_MOBILE;

        return NETWORN_NONE;
    }
}
