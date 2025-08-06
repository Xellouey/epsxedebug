package com.bda.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import com.bda.controller.IControllerListener;
import com.bda.controller.IControllerMonitor;
import com.bda.controller.IControllerService;

/* loaded from: classes.dex */
public final class Controller {
    public static final int ACTION_CONNECTED = 1;
    public static final int ACTION_CONNECTING = 2;
    public static final int ACTION_DISCONNECTED = 0;
    public static final int ACTION_DOWN = 0;
    public static final int ACTION_FALSE = 0;
    public static final int ACTION_TRUE = 1;
    public static final int ACTION_UP = 1;
    public static final int ACTION_VERSION_MOGA = 0;
    public static final int ACTION_VERSION_MOGAPRO = 1;
    public static final int AXIS_LTRIGGER = 17;
    public static final int AXIS_RTRIGGER = 18;
    public static final int AXIS_RZ = 14;
    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;
    public static final int AXIS_Z = 11;
    static final int CONTROLLER_ID = 1;
    public static final int INFO_ACTIVE_DEVICE_COUNT = 2;
    public static final int INFO_KNOWN_DEVICE_COUNT = 1;
    public static final int INFO_UNKNOWN = 0;
    public static final int KEYCODE_BUTTON_A = 96;
    public static final int KEYCODE_BUTTON_B = 97;
    public static final int KEYCODE_BUTTON_L1 = 102;
    public static final int KEYCODE_BUTTON_L2 = 104;
    public static final int KEYCODE_BUTTON_R1 = 103;
    public static final int KEYCODE_BUTTON_R2 = 105;
    public static final int KEYCODE_BUTTON_SELECT = 109;
    public static final int KEYCODE_BUTTON_START = 108;
    public static final int KEYCODE_BUTTON_THUMBL = 106;
    public static final int KEYCODE_BUTTON_THUMBR = 107;
    public static final int KEYCODE_BUTTON_X = 99;
    public static final int KEYCODE_BUTTON_Y = 100;
    public static final int KEYCODE_DPAD_DOWN = 20;
    public static final int KEYCODE_DPAD_LEFT = 21;
    public static final int KEYCODE_DPAD_RIGHT = 22;
    public static final int KEYCODE_DPAD_UP = 19;
    public static final int KEYCODE_UNKNOWN = 0;
    static final int LEGACY_KEYCODE_BUTTON_X = 98;
    static final int LEGACY_KEYCODE_BUTTON_Y = 99;
    public static final int STATE_CONNECTION = 1;
    public static final int STATE_CURRENT_PRODUCT_VERSION = 4;
    public static final int STATE_POWER_LOW = 2;

    @Deprecated
    public static final int STATE_SELECTED_VERSION = 4;
    public static final int STATE_SUPPORTED_PRODUCT_VERSION = 3;

    @Deprecated
    public static final int STATE_SUPPORTED_VERSION = 3;
    public static final int STATE_UNKNOWN = 0;
    final Context mContext;
    boolean mIsBound = false;
    IControllerService mService = null;
    final IControllerListener.Stub mListenerStub = new IControllerListenerStub();
    final IControllerMonitor.Stub mMonitorStub = new IControllerMonitorStub();
    final ServiceConnection mServiceConnection = new ServiceConnection();
    int mActivityEvent = 6;
    Handler mHandler = null;
    ControllerListener mListener = null;
    ControllerMonitor mMonitor = null;

    public static final Controller getInstance(Context context) {
        return new Controller(context);
    }

    Controller(Context context) {
        this.mContext = context;
    }

    public final void exit() {
        setListener(null, null);
        setMonitor(null);
        if (this.mIsBound) {
            this.mContext.unbindService(this.mServiceConnection);
            this.mIsBound = false;
        }
    }

    public final float getAxisValue(int axis) {
        if (this.mService != null) {
            try {
                return this.mService.getAxisValue(1, axis);
            } catch (RemoteException e) {
            }
        }
        return 0.0f;
    }

    public final int getInfo(int info) {
        if (this.mService != null) {
            try {
                return this.mService.getInfo(info);
            } catch (RemoteException e) {
            }
        }
        return 0;
    }

    public final int getKeyCode(int keyCode) {
        if (this.mService == null) {
            return 1;
        }
        try {
            return this.mService.getKeyCode2(1, keyCode);
        } catch (RemoteException e) {
            switch (keyCode) {
                case 99:
                    keyCode = 98;
                    break;
                case 100:
                    keyCode = 99;
                    break;
            }
            try {
                return this.mService.getKeyCode(1, keyCode);
            } catch (RemoteException e2) {
                return 1;
            }
        }
    }

    public final int getState(int state) {
        if (this.mService != null) {
            try {
                return this.mService.getState(1, state);
            } catch (RemoteException e) {
            }
        }
        return 0;
    }

    public final boolean init() {
        if (!this.mIsBound) {
            Intent intent = new Intent(IControllerService.class.getName());
            this.mContext.startService(intent);
            this.mContext.bindService(intent, this.mServiceConnection, 1);
            this.mIsBound = true;
        }
        return this.mIsBound;
    }

    public final void onPause() {
        this.mActivityEvent = 6;
        sendMessage(1, this.mActivityEvent);
        registerListener();
    }

