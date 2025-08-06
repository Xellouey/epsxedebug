package com.epsxe.ePSXe.input;

import android.util.Log;
import com.bda.controller.Controller;
import com.bda.controller.ControllerListener;
import com.bda.controller.KeyEvent;
import com.bda.controller.MotionEvent;
import com.bda.controller.StateEvent;
import com.epsxe.ePSXe.GamepadMoga;
import com.epsxe.ePSXe.ePSXe;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.util.MathUtil;

/* loaded from: classes.dex */
public class mogaDriver {

    /* renamed from: a */
    ePSXe f185a;

    /* renamed from: e */
    libepsxe f186e;
    Controller mController = null;
    final MogaControllerListener mListener = new MogaControllerListener();
    int mogapad;

    public mogaDriver(ePSXe epsxeact, libepsxe epsxelib, int pad) {
        this.mogapad = -1;
        this.f186e = epsxelib;
        this.f185a = epsxeact;
        this.mogapad = pad;
        mogaStart();
    }

    class MogaControllerListener implements ControllerListener {
        MogaControllerListener() {
        }

        @Override // com.bda.controller.ControllerListener
        public void onKeyEvent(KeyEvent event) {
            if (mogaDriver.this.mogapad != -1) {
                int npad = mogaDriver.this.mogapad;
                int action = event.getAction();
                switch (event.getKeyCode()) {
                    case 19:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 4096);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 4096);
                            break;
                        }
                    case 20:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 16384);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 16384);
                            break;
                        }
                    case 21:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 32768);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 32768);
                            break;
                        }
                    case 22:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 8192);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 8192);
                            break;
                        }
                    case 96:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 64);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 64);
                            break;
                        }
                    case 97:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 32);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 32);
                            break;
                        }
                    case 99:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 128);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 128);
                            break;
                        }
                    case 100:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 16);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 16);
                            break;
                        }
                    case 102:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 4);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 4);
                            break;
                        }
                    case 103:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 8);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 8);
                            break;
                        }
                    case 104:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 1);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 1);
                            break;
                        }
                    case 105:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 2);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 2);
                            break;
                        }
                    case 106:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 512);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 512);
                            break;
                        }
                    case 107:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 1024);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 1024);
                            break;
                        }
                    case 108:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 2048);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 2048);
                            break;
                        }
                    case 109:
                        if (action != 0) {
                            mogaDriver.this.f185a.unsetPushedButton(npad, 256);
                            break;
                        } else {
                            mogaDriver.this.f185a.setPushedButton(npad, 256);
                            break;
                        }
                }
            }
        }

        @Override // com.bda.controller.ControllerListener
        public void onMotionEvent(MotionEvent event) {
            if (mogaDriver.this.mogapad != -1) {
                int x1 = (int) (event.getAxisValue(0) * 128.0f);
                int y1 = (int) (event.getAxisValue(1) * 128.0f);
                int x2 = (int) (event.getAxisValue(11) * 128.0f);
                int y2 = (int) (event.getAxisValue(14) * 128.0f);
                float ratio1 = MathUtil.GetRatio(x1, y1);
                float ratio2 = MathUtil.GetRatio(x2, y2);
                if (x1 != 0 || y1 != 0) {
                    int tmpX = (int) (x1 * ratio1 * 1.15d);
                    int tmpY = (int) (y1 * ratio1 * 1.15d);
                    if (tmpX < -127) {
                        tmpX = -127;
                    } else if (tmpX > 127) {
                        tmpX = 127;
                    }
                    if (tmpY < -127) {
                        tmpY = -127;
                    } else if (tmpY > 127) {
                        tmpY = 127;
                    }
                    x1 = tmpX;
                    y1 = tmpY;
                }
                if (x2 != 0 || y2 != 0) {
                    int tmpX2 = (int) (x2 * ratio2 * 1.15d);
                    int tmpY2 = (int) (y2 * ratio2 * 1.15d);
                    if (tmpX2 < -127) {
                        tmpX2 = -127;
                    } else if (tmpX2 > 127) {
                        tmpX2 = 127;
                    }
                    if (tmpY2 < -127) {
                        tmpY2 = -127;
                    } else if (tmpY2 > 127) {
                        tmpY2 = 127;
                    }
                    x2 = tmpX2;
                    y2 = tmpY2;
                }
                if (mogaDriver.this.f186e != null) {
                    mogaDriver.this.f186e.setpadanalogMoga(mogaDriver.this.mogapad, 0, x1, y1);
                    mogaDriver.this.f186e.setpadanalogMoga(mogaDriver.this.mogapad, 1, x2, y2);
                }
            }
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:6:0x000c. Please report as an issue. */
        @Override // com.bda.controller.ControllerListener
        public void onStateEvent(StateEvent event) {
            if (mogaDriver.this.mogapad == -1) {
                return;
            }
            switch (event.getState()) {
            }
        }
    }

    private void mogaStart() {
        this.mController = Controller.getInstance(this.f185a);
        if (this.mController != null) {
            Log.e("moga", "mogaStart");
            GamepadMoga.init(this.mController, this.f185a);
            this.mController.setListener(this.mListener, null);
            return;
        }
        this.mogapad = -1;
    }

    public void mogaStop() {
        if (this.mController != null) {
            this.mController.exit();
        }
    }

    public void onPause() {
        if (this.mController != null) {
            this.mController.onPause();
        }
    }

    public void onResume() {
        if (this.mController != null) {
            this.mController.onResume();
        }
    }
}
