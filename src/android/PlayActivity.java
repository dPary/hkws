package io.github.dpary;

import android.app.Activity;
import io.github.dpary.FakeR;
import android.os.Bundle;  
import android.view.View;  
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import org.MediaPlayer.PlayM4.Player;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
  
public class PlayActivity extends Activity{  
  
    private SurfaceView surfaceview;  
    private int iUserID;  
    private int iChan;
    private FakeR fakeR;
    private SurfaceHolder m_hHolder;
    private int m_iPlayID=-1;
  
  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        fakeR = new FakeR(this);
        setContentView(fakeR.getId("layout", "activity_main"));
        iUserID = getIntent().getIntExtra("USER_ID", -1);
        iChan = getIntent().getIntExtra("CHAN", -1);
        findViewById();  
        initView();  
    }  
  
    protected void findViewById() {  
        surfaceview = (SurfaceView) findViewById(fakeR.getId("id", "Sur_Player"));
    }  
  
    protected void initView() {  
        m_hHolder = surfaceview.getHolder();
        m_hHolder.setKeepScreenOn(true);  
        m_hHolder.addCallback(new SurfaceCallback());  
    }  

        /*
     * 当SurfaceView所在的Activity离开了前台,SurfaceView会被destroy,
     * 当Activity又回到了前台时，SurfaceView会被重新创建，并且是在OnResume()方法之后被创建
     */
    private class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        public void surfaceCreated(SurfaceHolder holder) {
            System.out.println("surfaceCreated");
            NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
            previewInfo.lChannel = iChan;
            previewInfo.dwStreamType = 0; // substream
            previewInfo.bBlocked = 1;
            previewInfo.hHwnd = m_hHolder;
            // HCNetSDK start preview
            m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(iUserID,previewInfo, null);
        }



        /**
         * 离开SurfaceView时停止播放，保存播放位置
         */
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 隐藏view的时候销毁SurfaceHolder的时候记录当前的播放位置并停止播放
            System.out.println("surfaceDestroyed");
            if(m_iPlayID != -1){
                HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID);
                m_iPlayID = -1;
            }
            if(iUserID != -1){
                HCNetSDK.getInstance().NET_DVR_Logout_V30(iUserID);
                HCNetSDK.getInstance().NET_DVR_Cleanup();
                iUserID = -1;
            }
        }
    }
}