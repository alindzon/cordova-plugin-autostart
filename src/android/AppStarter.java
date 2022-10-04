package com.tonikorin.cordova.plugin.autostart;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import com.tonikorin.cordova.plugin.autostart.AutoStart;
// import android.util.Log;

public class AppStarter {

    public static final int BYPASS_USERPRESENT_MODIFICATION = -1;
    private static final String CORDOVA_AUTOSTART = "cordova_autostart";

    public void run(Context context, Intent intent, int componentState) {
	     this.run(context, intent, componentState, false);
    }

    public void run(Context context, Intent intent, int componentState, boolean onAutostart) {
        // Enable or Disable UserPresentReceiver (or bypass the modification)
        //Log.d("Cordova AppStarter", "UserPresentReceiver component, new state:" + String.valueOf(componentState));
        if( componentState != BYPASS_USERPRESENT_MODIFICATION ) {
            ComponentName receiver = new ComponentName(context, UserPresentReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver, componentState, PackageManager.DONT_KILL_APP);
        }

        // Starting your app...
        //Log.d("Cordova AppStarter", "STARTING APP...");
        SharedPreferences sp = context.getSharedPreferences(AutoStart.PREFS, Context.MODE_PRIVATE);
        String packageName = context.getPackageName();
        String activityClassName = sp.getString(AutoStart.ACTIVITY_CLASS_NAME, "");
        if( !activityClassName.equals("") ){
            //Log.d("Cordova AppStarter", className);
            Intent activityIntent = new Intent();
            activityIntent.setClassName(
                context, String.format("%s.%s", packageName, activityClassName));
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (onAutostart) {
              activityIntent.putExtra(CORDOVA_AUTOSTART, true);
            }
            context.startActivity(activityIntent);
        }
        // Start a service in the background.
        String serviceClassName = sp.getString(AutoStart.SERVICE_CLASS_NAME, "");
	Integer dot = serviceClassName.lastIndexOf(".");//to avoid  java.lang.StringIndexOutOfBoundsException: begin 0, end -1, length 0
	if (dot == -1) dot = 0;
        String servicePackageName = serviceClassName.substring(0, dot);
        if ( !serviceClassName.equals("") ) {
            Intent serviceIntent = new Intent();
            serviceIntent.setClassName(servicePackageName, serviceClassName);
            if ( onAutostart ) {
                serviceIntent.putExtra(CORDOVA_AUTOSTART, true);
            }
            context.startService(serviceIntent);
        }

    }
}
