package com.epsxe.ePSXe.jni;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class libepsxe {
    public native int changedisc(String str, int i);

    public native int copyPixelsFromVRAMToBitmap(Bitmap bitmap);

    public native int copyPixelsFromVRAMToBuffer();

    public native int createMemcard(String str);

    public native void disableAllGS();

    public native void disableGS(int i);

    public native void disableautofire(int i, int i2);

    public native void enableAllGS();

    public native void enableGS(int i);

    public native void enableautofire(int i, int i2, int i3, int i4, int i5);

    public native String getCode();

    public native String getError();

    public native int getFPS();

    public native int getGPUVersion();

    public native byte[] getGSName(int i);

    public native int getGSNumber();

    public native int getGSStatus(int i);

    public native String getGameInfo();

    public native int getepsxesoundata(byte[] bArr, int i, int i2);

    public native int getepsxesoundatafmod(byte[] bArr, int i, int i2);

    public native int getheight();

    public native int getwidth();

    public native int gpugetoptionfixesgl();

    public native int gpugetoptiongl();

    public native void gpusaveoptiongl(int i);

    public native void gpusaveoptiongl2(int i);

    public native void gpusetopenglmode(int i);

    public native void gpusetoptiongl(int i, int i2, int i3);

    public native void gpusetoptiongl2(int i, int i2, int i3);

    public native int hasframe();

    public native int isSkip();

    public native int isTainted();

    public native int isX86();

    public native int loadautosnap();

    public native int loadepsxe(String str, int i);

    public native int loadtmpsnap();

    public native int motionevent(long j, int i, float f, float f2, float f3, float f4, int i2, int i3);

    public native int motionevent2P(long j, int i, float f, float f2, float f3, float f4, int i2, int i3, int i4);

    public native void openglinit(int i, int i2);

    public native void openglrender(int i, int i2, int i3, int i4, int i5);

    public native void openglresize(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    public native void openglstartthread(int i);

    public native int quit();

    public native void reloadAllGS();

    public native void reset();

    public native void resetdefaultvalues();

    public native int runClient(String str, int i, int i2, int i3);

    public native int runClientInputReceiver(int i);

    public native int runServer(int i, int i2);

    public native int runServerInputSender(String str, int i);

    public native int runemulatorframeGLext();

    public native int runepsxeframe(int i, int i2, int i3);

    public native void runwrapper(int i);

    public native int sendClientOK(String str, int i);

    public native int sendGameInfo(String str, String str2, int i);

    public native void setAuxVol(int i);

    public native int setBios(String str);

    public native int setBiosHle(int i);

    public native int setBootMode(int i);

    public native void setCpuMaxFreq(int i);

    public native int setDithering(int i);

    public native void setDmachaincore(int i);

    public native int setFilterhw(int i);

    public native int setFrameLimit(int i);

    public native int setFrameSkip(int i);

    public native int setGpu(String str);

    public native int setGpuMtMode(int i);

    public native void setGpuShader(String str);

    public native int setGpuSoftMtMode(int i);

    public native void setGteaccurateH(int i);

    public native int setGunData(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    public native void setInterpreter(int i);

    public native int setMemcard1(String str);

    public native int setMemcard2(String str);

    public native void setMemcardFileMode(int i);

    public native void setMemcardMode(int i);

    public native int setMouseData(int i, int i2, int i3, int i4, int i5);

    public native int setPadDataDown(int i, int i2);

    public native int setPadDataMultitap(int i, int i2, int i3, int i4);

    public native int setPadDataUp(int i, int i2);

    public native int setPadModeMultitap(int i);

    public native void setPauseMode(int i, int i2);

    public native int setPlayerMode(int i);

    public native int setPluginMode(int i);

    public native void setResumeMode();

    public native void setSaveMode(int i, int i2);

    public native int setSdCardPath(String str);

    public native int setSoundLatency(int i);

    public native int setSplitMode(int i);

    public native void setStopMode();

    public native int setVibration(int i, int i2);

    public native void setWidescreen(int i);

    public native void setblackbands(int i);

    public native int setbuffer(ByteBuffer byteBuffer);

    public native void setcpuoverclocking(int i);

    public native void setcustomfps(int i);

    public native int setfbosettings(int i, int i2, int i3);

    public native void setgpu2dfilter(int i);

    public native void setgpublitskip(int i);

    public native void setgpubrightnessprofile(int i);

    public native void setgpuiresolution(int i);

    public native void setgpuoverscanbottom(int i);

    public native void setgpuoverscantop(int i);

    public native int setpadanalog(int i, int i2, int i3, int i4);

    public native int setpadanalogMoga(int i, int i2, int i3, int i4);

    public native int setpadanalogXP(int i, int i2, int i3, int i4);

    public native int setpadanalogmode(int i, int i2);

    public native int setpadmode(int i, int i2);

    public native void setscanlines(int i, int i2, int i3);

    public native int setscreendepth(int i);

    public native int setscreenorientation(int i);

    public native int setspuquality(int i);

    public native int setsslot(int i);

    public native int setwh(int i, int i2, int i3);

    public native int updategteaccuracy(int i);

    public native void updatescanlines(int i, int i2, int i3);

    public native int updatewh(int i, int i2, int i3);

    public native String waitClientConnect();

    public native int waitClientOK();

    public native String waitGameInfo();

    public libepsxe(int type) {
        if (type == 0) {
            System.loadLibrary("epsxe_tegra2");
        } else if (type == 1 || type == 2) {
            System.loadLibrary("epsxe");
        }
    }
}
