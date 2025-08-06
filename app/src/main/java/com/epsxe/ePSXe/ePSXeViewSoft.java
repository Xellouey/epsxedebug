package com.epsxe.ePSXe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Vibrator;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.epsxe.ePSXe.jni.libepsxe;
import java.nio.ByteBuffer;
import org.apache.http.HttpStatus;

/* loaded from: classes.dex */
class ePSXeViewSoft extends SurfaceView implements SurfaceHolder.Callback, ePSXeView {
    public static final int ACTION_POINTER_INDEX_MASK = 65280;
    public static final int ACTION_POINTER_INDEX_SHIFT = 8;
    private static final int INPUT_HWBUTTONS = 1;
    private static final int INPUT_TOUCHSCREEN = 0;
    private boolean biosmsg;
    private ByteBuffer byteBuffer;

    /* renamed from: e */
    private libepsxe f176e;
    private int emu_enable_framelimit;
    private int emu_enable_frameskip;
    private int emu_enable_frameskip_tmp;
    private int emu_enable_mme;
    private int emu_enable_printfps;
    private int[] emu_pad_mode;
    private int emu_pad_portrait;
    private int[] emu_pad_type;
    private int emu_player_mode;
    private int emu_screen_orientation;
    private int emu_screen_ratio;
    private int emu_sound_latency;
    private int emu_split_mode;
    private int emu_video_filter;
    private boolean gprofile;
    private int gpuVersionAdvise;
    private boolean license;
    private Context mContext;
    private int mfps;
    private int osVersion;
    private String skinName;
    private int tainted;
    private EmuThread thread;
    private int[] ts_vibration;
    private boolean hidePad = false;
    private boolean isInTouch = false;
    private ePSXeView.OnTouchListener touchListener;

    @Override
    public void setHidePad(boolean hide) {
        hidePad = hide;
    }

    @Override
    public boolean isInTouch() {
        return isInTouch;
    }

    @Override
    public void setOnTouchListener(ePSXeView.OnTouchListener listener) {
        touchListener = listener;
    }

    class EmuThread extends Thread {
        public static final int STATE_AUTOSAVING = 4;
        public static final int STATE_PAUSE = 1;
        public static final int STATE_RUNNING = 2;
        public static final int STATE_SAVING = 3;
        private int bitmapheight;
        private int bitmapwidth;
        private Bitmap mBitmap;
        private int mHeight;
        private int mMode;
        private SurfaceHolder mSurfaceHolder;
        private int mWidth;
        private Rect SourceRect = new Rect(0, 0, 0, 0);
        private Rect DestRect = new Rect(0, 0, 0, 0);
        private Rect DestRectP1 = new Rect(0, 0, 0, 0);
        private Rect DestRectP2 = new Rect(0, 0, 0, 0);
        private Rect DestRectPartial = new Rect(0, 0, 0, 0);
        private Rect SourceRectP1 = new Rect(0, 0, 0, 0);
        private Rect SourceRectP2 = new Rect(0, 0, 0, 0);
        private int mCanvasHeight = 1;
        private int mCanvasWidth = 1;
        private boolean mRun = false;
        private long mLastTime = 0;
        private long mStartTime = 0;
        private long mEmuTime = 0;
        private int skippedCount = 0;
        private Paint mPaint = new Paint();
        private Paint mPaintFilter = new Paint();

        private void doDrawFPS(Canvas canvas, int x) {
            this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setTextSize(30.0f);
            canvas.drawText(String.valueOf(ePSXeViewSoft.this.f176e.getFPS()) + "/" + ePSXeViewSoft.this.mfps, x, 30.0f, this.mPaint);
        }

