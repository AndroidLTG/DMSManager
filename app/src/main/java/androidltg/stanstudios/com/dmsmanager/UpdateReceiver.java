package androidltg.stanstudios.com.dmsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by DMSv4 on 9/11/2015.
 */
public class UpdateReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "TienTestInternet";
    private boolean isConnected = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Đang nhận thông tin về mạng");
        isNetworkAvailable(context);

    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            Log.v(LOG_TAG, "Bây giờ bạn đang kết nối mạng!");
                            Toast.makeText(context, " Kết nối mạng trở lại, chuyển sang chạy ONLINE", Toast.LENGTH_SHORT).show();
                            isConnected = true;
                            MyGlobal.isOnline =true;
                            MyGlobal.context =context;
                            MyGlobal.SyncData();
                       //     Login.imageloading.setImageResource(R.drawable.online);
                        }
                        return true;
                    }
                }
            }
        }
        Log.v(LOG_TAG, " Kết nối mạng bị ngắt!");
        Toast.makeText(context, " Kết nối mạng đã bị ngắt, chuyển sang chạy OFFLINE", Toast.LENGTH_SHORT).show();
        isConnected = false;
        MyGlobal.isOnline =false;
    //    Login.imageloading.setImageResource(R.drawable.offline);
        return false;
    }


}
