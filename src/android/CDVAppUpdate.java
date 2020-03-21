package com.kungfukingbetty.cordova.appupdate;

import java.io.StringWriter;
import java.io.PrintWriter;
import android.util.Log;
import org.jsoup.*;
import org.jsoup.nodes.*;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.pm.PackageManager.NameNotFoundException;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import android.os.AsyncTask;
import android.annotation.TargetApi;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CDVAppUpdate extends CordovaPlugin {

    public static final String TAG = "NativeAppUpdate";

    public static CallbackContext mCallbackContext;
    public static PluginResult mPluginResult;

    public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.v(TAG, "Init NativeAppUpdate");
        mPluginResult = new PluginResult(PluginResult.Status.NO_RESULT);

        if (android.os.Build.VERSION.SDK_INT < 21) {
            return;
        }
    }

    public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;
        Log.v(TAG, "NativeAppUpdate action: " + action);

        final JSONObject errorResponse = new JSONObject();

        if (android.os.Build.VERSION.SDK_INT < 21) {
            Log.e(TAG, "Minimum SDK version 21 required");
            mPluginResult = new PluginResult(PluginResult.Status.ERROR);
            mCallbackContext.error(errorResponse.put("message", "Minimum SDK version 23 required"));
            mCallbackContext.sendPluginResult(mPluginResult);
            return true;
        }

        if ("needsupdate".equalsIgnoreCase(action)) {
            try {
                needsUpdate(args.getString(0), args.getString(1));
            } catch (Exception ignore) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ignore.printStackTrace(pw);
                Log.e(TAG, sw.toString());
                mPluginResult = new PluginResult(PluginResult.Status.ERROR);
                try{
                    mCallbackContext.error(errorResponse.put("message", sw.toString()));
                } catch (JSONException e) {
                    System.out.println(e);
                }
                mCallbackContext.sendPluginResult(mPluginResult);
            }
            return true;
        }

        return false;
    }

    private void needsUpdate(final String force_api, final String force_key) throws JSONException {
    /*
        // Get the app context
       Context this_ctx = (Context) this.cordova.getActivity();
       // Creates instance of the manager.
       AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this_ctx);

       // Returns an intent object that you use to check for an update.
       Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

       // Checks that the platform will allow the specified type of update.
       appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
           int update_avail = 0;
           if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
               update_avail = 1;
           }
                 // For a flexible update, use AppUpdateType.FLEXIBLE
                 // && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                     // Request the update.
           // }

           mPluginResult = new PluginResult(PluginResult.Status.OK, update_avail);
           mCallbackContext.success(update_avail);
           mPluginResult.setKeepCallback(true);
           mCallbackContext.sendPluginResult(mPluginResult);
       });

       appUpdateInfoTask.addOnFailureListener(taskError -> {
           StringWriter sw = new StringWriter();
           PrintWriter pw = new PrintWriter(sw);
           taskError.printStackTrace(pw);
           Log.e(TAG, sw.toString());
           final JSONObject taskErrorResponse = new JSONObject();
           mPluginResult = new PluginResult(PluginResult.Status.ERROR);
           try {
               mCallbackContext.error(taskErrorResponse.put("message", sw.toString()));
           } catch (JSONException e) {
               System.out.println(e);
           }
           mCallbackContext.sendPluginResult(mPluginResult);
       });
    */
        int update_avail = 0;
        int update_force = 0;
        JSONObject resultObj = new JSONObject();
        String currentVersion = "0.0.0";
        try {
            currentVersion = this.cordova.getActivity().getPackageManager().getPackageInfo(this.cordova.getActivity().getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e(TAG, sw.toString());
            final JSONObject taskErrorResponse = new JSONObject();
            mPluginResult = new PluginResult(PluginResult.Status.ERROR);
            try {
                mCallbackContext.error(taskErrorResponse.put("message", sw.toString()));
            } catch (JSONException e_inner) {
                sw = new StringWriter();
                pw = new PrintWriter(sw);
                e_inner.printStackTrace(pw);
                Log.d(TAG, sw.toString());
            }
            mCallbackContext.sendPluginResult(mPluginResult);
            return;
        }
        String newVersion = null;
        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + this.cordova.getActivity().getPackageName() + "&hl=en")
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select(".hAyfc .htlgb")
                            .get(7)
                            .ownText();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e(TAG, sw.toString());
            final JSONObject taskErrorResponse = new JSONObject();
            mPluginResult = new PluginResult(PluginResult.Status.ERROR);
            try {
                mCallbackContext.error(taskErrorResponse.put("message", sw.toString()));
            } catch (JSONException e_inner) {
                sw = new StringWriter();
                pw = new PrintWriter(sw);
                e_inner.printStackTrace(pw);
                Log.d(TAG, sw.toString());
            }
            mCallbackContext.sendPluginResult(mPluginResult);
            return;
        }
        Log.d(TAG, "Current version: " + currentVersion + ", Play Store version: " + newVersion);
        if (newVersion != null && !newVersion.isEmpty()) {
            String[] currentVersionArr = currentVersion.split("\\.");
            String[] newVersionArr = newVersion.split("\\.");
            for (int i=0; i<currentVersionArr.length; i++) {
                if (Float.valueOf(currentVersionArr[i]) < Float.valueOf(newVersionArr[i])) {
                    update_avail = 1;
                    String force_str = null;
                    try {
                        force_str = Jsoup.connect(force_api)
                                    .ignoreContentType(true)
                                    .timeout(30000)
                                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                    .referrer("http://www.google.com")
                                    .execute()
                                    .body();
                        JSONObject force_data = new JSONObject(force_str);
                        Iterator<String> force_keys = force_data.keys();
                        while(force_keys.hasNext()) {
                            String key = force_keys.next();
                            resultObj.put(key, force_data.get(key));
                        }
                    } catch (IOException e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        Log.e(TAG, sw.toString());
                        final JSONObject taskErrorResponse = new JSONObject();
                        mPluginResult = new PluginResult(PluginResult.Status.ERROR);
                        try {
                            mCallbackContext.error(taskErrorResponse.put("message", sw.toString()));
                        } catch (JSONException e_inner) {
                            sw = new StringWriter();
                            pw = new PrintWriter(sw);
                            e_inner.printStackTrace(pw);
                            Log.d(TAG, sw.toString());
                        }
                        mCallbackContext.sendPluginResult(mPluginResult);
                        return;
                    }
                    break;
                }
            }
        }
        
        resultObj.put("update_available",update_avail);

        mPluginResult = new PluginResult(PluginResult.Status.OK, resultObj);
        mCallbackContext.success(resultObj);
        mPluginResult.setKeepCallback(true);
        mCallbackContext.sendPluginResult(mPluginResult);
    }
}
