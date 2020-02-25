#import "FlutterWxPlugin.h"
#if __has_include(<flutter_wx/flutter_wx-Swift.h>)
#import <flutter_wx/flutter_wx-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_wx-Swift.h"
#endif

@implementation FlutterWxPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterWxPlugin registerWithRegistrar:registrar];
}
@end
