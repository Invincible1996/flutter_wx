package com.example.flutter_wx.handler


import android.util.Log
import com.tencent.mm.opensdk.diffdev.DiffDevOAuthFactory
import com.tencent.mm.opensdk.diffdev.OAuthErrCode
import com.tencent.mm.opensdk.diffdev.OAuthListener
import com.tencent.mm.opensdk.modelmsg.SendAuth
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

internal class FlutterWxAuthHandler(private val methodChannel: MethodChannel) {
    //    private DiffDevOAuthFactory.getDiffDevOAuth()
    private val qrCodeAuth by lazy {
        DiffDevOAuthFactory.getDiffDevOAuth()
    }

    private val qrCodeAuthListener by lazy {
        object : OAuthListener {
            override fun onAuthFinish(p0: OAuthErrCode, authCode: String?) {
                methodChannel.invokeMethod("onAuthByQRCodeFinished", mapOf(
                        "errCode" to p0.code,
                        "authCode" to authCode
                ))
            }

            override fun onAuthGotQrcode(p0: String?, p1: ByteArray) {
                methodChannel.invokeMethod("onAuthGotQRCode", p1)
            }

            override fun onQrcodeScanned() {
                methodChannel.invokeMethod("onQRCodeScanned", null)
            }

        }
    }

    //登录
    fun sendAuth(call: MethodCall, result: MethodChannel.Result) {
        val req = SendAuth.Req()
        req.scope = call.argument("scope")
        req.state = call.argument("state")
        val openId = call.argument<String?>("openId")
        if (!openId.isNullOrBlank()) {
            req.openId = call.argument("openId")
        }

        Log.d("FlutterWxAuthHandler", req.scope)

        result.success(WXAPiHandler.wxApi?.sendReq(req))
    }


    fun authByQRCode(call: MethodCall, result: MethodChannel.Result) {
        val appId = call.argument("appId") ?: ""
        val scope = call.argument("scope") ?: ""
        val nonceStr = call.argument("nonceStr") ?: ""
        val timeStamp = call.argument("timeStamp") ?: ""
        val signature = call.argument("signature") ?: ""
//        val schemeData = call.argument("schemeData")?:""

        result.success(qrCodeAuth.auth(appId, scope, nonceStr, timeStamp, signature, qrCodeAuthListener))

    }

    fun stopAuthByQRCode(result: MethodChannel.Result) {
        result.success(qrCodeAuth.stopAuth())
    }

    fun removeAllListeners() {
        qrCodeAuth.removeAllListeners()
    }

}