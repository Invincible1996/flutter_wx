import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_wx/flutter_wx.dart' as FlutterWx;

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _result = "无";

  @override
  void initState() {
    super.initState();
    initPlatformState();
    FlutterWx.responseFromAuth.listen((data) {
      print('data----->>>>$data');
      setState(() {
        _result = "${data.errCode}";
      });
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    try {
      await FlutterWx.registerWxApi(
        appId: "wxd930ea5d5a258f4f",
        universalLink: "https://your.univeral.link.com/placeholder/",
      );
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    if (!mounted) return;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            Center(
              child: Text('Running on: $_result\n'),
            ),
            RaisedButton(
              onPressed: () {
                FlutterWx.sendWeChatAuth(
                    scope: "snsapi_userinfo", state: "wechat_sdk_demo_test");
              },
              child: Text('获取用户信息'),
            )
          ],
        ),
      ),
    );
  }
}
