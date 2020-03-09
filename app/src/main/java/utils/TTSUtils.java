package utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TTSUtils implements SpeechSynthesizerListener {
    private static final String TAG = "TTSUtils";
    private static volatile TTSUtils instance = null;
    private boolean isInitSuccess = false;
    private SpeechSynthesizer mTTSPlayer;

    private static final String SAMPLE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/unisound/tts/";
    private static final String FRONTEND_MODEL = "frontend_model";
    private static final String BACKEND_MODEL = "backend_lzl";

    private static final String APPKEY = "lstub2j4x5nnef2movzyxzlwqtabgtolahbfd5a6";
    private static final String SECRET = "4e5a742f05e980f942e225c74b9ffd81";

    private TTSUtils() {
    }

    public static TTSUtils getInstance() {
        if (instance == null) {
            synchronized (TTSUtils.class) {
                if (instance == null) {
                    instance = new TTSUtils();
                }
            }
        }
        return instance;
    }

    public void init() {
        Context context = MyApplication.getContext();
        mTTSPlayer = new SpeechSynthesizer(context, APPKEY, SECRET);
        mTTSPlayer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_LOCAL); // 设置本地合成
        File file = new File(SAMPLE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        File _FrontendModelFile = new File(SAMPLE_DIR + FRONTEND_MODEL);
        if (!_FrontendModelFile.exists()) {
            copyAssetsFile2SDCard(context, FRONTEND_MODEL, SAMPLE_DIR + FRONTEND_MODEL);
        }
        File _BackendModelFile = new File(SAMPLE_DIR + BACKEND_MODEL);
        if (!_BackendModelFile.exists()) {
            copyAssetsFile2SDCard(context, BACKEND_MODEL, SAMPLE_DIR + BACKEND_MODEL);
        }
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_FRONTEND_MODEL_PATH, SAMPLE_DIR + FRONTEND_MODEL);// 设置前端模型
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, SAMPLE_DIR + BACKEND_MODEL);// 设置后端模型
        mTTSPlayer.setTTSListener(this);// 设置回调监听
        mTTSPlayer.init(null);// 初始化合成引擎
    }

    public void speak(String msg) {
        if (isInitSuccess) {
            mTTSPlayer.playText(msg);
        }else {
            init();
        }
    }

    public void stop() {
        mTTSPlayer.stop();
    }

    public void pause() {
        mTTSPlayer.pause();
    }

    public void resume() {
        mTTSPlayer.resume();
    }

    public void release() {
        if (null != mTTSPlayer) {
            // 释放离线引擎
            mTTSPlayer.release(SpeechConstants.TTS_RELEASE_ENGINE, null);
        }
    }

    @Override
    public void onEvent(int type) {
        switch (type) {
            case SpeechConstants.TTS_EVENT_INIT:
                isInitSuccess = true;
                break;
            case SpeechConstants.TTS_EVENT_SYNTHESIZER_START:
                // 开始合成回调
                Log.i(TAG, "beginSynthesizer");
                break;
            case SpeechConstants.TTS_EVENT_SYNTHESIZER_END:
                // 合成结束回调
                Log.i(TAG, "endSynthesizer");
                break;
            case SpeechConstants.TTS_EVENT_BUFFER_BEGIN:
                // 开始缓存回调
                Log.i(TAG, "beginBuffer");
                break;
            case SpeechConstants.TTS_EVENT_BUFFER_READY:
                // 缓存完毕回调
                Log.i(TAG, "bufferReady");
                break;
            case SpeechConstants.TTS_EVENT_PLAYING_START:
                // 开始播放回调
                Log.i(TAG, "onPlayBegin");
                break;
            case SpeechConstants.TTS_EVENT_PLAYING_END:
                // 播放完成回调
                Log.i(TAG, "onPlayEnd");
                break;
            case SpeechConstants.TTS_EVENT_PAUSE:
                // 暂停回调
                Log.i(TAG, "pause");
                break;
            case SpeechConstants.TTS_EVENT_RESUME:
                // 恢复回调
                Log.i(TAG, "resume");
                break;
            case SpeechConstants.TTS_EVENT_STOP:
                // 停止回调
                Log.i(TAG, "stop");
                break;
            case SpeechConstants.TTS_EVENT_RELEASE:
                // 释放资源回调
                Log.i(TAG, "release");
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(int type, String errorMSG) {
        Log.e(TAG, "语音合成错误回调: " + errorMSG);
    }

    public static void copyAssetsFile2SDCard(Context context, String fileName, String path) {
        try {
            InputStream is = context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(new File(path));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            is.close();
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, "copyAssetsFile2SDCard: " + e.toString());
        }
    }
}