        private void doDrawVirtualPadPortrait(Canvas canvas, int mHeight, int mWidth, int ox, int oy) {
            RectF mRect = new RectF();
            this.mPaint.setColor(-12303292);
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(((mWidth * 200f) / 850) + ox, ((mHeight * 260f) / 480) + oy, (mHeight * 130f) / 480, this.mPaint);
            this.mPaint.setColor(-7829368);
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(((mWidth * 200f) / 850) + ox, ((mHeight * 260f) / 480) + oy, (mHeight * 110f) / 480, this.mPaint);
            this.mPaint.setColor(-12303292);
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(((mWidth * 200f) / 850) + ox, ((mHeight * 260f) / 480) + oy, (mHeight * 30f) / 480, this.mPaint);
            this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
            this.mPaint.setStyle(Paint.Style.FILL);
            Path path = new Path();
            path.moveTo(0.0f, 0.0f);
            path.lineTo(5.0f, 10.0f);
            path.lineTo(-5.0f, 10.0f);
            path.close();
            path.offset(((mWidth * 200f) / 850) + ox, ((((mHeight * 260f) / 480) - ((mHeight * 80f) / 480)) - 10) + oy);
            canvas.drawPath(path, this.mPaint);
            Path path2 = new Path();
            path2.moveTo(0.0f, 0.0f);
            path2.lineTo(-5.0f, -10.0f);
            path2.lineTo(5.0f, -10.0f);
            path2.close();
            path2.offset(((mWidth * 200f) / 850) + ox, ((mHeight * 260f) / 480) + ((mHeight * 80f) / 480) + 10 + oy);
            canvas.drawPath(path2, this.mPaint);
            Path path3 = new Path();
            path3.moveTo(0.0f, 0.0f);
            path3.lineTo(10.0f, -5.0f);
            path3.lineTo(10.0f, 5.0f);
            path3.close();
            path3.offset(((((mWidth * 200f) / 850) - ((mHeight * 80f) / 480)) - 10) + ox, ((mHeight * 260f) / 480) + oy);
            canvas.drawPath(path3, this.mPaint);
            Path path4 = new Path();
            path4.moveTo(0.0f, 0.0f);
            path4.lineTo(-10.0f, -5.0f);
            path4.lineTo(-10.0f, 5.0f);
            path4.close();
            path4.offset(((mWidth * 200f) / 850) + ((mHeight * 80f) / 480) + 10 + ox, ((mHeight * 260f) / 480) + oy);
            canvas.drawPath(path4, this.mPaint);
            this.mPaint.setColor(-7829368);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setAlpha(4095);
            Path path5 = new Path();
            path5.moveTo(0.0f, 0.0f);
            path5.lineTo((-(mHeight * 60f)) / 480, (-(mHeight * 25f)) / 480);
            path5.lineTo((-(mHeight * 60f)) / 480, (mHeight * 25f) / 480);
            path5.close();
            path5.offset(((mWidth * 540f) / 850) + ox, ((mHeight * 10f) / 480) + ((mHeight * 25f) / 480) + oy);
            canvas.drawPath(path5, this.mPaint);
            mRect.set(((mWidth * 350f) / 850) + ox, ((mHeight * 15f) / 480) + oy, ((mWidth * 410f) / 850) + ox, ((mHeight * 55f) / 480) + oy);
            canvas.drawRoundRect(mRect, 5.0f, 5.0f, this.mPaint);
            this.mPaint.setColor(-7829368);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setAlpha(4095);
            canvas.drawCircle(((mWidth * 560f) / 850) + ox, ((mHeight * 320f) / 480) + oy, (mHeight * 50f) / 480, this.mPaint);
            this.mPaint.setColor(-65281);
            canvas.drawLine((((mWidth * 560f) / 850) - 10) + ox, (((mHeight * 320f) / 480) - 10) + oy, ((mWidth * 560f) / 850) + 10 + ox, (((mHeight * 320f) / 480) - 10) + oy, this.mPaint);
            canvas.drawLine(((mWidth * 560f) / 850) + 10 + ox, (((mHeight * 320f) / 480) - 10) + oy, ((mWidth * 560f) / 850) + 10 + ox, ((mHeight * 320f) / 480) + 10 + oy, this.mPaint);
            canvas.drawLine(((mWidth * 560f) / 850) + 10 + ox, ((mHeight * 320f) / 480) + 10 + oy, (((mWidth * 560f) / 850) - 10) + ox, ((mHeight * 320f) / 480) + 10 + oy, this.mPaint);
            canvas.drawLine((((mWidth * 560f) / 850) - 10) + ox, ((mHeight * 320f) / 480) + 10 + oy, (((mWidth * 560f) / 850) - 10) + ox, (((mHeight * 320f) / 480) - 10) + oy, this.mPaint);
            this.mPaint.setColor(-7829368);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setAlpha(4095);
            canvas.drawCircle(((mWidth * 665f) / 850) + ox, ((mHeight * 240f) / 480) + oy, (mHeight * 50f) / 480, this.mPaint);
            this.mPaint.setColor(-16711936);
            canvas.drawLine(((mWidth * 665f) / 850) + ox, (((mHeight * 240f) / 480) - 10) + oy, ((mWidth * 665f) / 850) + 10 + ox, ((mHeight * 240f) / 480) + 10 + oy, this.mPaint);
            canvas.drawLine(((mWidth * 665f) / 850) + 10 + ox, ((mHeight * 240f) / 480) + 10 + oy, (((mWidth * 665f) / 850) - 10) + ox, ((mHeight * 240f) / 480) + 10 + oy, this.mPaint);
            canvas.drawLine((((mWidth * 665f) / 850) - 10) + ox, ((mHeight * 240f) / 480) + 10 + oy, ((mWidth * 665f) / 850) + ox, (((mHeight * 240f) / 480) - 10) + oy, this.mPaint);
            this.mPaint.setColor(-7829368);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setAlpha(4095);
            canvas.drawCircle(((mWidth * 760f) / 850) + ox, ((mHeight * 320f) / 480) + oy, (mHeight * 50f) / 480, this.mPaint);
            this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setAlpha(4095);
            this.mPaint.setStrokeWidth(3.0f);
            canvas.drawCircle(((mWidth * 760f) / 850) + ox, ((mHeight * 320f) / 480) + oy, ((mHeight * 50f) / 3) / 480, this.mPaint);
            this.mPaint.setColor(-7829368);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setAlpha(4095);
            canvas.drawCircle(((mWidth * 665f) / 850) + ox, ((mHeight * 400f) / 480) + oy, (mHeight * 50f) / 480, this.mPaint);
            this.mPaint.setColor(-16776961);
            canvas.drawLine((((mWidth * 665f) / 850) - 10) + ox, (((mHeight * 400f) / 480) - 10) + oy, ((mWidth * 665f) / 850) + 10 + ox, ((mHeight * 400f) / 480) + 10 + oy, this.mPaint);
            canvas.drawLine(((mWidth * 665f) / 850) + 10 + ox, (((mHeight * 400f) / 480) - 10) + oy, (((mWidth * 665f) / 850) - 10) + ox, ((mHeight * 400f) / 480) + 10 + oy, this.mPaint);
            this.mPaint.setColor(-7829368);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setAlpha(4095);
            mRect.set(((mWidth * 30f) / 850) + ox, ((mHeight * 10f) / 480) + oy, ((mWidth * 210f) / 850) + ox, ((mHeight * 70f) / 480) + oy);
            canvas.drawRoundRect(mRect, 5.0f, 5.0f, this.mPaint);
            this.mPaint.setColor(-12303292);
            canvas.drawLine(((mWidth * 120f) / 850) + ox, ((mHeight * 10f) / 480) + oy, ((mWidth * 120f) / 850) + ox, ((mHeight * 70f) / 480) + oy, this.mPaint);
            this.mPaint.setColor(-12303292);
            canvas.drawLine(((mWidth * 75f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 75f) / 850) + ox, ((mHeight * 60f) / 480) + oy, this.mPaint);
            canvas.drawLine(((mWidth * 160f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 160f) / 850) + ox, ((mHeight * 60f) / 480) + oy, this.mPaint);
            canvas.drawLine(((mWidth * 170f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 170f) / 850) + ox, ((mHeight * 60f) / 480) + oy, this.mPaint);
            this.mPaint.setColor(-7829368);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setAlpha(4095);
            mRect.set(((mWidth * 640f) / 850) + ox, ((mHeight * 10f) / 480) + oy, ((mWidth * 820f) / 850) + ox, ((mHeight * 70f) / 480) + oy);
            canvas.drawRoundRect(mRect, 5.0f, 5.0f, this.mPaint);
            this.mPaint.setColor(-12303292);
            canvas.drawLine(((mWidth * 730f) / 850) + ox, ((mHeight * 10f) / 480) + oy, ((mWidth * 730f) / 850) + ox, ((mHeight * 70f) / 480) + oy, this.mPaint);
            this.mPaint.setColor(-12303292);
            canvas.drawLine(((mWidth * 680f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 680f) / 850) + ox, ((mHeight * 60f) / 480) + oy, this.mPaint);
            canvas.drawLine(((mWidth * 690f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 690f) / 850) + ox, ((mHeight * 60f) / 480) + oy, this.mPaint);
            canvas.drawLine(((mWidth * 775f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 775f) / 850) + ox, ((mHeight * 60f) / 480) + oy, this.mPaint);
        }

