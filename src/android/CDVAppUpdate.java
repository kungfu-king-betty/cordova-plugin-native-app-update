/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package com.kungfukingbetty.cordova.appupdate;

/* General class imports */
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

/* Class imports for error handling */
import java.io.StringWriter;
import java.io.PrintWriter;
import org.json.JSONException;

/* Class imports for Cordova */
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

/* Class imports for In-App Updates */
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import com.android.volley.RequestQueue;

/**
 * Definition of the CDVAppUpdate class which defines the blueprint for the AppUpdate plugin.
 *
 * This plugin uses the In-App Updates class to check if the app using the plugin has an update
 * available natively. The plugin will also check the app update type to see if the update should be
 * forced using the forceAPI passed into the plugin or by defaulting to the In-App Updates class.
 */
public class CDVAppUpdate extends CordovaPlugin {
    /* Public AppUpdate properties */
    public static final String TAG = "NativeAppUpdate";

    public static CallbackContext mCallbackContext;
    public static PluginResult mPluginResult;

    /* Private variables */
    private AppUpdateManager appUpdateManager;
    private int updateAvailable = 0;
    private int forceUpdate = 0;
    private final int MINIMUM_SDK = 21;

    private JSONObject resultObj;

    private static final StringWriter sw = new StringWriter();
    private static final PrintWriter pw = new PrintWriter(sw);

    /* Custom AppUpdate Errors */
    private static final HashMap<String, Integer> errorList;
    static {
        errorList = new HashMap<>();
        errorList.put("Unexpected Error Occurred", -1);
        errorList.put("Plugin Execution Failed", -2);
        errorList.put("App Update Check Failed", -3);
        errorList.put("App Update Type API Check Failed", -4);
        errorList.put("App Update Type Check Failed", -5);
    }

    //--------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------
    /**
     * Initializes the AppUpdate object for use in hybrid mobile app
     * @param cordova   The cordova app interface to create the object for
     * @param webView   The webView of the cordova/hybrid app
     */
    public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        Log.v(TAG, "Initialized");

        // Initialize the hybrid app result objects
        mPluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        resultObj = new JSONObject();

        // Create the app update manager
        appUpdateManager = AppUpdateManagerFactory.create(this.cordova.getActivity());

