/*
 * @author kevin
 * @date 2020-02-25
 * @Description: dart file
*/

class WeChatAuthResponse {
  final String errStr;
  final int type;
  final int errCode;
  final String androidOpenId;
  final String iOSDescription;
  final String country;
  final String lang;
  final String code;
  final String androidUrl;
  final String state;
  final String androidTransaction;

  WeChatAuthResponse.fromMap(Map map)
      : errStr = map["errStr"],
        type = map["type"],
        errCode = map["errCode"],
        androidOpenId = map["openId"],
        iOSDescription = map["description"],
        country = map["country"],
        lang = map["lang"],
        code = map["code"],
        androidUrl = map["url"],
        state = map["state"],
        androidTransaction = map["transaction"];
}