        private void doDrawVirtualPad(Canvas canvas, int mHeight, int mWidth, int ox, int oy, int player) {
            if (ePSXeViewSoft.this.emu_pad_mode[player] != 2) {
                RectF mRect = new RectF();
                if (ePSXeViewSoft.this.emu_pad_mode[player] == 1) {
                    this.mPaint.setColor(-3355444);
                    this.mPaint.setStyle(Paint.Style.FILL);
                    this.mPaint.setAlpha(1092);
                    canvas.drawCircle(((mWidth * 150f) / 850) + ox, ((mHeight * 360f) / 480) + oy, (mHeight * 110f) / 480, this.mPaint);
                    this.mPaint.setColor(-7829368);
                    this.mPaint.setStyle(Paint.Style.FILL);
                    this.mPaint.setAlpha(1092);
                    canvas.drawCircle(((mWidth * 150f) / 850) + ox, ((mHeight * 360f) / 480) + oy, (mHeight * 80f) / 480, this.mPaint);
                    this.mPaint.setColor(-12303292);
                    this.mPaint.setStyle(Paint.Style.FILL);
                    this.mPaint.setAlpha(1092);
                    canvas.drawCircle(((mWidth * 150f) / 850) + ox, ((mHeight * 360f) / 480) + oy, (mHeight * 20f) / 480, this.mPaint);
                }
                this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
                this.mPaint.setStyle(Paint.Style.FILL);
                this.mPaint.setAlpha(3276);
                Path path = new Path();
                path.moveTo(0.0f, 0.0f);
                path.lineTo(5.0f, 10.0f);
                path.lineTo(-5.0f, 10.0f);
                path.close();
                path.offset(((mWidth * 150f) / 850) + ox, ((((mHeight * 360f) / 480) - ((mHeight * 80f) / 480)) - 10) + oy);
                canvas.drawPath(path, this.mPaint);
                Path path2 = new Path();
                path2.moveTo(0.0f, 0.0f);
                path2.lineTo(-5.0f, -10.0f);
                path2.lineTo(5.0f, -10.0f);
                path2.close();
                path2.offset(((mWidth * 150f) / 850) + ox, ((mHeight * 360f) / 480) + ((mHeight * 80f) / 480) + 10 + oy);
                canvas.drawPath(path2, this.mPaint);
                Path path3 = new Path();
                path3.moveTo(0.0f, 0.0f);
                path3.lineTo(10.0f, -5.0f);
                path3.lineTo(10.0f, 5.0f);
                path3.close();
                path3.offset(((((mWidth * 150f) / 850) - ((mHeight * 80f) / 480)) - 10) + ox, ((mHeight * 360f) / 480) + oy);
                canvas.drawPath(path3, this.mPaint);
                Path path4 = new Path();
                path4.moveTo(0.0f, 0.0f);
                path4.lineTo(-10.0f, -5.0f);
                path4.lineTo(-10.0f, 5.0f);
                path4.close();
                path4.offset(((mWidth * 150f) / 850) + ((mHeight * 80f) / 480) + 10 + ox, ((mHeight * 360f) / 480) + oy);
                canvas.drawPath(path4, this.mPaint);
                this.mPaint.setColor(-3355444);
                if (ePSXeViewSoft.this.emu_pad_mode[player] == 1) {
                    this.mPaint.setStyle(Paint.Style.FILL);
                } else {
                    this.mPaint.setStyle(Paint.Style.STROKE);
                }
                this.mPaint.setAlpha(1092);
                Path path5 = new Path();
                path5.moveTo(0.0f, 0.0f);
                path5.lineTo((-(mHeight * 60f)) / 480, (-(mHeight * 25f)) / 480);
                path5.lineTo((-(mHeight * 60f)) / 480, (mHeight * 25f) / 480);
                path5.close();
                path5.offset(((mWidth * 540f) / 850) + ox, ((mHeight * 385f) / 480) + ((mHeight * 25f) / 480) + oy);
                canvas.drawPath(path5, this.mPaint);
                mRect.set(((mWidth * 350f) / 850) + ox, ((mHeight * 390f) / 480) + oy, ((mWidth * 410f) / 850) + ox, ((mHeight * 430f) / 480) + oy);
                canvas.drawRoundRect(mRect, 5.0f, 5.0f, this.mPaint);
                if (ePSXeViewSoft.this.emu_pad_mode[player] == 1) {
                    this.mPaint.setColor(-3355444);
                    this.mPaint.setStyle(Paint.Style.FILL);
                    this.mPaint.setAlpha(1092);
                    canvas.drawCircle(((mWidth * 655f) / 850) + ox, ((mHeight * 355f) / 480) + oy, (mHeight * 40f) / 480, this.mPaint);
                }
                this.mPaint.setColor(-65281);
                canvas.drawLine((((mWidth * 655f) / 850) - 10) + ox, (((mHeight * 355f) / 480) - 10) + oy, ((mWidth * 655f) / 850) + 10 + ox, (((mHeight * 355f) / 480) - 10) + oy, this.mPaint);
                canvas.drawLine(((mWidth * 655f) / 850) + 10 + ox, (((mHeight * 355f) / 480) - 10) + oy, ((mWidth * 655f) / 850) + 10 + ox, ((mHeight * 355f) / 480) + 10 + oy, this.mPaint);
                canvas.drawLine(((mWidth * 655f) / 850) + 10 + ox, ((mHeight * 355f) / 480) + 10 + oy, (((mWidth * 655f) / 850) - 10) + ox, ((mHeight * 355f) / 480) + 10 + oy, this.mPaint);
                canvas.drawLine((((mWidth * 655f) / 850) - 10) + ox, ((mHeight * 355f) / 480) + 10 + oy, (((mWidth * 655f) / 850) - 10) + ox, (((mHeight * 355f) / 480) - 10) + oy, this.mPaint);
                if (ePSXeViewSoft.this.emu_pad_mode[player] == 1) {
                    this.mPaint.setColor(-3355444);
                    this.mPaint.setStyle(Paint.Style.FILL);
                    this.mPaint.setAlpha(1092);
                    canvas.drawCircle(((mWidth * 725f) / 850) + ox, ((mHeight * 280f) / 480) + oy, (mHeight * 40f) / 480, this.mPaint);
                }
                this.mPaint.setColor(-16711936);
                canvas.drawLine(((mWidth * 725f) / 850) + ox, (((mHeight * 280f) / 480) - 10) + oy, ((mWidth * 725f) / 850) + 10 + ox, ((mHeight * 280f) / 480) + 10 + oy, this.mPaint);
                canvas.drawLine(((mWidth * 725f) / 850) + 10 + ox, ((mHeight * 280f) / 480) + 10 + oy, (((mWidth * 725f) / 850) - 10) + ox, ((mHeight * 280f) / 480) + 10 + oy, this.mPaint);
                canvas.drawLine((((mWidth * 725f) / 850) - 10) + ox, ((mHeight * 280f) / 480) + 10 + oy, ((mWidth * 725f) / 850) + ox, (((mHeight * 280f) / 480) - 10) + oy, this.mPaint);
                if (ePSXeViewSoft.this.emu_pad_mode[player] == 1) {
                    this.mPaint.setColor(-3355444);
                    this.mPaint.setStyle(Paint.Style.FILL);
                    this.mPaint.setAlpha(1092);
                    canvas.drawCircle(((mWidth * 795f) / 850) + ox, ((mHeight * 355f) / 480) + oy, (mHeight * 40f) / 480, this.mPaint);
                }
                this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
                this.mPaint.setStyle(Paint.Style.STROKE);
                this.mPaint.setAlpha(1092);
                this.mPaint.setStrokeWidth(3.0f);
                canvas.drawCircle(((mWidth * 795f) / 850) + ox, ((mHeight * 355f) / 480) + oy, ((mHeight * 40f) / 3) / 480, this.mPaint);
                if (ePSXeViewSoft.this.emu_pad_mode[player] == 1) {
                    this.mPaint.setColor(-3355444);
                    this.mPaint.setStyle(Paint.Style.FILL);
                    this.mPaint.setAlpha(1092);
                    canvas.drawCircle(((mWidth * 725f) / 850) + ox, ((mHeight * 430f) / 480) + oy, (mHeight * 40f) / 480, this.mPaint);
                }
                this.mPaint.setColor(-16776961);
                canvas.drawLine((((mWidth * 725f) / 850) - 10) + ox, (((mHeight * 430f) / 480) - 10) + oy, ((mWidth * 725f) / 850) + 10 + ox, ((mHeight * 430f) / 480) + 10 + oy, this.mPaint);
                canvas.drawLine(((mWidth * 725f) / 850) + 10 + ox, (((mHeight * 430f) / 480) - 10) + oy, (((mWidth * 725f) / 850) - 10) + ox, ((mHeight * 430f) / 480) + 10 + oy, this.mPaint);
                this.mPaint.setColor(-3355444);
                if (ePSXeViewSoft.this.emu_pad_mode[player] == 1) {
                    this.mPaint.setStyle(Paint.Style.FILL);
                } else {
                    this.mPaint.setStyle(Paint.Style.STROKE);
                }
                this.mPaint.setAlpha(1092);
                mRect.set(((mWidth * 90f) / 850) + ox, ((mHeight * 10f) / 480) + oy, ((mWidth * 210f) / 850) + ox, ((mHeight * 60f) / 480) + oy);
                canvas.drawRoundRect(mRect, 5.0f, 5.0f, this.mPaint);
                this.mPaint.setColor(-7829368);
                canvas.drawLine(((mWidth * 150f) / 850) + ox, ((mHeight * 10f) / 480) + oy, ((mWidth * 150f) / 850) + ox, ((mHeight * 60f) / 480) + oy, this.mPaint);
                this.mPaint.setColor(-12303292);
                canvas.drawLine(((mWidth * 120f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 120f) / 850) + ox, ((mHeight * 50f) / 480) + oy, this.mPaint);
                canvas.drawLine(((mWidth * 177f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 177f) / 850) + ox, ((mHeight * 50f) / 480) + oy, this.mPaint);
                canvas.drawLine(((mWidth * 182f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 182f) / 850) + ox, ((mHeight * 50f) / 480) + oy, this.mPaint);
                this.mPaint.setColor(-3355444);
                if (ePSXeViewSoft.this.emu_pad_mode[player] == 1) {
                    this.mPaint.setStyle(Paint.Style.FILL);
                } else {
                    this.mPaint.setStyle(Paint.Style.STROKE);
                }
                this.mPaint.setAlpha(1092);
                mRect.set(((mWidth * 680f) / 850) + ox, ((mHeight * 10f) / 480) + oy, ((mWidth * 800f) / 850) + ox, ((mHeight * 60f) / 480) + oy);
                canvas.drawRoundRect(mRect, 5.0f, 5.0f, this.mPaint);
                this.mPaint.setColor(-7829368);
                canvas.drawLine(((mWidth * 740f) / 850) + ox, ((mHeight * 10f) / 480) + oy, ((mWidth * 740f) / 850) + ox, ((mHeight * 60f) / 480) + oy, this.mPaint);
                this.mPaint.setColor(-12303292);
                canvas.drawLine(((mWidth * 707f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 707f) / 850) + ox, ((mHeight * 50f) / 480) + oy, this.mPaint);
                canvas.drawLine(((mWidth * 712f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 712f) / 850) + ox, ((mHeight * 50f) / 480) + oy, this.mPaint);
                canvas.drawLine(((mWidth * 770f) / 850) + ox, ((mHeight * 20f) / 480) + oy, ((mWidth * 770f) / 850) + ox, ((mHeight * 50f) / 480) + oy, this.mPaint);
            }
        }