        // Make sure the app using the plugin meets the minimum SDK requirement
        if (android.os.Build.VERSION.SDK_INT < MINIMUM_SDK) {
            Log.e(TAG, "Minimum SDK version " + MINIMUM_SDK + " required");
        }
    }

    /**
     * Executes the plugin method based on the action passed in
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments, wrapped with some Cordova helpers.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return boolean
     */
    public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) {
        mCallbackContext = callbackContext;

        Log.v(TAG, "execute - " + action);

        // Make sure the app using the plugin meets the minimum SDK requirement
        if (android.os.Build.VERSION.SDK_INT < MINIMUM_SDK) {
            Log.e(TAG, "Minimum SDK version " + MINIMUM_SDK + " required");

            final JSONObject errorResponse = new JSONObject();

            mPluginResult = new PluginResult(PluginResult.Status.ERROR);

            // Return an error response to the callback for the minimum sdk
            try {
                errorResponse.put("status", "error");
                errorResponse.put("errno", 0);
                errorResponse.put("message", "Minimum SDK version " + MINIMUM_SDK + " required");

                mCallbackContext.error(errorResponse);
                mCallbackContext.sendPluginResult(mPluginResult);
            } catch (JSONException e) {
                this.errorHandler("Unexpected Error Occurred", e);
            }

            return false;
        }

        // Check if the plugin needsUpdate method was called
        if ("needsupdate".equalsIgnoreCase(action)) {
            try {
                needsUpdate(args.getString(0), args.getString(1));
                return true;
            } catch (Exception e) {
                this.errorHandler("Plugin Execution Failed", e);
                return false;
            }
        }

        return false;
    }

    //--------------------------------------------------
    // PRIVATE LOCAL METHODS
    //--------------------------------------------------
    /**
     * Checks if there is an update available for the app using the plugin with the in-app updates class
     *
     * @param forceApi The api endpoint to hit when checking if an app update should be forced, if not passed in the native
     *                 in-app updates class will be used to check for a force update
     */
    private void needsUpdate(final String forceApi, final String forceResponseKey) {
        // Start the task of getting the in-app update info
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Add a listener to handle when the app update info is returned successfully
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // There is an update available
                updateAvailable = 1;

                // Check if the app update should be forced
                checkAppUpdateType(forceApi, forceResponseKey, appUpdateInfo);
            } else {
                // There is NO app update available
                updateAvailable = 0;
                forceUpdate = 0;

                triggerCallback();
            }
        }).addOnFailureListener(taskError -> this.errorHandler("App Update Check Failed", taskError));
    }

    /**
     * Checks the forceAPI for a force update if passed in, otherwise the native in-app updates class is used
     * @param forceApi         The api endpoint to hit when checking if an app update should be forced, if not
     *                         passed in the native in-app updates class will be used to check for a force update
     * @param forceResponseKey The key to look for in the api response
     * @param appUpdateInfo    The app update info object returned from the in-app updates class
     */
    private void checkAppUpdateType(final String forceApi, final String forceResponseKey, final AppUpdateInfo appUpdateInfo) {
        if (forceApi.length() > 0) {
            // Create a new request queue to run the request
            RequestQueue queue = Volley.newRequestQueue(this.cordova.getActivity());

            // Create the request to get a json object response
            JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                forceApi,
                null,
                (response) -> {
                    // Handle when the request responds successfully
                    try {
                        // Loop through the response key:value pairs and add to the result object to be returned
                        Iterator<String> responseKeys = response.keys();
                        while (responseKeys.hasNext()) {
                            String key = responseKeys.next();

                            // Handle when the force_update key is returned in the api response
                            if (forceResponseKey.length() > 0 && key.equals(forceResponseKey)) {
                                forceUpdate = (int) response.get(key);
                            }

                            resultObj.put(key, response.get(key));
                        }

                        // Call the success callback
                        triggerCallback();
                    } catch (JSONException e) {
                        errorHandler("App Update Type Check Failed", e);
                    }
                },
                (e) -> {
                    // Handle when the request fails
                    errorHandler("App Update Type API Check Failed", e);
                });

            // Add the request to the queue for the request to be run
            queue.add(request);
        } else {
            // Use the In-App Updates class to determine if the update should be forced
            forceUpdate = (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) ? 1 : 0);

            try {
                resultObj.put("force_update", forceUpdate);
            } catch (JSONException e) {
                this.errorHandler("Unexpected Error Occurred", e);
            }

            // Call the success callback
            triggerCallback();
        }
    }

    /** Triggers the success callback to return the resultObj to the calling JavaScript */
    private void triggerCallback() {
        try {
            // Add the values to be returned
            resultObj.put("update_available", updateAvailable);

            // Log the update check completed
            Log.v(TAG, "App Update Check Complete");

            // Return the result object
            mPluginResult = new PluginResult(PluginResult.Status.OK, resultObj);
            mCallbackContext.success(resultObj);
            mPluginResult.setKeepCallback(true);
            mCallbackContext.sendPluginResult(mPluginResult);
        } catch (JSONException e) {
            this.errorHandler("Unexpected Error Occurred", e);
        }
    }

    /**
     * Returns the error code bound to the error message
     *
     * @param errorMessage Error message to get the error code for
     * @return Integer
     */
    private Integer getErrorCode(final String errorMessage) {
        return errorList.get(errorMessage);
    }

    /**
     * Returns an error to the JavaScript callback object
     *
     * @param errorMessage The error message to return
     * @param e            The error object to return
     */
    private void errorHandler(final String errorMessage, final Exception e) {
        final Integer errorNo = this.getErrorCode(errorMessage);

        // Log the error stack for easier debugging
        Log.e(TAG, "ERROR (" + errorNo + "): " + errorMessage);
        e.printStackTrace(pw);
        Log.e(TAG, sw.toString());

        // Build the error response to return to the callback
        final JSONObject taskErrorResponse = new JSONObject();
        mPluginResult = new PluginResult(PluginResult.Status.ERROR);
        try {
            taskErrorResponse.put("status", "error");
            taskErrorResponse.put("code", this.getErrorCode(errorMessage));
            taskErrorResponse.put("message", errorMessage);

            mCallbackContext.error(taskErrorResponse);
        } catch (JSONException e_inner) {
            Log.e(TAG, "ERROR (0): Error Parsing Failed!");
            e_inner.printStackTrace(pw);
            Log.e(TAG, sw.toString());

            mCallbackContext.error("Failed to return error object");
        }

        // Return error to callback
        mCallbackContext.sendPluginResult(mPluginResult);
    }
}
