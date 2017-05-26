#import "HKWS.h"
#import "hcnetsdk.h"
#import "PlayerDemoViewController.h"



@implementation HKWS
- (void)InitHKWS:(CDVInvokedUrlCommand*)command
{
    BOOL bRet = NET_DVR_Init();
    CDVPluginResult* result;
    if(bRet){
        result = [CDVPluginResult resultWithStatus: CDVCommandStatus_OK messageAsBool:bRet];
    }else{
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsBool:bRet];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)Login:(CDVInvokedUrlCommand*)command
{
    NSString * iP = [command argumentAtIndex:0 withDefault:nil];
    NSString * port = [command argumentAtIndex:1 withDefault:nil];
    NSString * usrName = [command argumentAtIndex:2 withDefault:nil];
    NSString * password = [command argumentAtIndex:3 withDefault:nil];

    DeviceInfo *deviceInfo = [[DeviceInfo alloc] init];
    deviceInfo.chDeviceAddr = iP;
    deviceInfo.nDevicePort = [port integerValue];
    deviceInfo.chLoginName = usrName;
    deviceInfo.chPassWord = password;

    NET_DVR_DEVICEINFO_V30 logindeviceInfo = {0};
    
    // encode type
    NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
    NSInteger m_lUserID = -1;
    NSInteger g_iStartChan = -1;
    NSInteger g_iPreviewChanNum = -1;
    m_lUserID = NET_DVR_Login_V30((char*)[deviceInfo.chDeviceAddr UTF8String],
                                  deviceInfo.nDevicePort,
                                  (char*)[deviceInfo.chLoginName cStringUsingEncoding:enc],
                                  (char*)[deviceInfo.chPassWord UTF8String],
                                  &logindeviceInfo);
    CDVPluginResult* result;
    if (m_lUserID == -1)
    {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"error"];
    }else{
       
        if(logindeviceInfo.byChanNum > 0)
            {
                g_iStartChan = logindeviceInfo.byStartChan;
                g_iPreviewChanNum = logindeviceInfo.byChanNum;
            }
        else if(logindeviceInfo.byIPChanNum > 0)
            {
                g_iStartChan = logindeviceInfo.byStartDChan;
                g_iPreviewChanNum = logindeviceInfo.byIPChanNum + logindeviceInfo.byHighDChanNum * 256;
            }
            NSDictionary *dict = @{@"iChan" : [NSNumber numberWithInt:g_iStartChan], @"iUserID" : [NSNumber numberWithInt:m_lUserID], @"ChanNum" : [NSNumber numberWithInt:g_iPreviewChanNum]};

            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict options:0 error:NULL];

            result = [CDVPluginResult resultWithStatus: CDVCommandStatus_OK messageAsDictionary:dict]; 
    }

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)Play:(CDVInvokedUrlCommand*)command
{
    NSNumber * iUserID = [command argumentAtIndex:0 withDefault:nil];
    NSNumber * iChan = [command argumentAtIndex:1 withDefault:nil];
    

        
        self.rootVC = [[PlayerDemoViewController alloc]init];
        
        self.rootVC.UserID = iUserID;
        
        self.rootVC.StartNum = iChan;

        [self.viewController presentViewController:self.rootVC animated:YES completion:nil];
        
        //添加通知跳转
        [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(goHome) name:@"goHome" object:nil];
        
}
- (void)goHome{
    
    [self.rootVC dismissViewControllerAnimated:YES completion:nil];
    
}
@end