        public EmuThread(SurfaceHolder surfaceHolder, Context context) {
            this.mSurfaceHolder = surfaceHolder;
            ePSXeViewSoft.this.mContext = context;
        }

        public void saving() {
            synchronized (this.mSurfaceHolder) {
                setState(3);
            }
        }

        public void autosaving() {
            synchronized (this.mSurfaceHolder) {
                setState(4);
            }
        }

        public void pause() {
            synchronized (this.mSurfaceHolder) {
                if (this.mMode == 2) {
                    setState(1);
                }
            }
        }

        public void unpause() {
            synchronized (this.mSurfaceHolder) {
                this.mLastTime = System.currentTimeMillis() + 100;
            }
            setState(2);
        }

        public void setState(int mode) {
            synchronized (this.mSurfaceHolder) {
                this.mMode = mode;
            }
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (this.mSurfaceHolder) {
                this.mCanvasWidth = width;
                this.mCanvasHeight = height;
                this.mWidth = width;
                this.mHeight = height;
            }
        }

        public void setRunning(boolean b) {
            this.mRun = b;
        }

        private void clear(Canvas canvas) {
            Rect rect = new Rect();
            rect.set(0, 0, canvas.getWidth(), canvas.getHeight());
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            p.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawRect(rect, p);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            int frame = 0;
            int pad = 0;
            int saving = 0;
            int flimit = 0;
            while (this.mRun) {
                if (this.mMode == 3) {
                    saving = 1;
                    ePSXeViewSoft.this.f176e.setsslot(20);
                    Log.e("ePSXeViewSoft", "saving game in slot fixed");
                }
                if (this.mMode == 4) {
                    saving = 1;
                    ePSXeViewSoft.this.f176e.setsslot(15);
                    Log.e("ePSXeViewSoft", "saving game in slot fixed");
                }
                Canvas c = null;
                int flag = 0;
                long mMillis = System.currentTimeMillis();
                long deltaT = (((mMillis - this.mStartTime) * ePSXeViewSoft.this.mfps) / 1000) - this.mEmuTime;
                if (deltaT < (-ePSXeViewSoft.this.mfps) || deltaT > ePSXeViewSoft.this.mfps) {
                    Log.e("ePSXeViewSoft", "Reseting time base " + deltaT);
                    this.mEmuTime = 0L;
                    this.mStartTime = mMillis;
                    deltaT = 0;
                }
                if (deltaT > 0) {
                    this.skippedCount++;
                    if (this.skippedCount < 2) {
                        flag = 0 | 2;
                    } else {
                        this.skippedCount = 0;
                    }
                } else {
                    if (deltaT < 0 && this.mEmuTime > 1) {
                        try {
                            if (ePSXeViewSoft.this.emu_enable_framelimit == 1 && ePSXeViewSoft.this.emu_sound_latency == 0) {
                                sleep(((-deltaT) * 1000) / ePSXeViewSoft.this.mfps);
                            }
                        } catch (Exception e) {
                        }
                    }
                    this.skippedCount = 0;
                }
                ePSXeViewSoft.this.f176e.runepsxeframe(this.mWidth, this.mHeight, flag);
                if ((((flag & 2) == 0 && ePSXeViewSoft.this.emu_enable_frameskip == 0) || (ePSXeViewSoft.this.f176e.isSkip() > 0 && ePSXeViewSoft.this.emu_enable_frameskip > 0)) && (ePSXeViewSoft.this.emu_enable_framelimit == 1 || flimit > 7)) {
                    int x = ePSXeViewSoft.this.f176e.getwidth();
                    int y = ePSXeViewSoft.this.f176e.getheight();
                    doBlitToBitmap(x, y);
                    try {
                        if (ePSXeViewSoft.this.emu_screen_orientation != 1 || ePSXeViewSoft.this.emu_player_mode != 1 || (frame & 63) == 0) {
                            if (ePSXeViewSoft.this.emu_screen_ratio == 1 && ePSXeViewSoft.this.emu_screen_orientation == 0 && ePSXeViewSoft.this.emu_player_mode == 1 && x > 0 && y > 0) {
                                c = this.mSurfaceHolder.lockCanvas(this.DestRectPartial);
                            } else {
                                c = this.mSurfaceHolder.lockCanvas(null);
                                pad = 1;
                            }
                        } else {
                            c = this.mSurfaceHolder.lockCanvas(this.DestRectPartial);
                            pad = 0;
                        }
                        if (c != null) {
                            synchronized (this.mSurfaceHolder) {
                                doDraw(c, x, y, pad);
                            }
                        }
                        frame++;
                        flimit = 0;
                    } finally {
                        if (c != null) {
                            this.mSurfaceHolder.unlockCanvasAndPost(c);
                        }
                    }
                }
                this.mEmuTime++;
                flimit++;
                if (saving == 1) {
                    this.mRun = false;
                    saving = 0;
                }
            }
        }

