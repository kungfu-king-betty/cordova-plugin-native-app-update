package com.kungfukingbetty.cordova.appupdate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import android.annotation.TargetApi;

import com.google.android.play.core.tasks.Task<ResultT>;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;

public class CDVAppUpdate extends CordovaPlugin {

    @Override
    protected void pluginInitialize() { }

    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        boolean result = false;
        if (action.equalsIgnoreCase('needsupdate')) {
            result = true;
            needsUpdate(data.getJSONObject(0), callbackContext);
        }
        return result;
    }

    @TargetApi(21)
    private boolean needsUpdate(JSONObject options, final CallbackContext callbackContext) throws JSONException {
        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);

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
