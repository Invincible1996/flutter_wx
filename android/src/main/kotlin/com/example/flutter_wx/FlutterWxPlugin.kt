package com.example.flutter_wx

import android.app.Activity
import android.util.Log
import androidx.annotation.NonNull;
import com.example.flutter_wx.constant.WeChatPluginMethods
import com.example.flutter_wx.handler.FlutterWxAuthHandler
import com.example.flutter_wx.handler.FlutterWxResponseHandler
import com.example.flutter_wx.handler.WXAPiHandler
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** FlutterWxPlugin */
open class FlutterWxPlugin() : FlutterPlugin, MethodCallHandler, ActivityAware {

    private lateinit var activity: Activity
    private lateinit var channel: MethodChannel;

    override fun onDetachedFromActivity() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_wx")
        FlutterWxResponseHandler.setMethodChannel(channel)
        channel.setMethodCallHandler(this); //注意此处的修改
    }

    companion object {
        const val TAG = "FlutterWxPlugin"
//        @JvmStatic
//        fun registerWith(registrar: Registrar) {
//            val channel = MethodChannel(registrar.messenger(), "flutter_wx")
//
//            channel.setMethodCallHandler(FlutterWxPlugin())
//        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {

        Log.d(TAG, call.method)

        when (call.method) {
            //注册
            WeChatPluginMethods.REGISTER_APP -> WXAPiHandler.registerApp(activity, call, result)
            //检查是否安装了微信
            WeChatPluginMethods.IS_WE_CHAT_INSTALLED -> WXAPiHandler.checkWeChatInstallation(result)

            //微信登录
            WeChatPluginMethods.SEND_AUTH -> FlutterWxAuthHandler(channel).sendAuth(call, result)

            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }
}
