#import "OrbaonePlugin.h"
#if __has_include(<orbaone_plugin/orbaone_plugin-Swift.h>)
#import <orbaone_plugin/orbaone_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "orbaone_plugin-Swift.h"
#endif

@implementation OrbaonePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftOrbaonePlugin registerWithRegistrar:registrar];
}
@end
