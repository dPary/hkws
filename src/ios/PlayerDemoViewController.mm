#import "PlayerDemoViewController.h"
#import "hcnetsdk.h"
#import "Preview.h"

#define HEIGHT self.view.bounds.size.height
#define WIDTH self.view.bounds.size.width
@implementation PlayerDemoViewController

@synthesize m_playView;
@synthesize m_nPreviewPort;
@synthesize m_lUserID;
@synthesize m_lRealPlayID;

PlayerDemoViewController *g_pController = NULL;

int g_iStartChan = 0;
int g_iPreviewChanNum = 0;
bool g_bDecode = true;


- (void)previewPlay:(int*)iPlayPort playView:(UIView*)playView
{
    m_nPreviewPort = *iPlayPort;
    int iRet = PlayM4_Play(*iPlayPort, playView);
    PlayM4_PlaySound(*iPlayPort);
    if (iRet != 1)
    {
        NSLog(@"PlayM4_Play fail");
        [self stopPreviewPlay];
        return;
    }
}
- (void)stopPreviewPlay:(int*)iPlayPort
{
    PlayM4_StopSound();
    if (!PlayM4_Stop(*iPlayPort))
    {
        NSLog(@"PlayM4_Stop failed");
    }
    if(!PlayM4_CloseStream(*iPlayPort))
    {
        NSLog(@"PlayM4_CloseStream failed");
    }
    if (!PlayM4_FreePort(*iPlayPort))
    {
        NSLog(@"PlayM4_FreePort failed");
    }
    *iPlayPort = -1;
}

//stop preview
-(void) stopPlay
{
	if (m_lRealPlayID != -1)
	{
		NET_DVR_StopRealPlay(m_lRealPlayID);
		m_lRealPlayID = -1;		
	}
	
       if(m_nPreviewPort >= 0)
       {
           if(!PlayM4_StopSound())
           {
               NSLog(@"PlayM4_StopSound failed");
           }
           if (!PlayM4_Stop(m_nPreviewPort))
           {
               NSLog(@"PlayM4_Stop failed");
           }
           if(!PlayM4_CloseStream(m_nPreviewPort))
           {
               NSLog(@"PlayM4_CloseStream failed");
           }
           if (!PlayM4_FreePort(m_nPreviewPort))
           {
               NSLog(@"PlayM4_FreePort failed");
           }
             m_nPreviewPort = -1;
       }
}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad 
{
	m_lUserID = [self.UserID integerValue];
    g_iStartChan = [self.StartNum integerValue];
	m_lRealPlayID = -1;
    m_nPreviewPort = -1;
    UIView *m_playView = [[UIView alloc]initWithFrame:CGRectMake(0,0,HEIGHT, WIDTH)];
    m_playView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:m_playView];

    UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(10, 10, 55, 45)];
    button.backgroundColor = [UIColor blackColor];
    button.alpha = 0.5;
    [button setTitle:@"返回" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [button addTarget:self action:@selector(backHome) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:button];
    // for(int i = 0; i < MAX_VIEW_NUM; i++)
    // {
    //     m_multiView[i] = [[UIView alloc] initWithFrame:CGRectMake(0, 0, HEIGHT, WIDTH)];
    //     m_multiView[i].backgroundColor = [UIColor clearColor];
    //     [m_playView addSubview:m_multiView[i]];
    // }

    g_pController = self;
    
     m_lRealPlayID = startPreview(m_lUserID, g_iStartChan, m_playView, 0);
    
	[super viewDidLoad];
}

- (void)backHome{
    [self releaseView];
    [[NSNotificationCenter defaultCenter]postNotificationName:@"goHome" object:nil userInfo:nil];
    
}

- (void)didReceiveMemoryWarning 
{
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)releaseView
{
    stopPreview(0);
	if (m_lRealPlayID != -1)
	{
        NSLog(@"NET_DVR_StopRealPlay");
		NET_DVR_StopRealPlay(m_lRealPlayID);
		m_lRealPlayID = -1;
	}
    
       if(m_lUserID != -1)
       {
           NSLog(@"NET_DVR_Logout");
           NET_DVR_Logout(m_lUserID);
           NET_DVR_Cleanup();
           m_lUserID = -1;
       }
       if (m_playView != nil){
           NSLog(@"m_playView release");
            [m_playView release];
		    m_playView = nil;
	    }
}
-(NSUInteger)supportedInterfaceOrientations{
    return UIInterfaceOrientationMaskLandscape;
}

@end
