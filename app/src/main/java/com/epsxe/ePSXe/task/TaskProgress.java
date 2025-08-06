package com.epsxe.ePSXe.task;

/* loaded from: classes.dex */
public class TaskProgress {
    final String message;
    final int percentage;

    TaskProgress(int percentage, String message) {
        this.percentage = percentage;
        this.message = message;
    }
}
