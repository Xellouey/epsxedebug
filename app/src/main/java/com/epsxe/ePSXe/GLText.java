package com.epsxe.ePSXe;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLUtils;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class GLText {
    public static final int CHAR_BATCH_SIZE = 100;
    public static final int CHAR_CNT = 96;
    public static final int CHAR_END = 126;
    public static final int CHAR_NONE = 32;
    public static final int CHAR_START = 32;
    public static final int CHAR_UNKNOWN = 95;
    public static final int FONT_SIZE_MAX = 180;
    public static final int FONT_SIZE_MIN = 6;
    AssetManager assets;
    SpriteBatch batch;

    /* renamed from: gl */
    GL10 f125gl;
    TextureRegion textureRgn;
    final float[] charWidths = new float[96];
    TextureRegion[] charRgn = new TextureRegion[96];
    int fontPadX = 0;
    int fontPadY = 0;
    float fontHeight = 0.0f;
    float fontAscent = 0.0f;
    float fontDescent = 0.0f;
    int textureId = -1;
    int textureSize = 0;
    float charWidthMax = 0.0f;
    float charHeight = 0.0f;
    int cellWidth = 0;
    int cellHeight = 0;
    int rowCnt = 0;
    int colCnt = 0;
    float scaleX = 1.0f;
    float scaleY = 1.0f;
    float spaceX = 0.0f;

    public GLText(GL10 gl, AssetManager assets) {
        this.f125gl = gl;
        this.assets = assets;
        this.batch = new SpriteBatch(gl, 100);
    }

    public boolean load(String file, int size, int padX, int padY) {
        this.fontPadX = padX;
        this.fontPadY = padY;
        Typeface tf = Typeface.createFromAsset(this.assets, file);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(size);
        paint.setColor(-1);
        paint.setTypeface(tf);
        Paint.FontMetrics fm = paint.getFontMetrics();
        this.fontHeight = (float) Math.ceil(Math.abs(fm.bottom) + Math.abs(fm.top));
        this.fontAscent = (float) Math.ceil(Math.abs(fm.ascent));
        this.fontDescent = (float) Math.ceil(Math.abs(fm.descent));
        char[] s = new char[2];
        this.charHeight = 0.0f;
        this.charWidthMax = 0.0f;
        float[] w = new float[2];
        int cnt = 0;
        for (char c = ' '; c <= '~'; c = (char) (c + 1)) {
            s[0] = c;
            paint.getTextWidths(s, 0, 1, w);
            this.charWidths[cnt] = w[0];
            if (this.charWidths[cnt] > this.charWidthMax) {
                this.charWidthMax = this.charWidths[cnt];
            }
            cnt++;
        }
        s[0] = ' ';
        paint.getTextWidths(s, 0, 1, w);
        this.charWidths[cnt] = w[0];
        if (this.charWidths[cnt] > this.charWidthMax) {
            this.charWidthMax = this.charWidths[cnt];
        }
        int i = cnt + 1;
        this.charHeight = this.fontHeight;
        this.cellWidth = ((int) this.charWidthMax) + (this.fontPadX * 2);
        this.cellHeight = ((int) this.charHeight) + (this.fontPadY * 2);
        int maxSize = this.cellWidth > this.cellHeight ? this.cellWidth : this.cellHeight;
        if (maxSize < 6 || maxSize > 180) {
            return false;
        }
        if (maxSize <= 24) {
            this.textureSize = 256;
        } else if (maxSize <= 40) {
            this.textureSize = 512;
        } else if (maxSize <= 80) {
            this.textureSize = 1024;
        } else {
            this.textureSize = 2048;
        }
        Bitmap bitmap = Bitmap.createBitmap(this.textureSize, this.textureSize, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(0);
        this.colCnt = this.textureSize / this.cellWidth;
        this.rowCnt = (int) Math.ceil(96.0f / this.colCnt);
        float x = this.fontPadX;
        float y = ((this.cellHeight - 1) - this.fontDescent) - this.fontPadY;
        for (char c2 = ' '; c2 <= '~'; c2 = (char) (c2 + 1)) {
            s[0] = c2;
            canvas.drawText(s, 0, 1, x, y, paint);
            x += this.cellWidth;
            if ((this.cellWidth + x) - this.fontPadX > this.textureSize) {
                x = this.fontPadX;
                y += this.cellHeight;
            }
        }
        s[0] = ' ';
        canvas.drawText(s, 0, 1, x, y, paint);
        int[] textureIds = new int[1];
        this.f125gl.glGenTextures(1, textureIds, 0);
        this.textureId = textureIds[0];
        this.f125gl.glBindTexture(3553, this.textureId);
        this.f125gl.glTexParameterf(3553, 10241, 9728.0f);
        this.f125gl.glTexParameterf(3553, 10240, 9729.0f);
        this.f125gl.glTexParameterf(3553, 10242, 33071.0f);
        this.f125gl.glTexParameterf(3553, 10243, 33071.0f);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        this.f125gl.glBindTexture(3553, 0);
        bitmap.recycle();
        float x2 = 0.0f;
        float y2 = 0.0f;
        for (int c3 = 0; c3 < 96; c3++) {
            this.charRgn[c3] = new TextureRegion(this.textureSize, this.textureSize, x2, y2, this.cellWidth - 1, this.cellHeight - 1);
            x2 += this.cellWidth;
            if (this.cellWidth + x2 > this.textureSize) {
                x2 = 0.0f;
                y2 += this.cellHeight;
            }
        }
        this.textureRgn = new TextureRegion(this.textureSize, this.textureSize, 0.0f, 0.0f, this.textureSize, this.textureSize);
        return true;
    }

    public void begin() {
        begin(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void begin(float alpha) {
        begin(1.0f, 1.0f, 1.0f, alpha);
    }

    public void begin(float red, float green, float blue, float alpha) {
        this.f125gl.glColor4f(red, green, blue, alpha);
        this.f125gl.glBindTexture(3553, this.textureId);
        this.batch.beginBatch();
    }

    public void end() {
        this.batch.endBatch();
        this.f125gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void draw(String text, float x, float y) {
        float chrHeight = this.cellHeight * this.scaleY;
        float chrWidth = this.cellWidth * this.scaleX;
        int len = text.length();
        float x2 = x + ((chrWidth / 2.0f) - (this.fontPadX * this.scaleX));
        float y2 = y + ((chrHeight / 2.0f) - (this.fontPadY * this.scaleY));
        for (int i = 0; i < len; i++) {
            int c = text.charAt(i) - ' ';
            if (c < 0 || c >= 96) {
                c = 95;
            }
            this.batch.drawSprite(x2, y2, chrWidth, chrHeight, this.charRgn[c]);
            x2 += (this.charWidths[c] + this.spaceX) * this.scaleX;
        }
    }

    public float drawC(String text, float x, float y) {
        float len = getLength(text);
        draw(text, x - (len / 2.0f), y - (getCharHeight() / 2.0f));
        return len;
    }

    public float drawCX(String text, float x, float y) {
        float len = getLength(text);
        draw(text, x - (len / 2.0f), y);
        return len;
    }

    public void drawCY(String text, float x, float y) {
        draw(text, x, y - (getCharHeight() / 2.0f));
    }

    public void setScale(float scale) {
        this.scaleY = scale;
        this.scaleX = scale;
    }

    public void setScale(float sx, float sy) {
        this.scaleX = sx;
        this.scaleY = sy;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void setSpace(float space) {
        this.spaceX = space;
    }

    public float getSpace() {
        return this.spaceX;
    }

    public float getLength(String text) {
        float len = 0.0f;
        int strLen = text.length();
        for (int i = 0; i < strLen; i++) {
            int c = text.charAt(i) - ' ';
            len += this.charWidths[c] * this.scaleX;
        }
        return len + (strLen > 1 ? (strLen - 1) * this.spaceX * this.scaleX : 0.0f);
    }

    public float getCharWidth(char chr) {
        int c = chr - ' ';
        return this.charWidths[c] * this.scaleX;
    }

    public float getCharWidthMax() {
        return this.charWidthMax * this.scaleX;
    }

    public float getCharHeight() {
        return this.charHeight * this.scaleY;
    }

    public float getAscent() {
        return this.fontAscent * this.scaleY;
    }

    public float getDescent() {
        return this.fontDescent * this.scaleY;
    }

    public float getHeight() {
        return this.fontHeight * this.scaleY;
    }

    public void drawTexture(int width, int height) {
        this.batch.beginBatch(this.textureId);
        this.batch.drawSprite(this.textureSize / 2f, height - (this.textureSize / 2f), this.textureSize, this.textureSize, this.textureRgn);
        this.batch.endBatch();
    }
}
