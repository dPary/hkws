#import <Cordova/CDVPlugin.h>
#import "DeviceInfo.h"
#import "PlayerDemoViewController.h"

@interface HKWS : CDVPlugin{

}

- (void) InitHKWS:(CDVInvokedUrlCommand *)command;
- (void) Login:(CDVInvokedUrlCommand *)command;
- (void) Play:(CDVInvokedUrlCommand *)command;

@property (nonatomic,strong)PlayerDemoViewController *rootVC;


@end
