package com.kungfukingbetty.cordova.appupdate;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import android.annotation.TargetApi;
import android.content.Context;
import com.google.android.play.core.tasks.Task;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;

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
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        needsUpdate();
                    } catch (Exception ignore) {
                        mPluginResult = new PluginResult(PluginResult.Status.ERROR);
                        mCallbackContext.error(errorResponse.put("message", ignore));
                        mCallbackContext.sendPluginResult(mPluginResult);
                    }
                }
            });
            return true;
        }

        return false;
    }

    private void needsUpdate() throws JSONException {
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
            final JSONObject taskErrorResponse = new JSONObject();
            mPluginResult = new PluginResult(PluginResult.Status.ERROR);
            mCallbackContext.error(taskErrorResponse.put("message", taskError));
            mCallbackContext.sendPluginResult(mPluginResult);
        });
    }
}
