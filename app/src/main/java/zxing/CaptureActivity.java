package zxing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.dufangyu.lecatapp.R;
import com.example.dufangyu.lecatapp.activity.AddDeviceActivity;
import com.example.dufangyu.lecatapp.utils.ActivityControl;
import com.example.dufangyu.lecatapp.utils.LogUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Vector;

import zxing.camera.BeepManager;
import zxing.camera.CameraManager;
import zxing.decode.CaptureActivityHandler;
import zxing.decode.DecodeThread;
import zxing.decode.FinishListener;
import zxing.decode.InactivityTimer;
import zxing.view.ViewfinderView;


/**
 * 条码二维码扫描功能实现
 */
public class CaptureActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = CaptureActivity.class.getSimpleName();
    private boolean hasSurface;
    private BeepManager beepManager;// 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
    public SharedPreferences mSharedPreferences;// 存储二维码条形码选择的状态
    public static String currentState;// 条形码二维码选择状态
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private String terminalNo;
    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    /**
     * 活动监控器，用于省电，如果手机没有连接电源线，那么当相机开启后如果一直处于不被使用状态则该服务会将当前activity关闭。
     * 活动监控器全程监控扫描活跃状态，与CaptureActivity生命周期相同.每一次扫描过后都会重置该监控，即重新倒计时。
     */
    private InactivityTimer inactivityTimer;
    private CameraManager cameraManager;
    private Vector<BarcodeFormat> decodeFormats;// 编码格式
    private CaptureActivityHandler mHandler;// 解码线程

    private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet.of(
            ResultMetadataType.ISSUE_NUMBER,
            ResultMetadataType.SUGGESTED_PRICE,
            ResultMetadataType.ERROR_CORRECTION_LEVEL,
            ResultMetadataType.POSSIBLE_COUNTRY);
    private ImageView mbutton_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initSetting();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_capture);
        ActivityControl.addActivity(this);
        initComponent();
        initView();
        initEvent();

        
    }

    /**
     * 初始化窗口设置
     */
    private void initSetting() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕处于点亮状态
        // window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 竖屏
    }

    /**
     * 初始化功能组件
     */
    private void initComponent() {
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        currentState = this.mSharedPreferences.getString("currentState","onecode");
        cameraManager = new CameraManager(getApplication());
    }

    public static void actionStart(Context context)
    {
    	Intent intent= new Intent(context, CaptureActivity.class);
        context.startActivity(intent);
    }
    
    
    
    /**
     * 初始化视图
     */
    private void initView() {

        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        mbutton_back = (ImageView) findViewById(R.id.capture_back);

    }

    /**
     * 初始化点击切换扫描类型事件
     */
    private void initEvent() {
        mbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewfinderView=null;
                finish();
            }
        });
    }
    /**
     * 初始设置扫描类型（最后一次使用类型）
     */
    private void setScanType() {
        do {
            if ((CaptureActivity.currentState != null) && (CaptureActivity.currentState.equals("onecode"))) {
                viewfinderView.setVisibility(View.VISIBLE);
                onecodeSetting();
                return;
            }
        }

        while ((CaptureActivity.currentState == null) || (!CaptureActivity.currentState.equals("qrcode")));
        viewfinderView.setVisibility(View.VISIBLE);
        qrcodeSetting();
    }

    /**
     * 主要对相机进行初始化工作
     */
    @Override
    protected void onResume() {
        super.onResume();
        inactivityTimer.onActivity();
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        surfaceHolder = surfaceView.getHolder();
        setScanType();
        resetStatusView();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            // 如果SurfaceView已经渲染完毕，会回调surfaceCreated，在surfaceCreated中调用initCamera()
            surfaceHolder.addCallback(this);
        }
        // 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
        beepManager.updatePrefs();
        // 恢复活动监控器
        inactivityTimer.onResume();
    }

    /**
     * 展示状态视图和扫描窗口，隐藏结果视图
     */
    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 初始化摄像头。打开摄像头，检查摄像头是否被开启及是否被占用
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the mHandler starts the preview, which can also throw a
            // RuntimeException.
            if (mHandler == null) {
                mHandler = new CaptureActivityHandler(this, decodeFormats, "", cameraManager);
                viewfinderView.setHandler(mHandler);
            }
            // decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 若摄像头被占用或者摄像头有问题则跳出提示对话框
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    /**
     * 暂停活动监控器,关闭摄像头
     */
    @Override
    protected void onPause() {
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        // 暂停活动监控器
        inactivityTimer.onPause();
        // 关闭摄像头
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    /**
     * 停止活动监控器,保存最后选中的扫描类型
     */
    @Override
    protected void onDestroy() {
        // 停止活动监控器
        inactivityTimer.shutdown();
        saveScanTypeToSp();
        ActivityControl.removeActivity(this);
        super.onDestroy();
    }

    /**
     * 保存退出进程前选中的二维码条形码的状态
     */
    private void saveScanTypeToSp() {
        SharedPreferences.Editor localEditor = this.mSharedPreferences.edit();
        localEditor.putString("currentState", CaptureActivity.currentState);
        localEditor.commit();
    }

    /**
     * 获取扫描结果
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        LogUtil.d("dfy", "扫描结果："+ rawResult.getText().toString());
        terminalNo = rawResult.getText().toString();

        AddDeviceActivity.actionStart(CaptureActivity.this,terminalNo);
        finish();


    }



//    //发送网络请求
//    private void sendRequest(String strvalue)
//    {
//        CustomLoadDialog.show(this,"",true,null,R.layout.logindialog);
//        NetWork.getInstance().setRequestParam("terNo",strvalue);
//        NetWork.getInstance().doPostNetWork(CaptureActivity.this,codeBack, Constant.SCANTERNO,otheraction);
//    }




//    NetCallBack<String> codeBack = new NetCallBack<String>() {
//        @Override
//        public void getHttpResult(String s) {
//            CustomLoadDialog.dismisDialog();
////            LogUtil.d("dfy","扫描 = "+s);
//            ScanBean bean = GsonUtil.getInstance().fromJson(s,ScanBean.class);
//            Intent intent =new Intent();
//            intent.putExtra("result", bean.getData());
//            intent.putExtra("terminalNo",terminalNo);
//            setResult(SCAN_RESULT, intent);
//            finish();
//        }
//
//        @Override
//        public void getHttpErrorResult(int errorCode, String msg) {
//            CustomLoadDialog.dismisDialog();
//            CustomDialog.show(CaptureActivity.this, "连接服务器异常，请重试", false, null, R.layout.nocache_dialog);
//            CustomDialog.setAutoDismiss(true, 1500);
//            drawViewfindderGoon("扫描失败");
//        }
//    };


    /**
     * 提示扫描成功，继续下次扫描
     */
    public void drawViewfindderGoon(String text)
    {
        viewfinderView.drawViewfinderGoon(text);
    }
    /**
     * 点击响应条形码扫描
     */
    private View.OnClickListener onecodeImageListener = new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {

            viewfinderView.setVisibility(View.VISIBLE);
            currentState = "onecode";
            onecodeSetting();

        }
    };

    private void onecodeSetting() {
        decodeFormats = new Vector<BarcodeFormat>(7);
        decodeFormats.clear();
        decodeFormats.addAll(DecodeThread.ONE_D_FORMATS);
        if (null != mHandler) {
            mHandler.setDecodeFormats(decodeFormats);
        }

        viewfinderView.refreshDrawableState();
        cameraManager.setManualFramingRect(600, 222);
        viewfinderView.refreshDrawableState();

    }

    /**
     * 点击响应二维码扫描
     */
    private View.OnClickListener qrcodeImageListener = new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {

            viewfinderView.setVisibility(View.VISIBLE);
            currentState = "qrcode";
            qrcodeSetting();
        }
    };

    private void qrcodeSetting() {
        decodeFormats = new Vector<BarcodeFormat>(2);
        decodeFormats.clear();
        decodeFormats.add(BarcodeFormat.QR_CODE);
        decodeFormats.add(BarcodeFormat.DATA_MATRIX);
        if (null != mHandler) {
            mHandler.setDecodeFormats(decodeFormats);
        }
        viewfinderView.refreshDrawableState();
        cameraManager.setManualFramingRect(300, 300);
        viewfinderView.refreshDrawableState();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG,
                    "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * 在经过一段延迟后重置相机以进行下一次扫描。 成功扫描过后可调用此方法立刻准备进行下次扫描
     *
     * @param delayMS
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: // 拦截返回键
                viewfinderView=null;
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}