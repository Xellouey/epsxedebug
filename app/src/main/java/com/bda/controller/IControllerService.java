package com.bda.controller;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.bda.controller.IControllerListener;
import com.bda.controller.IControllerMonitor;

/* loaded from: classes.dex */
public interface IControllerService extends IInterface {
    void allowNewConnections() throws RemoteException;

    void disallowNewConnections() throws RemoteException;

    float getAxisValue(int i, int i2) throws RemoteException;

    int getInfo(int i) throws RemoteException;

    int getKeyCode(int i, int i2) throws RemoteException;

    int getKeyCode2(int i, int i2) throws RemoteException;

    int getState(int i, int i2) throws RemoteException;

    boolean isAllowingNewConnections() throws RemoteException;

    void registerListener(IControllerListener iControllerListener, int i) throws RemoteException;

    void registerListener2(IControllerListener iControllerListener, int i) throws RemoteException;

    void registerMonitor(IControllerMonitor iControllerMonitor, int i) throws RemoteException;

    void sendMessage(int i, int i2) throws RemoteException;

    void unregisterListener(IControllerListener iControllerListener, int i) throws RemoteException;

    void unregisterMonitor(IControllerMonitor iControllerMonitor, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IControllerService {
        private static final String DESCRIPTOR = "com.bda.controller.IControllerService";
        static final int TRANSACTION_allowNewConnections = 12;
        static final int TRANSACTION_disallowNewConnections = 13;
        static final int TRANSACTION_getAxisValue = 7;
        static final int TRANSACTION_getInfo = 5;
        static final int TRANSACTION_getKeyCode = 6;
        static final int TRANSACTION_getKeyCode2 = 11;
        static final int TRANSACTION_getState = 8;
        static final int TRANSACTION_isAllowingNewConnections = 14;
        static final int TRANSACTION_registerListener = 1;
        static final int TRANSACTION_registerListener2 = 10;
        static final int TRANSACTION_registerMonitor = 3;
        static final int TRANSACTION_sendMessage = 9;
        static final int TRANSACTION_unregisterListener = 2;
        static final int TRANSACTION_unregisterMonitor = 4;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IControllerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IControllerService)) {
                return (IControllerService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IControllerListener _arg0 = IControllerListener.Stub.asInterface(data.readStrongBinder());
                    int _arg1 = data.readInt();
                    registerListener(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IControllerListener _arg02 = IControllerListener.Stub.asInterface(data.readStrongBinder());
                    int _arg12 = data.readInt();
                    unregisterListener(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IControllerMonitor _arg03 = IControllerMonitor.Stub.asInterface(data.readStrongBinder());
                    int _arg13 = data.readInt();
                    registerMonitor(_arg03, _arg13);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IControllerMonitor _arg04 = IControllerMonitor.Stub.asInterface(data.readStrongBinder());
                    int _arg14 = data.readInt();
                    unregisterMonitor(_arg04, _arg14);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    int _result = getInfo(_arg05);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    int _arg15 = data.readInt();
                    int _result2 = getKeyCode(_arg06, _arg15);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    int _arg16 = data.readInt();
                    float _result3 = getAxisValue(_arg07, _arg16);
                    reply.writeNoException();
                    reply.writeFloat(_result3);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg17 = data.readInt();
                    int _result4 = getState(_arg08, _arg17);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    int _arg18 = data.readInt();
                    sendMessage(_arg09, _arg18);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IControllerListener _arg010 = IControllerListener.Stub.asInterface(data.readStrongBinder());
                    int _arg19 = data.readInt();
                    registerListener2(_arg010, _arg19);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    int _arg110 = data.readInt();
                    int _result5 = getKeyCode2(_arg011, _arg110);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    allowNewConnections();
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    disallowNewConnections();
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result6 = isAllowingNewConnections();
                    reply.writeNoException();
                    reply.writeInt(_result6 ? 1 : 0);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IControllerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.bda.controller.IControllerService
            public void registerListener(IControllerListener callback, int param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(param);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public void unregisterListener(IControllerListener callback, int param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(param);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public void registerMonitor(IControllerMonitor callback, int param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(param);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public void unregisterMonitor(IControllerMonitor callback, int param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(param);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public int getInfo(int info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(info);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public int getKeyCode(int id, int keyCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(keyCode);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public float getAxisValue(int id, int axis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(axis);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public int getState(int id, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(state);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public void sendMessage(int msg, int param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(msg);
                    _data.writeInt(param);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public void registerListener2(IControllerListener callback, int param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(param);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public int getKeyCode2(int id, int keyCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(keyCode);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public void allowNewConnections() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public void disallowNewConnections() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.bda.controller.IControllerService
            public boolean isAllowingNewConnections() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
