package com.epsxe.ePSXe;

import androidx.core.internal.view.SupportMenu;

import com.epsxe.ePSXe.jni.libepsxe;

public class AnimateButton {
    public static int handleDpadDynamic(
            int xi, int yi, int[] virtualPadId, int[] dpadsection, int[] virtualPadBit,
            int statebuttons, float[][] padOffScreenLan, float[][] padSizeScreenLan,
            float[][] padScreenResize, int mode, int[] emu_pad_mode, int[] emu_pad_mode_analog,
            int emu_pad_type_selected, int mHeight, libepsxe f165e) {

        if (virtualPadId[0] < 0 || virtualPadId[0] >= virtualPadBit.length) {
            return -1;
        }
        int[] result = new int[]{virtualPadId[0], statebuttons};
        if (emu_pad_mode[emu_pad_type_selected] == 1 ||
                (emu_pad_mode[emu_pad_type_selected] == 4 && emu_pad_mode_analog[emu_pad_type_selected] == 0)) {
            int xdiff = xi - ((int) padOffScreenLan[mode][0]);
            int ydiff = (mHeight - yi) - ((int) padOffScreenLan[mode][1]);
            int seg2 = ((int) Math.toDegrees(Math.atan2(ydiff, xdiff) / 22.5d)) + 8;
            int siz = (int) Math.sqrt((xdiff * xdiff) + (ydiff * ydiff));
            if (siz >= ((int) ((padSizeScreenLan[mode][0] * padScreenResize[mode][0]) / 12.0f)) &&
                    seg2 > 0 && seg2 < 17) {
                int up = virtualPadId[0];
                int ind24 = dpadsection[seg2];
                if ((virtualPadBit[up] & 65536) == 65536) {
                    f165e.setPadDataUp(virtualPadBit[up] & SupportMenu.USER_MASK, 0);
                    result[1] &= (~virtualPadBit[up]) & SupportMenu.USER_MASK;
                } else {
                    f165e.setPadDataUp(0, virtualPadBit[up] & SupportMenu.USER_MASK);
                }
                if ((virtualPadBit[ind24] & 65536) == 65536) {
                    f165e.setPadDataDown(virtualPadBit[ind24] & SupportMenu.USER_MASK, 0);
                    result[1] |= virtualPadBit[ind24] & SupportMenu.USER_MASK;
                } else {
                    f165e.setPadDataDown(0, virtualPadBit[ind24] & SupportMenu.USER_MASK);
                }
                result[0] = ind24;
            }
        }
        return result[0];
    }

    public static int getAnimatedDpadButton(int touchX, int touchY, int[][] virtualPadPos) {
        if (!isInDpadZone(touchX, touchY, virtualPadPos)) {
            return -1;
        }
        for (int i = 12; i <= 15; i++) {
            if (touchX >= virtualPadPos[i][0] && touchX <= virtualPadPos[i][2] &&
                    touchY >= virtualPadPos[i][1] && touchY <= virtualPadPos[i][3]) {
                return i;
            }
        }
        return getNearestButton(touchX, touchY, virtualPadPos);
    }

    public static  boolean isInDpadZone(int x, int y, int[][] virtualPadPos) {
        int left = Integer.MAX_VALUE, top = Integer.MAX_VALUE;
        int right = 0, bottom = 0;

        for (int i = 12; i <= 15; i++) {
            left = Math.min(left, virtualPadPos[i][0]);
            top = Math.min(top, virtualPadPos[i][1]);
            right = Math.max(right, virtualPadPos[i][2]);
            bottom = Math.max(bottom, virtualPadPos[i][3]);
        }
        int padding = (int)((right - left) * 0.2);
        return x >= (left - padding) && x <= (right + padding) &&
                y >= (top - padding) && y <= (bottom + padding);
    }

    public static  int getNearestButton(int touchX, int touchY, int[][] virtualPadPos) {
        final int[] priorityOrder = {12, 14, 15, 13};
        float minDistance = Float.MAX_VALUE;
        int closestButton = -1;

        for (int btn : priorityOrder) {
            int centerX = (virtualPadPos[btn][0] + virtualPadPos[btn][2]) / 2;
            int centerY = (virtualPadPos[btn][1] + virtualPadPos[btn][3]) / 2;
            float distance = (touchX - centerX) * (touchX - centerX) + (touchY - centerY) * (touchY - centerY);

            if (distance < minDistance) {
                minDistance = distance;
                closestButton = btn;
            }
        }

        return closestButton;
    }
}
