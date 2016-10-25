package be.david.mangaapp.lab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by David on 25/10/2016.
 */

public class ConnectivityDetector {

    private Context context;

    public ConnectivityDetector(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet() {


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo =  connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
