package com.kungfukingbetty.cordova.appupdate;

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

    protected void pluginInitialize(final CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        boolean result = false;
        if ("needsupdate".equalsIgnoreCase(action)) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        needsUpdate(callbackContext);
                    } catch (Exception ignore) {
                        System.out.println("AppUpdate Error:" + ignore);
                    }
                }
            });
            return true;
        }

        return false;
    }

    @TargetApi(21)
    private void needsUpdate(final CallbackContext callbackContext) throws JSONException {
        // Get the app context
        Context this_ctx = (Context) this.cordova.getActivity();
        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this_ctx);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            boolean update_avail = false;
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                update_avail = true;
            }
                  // For a flexible update, use AppUpdateType.FLEXIBLE
                  // && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                      // Request the update.
            // }

            PluginResult result = new PluginResult(PluginResult.Status.OK, update_avail);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
        });
    }
}
