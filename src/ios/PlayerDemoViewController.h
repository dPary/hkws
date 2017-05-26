#import <UIKit/UIKit.h>
#import "IOSPlayM4.h"
#import "DeviceInfo.h"

#define RTP

@interface PlayerDemoViewController : UIViewController 
{
	UIView                  *m_playView;
    UIView                  *m_multiView[4];
    int                     m_nPreviewPort;
	int                     m_lUserID;
	int                     m_lRealPlayID;
    }


@property (nonatomic, retain) IBOutlet UIView *m_playView;
@property (nonatomic,strong)NSNumber *UserID;
@property (nonatomic,strong)NSNumber *StartNum;
@property int m_nPreviewPort;
@property int m_lUserID;
@property int m_lRealPlayID;


- (void) stopPlay;
- (bool) loginNormalDevice;


@end
