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
var exec = require('cordova/exec'),
    argscheck = require('cordova/argscheck');

function HKWS() {}

/**
 * Get device InitHKWS
 *
 * @param {Function} successCallback The function to call when the heading data is available
 * @param {Function} errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
 */
HKWS.prototype.InitHKWS = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "HKWS", "InitHKWS", []);
};
HKWS.prototype.Login = function(options, successCallback, errorCallback) {
    var getValue = argscheck.getValue;
    options = options || {};
    var strIP = getValue(options.strIP, '10.0.0.107');
    var nPort = getValue(options.nPort, 8000);
    var strUser = getValue(options.strUser, 'admin');
    var strPsd = getValue(options.strPsd, 'Rainier123');
    exec(successCallback, errorCallback, "HKWS", "Login", [strIP, nPort, strUser, strPsd]);
};
HKWS.prototype.Play = function(options, successCallback, errorCallback) {
    var getValue = argscheck.getValue;
    options = options || {};
    var iUserID = getValue(options.iUserID, -1);
    var iChan = getValue(options.iChan, -1);
    exec(successCallback, errorCallback, "HKWS", "Play", [iUserID, iChan]);
};
module.exports = new HKWS();