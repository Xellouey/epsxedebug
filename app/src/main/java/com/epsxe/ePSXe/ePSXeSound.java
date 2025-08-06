package com.epsxe.ePSXe;

import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;
import com.epsxe.ePSXe.jni.libepsxe;

/* loaded from: classes.dex */
public class ePSXeSound extends Thread {
    private static final int AUDIO_BUFFER_PUT_GET_LEN = 65536;
    private static final int AUDIO_BUFFER_TOTAL_LEN = 262144;

    /* renamed from: e */
    private libepsxe f164e;
    private AudioTrack m_audiotrack;
    private int osVersion;
    private int soundQA = 1;
    private int emu_sound_latency = 0;
    private boolean positionChanged = false;
    private boolean mRun = false;
    private int bufferlow = 1764;
    private int bufferms = 8192;
    private boolean onPause = false;

    public void setRunning(boolean b) {
        this.mRun = b;
    }

    public void dosound() {
        int frames;
        int user = 0;
        byte[] audioData = new byte[65536];
        byte[] audioDataZero = new byte[65536];
        this.bufferlow = 1764;
        this.bufferms = 8192;
        Log.i("epsxesnd", "ePSXeSoundThread run!");
        if (this.emu_sound_latency == 3) {
            this.bufferlow = 2048;
            this.bufferms = 2048;
        } else if (this.emu_sound_latency == 4) {
            this.bufferlow = 1024;
            this.bufferms = 1024;
        }
        for (int i = 0; i < 65536; i++) {
            audioData[i] = 0;
        }
        for (int i2 = 0; i2 < 65536; i2++) {
            audioDataZero[i2] = 0;
        }
        if (this.osVersion < 18) {
            this.positionChanged = true;
        }
        while (!isInterrupted() && this.mRun) {
            try {
                Thread.currentThread();
                Thread.sleep(10L);
                int headFrame = this.m_audiotrack.getPlaybackHeadPosition();
                if (this.onPause) {
                    user = 0;
                    int headFrame2 = this.bufferms;
                } else if (user - headFrame < this.bufferms || (headFrame == 0 && !this.positionChanged)) {
                    int soundFrames = this.bufferms - (user - headFrame);
                    if (soundFrames < 0) {
                        soundFrames = 0;
                    }
                    if (user - headFrame < this.bufferlow) {
                        frames = this.f164e.getepsxesoundata(audioData, soundFrames, 1);
                    } else {
                        if (soundFrames == 0 && headFrame == 0 && !this.positionChanged) {
                            soundFrames = 441;
                        }
                        frames = this.f164e.getepsxesoundata(audioData, soundFrames, 0);
                    }
                    if (frames > 0) {
                        int bytes = this.m_audiotrack.write(audioData, 0, frames * 4);
                        user += bytes / 4;
                    }
                    if (user - headFrame < 485) {
                        int bytes2 = this.m_audiotrack.write(audioDataZero, 0, (485 - (user - headFrame)) * 4);
                        user += bytes2 / 4;
                    }
                } else {
                    this.positionChanged = true;
                }
            } catch (InterruptedException e) {
            }
        }
        Log.i("epsxesnd", "ePSXeSoundThread exit!");
    }

    public ePSXeSound() {
        Log.i("epsxesnd", "ePSXeSound constructor!");
        this.m_audiotrack = new AudioTrack(3, 44100, 3, 2, 65536, 1);
        this.osVersion = Integer.parseInt(Build.VERSION.SDK);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int ret = this.m_audiotrack.setPlaybackHeadPosition(0);
        this.onPause = false;
        try {
            this.m_audiotrack.play();
            this.m_audiotrack.setStereoVolume(1.0f, 1.0f);
            Log.i("epsxesnd", "ePSXeSound run! rate: " + this.m_audiotrack.getPlaybackRate() + " headFrame: " + ret);
            setRunning(true);
            dosound();
        } catch (IllegalStateException e) {
            Log.i("epsxesnd", "Unable to init audio!");
        }
    }

    public void exit() {
        setRunning(false);
        try {
            this.m_audiotrack.flush();
            this.m_audiotrack.stop();
            this.m_audiotrack.release();
            Log.i("epsxesnd", "AudioDataPutThread exit!");
        } catch (IllegalStateException e) {
            Log.i("epsxesnd", "Unable to stop audio!");
        }
    }

    public void setePSXeLib(libepsxe epsxelib) {
        this.f164e = epsxelib;
    }

    public void setsoundqa(int qa) {
        this.soundQA = qa;
        this.f164e.setspuquality(qa);
    }

    public void setsoundlatency(int mode) {
        this.emu_sound_latency = this.f164e.setSoundLatency(mode);
        if (this.emu_sound_latency == 3) {
            this.bufferlow = 2048;
            this.bufferms = 2048;
        } else if (this.emu_sound_latency == 4) {
            this.bufferlow = 1024;
            this.bufferms = 1024;
        } else {
            this.bufferlow = 1764;
            this.bufferms = 8192;
        }
    }

    public void onPause() {
        this.onPause = true;
        try {
            this.m_audiotrack.pause();
            Log.i("epsxesnd", "Pause!");
        } catch (IllegalStateException e) {
            Log.i("epsxesnd", "Unable to pause audio!");
        }
    }

    public void onResume() {
        try {
            this.m_audiotrack.setPlaybackHeadPosition(0);
            this.m_audiotrack.play();
        } catch (IllegalStateException e) {
            Log.i("epsxesnd", "Unable to resume audio!");
        }
        Log.i("epsxesnd", "Resume!");
        this.onPause = false;
        this.positionChanged = false;
    }
}
