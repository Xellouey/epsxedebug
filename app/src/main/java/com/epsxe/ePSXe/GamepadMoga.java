package com.epsxe.ePSXe;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import com.bda.controller.Controller;
import com.bda.controller.IControllerService;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class GamepadMoga {
    public static void init(Controller mController, Context ctx) {
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion >= 21) {
            try {
                Class<?> mControllerClass = mController.getClass();
                Field mServiceConnectionF = mControllerClass.getDeclaredField("mServiceConnection");
                mServiceConnectionF.setAccessible(true);
                ServiceConnection mServiceConnection = (ServiceConnection) mServiceConnectionF.get(mController);
                Field mIsBoundF = mControllerClass.getDeclaredField("mIsBound");
                mIsBoundF.setAccessible(true);
                boolean mIsBound = mIsBoundF.getBoolean(mController);
                if (mServiceConnection != null && !mIsBound) {
                    Intent intent = new Intent(IControllerService.class.getName());
                    intent.setPackage("com.bda.pivot.mogapgp");
                    ctx.startService(intent);
                    ctx.bindService(intent, mServiceConnection, 1);
                    try {
                        mIsBoundF.setBoolean(mController, true);
                        return;
                    } catch (Exception e) {
                        return;
                    }
                }
                return;
            } catch (Exception e2) {
                return;
            }
        }
        mController.init();
    }
}
