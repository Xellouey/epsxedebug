package com.epsxe.ePSXe.util;

import android.text.TextUtils;

/* loaded from: classes.dex */
public final class StringUtil {
    public static String capitalize(String str) {
        if (!TextUtils.isEmpty(str)) {
            char[] arr = str.toCharArray();
            boolean capitalizeNext = true;
            String phrase = "";
            for (char c : arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase = phrase + Character.toUpperCase(c);
                    capitalizeNext = false;
                } else {
                    if (Character.isWhitespace(c)) {
                        capitalizeNext = true;
                    }
                    phrase = phrase + c;
                }
            }
            return phrase;
        }
        return str;
    }
}
