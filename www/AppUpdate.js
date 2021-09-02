/***
 * Custom Cordova App Update plugin.
 * @author <developerDawg@gmail.com>
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
var exec = require("cordova/exec");

var EMPTY_FN = function() {}

var AppUpdate = {
    /**
     * The method used to execute and check for an app update using the AppUpdate plugin
     *
     * @param success {Function} - the success callback function that will be called when the plugin executes successfully
     * @param failure {Function} - the error callback function that will be called when the plugin fails to execute
     * @param force_api_url {String=""} - an api endpoint to call when checking the app update type for a forced update
     * @param force_api_response_key {String=""} - the key returned from the api containing the app update type value
     */
    needsUpdate: function(success, failure, force_api_url, force_api_response_key) {
        success = success || EMPTY_FN;
        failure = failure || EMPTY_FN;
        force_api_url = force_api_url || "";
        force_api_response_key = force_api_response_key || "";
        exec(success, failure, "AppUpdate", "needsUpdate", [force_api_url, force_api_response_key]);
    }
};

module.exports = AppUpdate;
