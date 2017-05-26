/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package io.github.dpary;

import android.app.Activity;
import android.content.Intent;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.MediaPlayer.PlayM4.Player;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;

public class HKWS extends CordovaPlugin {
    public static final String INIT_SDK = "InitHKWS";
    public static final String LOGIN = "Login";
    public static final String PALY = "Play";
    private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(INIT_SDK.equals(action)){
            return initeSdk(args,callbackContext);
        }
         if(LOGIN.equals(action)){
            return loginNormalDevice(args,callbackContext);
        }
        if(PALY.equals(action)){
            Intent intent = new Intent(cordova.getActivity(), PlayActivity.class);
            int iUserID = args.optInt(0);
            int iChan = args.optInt(1);
            intent.putExtra("USER_ID", iUserID);
			intent.putExtra("CHAN", iChan);
            if (this.cordova != null) {
				this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
			}
        }
        return true;
    }

    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------
    private boolean initeSdk(final JSONArray args, CallbackContext callbackContext) {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            callbackContext.error("error");
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",true);
        callbackContext.success("success");
        return true;
    }
    private boolean loginNormalDevice(final JSONArray args, CallbackContext callbackContext) {
        // get instance
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30) {
            callbackContext.error("error");
            return false;
        }
          String strIP = args.optString(0);
          int nPort = args.optInt(1);
          String strUser = args.optString(2);
          String strPsd = args.optString(3);

        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort,
                strUser, strPsd, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0) {
            callbackContext.error("error");
            return false;
        }
        int m_iStartChan=-1;
        int m_iChanNum=-1;


        System.out.println("设备模拟通道个数=" + m_oNetDvrDeviceInfoV30.byChanNum);
        System.out.println("模拟通道起始通道号=" + m_oNetDvrDeviceInfoV30.byStartChan);
        System.out.println("设备最大数字通道个数，低 8 位=" + m_oNetDvrDeviceInfoV30.byIPChanNum);
        System.out.println("零通道个数=" + m_oNetDvrDeviceInfoV30.byZeroChanNum);
        System.out.println("起始数字通道号=" + m_oNetDvrDeviceInfoV30.byStartDChan);
        System.out.println("数字通道个数，高 8 位=" + m_oNetDvrDeviceInfoV30.byHighDChanNum);
        System.out.println("Test=" + m_oNetDvrDeviceInfoV30.byIPChanNum + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256);

        
        if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        } else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = 1/*m_oNetDvrDeviceInfoV30.byIPChanNum
                    + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256*/;
        }

        JSONObject obj = new JSONObject();
			try {
				obj.put("iChan", m_iStartChan);
				obj.put("iUserID", iLogID);
				obj.put("ChanNum", m_iChanNum);
			} catch (JSONException e) {
				e.printStackTrace();
				callbackContext.error("JSON Exception");
			}
			callbackContext.success(obj);
        return true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (resultCode == Activity.RESULT_OK && data != null) {
		// 	ArrayList<String> fileNames = data.getStringArrayListExtra("MULTIPLEFILENAMES");
		// 	JSONArray res = new JSONArray(fileNames);
		// 	this.callbackContext.success(res);
		// } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
		// 	String error = data.getStringExtra("ERRORMESSAGE");
		// 	this.callbackContext.error(error);
		// } else if (resultCode == Activity.RESULT_CANCELED) {
		// 	JSONArray res = new JSONArray();
		// 	this.callbackContext.success(res);
		// } else {
		// 	this.callbackContext.error("No images selected");
		// }
	}

}
