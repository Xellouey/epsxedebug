package com.bda.controller;

/* loaded from: classes.dex */
public final class Constants {
    public static final int MSG_SET_ACTIVITY_EVENT = 1;

    private Constants() {
    }

    public static final class ActivityEvent {
        public static final int CREATE = 1;
        public static final int DESTROY = 2;
        public static final int PAUSE = 6;
        public static final int RESUME = 5;
        public static final int SERVICE_CONNECTED = 7;
        public static final int START = 3;
        public static final int STOP = 4;

        private ActivityEvent() {
        }
    }
}