        private void doBlitToBitmap(int x, int y) {
            int x8 = x & (-8);
            if (x8 < x) {
                x8 += 8;
            }
            if (x > 0 && y > 0 && (x8 != this.bitmapwidth || y != this.bitmapheight)) {
                if (this.mBitmap != null) {
                    this.mBitmap.recycle();
                }
                this.bitmapwidth = x8;
                this.bitmapheight = y;
                Log.e("ePSXeViewSoft", "Creating bitmap of " + x8 + " x " + y + " resol (" + x + "," + y + ")");
                this.mBitmap = Bitmap.createBitmap(this.bitmapwidth, this.bitmapheight, Bitmap.Config.RGB_565);
            }
            if (x > 0 && y > 0) {
                if (ePSXeViewSoft.this.osVersion >= 8) {
                    ePSXeViewSoft.this.f176e.copyPixelsFromVRAMToBitmap(this.mBitmap);
                    return;
                }
                ePSXeViewSoft.this.f176e.copyPixelsFromVRAMToBuffer();
                this.mBitmap.copyPixelsFromBuffer(ePSXeViewSoft.this.byteBuffer);
                ePSXeViewSoft.this.byteBuffer.clear();
            }
        }

        private void doDraw(Canvas canvas, int x, int y, int pad) {
            if (x > 0 && y > 0) {
                if (this.SourceRect.right != x || this.SourceRect.bottom != y) {
                    this.SourceRect.set(0, 0, x, y);
                }
                if (this.DestRect == null || this.DestRect.right != canvas.getWidth() || this.DestRect.bottom != canvas.getHeight()) {
                    int offw = (canvas.getWidth() - ((canvas.getHeight() * 4) / 3)) / 2;
                    this.DestRect.set(0, 0, canvas.getWidth(), canvas.getHeight());
                    this.DestRectP1.set(0, 0, canvas.getHeight(), canvas.getWidth() / 2);
                    this.DestRectP2.set(canvas.getWidth() - canvas.getHeight(), 0, canvas.getWidth(), canvas.getWidth() / 2);
                    this.DestRectPartial.set(offw, 0, ((canvas.getHeight() * 4) / 3) + offw, canvas.getHeight());
                }
                if (ePSXeViewSoft.this.emu_player_mode == 1) {
                    if (ePSXeViewSoft.this.emu_screen_orientation == 0) {
                        if (ePSXeViewSoft.this.emu_video_filter != 1) {
                            if (ePSXeViewSoft.this.emu_screen_ratio == 1) {
                                canvas.drawBitmap(this.mBitmap, (Rect) null, this.DestRectPartial, (Paint) null);
                            } else {
                                canvas.drawBitmap(this.mBitmap, (Rect) null, this.DestRect, (Paint) null);
                            }
                        } else {
                            this.mPaintFilter.setFilterBitmap(true);
                            if (ePSXeViewSoft.this.emu_screen_ratio == 1) {
                                canvas.drawBitmap(this.mBitmap, (Rect) null, this.DestRectPartial, this.mPaintFilter);
                            } else {
                                canvas.drawBitmap(this.mBitmap, (Rect) null, this.DestRect, this.mPaintFilter);
                            }
                        }
                        doDrawVirtualPad(canvas, this.mHeight, this.mWidth, 0, 0, 0);
                        if (ePSXeViewSoft.this.emu_enable_printfps == 1) {
                            if (ePSXeViewSoft.this.emu_screen_ratio == 1) {
                                doDrawFPS(canvas, canvas.getHeight() / 4);
                            } else {
                                doDrawFPS(canvas, 0);
                            }
                        }
                    } else {
                        this.DestRectPartial.set(0, 0, canvas.getWidth(), canvas.getHeight() / 2);
                        if (ePSXeViewSoft.this.emu_video_filter == 1) {
                            this.mPaintFilter.setFilterBitmap(true);
                            canvas.drawBitmap(this.mBitmap, (Rect) null, this.DestRectPartial, this.mPaintFilter);
                        } else {
                            canvas.drawBitmap(this.mBitmap, (Rect) null, this.DestRectPartial, (Paint) null);
                        }
                        if (pad == 1) {
                            doDrawVirtualPadPortrait(canvas, canvas.getHeight() / 2, canvas.getWidth(), 0, canvas.getHeight() / 2);
                        }
                        if (ePSXeViewSoft.this.emu_enable_printfps == 1) {
                            doDrawFPS(canvas, 0);
                        }
                    }
                } else if (ePSXeViewSoft.this.emu_split_mode != 0) {
                    if (ePSXeViewSoft.this.emu_split_mode == 1 || ePSXeViewSoft.this.emu_split_mode == 2) {
                        this.DestRectP1.set(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight());
                        this.DestRectP2.set(0, 0, canvas.getWidth(), canvas.getHeight() / 2);
                        this.SourceRectP1.set(this.SourceRect.left, this.SourceRect.bottom / 2, this.SourceRect.right, this.SourceRect.bottom);
                        this.SourceRectP2.set(this.SourceRect.left, this.SourceRect.top, this.SourceRect.right, this.SourceRect.bottom / 2);
                        canvas.drawBitmap(this.mBitmap, this.SourceRectP1, this.DestRectP1, (Paint) null);
                        doDrawVirtualPad(canvas, canvas.getHeight() / 2, canvas.getWidth(), 0, canvas.getHeight() / 2, 0);
                        Matrix orig = canvas.getMatrix();
                        orig.setTranslate(0.0f, 0.0f);
                        orig.postRotate(180.0f, canvas.getWidth() / 2f, canvas.getHeight() / 4f);
                        canvas.setMatrix(orig);
                        canvas.drawBitmap(this.mBitmap, this.SourceRectP2, this.DestRectP2, (Paint) null);
                        doDrawVirtualPad(canvas, canvas.getHeight() / 2, canvas.getWidth(), 0, 0, 1);
                        canvas.setMatrix(orig);
                    }
                } else {
                    this.DestRectP1.set(0, 0, canvas.getHeight(), canvas.getWidth() / 2);
                    this.DestRectP2.set(canvas.getWidth() - canvas.getHeight(), 0, canvas.getWidth(), canvas.getWidth() / 2);
                    Matrix orig2 = canvas.getMatrix();
                    orig2.setTranslate((canvas.getHeight() - (canvas.getWidth() / 2f)) / 2, (-((canvas.getWidth() / 2f) - canvas.getHeight())) / 2);
                    orig2.postRotate(90.0f, canvas.getHeight() / 2f, canvas.getWidth() / 4f);
                    canvas.setMatrix(orig2);
                    canvas.drawBitmap(this.mBitmap, (Rect) null, this.DestRectP1, (Paint) null);
                    doDrawVirtualPad(canvas, canvas.getWidth() / 2, canvas.getHeight(), 0, 0, 0);
                    canvas.setMatrix(orig2);
                    Matrix orig3 = canvas.getMatrix();
                    orig3.setTranslate((-(canvas.getHeight() - (canvas.getWidth() / 2f))) / 2, (-((canvas.getWidth() / 2f) - canvas.getHeight())) / 2);
                    orig3.postRotate(270.0f, (canvas.getWidth() - canvas.getHeight()) + (canvas.getHeight() / 2f), canvas.getWidth() / 4f);
                    canvas.setMatrix(orig3);
                    canvas.drawBitmap(this.mBitmap, (Rect) null, this.DestRectP2, (Paint) null);
                    doDrawVirtualPad(canvas, canvas.getWidth() / 2, canvas.getHeight(), canvas.getWidth() - canvas.getHeight(), 0, 1);
                    canvas.setMatrix(orig3);
                }
            }
            if (x == 0 && y == 0) {
                clear(canvas);
                if (ePSXeViewSoft.this.emu_player_mode == 1) {
                    if (ePSXeViewSoft.this.emu_screen_orientation == 0) {
                        doDrawVirtualPad(canvas, this.mHeight, this.mWidth, 0, 0, 0);
                        if (ePSXeViewSoft.this.emu_enable_printfps == 1) {
                            if (ePSXeViewSoft.this.emu_screen_ratio != 1) {
                                doDrawFPS(canvas, 0);
                                return;
                            } else {
                                doDrawFPS(canvas, canvas.getHeight() / 4);
                                return;
                            }
                        }
                        return;
                    }
                    this.DestRectPartial.set(0, 0, canvas.getWidth(), canvas.getHeight() / 2);
                    if (pad == 1) {
                        doDrawVirtualPadPortrait(canvas, canvas.getHeight() / 2, canvas.getWidth(), 0, canvas.getHeight() / 2);
                    }
                    if (ePSXeViewSoft.this.emu_enable_printfps == 1) {
                        doDrawFPS(canvas, 0);
                        return;
                    }
                    return;
                }
                if (ePSXeViewSoft.this.emu_split_mode != 0) {
                    if (ePSXeViewSoft.this.emu_split_mode == 1 || ePSXeViewSoft.this.emu_split_mode == 2) {
                        this.DestRectP1.set(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight());
                        this.DestRectP2.set(0, 0, canvas.getWidth(), canvas.getHeight() / 2);
                        doDrawVirtualPad(canvas, canvas.getHeight() / 2, canvas.getWidth(), 0, canvas.getHeight() / 2, 0);
                        Matrix orig4 = canvas.getMatrix();
                        orig4.setTranslate(0.0f, 0.0f);
                        orig4.postRotate(180.0f, canvas.getWidth() / 2f, canvas.getHeight() / 4f);
                        canvas.setMatrix(orig4);
                        doDrawVirtualPad(canvas, canvas.getHeight() / 2, canvas.getWidth(), 0, 0, 1);
                        canvas.setMatrix(orig4);
                        return;
                    }
                    return;
                }
                this.DestRectP1.set(0, 0, canvas.getHeight(), canvas.getWidth() / 2);
                this.DestRectP2.set(canvas.getWidth() - canvas.getHeight(), 0, canvas.getWidth(), canvas.getWidth() / 2);
                Matrix orig5 = canvas.getMatrix();
                orig5.setTranslate((canvas.getHeight() - (canvas.getWidth() / 2f)) / 2, (-((canvas.getWidth() / 2f) - canvas.getHeight())) / 2);
                orig5.postRotate(90.0f, canvas.getHeight() / 2f, canvas.getWidth() / 4f);
                canvas.setMatrix(orig5);
                doDrawVirtualPad(canvas, canvas.getWidth() / 2, canvas.getHeight(), 0, 0, 0);
                canvas.setMatrix(orig5);
                Matrix orig6 = canvas.getMatrix();
                orig6.setTranslate((-(canvas.getHeight() - (canvas.getWidth() / 2f))) / 2, (-((canvas.getWidth() / 2f) - canvas.getHeight())) / 2);
                orig6.postRotate(270.0f, (canvas.getWidth() - canvas.getHeight()) + (canvas.getHeight() / 2f), canvas.getWidth() / 4f);
                canvas.setMatrix(orig6);
                doDrawVirtualPad(canvas, canvas.getWidth() / 2, canvas.getHeight(), canvas.getWidth() - canvas.getHeight(), 0, 1);
                canvas.setMatrix(orig6);
            }
        }
    }

