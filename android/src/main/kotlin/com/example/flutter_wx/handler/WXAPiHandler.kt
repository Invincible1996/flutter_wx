package com.example.flutter_wx.handler

import android.content.Context
import com.example.flutter_wx.constant.CallResult
import com.example.flutter_wx.constant.WechatPluginKeys
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

object WXAPiHandler {

    var wxApi: IWXAPI? = null

    fun registerApp(context: Context, call: MethodCall, result: MethodChannel.Result) {

        if (call.argument<Boolean>(WechatPluginKeys.ANDROID) == false) {
            return
        }

        if (wxApi != null) {
            result.success(mapOf(
                    WechatPluginKeys.PLATFORM to WechatPluginKeys.ANDROID,
                    WechatPluginKeys.RESULT to true
            ))
            return
        }

        val appId: String? = call.argument(WechatPluginKeys.APP_ID)
        if (appId.isNullOrBlank()) {
            result.error("invalid app id", "are you sure your app id is correct ?", appId)
            return
        }


        val api = WXAPIFactory.createWXAPI(context.applicationContext, appId)
        val registered = api.registerApp(appId)
        wxApi = api
        result.success(mapOf(
                WechatPluginKeys.PLATFORM to WechatPluginKeys.ANDROID,
                WechatPluginKeys.RESULT to registered
        ))
    }

    fun checkWeChatInstallation(result: MethodChannel.Result) {
        if (wxApi == null) {
            result.error(CallResult.RESULT_API_NULL, "please config  wxapi first", null)
            return
        } else {
            result.success(wxApi!!.isWXAppInstalled)
        }

    }
}