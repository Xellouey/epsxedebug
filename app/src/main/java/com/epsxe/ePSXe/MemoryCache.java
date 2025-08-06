package com.epsxe.ePSXe;

import android.graphics.Bitmap;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class MemoryCache {
    private Map<String, SoftReference<Bitmap>> cache = Collections.synchronizedMap(new HashMap());

    public Bitmap get(String id) {
        if (!this.cache.containsKey(id)) {
            return null;
        }
        SoftReference<Bitmap> ref = this.cache.get(id);
        return ref.get();
    }

    public void put(String id, Bitmap bitmap) {
        this.cache.put(id, new SoftReference<>(bitmap));
    }

    public void clear() {
        this.cache.clear();
    }
}
