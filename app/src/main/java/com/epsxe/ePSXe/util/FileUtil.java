package com.epsxe.ePSXe.util;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class FileUtil {
    public static boolean copyFile(File source, File dest) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream(source));
            try {
                BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(dest, false));
                try {
                    byte[] buf = new byte[1024];
                    bis2.read(buf);
                    do {
                        bos2.write(buf);
                    } while (bis2.read(buf) != -1);
                    if (bis2 != null) {
                        try {
                            bis2.close();
                        } catch (IOException e) {
                            return false;
                        }
                    }
                    if (bos2 != null) {
                        bos2.close();
                    }
                    return true;
                } catch (IOException e2) {
                    bos = bos2;
                    bis = bis2;
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e3) {
                            return false;
                        }
                    }
                    if (bos == null) {
                        return false;
                    }
                    bos.close();
                    return false;
                } catch (Throwable th) {
                    th = th;
                    bos = bos2;
                    bis = bis2;
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e4) {
                            return false;
                        }
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    throw th;
                }
            } catch (IOException e5) {
                bis = bis2;
            } catch (Throwable th2) {
                Throwable th = th2;
                bis = bis2;
            }
        } catch (IOException e6) {
        } catch (Throwable th3) {
            Throwable th = th3;
        }

        return true;
    }

    public static boolean isFileBios(String path) {
        File f = new File(path);
        return f.exists() && f.length() == 524288;
    }

    public static byte[] createChecksum(String filename) throws Exception {
        int numRead;
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }

    public static String getMD5fromFile(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";
        for (byte b2 : b) {
            result = result + Integer.toString((b2 & 255) + 256, 16).substring(1);
        }
        return result;
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        int numRead;
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > 2147483647L) {
        }
        byte[] bytes = new byte[(int) length];
        byte[] res = new byte[24576];
        int offset = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        int offres = 0;
        for (int ind = 0; ind < ((int) length) / 3; ind++) {
            res[offres] = (byte) (((bytes[(ind * 3) + 2] & 248) >> 3) | ((bytes[(ind * 3) + 1] & 28) << 3));
            res[offres + 1] = (byte) (((bytes[(ind * 3) + 1] & 224) >> 5) | (bytes[ind * 3] & 248));
            offres += 2;
        }
        return res;
    }

    public static void writeStringToFile(String name, String data) {
        new File(name);
        try {
            FileOutputStream s = new FileOutputStream(name);
            s.write(data.getBytes());
            s.flush();
            s.close();
        } catch (IOException e) {
            Log.e("epsxe", "IOException");
        }
    }

    public static String readFileToString(String name) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(name));
            if (inputStream == null) {
                return "";
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String receiveString = bufferedReader.readLine();
                if (receiveString != null) {
                    stringBuilder.append(receiveString + "\n");
                } else {
                    inputStream.close();
                    String ret = stringBuilder.toString();
                    return ret;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("epsxe", "File not found: " + e.toString());
            return "";
        } catch (IOException e2) {
            Log.e("epsxe", "Can not read file: " + e2.toString());
            return "";
        }
    }

    public static String md5(String md5) {
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

    public static String md5FromList(List<File> dirs) {
        ArrayList<String> fls = new ArrayList<>();
        for (File f : dirs) {
            new ArrayList();
            new ArrayList();
            File[] lfiles = f.listFiles();
            try {
                for (File ff : lfiles) {
                    if (isPSX(ff.getName())) {
                        fls.add(ff.getName());
                    }
                }
            } catch (Exception e) {
            }
        }
        Collections.sort(fls);
        String name = "";
        for (int i = 0; i < fls.size(); i++) {
            name = md5(name + fls.get(i));
        }
        return name;
    }

    public static boolean isPSX(String name) {
        return name.toLowerCase().endsWith(".cue") || name.toLowerCase().endsWith(".ccd") || name.toLowerCase().endsWith(".bin") || name.toLowerCase().endsWith(".img") || name.toLowerCase().endsWith(".iso") || name.toLowerCase().endsWith(".mds") || name.toLowerCase().endsWith(".mdf") || name.toLowerCase().endsWith(".cdi") || name.toLowerCase().endsWith(".nrg") || name.toLowerCase().endsWith(".pbp") || name.toLowerCase().endsWith(".ecm");
    }

    public static boolean acceptPSX(String name) {
        return name.toLowerCase().endsWith(".cue") || name.toLowerCase().endsWith(".ccd") || name.toLowerCase().endsWith(".bin") || name.toLowerCase().endsWith(".img") || name.toLowerCase().endsWith(".iso") || name.toLowerCase().endsWith(".mds") || name.toLowerCase().endsWith(".mdf") || name.toLowerCase().endsWith(".cdi") || name.toLowerCase().endsWith(".nrg") || name.toLowerCase().endsWith(".pbp") || name.toLowerCase().endsWith(".ecm") || name.toLowerCase().endsWith(".7z") || name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".rar") || name.toLowerCase().endsWith(".exe");
    }

    public static boolean compressed7z(String name) {
        return name.toLowerCase().endsWith(".7z") || name.toLowerCase().endsWith(".zip");
    }

    public static boolean compressed(String name) {
        return name.toLowerCase().endsWith(".7z") || name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".rar");
    }

    public static boolean isifile(String name) {
        return name.toLowerCase().endsWith(".cue") || name.toLowerCase().endsWith(".ccd") || name.toLowerCase().endsWith(".mds") || name.toLowerCase().endsWith(".cdi") || name.toLowerCase().endsWith(".nrg") || name.toLowerCase().endsWith(".pbp");
    }

    public static boolean isPBP(String name) {
        return name.toLowerCase().endsWith(".pbp");
    }

    public static boolean isIndex(String name) {
        return name.toLowerCase().endsWith(".ccd") || name.toLowerCase().endsWith(".mds") || name.toLowerCase().endsWith(".cue");
    }

    public static boolean isFullRom(String name) {
        return name.toLowerCase().endsWith(".cdi") || name.toLowerCase().endsWith(".nrg");
    }

    public static boolean isRom(String name) {
        return name.toLowerCase().endsWith(".mdf") || name.toLowerCase().endsWith(".bin") || name.toLowerCase().endsWith(".img") || name.toLowerCase().endsWith(".iso") || name.toLowerCase().endsWith(".ecm");
    }

    public static boolean isMemcard(String name) {
        return name.toLowerCase().endsWith(".mcr") || name.toLowerCase().endsWith(".mem") || name.toLowerCase().endsWith(".mcd") || name.toLowerCase().endsWith(".gme") || name.toLowerCase().endsWith(".srm");
    }

    public static boolean isCUE(String name) {
        return name.toLowerCase().endsWith(".cue");
    }
}