    public ePSXeViewSoft(Context context, ePSXe act, int ry) {
        super(context);
        this.mfps = 60;
        this.ts_vibration = new int[]{0, 0};
        this.emu_enable_frameskip = 0;
        this.emu_enable_frameskip_tmp = 0;
        this.emu_enable_printfps = 1;
        this.emu_player_mode = 1;
        this.emu_split_mode = 0;
        this.emu_enable_mme = 1;
        this.emu_screen_orientation = 0;
        this.emu_screen_ratio = 0;
        this.emu_pad_portrait = 0;
        this.emu_pad_mode = new int[]{1, 1};
        this.emu_enable_framelimit = 1;
        this.emu_pad_type = new int[]{0, 0};
        this.emu_video_filter = 0;
        this.emu_sound_latency = 0;
        this.license = true;
        this.gprofile = false;
        this.skinName = "/sdcard/skin.png";
        this.biosmsg = false;
        this.gpuVersionAdvise = InputList.KEYCODE_NUMPAD_6;
        this.tainted = 0;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        this.thread = new EmuThread(holder, context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.thread.setSurfaceSize(width, height);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setePSXeLib(libepsxe epsxelib, int glVersion, int net) {
        this.f176e = epsxelib;
        this.osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (this.osVersion < 8) {
            this.byteBuffer = ByteBuffer.allocateDirect(655360);
            Log.e("ePSXeViewSoft", "byteBuffer " + this.byteBuffer);
            this.f176e.setbuffer(this.byteBuffer);
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdevice(int dev) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setquitonexit() {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setanalogdebug(int lx1, int ly1, int lx2, int ily2) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setfps(int fps) {
        this.mfps = fps;
        Log.e("ePSXeViewSoft", "fps = " + fps);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setlicense(boolean nowlicense) {
        this.license = nowlicense;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setcustomgameprofile(boolean gp) {
        this.gprofile = gp;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setbiosmsg(boolean msg) {
        this.biosmsg = msg;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputvibration(int vibration1, int vibration2) {
        this.ts_vibration[0] = vibration1;
        this.ts_vibration[1] = vibration2;
        Log.e("epsxeView", "InputVibrate1 = " + vibration1);
        Log.e("epsxeView", "InputVibrate2 = " + vibration2);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputalpha(float alpha) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputmag(float mag) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setframeskip(int fs) {
        this.emu_enable_frameskip = fs;
        this.emu_enable_frameskip_tmp = fs;
        Log.e("epsxeView", "FrameSkip = " + fs);
        this.emu_enable_frameskip = this.f176e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_frameskip_tmp = this.emu_enable_frameskip;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenorientation(int ori) {
        Log.e("epsxeView", "Orientation = " + ori);
        if (ori == 3) {
            ori = 0;
        }
        this.emu_screen_orientation = this.f176e.setscreenorientation(ori);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setportraitmode(int mode) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenvrmode(int vrmode, int vrdistorsion) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenratio(int rat) {
        Log.e("epsxeView", "Ratio = " + rat);
        this.emu_screen_ratio = rat;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void updatescreenratio(int rat) {
        setscreenratio(rat);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreendepth(int dep) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setscreenblackbands(int bb) {
        Log.e("epsxeView", "blackbands = " + bb);
        this.f176e.setblackbands(bb);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setvideofilter(int fil) {
        Log.e("epsxeView", "Filter = " + fil);
        this.emu_video_filter = fil;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setgpumtmodeH(int modeH) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setgpumtmodeS(int modeS) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void settainted(int mode) {
        this.tainted = mode;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setvideodither(int fil) {
        Log.e("epsxeView", "PSX Dither = " + fil);
        this.f176e.setDithering(fil);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setvideofilterhw(int fil) {
        Log.e("epsxeView", "PSX Filter hw = " + fil);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setplayermode(int mode) {
        this.emu_player_mode = mode;
        Log.e("epsxeView", "PlayerMode = " + mode);
        this.emu_player_mode = this.f176e.setPlayerMode(this.emu_player_mode);
        if (this.emu_player_mode == 10) {
            if (this.emu_screen_orientation != 0) {
                this.emu_split_mode = 0;
            } else {
                this.emu_split_mode = 1;
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setsplitmode(int mode) {
        this.emu_split_mode = mode;
        Log.e("epsxeView", "SplitMode = " + mode);
        this.emu_split_mode = this.f176e.setSplitMode(mode);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setsoundlatency(int mode) {
        Log.e("epsxeView", "SoundLatency = " + mode);
        this.emu_sound_latency = this.f176e.setSoundLatency(mode);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void toggleframelimit() {
        if (this.emu_enable_framelimit == 1) {
            this.emu_enable_frameskip_tmp = this.emu_enable_frameskip;
            this.emu_enable_frameskip = 0;
            this.emu_enable_frameskip = this.f176e.setFrameSkip(this.emu_enable_frameskip);
            this.emu_enable_framelimit ^= 1;
            this.emu_enable_framelimit = this.f176e.setFrameLimit(this.emu_enable_framelimit);
            return;
        }
        this.emu_enable_frameskip = this.emu_enable_frameskip_tmp;
        this.emu_enable_frameskip = this.f176e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_framelimit ^= 1;
        this.emu_enable_framelimit = this.f176e.setFrameLimit(this.emu_enable_framelimit);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setframelimit() {
        this.emu_enable_frameskip = this.emu_enable_frameskip_tmp;
        this.emu_enable_frameskip = this.f176e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_framelimit = 1;
        this.emu_enable_framelimit = this.f176e.setFrameLimit(this.emu_enable_framelimit);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void unsetframelimit() {
        this.emu_enable_frameskip_tmp = this.emu_enable_frameskip;
        this.emu_enable_frameskip = 0;
        this.emu_enable_frameskip = this.f176e.setFrameSkip(this.emu_enable_frameskip);
        this.emu_enable_framelimit = 0;
        this.emu_enable_framelimit = this.f176e.setFrameLimit(this.emu_enable_framelimit);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setshowfps(int fps) {
        this.emu_enable_printfps = fps;
        Log.e("epsxeView", "CpuShowFPS " + fps);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpaintpadmode(int mode, int mode2) {
        this.emu_pad_mode[0] = mode;
        this.emu_pad_mode[1] = mode2;
        Log.e("epsxeView", "PadMode " + mode);
        Log.e("epsxeView", "PadMode2 " + mode2);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputskinname(String name) {
        this.skinName = name;
        Log.e("epsxeView", "skinName " + name);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpadtype(int type, int type2) {
        this.emu_pad_type[0] = type;
        this.emu_pad_type[1] = type2;
        Log.e("epsxeView", "PadType " + type);
        Log.e("epsxeView", "PadType2 " + type2);
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpadmode(int mode1, int mode2, int analog1, int analog2) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputpadmodeanalog(int p) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdebugstring(String s) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdebugstring2(String s) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setsnaprestoring(boolean snaprestoring) {
        Log.e("epsxeView", "loadtmp_snap ");
        this.f176e.loadtmpsnap();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setautosnaprestoring() {
        Log.e("epsxeView", "loadauto_snap ");
        this.f176e.loadautosnap();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setinputprofile(int profile) {
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent ev) {
        queueMotionEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
            case MotionEvent.ACTION_MOVE: // движение
                isInTouch = true;
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                isInTouch = false;
                break;
        }
        if (touchListener != null) {
            touchListener.onTouch(isInTouch);
        }
        return true;
    }

    private void dumpEvent(MotionEvent event) {
        String[] names = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & 255;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == 5 || actionCode == 6) {
            sb.append("(pid ").append(action >> 8);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount()) {
                sb.append(";");
            }
        }
        sb.append("]");
        Log.e("epsxegl", sb.toString());
    }

    public static int getActionIndexEclair(MotionEvent event) {
        return (event.getAction() & 65280) >> 8;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void queueMotionEvent(int action, int x, int y, int pointerId) {
        int actionCode = action & 255;
        int vib = 0;
        if (this.emu_pad_type[0] != 1 || this.emu_pad_type[1] != 1) {
            if ((actionCode == 5 || actionCode == 6 || actionCode == 1 || actionCode == 0 || actionCode == 2) && (this.emu_player_mode != 1 || this.emu_pad_type[0] == 0)) {
                vib = 0 + this.f176e.motionevent(0L, action, x, y, 0.0f, 0.0f, 0, pointerId);
            }
            if ((this.ts_vibration[0] == 1 && vib == 1) || (this.ts_vibration[1] == 1 && vib == 2)) {
                Vibrator v = (Vibrator) this.mContext.getSystemService("vibrator");
                v.vibrate(35L);
            }
        }
    }

    private void queueMotionEvent(MotionEvent event) {
        int action = event.getAction();
        int actionCode = action & 255;
        int vib = 0;
        int i = this.osVersion >= 8 ? event.getActionIndex() : getActionIndexEclair(event);
        if (this.emu_pad_type[0] != 1 || this.emu_pad_type[1] != 1) {
            if (actionCode == 5 || actionCode == 6 || actionCode == 1 || actionCode == 0) {
                if (this.emu_player_mode == 1) {
                    if (this.emu_pad_type[0] == 0) {
                        vib = this.f176e.motionevent(event.getEventTime(), event.getAction(), event.getX(i), event.getY(i), event.getPressure(i), event.getSize(i), event.getDeviceId(), event.getPointerId(i));
                    }
                } else {
                    vib = this.f176e.motionevent2P(event.getEventTime(), event.getAction(), event.getX(i), event.getY(i), event.getPressure(i), event.getSize(i), event.getDeviceId(), event.getPointerId(i), this.emu_pad_type[0] | (this.emu_pad_type[1] << 1));
                }
            } else if (actionCode == 2) {
                for (int ind = 0; ind < event.getPointerCount(); ind++) {
                    if (this.emu_player_mode == 1) {
                        if (this.emu_pad_type[0] == 0) {
                            vib += this.f176e.motionevent(event.getEventTime(), event.getAction(), event.getX(ind), event.getY(ind), event.getPressure(ind), event.getSize(ind), event.getDeviceId(), event.getPointerId(ind));
                        }
                    } else {
                        vib += this.f176e.motionevent2P(event.getEventTime(), event.getAction(), event.getX(ind), event.getY(ind), event.getPressure(ind), event.getSize(ind), event.getDeviceId(), event.getPointerId(ind), this.emu_pad_type[0] | (this.emu_pad_type[1] << 1));
                    }
                }
            }
            if ((this.ts_vibration[0] == 1 && vib == 1) || (this.ts_vibration[1] == 1 && vib == 2)) {
                Vibrator v = (Vibrator) this.mContext.getSystemService("vibrator");
                v.vibrate(15L);
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setSaveMode(int mode, int auto) {
        Log.e("epsxe", "epsxeview saving status");
        this.thread.saving();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onPause(int mode, int auto) {
        Log.e("epsxe", "epsxeview pause status");
        this.thread.saving();
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onResume() {
        Log.e("epsxe", "epsxeview resume status");
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onStop() {
        Log.e("epsxe", "epsxeview stop status");
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void onAutosave(int mode) {
        Log.e("epsxe", "epsxeview saving status");
        this.thread.autosaving();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        this.thread.setRunning(true);
        this.thread.start();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        this.thread.setRunning(false);
        while (retry) {
            try {
                this.thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public boolean setIsoName(String name, int slot, String gpu) {
        return true;
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setSaveslot(int slot) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void toggletools() {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdynamicpad(int dyn) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setdynamicaction(int dyn) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setservermode(int sm) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setemuvolumen(int vol) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setverbose(int ver) {
    }

    @Override // com.epsxe.ePSXe.ePSXeView
    public void setplugin(int mode) {
    }
}
