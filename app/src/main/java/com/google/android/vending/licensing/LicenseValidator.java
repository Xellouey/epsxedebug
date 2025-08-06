package com.google.android.vending.licensing;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.vending.licensing.util.Base64;
import com.google.android.vending.licensing.util.Base64DecoderException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

/* loaded from: classes.dex */
class LicenseValidator {
    private static final int ERROR_CONTACTING_SERVER = 257;
    private static final int ERROR_INVALID_PACKAGE_NAME = 258;
    private static final int ERROR_NON_MATCHING_UID = 259;
    private static final int ERROR_NOT_MARKET_MANAGED = 3;
    private static final int ERROR_OVER_QUOTA = 5;
    private static final int ERROR_SERVER_FAILURE = 4;
    private static final int LICENSED = 0;
    private static final int LICENSED_OLD_KEY = 2;
    private static final int NOT_LICENSED = 1;
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String TAG = "LicenseValidator";
    private final LicenseCheckerCallback mCallback;
    private final DeviceLimiter mDeviceLimiter;
    private final int mNonce;
    private final String mPackageName;
    private final Policy mPolicy;
    private final String mVersionCode;

    LicenseValidator(Policy policy, DeviceLimiter deviceLimiter, LicenseCheckerCallback callback, int nonce, String packageName, String versionCode) {
        this.mPolicy = policy;
        this.mDeviceLimiter = deviceLimiter;
        this.mCallback = callback;
        this.mNonce = nonce;
        this.mPackageName = packageName;
        this.mVersionCode = versionCode;
    }

    public LicenseCheckerCallback getCallback() {
        return this.mCallback;
    }

    public int getNonce() {
        return this.mNonce;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void verify(PublicKey publicKey, int responseCode, String signedData, String signature) {
        String userId = null;
        ResponseData data = null;
        if (responseCode == 0 || responseCode == 1 || responseCode == 2) {
            try {
                Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
                sig.initVerify(publicKey);
                if (signedData == null) {
                    Log.e(TAG, "Signature verification failed.");
                    handleInvalidResponse();
                } else {
                    sig.update(signedData.getBytes());
                    if (!sig.verify(Base64.decode(signature))) {
                        Log.e(TAG, "Signature verification failed.");
                        handleInvalidResponse();
                    } else {
                        try {
                            data = ResponseData.parse(signedData);
                            if (data.responseCode != responseCode) {
                                Log.e(TAG, "Response codes don't match.");
                                handleInvalidResponse();
                            } else if (data.nonce != this.mNonce) {
                                Log.e(TAG, "Nonce doesn't match.");
                                handleInvalidResponse();
                            } else if (!data.packageName.equals(this.mPackageName)) {
                                Log.e(TAG, "Package name doesn't match.");
                                handleInvalidResponse();
                            } else if (!data.versionCode.equals(this.mVersionCode)) {
                                Log.e(TAG, "Version codes don't match.");
                                handleInvalidResponse();
                            } else {
                                userId = data.userId;
                                if (TextUtils.isEmpty(userId)) {
                                    Log.e(TAG, "User identifier is empty.");
                                    handleInvalidResponse();
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            Log.e(TAG, "Could not parse response.");
                            handleInvalidResponse();
                        }
                    }
                }
            } catch (Base64DecoderException e2) {
                Log.e(TAG, "Could not Base64-decode signature.");
                handleInvalidResponse();
                return;
            } catch (InvalidKeyException e3) {
                handleApplicationError(5);
                return;
            } catch (NoSuchAlgorithmException e4) {
                throw new RuntimeException(e4);
            } catch (SignatureException e5) {
                throw new RuntimeException(e5);
            }
        }
        switch (responseCode) {
            case 0:
            case 2:
                int limiterResponse = this.mDeviceLimiter.isDeviceAllowed(userId);
                handleResponse(limiterResponse, data);
                break;
            case 1:
                handleResponse(Policy.NOT_LICENSED, data);
                break;
            case 3:
                handleApplicationError(3);
                break;
            case 4:
                Log.w(TAG, "An error has occurred on the licensing server.");
                handleResponse(Policy.RETRY, data);
                break;
            case 5:
                Log.w(TAG, "Licensing server is refusing to talk to this device, over quota.");
                handleResponse(Policy.RETRY, data);
                break;
            case 257:
                Log.w(TAG, "Error contacting licensing server.");
                handleResponse(Policy.RETRY, data);
                break;
            case ERROR_INVALID_PACKAGE_NAME /* 258 */:
                handleApplicationError(1);
                break;
            case ERROR_NON_MATCHING_UID /* 259 */:
                handleApplicationError(2);
                break;
            default:
                Log.e(TAG, "Unknown response code for license check.");
                handleInvalidResponse();
                break;
        }
    }

    private void handleResponse(int response, ResponseData rawData) {
        this.mPolicy.processServerResponse(response, rawData);
        if (this.mPolicy.allowAccess()) {
            this.mCallback.allow(response);
        } else {
            this.mCallback.dontAllow(response);
        }
    }

    private void handleApplicationError(int code) {
        this.mCallback.applicationError(code);
    }

    private void handleInvalidResponse() {
        this.mCallback.dontAllow(Policy.NOT_LICENSED);
    }
}
