package com.epsxe.ePSXe.touchscreen;

import android.opengl.GLES20;
import com.epsxe.ePSXe.SpriteBatch;
import com.epsxe.ePSXe.SpriteBatch2;
import com.epsxe.ePSXe.TextureRegion;
import com.epsxe.ePSXe.jni.libepsxe;
import java.lang.reflect.Array;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class Gun {
    private static final int BUTTON_DOWN = 1;
    private static final int NO_BUTTON_DOWN = 0;
    private int gunb0 = 0;
    private int gunb1 = 0;
    private int gunb2 = 0;
    private int gunb3 = 0;
    private int gunX = 0;
    private int gunY = 0;
    int[][] virtualPadPos = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 60, 4);
    int[] virtualPadBit = new int[60];
    int[] virtualPadId = new int[60];
    int initvirtualPad = 0;

    public void Gun() {
    }

    public void initGun(int mWidth, int mHeight, int emu_pad_type_selected) {
        this.virtualPadPos[0][0] = (mWidth - ((mHeight * 4) / 3)) / 2;
        this.virtualPadPos[0][1] = 0;
        this.virtualPadPos[0][2] = ((mWidth - ((mHeight * 4) / 3)) / 2) + ((mHeight * 4) / 3);
        this.virtualPadPos[0][3] = mHeight;
        this.virtualPadBit[0] = 1;
        if (emu_pad_type_selected == 0) {
            int[] iArr = this.virtualPadBit;
            iArr[0] = iArr[0] | 65536;
        }
        this.virtualPadId[0] = -1;
        this.virtualPadPos[1][0] = 0;
        this.virtualPadPos[1][1] = 0;
        this.virtualPadPos[1][2] = (mWidth - ((mHeight * 4) / 3)) / 2;
        this.virtualPadPos[1][3] = mHeight / 2;
        this.virtualPadBit[1] = 2;
        if (emu_pad_type_selected == 0) {
            int[] iArr2 = this.virtualPadBit;
            iArr2[1] = iArr2[1] | 65536;
        }
        this.virtualPadId[1] = -1;
        this.virtualPadPos[2][0] = 0;
        this.virtualPadPos[2][1] = mHeight / 2;
        this.virtualPadPos[2][2] = (mWidth - ((mHeight * 4) / 3)) / 2;
        this.virtualPadPos[2][3] = mHeight;
        this.virtualPadBit[2] = 4;
        if (emu_pad_type_selected == 0) {
            int[] iArr3 = this.virtualPadBit;
            iArr3[2] = iArr3[2] | 65536;
        }
        this.virtualPadId[2] = -1;
        this.virtualPadPos[3][0] = mWidth - ((mWidth - ((mHeight * 4) / 3)) / 2);
        this.virtualPadPos[3][1] = 0;
        this.virtualPadPos[3][2] = mWidth;
        this.virtualPadPos[3][3] = mHeight;
        this.virtualPadBit[3] = 1;
        if (emu_pad_type_selected == 0) {
            int[] iArr4 = this.virtualPadBit;
            iArr4[3] = iArr4[3] | 65536;
        }
        this.virtualPadId[3] = -1;
        this.initvirtualPad = 1;
    }

    public int touchscreeneventgun(long eventTime, int action, int xi, int yi, float pressure, float size, int deviceId, int Id, libepsxe e, int emu_pad_mode, int mfps, int mWidth, int mHeight, int emu_pad_type_selected) {
        int ret = 0;
        if (this.initvirtualPad == 0) {
            initGun(mWidth, mHeight, emu_pad_type_selected);
        }
        if (action == 2) {
            action = 0;
        }
        if (action == 261 || action == 5 || action == 517) {
            action = 0;
        }
        if (action == 262 || action == 6 || action == 518) {
            action = 1;
        }
        if (action == 1 && this.virtualPadId[Id] != -1) {
            int but = this.virtualPadId[Id];
            if (but < 4) {
                if (but == 1) {
                    this.gunb1 = 0;
                }
                if (but == 2) {
                    this.gunb2 = 0;
                }
                if (but == 3) {
                    this.gunb3 = 0;
                }
                if (but == 0) {
                    this.gunb0 = 0;
                }
                e.setGunData(0, this.gunX, this.gunY, this.gunb0, this.gunb1, this.gunb2, this.gunb3, (mHeight * 4) / 3, mHeight, emu_pad_mode);
            }
            this.virtualPadId[Id] = -1;
        }
        if (action != 0) {
            return 0;
        }
        int ind = 0;
        while (true) {
            if (ind >= 4) {
                break;
            }
            if (xi < this.virtualPadPos[ind][0] || xi > this.virtualPadPos[ind][2] || yi < this.virtualPadPos[ind][1] || yi > this.virtualPadPos[ind][3] || action != 0) {
                ind++;
            } else {
                if (this.virtualPadId[Id] != -1) {
                    int ind2 = this.virtualPadId[Id];
                    if (ind2 < 4) {
                        if (ind2 == 1) {
                            this.gunb1 = 0;
                        }
                        if (ind2 == 2) {
                            this.gunb2 = 0;
                        }
                        if (ind2 == 3) {
                            this.gunb3 = 0;
                        }
                        if (ind2 == 0) {
                            this.gunb0 = 0;
                        }
                        e.setGunData(0, this.gunX, this.gunY, this.gunb0, this.gunb1, this.gunb2, this.gunb3, (mHeight * 4) / 3, mHeight, emu_pad_mode);
                    }
                }
                if (ind < 4) {
                    if (ind == 1) {
                        this.gunb1 = 1;
                    }
                    if (ind == 2) {
                        this.gunb2 = 1;
                    }
                    if (ind == 3) {
                        this.gunb3 = 1;
                    }
                    if (ind == 0) {
                        this.gunb0 = 1;
                        if (emu_pad_mode == 8) {
                            this.gunX = xi - ((mWidth - ((mHeight * 4) / 3)) / 2);
                            this.gunY = yi;
                        } else {
                            this.gunX = (((xi - ((mWidth - ((mHeight * 4) / 3)) / 2)) * 384) / ((mHeight * 4) / 3)) + 77;
                            if (mfps == 60) {
                                this.gunY = ((yi * 223) / mHeight) + 25;
                            } else {
                                this.gunY = ((yi * 263) / mHeight) + 32;
                            }
                        }
                    }
                    e.setGunData(0, this.gunX, this.gunY, this.gunb0, this.gunb1, this.gunb2, this.gunb3, (mHeight * 4) / 3, mHeight, emu_pad_mode);
                }
                this.virtualPadId[Id] = ind;
                ret = action != 2 ? 1 : 0;
            }
        }
        return ret;
    }

    public void drawGunGl(int mTexLan, int mProgram, TextureRegion[] textureRgnLan, SpriteBatch2[] batchLan, int mWidth, int mHeight) {
        GLES20.glBindTexture(3553, mTexLan);
        GLES20.glUseProgram(mProgram);
        GLES20.glEnable(3042);
        batchLan[0].beginBatch(mTexLan);
        batchLan[0].drawSprite(((mWidth - ((mHeight * 4) / 3f)) / 4) / mWidth, ((mHeight / 4f) * 3) / mHeight, ((mWidth - ((mHeight * 4) / 3f)) / 2) / mWidth, (mHeight / 2f) / mHeight, textureRgnLan[0]);
        batchLan[0].endBatch();
        batchLan[1].beginBatch(mTexLan);
        batchLan[1].drawSprite(((mWidth - ((mHeight * 4) / 3f)) / 4) / mWidth, (mHeight / 4f) / mHeight, ((mWidth - ((mHeight * 4) / 3f)) / 2) / mWidth, (mHeight / 2f) / mHeight, textureRgnLan[1]);
        batchLan[1].endBatch();
        GLES20.glDisable(3042);
    }

    public void drawGunGl(GL10 gl, int mTexLan, TextureRegion[] textureRgnLan, SpriteBatch[] batchLan, int mWidth, int mHeight, float emu_input_alpha) {
        gl.glEnable(3553);
        gl.glEnable(3042);
        gl.glBlendFunc(770, 771);
        gl.glColor4f(1.0f, 1.0f, 1.0f, emu_input_alpha);
        gl.glBindTexture(3553, mTexLan);
        batchLan[0].beginBatch();
        batchLan[0].drawSprite((mWidth - ((mHeight * 4) / 3f)) / 4, (mHeight / 4f) * 3, (mWidth - ((mHeight * 4) / 3f)) / 2, mHeight / 2f, textureRgnLan[0]);
        batchLan[0].endBatch();
        batchLan[1].beginBatch();
        batchLan[1].drawSprite((mWidth - ((mHeight * 4) / 3f)) / 4, mHeight / 4f, (mWidth - ((mHeight * 4) / 3f)) / 2, mHeight / 2f, textureRgnLan[1]);
        batchLan[1].endBatch();
        gl.glDisable(3042);
        gl.glDisable(3553);
    }

    public void drawGunGlExt(GL10 gl, int mTexLan, TextureRegion[] textureRgnLan, SpriteBatch[] batchLan, int mWidth, int mHeight, float emu_input_alpha, float screenResize) {
        gl.glDisable(3089);
        gl.glEnable(3553);
        gl.glEnable(3042);
        gl.glBlendFunc(770, 771);
        gl.glColor4f(1.0f, 1.0f, 1.0f, emu_input_alpha);
        gl.glBindTexture(3553, mTexLan);
        batchLan[0].beginBatch();
        batchLan[0].drawSprite(((mWidth - ((mHeight * 4) / 3f)) / 4) * screenResize, (mHeight / 4f) * 3 * screenResize, ((mWidth - ((mHeight * 4) / 3f)) / 2) * screenResize, (mHeight / 2f) * screenResize, textureRgnLan[0]);
        batchLan[0].endBatch();
        batchLan[1].beginBatch();
        batchLan[1].drawSprite(((mWidth - ((mHeight * 4) / 3f)) / 4) * screenResize, (mHeight / 4f) * screenResize, ((mWidth - ((mHeight * 4) / 3f)) / 2) * screenResize, (mHeight / 2f) * screenResize, textureRgnLan[1]);
        batchLan[1].endBatch();
        gl.glDisable(3042);
        gl.glDisable(3553);
    }
}
