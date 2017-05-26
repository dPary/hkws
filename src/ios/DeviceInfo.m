#import "DeviceInfo.h"

@implementation DeviceInfo
@synthesize nDeviceID;
@synthesize chDeviceName;
@synthesize chDeviceAddr;
@synthesize nDevicePort;
@synthesize chLoginName;
@synthesize chPassWord;
@synthesize nChannelNum;


- (id)init
{
    return self;
}

- (void)dealloc 
{
	[chDeviceName release];
	[chDeviceAddr release];
	[chLoginName release];
	[chPassWord release];

    [super dealloc];
}

@end
