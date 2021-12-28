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
    needsUpdate: function(success, failure, force_api=null, force_key=null, country="") {
        // if (typeof(taskId) !== 'string') {
        //     throw "AppUpdate.needsUpdate now requires an appId string as the first argument";
        // }
        success = success || EMPTY_FN;
        failure = failure || EMPTY_FN;
        exec(success, failure, "AppUpdate", "needsUpdate",[force_api,force_key,country]);
    }
};

module.exports = AppUpdate;
