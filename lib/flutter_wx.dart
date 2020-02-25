import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'models/wechat_response.dart';

StreamController<WeChatAuthResponse> _responseAuthController =
    new StreamController.broadcast();

Stream<WeChatAuthResponse> get responseFromAuth =>
    _responseAuthController.stream;

final MethodChannel _channel = const MethodChannel('flutter_wx')
  ..setMethodCallHandler(_handler);

Future<dynamic> _handler(MethodCall call) {
  switch (call.method) {
    case "onAuthResponse":
      _responseAuthController.sink
          .add(WeChatAuthResponse.fromMap(call.arguments));
      break;
    default:
      break;
  }
  return Future.value(true);
}

Future<String> get platformVersion async {
  final String version = await _channel.invokeMethod('getPlatformVersion');
  return version;
}

/*
   *注册 
   */
Future registerWxApi(
    {String appId,
    bool doOnIOS: true,
    bool doOnAndroid: true,
    String universalLink}) async {
  if (doOnIOS && Platform.isIOS) {
    if (universalLink.trim().isEmpty || !universalLink.startsWith("https")) {
      throw ArgumentError.value(universalLink,
          "your universal link is illegal, see https://developers.weixin.qq.com/doc/oplatform/Mobile_App/Access_Guide/iOS.html for detail");
    }
  }

  return await _channel.invokeMethod("registerApp", {
    "appId": appId,
    "iOS": doOnIOS,
    "android": doOnAndroid,
    "universalLink": universalLink
  });
}

/*
   *发送登录申请 
   */
Future sendWeChatAuth(
    {String openId, @required String scope, String state}) async {
  // "scope": scope, "state": state, "openId": openId
  assert(scope != null && scope.trim().isNotEmpty);
  return await _channel.invokeMethod(
      "sendAuth", {"scope": scope, "state": state, "openId": openId});
}