    public final void onResume() {
        this.mActivityEvent = 5;
        sendMessage(1, this.mActivityEvent);
        registerListener();
    }

    void registerListener() {
        if (this.mListener != null && this.mService != null) {
            try {
                this.mService.registerListener2(this.mListenerStub, this.mActivityEvent);
            } catch (RemoteException e) {
                try {
                    this.mService.registerListener(this.mListenerStub, this.mActivityEvent);
                } catch (RemoteException e2) {
                }
            }
        }
    }

    void registerMonitor() {
        if (this.mMonitor != null && this.mService != null) {
            try {
                this.mService.registerMonitor(this.mMonitorStub, this.mActivityEvent);
            } catch (RemoteException e) {
            }
        }
    }

    void sendMessage(int msg, int param) {
        if (this.mService != null) {
            try {
                this.mService.sendMessage(msg, param);
            } catch (RemoteException e) {
            }
        }
    }

    public final void setListener(ControllerListener listener, Handler handler) {
        unregisterListener();
        this.mListener = listener;
        this.mHandler = handler;
        registerListener();
    }

    public final void setMonitor(ControllerMonitor monitor) {
        unregisterMonitor();
        this.mMonitor = monitor;
        registerMonitor();
    }

    void unregisterListener() {
        if (this.mService != null) {
            try {
                this.mService.unregisterListener(this.mListenerStub, this.mActivityEvent);
            } catch (RemoteException e) {
            }
        }
    }

    void unregisterMonitor() {
        if (this.mService != null) {
            try {
                this.mService.unregisterMonitor(this.mMonitorStub, this.mActivityEvent);
            } catch (RemoteException e) {
            }
        }
    }

    public void allowNewConnections() {
        if (this.mService != null) {
            try {
                this.mService.allowNewConnections();
            } catch (RemoteException e) {
            }
        }
    }

    public void disallowNewConnections() {
        if (this.mService != null) {
            try {
                this.mService.disallowNewConnections();
            } catch (RemoteException e) {
            }
        }
    }

    public void isAllowingNewConnections() {
        if (this.mService != null) {
            try {
                this.mService.isAllowingNewConnections();
            } catch (RemoteException e) {
            }
        }
    }

    class IControllerListenerStub extends IControllerListener.Stub {
        IControllerListenerStub() {
        }

        @Override // com.bda.controller.IControllerListener
        public void onKeyEvent(KeyEvent event) throws RemoteException {
            if (event.getControllerId() == 1 && Controller.this.mListener != null) {
                KeyRunnable runnable = Controller.this.new KeyRunnable(event);
                if (Controller.this.mHandler != null) {
                    Controller.this.mHandler.post(runnable);
                } else {
                    runnable.run();
                }
            }
        }

        @Override // com.bda.controller.IControllerListener
        public void onMotionEvent(MotionEvent event) throws RemoteException {
            if (event.getControllerId() == 1 && Controller.this.mListener != null) {
                MotionRunnable runnable = Controller.this.new MotionRunnable(event);
                if (Controller.this.mHandler != null) {
                    Controller.this.mHandler.post(runnable);
                } else {
                    runnable.run();
                }
            }
        }

        @Override // com.bda.controller.IControllerListener
        public void onStateEvent(StateEvent event) throws RemoteException {
            if (event.getControllerId() == 1 && Controller.this.mListener != null) {
                StateRunnable runnable = Controller.this.new StateRunnable(event);
                if (Controller.this.mHandler != null) {
                    Controller.this.mHandler.post(runnable);
                } else {
                    runnable.run();
                }
            }
        }
    }

    class IControllerMonitorStub extends IControllerMonitor.Stub {
        IControllerMonitorStub() {
        }

        @Override // com.bda.controller.IControllerMonitor
        public void onLog(int level, int id, String message) throws RemoteException {
            if (Controller.this.mMonitor != null) {
                Controller.this.mMonitor.onLog(level, id, message);
            }
        }
    }

    class KeyRunnable implements Runnable {
        final KeyEvent mEvent;

        public KeyRunnable(KeyEvent event) {
            this.mEvent = event;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Controller.this.mListener != null) {
                Controller.this.mListener.onKeyEvent(this.mEvent);
            }
        }
    }

    class MotionRunnable implements Runnable {
        final MotionEvent mEvent;

        public MotionRunnable(MotionEvent event) {
            this.mEvent = event;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Controller.this.mListener != null) {
                Controller.this.mListener.onMotionEvent(this.mEvent);
            }
        }
    }

    class ServiceConnection implements android.content.ServiceConnection {
        ServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName className, IBinder service) {
            Controller.this.mService = IControllerService.Stub.asInterface(service);
            Controller.this.registerListener();
            Controller.this.registerMonitor();
            if (Controller.this.mActivityEvent == 5) {
                Controller.this.sendMessage(1, 5);
                Controller.this.sendMessage(1, 7);
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName className) {
            Controller.this.mService = null;
        }
    }

    class StateRunnable implements Runnable {
        final StateEvent mEvent;

        public StateRunnable(StateEvent event) {
            this.mEvent = event;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Controller.this.mListener != null) {
                Controller.this.mListener.onStateEvent(this.mEvent);
            }
        }
    }
}
