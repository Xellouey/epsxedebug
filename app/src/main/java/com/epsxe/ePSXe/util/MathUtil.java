package com.epsxe.ePSXe.util;

/* loaded from: classes.dex */
public final class MathUtil {
    public static float GetRadiusf(float x, float y) {
        return (float) Math.sqrt((x * x) + (y * y));
    }

    public static float GetRatio(int x, int y) {
        int A1;
        int B1;
        int C1;
        int A2;
        int B2;
        int C2;
        if (x >= 0 && y >= 0) {
            A1 = 0 - y;
            B1 = x + 0;
            C1 = (A1 * x) + (B1 * y);
            if (Math.abs(y) >= Math.abs(x)) {
                A2 = 0;
                B2 = 128;
                C2 = 0 + 16384;
            } else {
                A2 = -128;
                B2 = 0;
                C2 = (-16384) + 0;
            }
        } else if (x <= 0 && y >= 0) {
            A1 = 0 - y;
            B1 = x + 0;
            C1 = (A1 * x) + (B1 * y);
            if (Math.abs(y) >= Math.abs(x)) {
                A2 = 0;
                B2 = 128;
                C2 = 0 + 16384;
            } else {
                A2 = -128;
                B2 = 0;
                C2 = 16384 + 0;
            }
        } else if (x <= 0 && y <= 0) {
            A1 = 0 - y;
            B1 = x + 0;
            C1 = (A1 * x) + (B1 * y);
            if (Math.abs(y) >= Math.abs(x)) {
                A2 = 0;
                B2 = -128;
                C2 = 0 + 16384;
            } else {
                A2 = 128;
                B2 = 0;
                C2 = (-16384) + 0;
            }
        } else {
            A1 = 0 - y;
            B1 = x + 0;
            C1 = (A1 * x) + (B1 * y);
            if (Math.abs(y) >= Math.abs(x)) {
                A2 = 0;
                B2 = -128;
                C2 = 0 + 16384;
            } else {
                A2 = 128;
                B2 = 0;
                C2 = 16384 + 0;
            }
        }
        float det = (A1 * B2) - (A2 * B1);
        float f = ((B2 * C1) - (B1 * C2)) / det;
        float f2 = ((A1 * C2) - (A2 * C1)) / det;
        float rad = GetRadiusf(x, y);
        float ratio = rad / 128.0f;
        return ratio;
    }
}
