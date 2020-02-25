package com.example.flutter_wx.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.flutter_wx.handler.FlutterWxRequestHandler
import com.example.flutter_wx.handler.FlutterWxResponseHandler
import com.example.flutter_wx.handler.WXAPiHandler
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

open class FlutterWxEntryActivity : Activity(), IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        try {
            WXAPiHandler.wxApi?.handleIntent(intent, this)
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)

        try {
            WXAPiHandler.wxApi?.handleIntent(intent, this)
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    override fun onResp(resp: BaseResp) {
        FlutterWxResponseHandler.handleResponse(resp)
    }

    override fun onReq(baseReq: BaseReq) {
// FIXME: 可能是官方的Bug，从微信拉起APP的Intent类型不对，无法跳转回Flutter Activity
        // 稳定复现场景：微信版本为7.0.5，小程序SDK为2.7.7
        val activity = FlutterWxRequestHandler.getRegistrar()?.activity()
        if (baseReq.type == 4 && activity is Activity) {
            // com.tencent.mm.opensdk.constants.ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX = 4
            startActivity(Intent(this, activity::class.java))
            finish()
        }
    }

}