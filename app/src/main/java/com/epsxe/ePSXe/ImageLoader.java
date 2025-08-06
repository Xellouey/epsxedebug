package com.epsxe.ePSXe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class ImageLoader {
    FileCache fileCache;
    MemoryCache memoryCache = new MemoryCache();
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap());
    final int stub_id = R.drawable.no_image;
    ExecutorService executorService = Executors.newFixedThreadPool(5);

    public ImageLoader(Context context) {
        this.fileCache = new FileCache(context);
    }

    public void DisplayImage(String url, ImageView imageView) {
        this.imageViews.put(imageView, url);
        Bitmap bitmap = this.memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            queuePhoto(url, imageView);
            imageView.setImageResource(R.drawable.no_image);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        this.executorService.submit(new PhotosLoader(p));
    }

    public String md5(String md5) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 255) | 256).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        try {
            byte[] bytes = new byte[1024];
            while (true) {
                int count = is.read(bytes, 0, 1024);
                if (count != -1) {
                    os.write(bytes, 0, count);
                } else {
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap getBitmap(String code) {
        String hash = md5("ePSXe_Cover_128x128_" + code);
        File f = this.fileCache.getFile(code, hash);
        Bitmap b = decodeFile(f);
        if (b == null) {
            try {
                String url = "http://epsxe.com/files/androidcovers212/" + hash.substring(0, 1) + "/" + hash.substring(1, 2) + "/" + hash + ".jpg";
                URL imageUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                CopyStream(is, os);
                os.close();
                Bitmap bitmap = decodeFile(f);
                return bitmap;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return b;
    }

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private class PhotoToLoad {
        public ImageView imageView;
        public String url;

        public PhotoToLoad(String u, ImageView i) {
            this.url = u;
            this.imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!ImageLoader.this.imageViewReused(this.photoToLoad)) {
                Bitmap bmp = ImageLoader.this.getBitmap(this.photoToLoad.url);
                ImageLoader.this.memoryCache.put(this.photoToLoad.url, bmp);
                if (!ImageLoader.this.imageViewReused(this.photoToLoad)) {
                    BitmapDisplayer bd = ImageLoader.this.new BitmapDisplayer(bmp, this.photoToLoad);
                    Activity a = (Activity) this.photoToLoad.imageView.getContext();
                    a.runOnUiThread(bd);
                }
            }
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = this.imageViews.get(photoToLoad.imageView);
        return tag == null || !tag.equals(photoToLoad.url);
    }

    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            this.bitmap = b;
            this.photoToLoad = p;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!ImageLoader.this.imageViewReused(this.photoToLoad)) {
                if (this.bitmap != null) {
                    this.photoToLoad.imageView.setImageBitmap(this.bitmap);
                } else {
                    this.photoToLoad.imageView.setImageResource(R.drawable.no_image);
                }
            }
        }
    }

    public void clearCache() {
        this.memoryCache.clear();
    }
}
