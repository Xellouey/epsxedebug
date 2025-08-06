package com.epsxe.ePSXe.util;

import android.os.Environment;

import androidx.core.content.ContextCompat;

import com.epsxe.ePSXe.OptionDesc;
import com.epsxe.ePSXe.ePSXeApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/* loaded from: classes.dex */
public final class CacheUtil {
    private static final String CACHEFILE = "epsxe/info/gamelistv2";
    private static final String CACHEMD5FILE = "epsxe/info/gamelistv2.md5";
    private static final String TRACEFILE = "epsxe/info/tracescan.txt";
    private static final String WIPFILE = "epsxe/info/wipscanning";
    private static final String WIPFOLDER = "epsxe/info";
    private static final String WIPNAME = "wipscanning";

    public static void deletewipscan() {
        File f = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), WIPFILE);
        if (f.exists()) {
            f.delete();
        }
    }

    public static void deletetracescan() {
        File f = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), TRACEFILE);
        if (f.exists()) {
            f.delete();
        }
    }

    public static void createwipscan() {
        try {
            File root = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), WIPFOLDER);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, WIPNAME);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append((CharSequence) "scanning for games\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCache() {
        File f = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), CACHEMD5FILE);
        if (f.exists()) {
            f.delete();
        }
        File d = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), CACHEFILE);
        if (d.exists()) {
            d.delete();
        }
    }

    public static void saveCacheList(List<OptionDesc> fls) {
        try {
            File f = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), CACHEFILE);
            FileWriter writer = new FileWriter(f);
            for (int i = 0; i < fls.size(); i++) {
                String ffile = fls.get(i).getFile().replace(";", "_:#:_");
                String fpath = fls.get(i).getPath().replace(";", "_:#:_");
                writer.append((CharSequence) (fls.get(i).getName() + ";" + fls.get(i).getNameJP() + ";" + fls.get(i).getCode() + ";" + fls.get(i).getText() + ";" + ffile + ";" + fls.get(i).getCountry() + ";" + fpath + ";" + fls.get(i).getMultitap() + ";" + fls.get(i).getNumPlayers() + ";" + fls.get(i).getPadType() + ";" + fls.get(i).getSlot() + ";\n"));
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
        }
    }

    public static List<OptionDesc> restoreListformCache(List<OptionDesc> fls) {
        try {
            File f = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), CACHEFILE);
            if (f != null) {
                BufferedReader r = new BufferedReader(new FileReader(f));
                new StringBuilder();
                while (true) {
                    String line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    String[] tokens = line.split(";");
                    String name = tokens[0];
                    String nameJP = tokens[1];
                    String code = tokens[2];
                    String company = tokens[3];
                    String fnametmp = tokens[4];
                    String fname = fnametmp.replace("_:#:_", ";");
                    String country = tokens[5];
                    String pathtmp = tokens[6];
                    String path = pathtmp.replace("_:#:_", ";");
                    String multitap = tokens[7];
                    String numplayers = tokens[8];
                    String padtype = tokens[9];
                    if (padtype.equals("null")) {
                        padtype = null;
                    }
                    int slot = Integer.parseInt(tokens[10]);
                    fls.add(new OptionDesc(name, nameJP, code, company, fname, country, path, multitap, numplayers, padtype, slot, null));
                }
            }
        } catch (Exception e) {
        }
        return fls;
    }

    public static List<OptionDesc> restoreCache(List<OptionDesc> fls, List<File> dirs) {
        try {
            File f = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), CACHEMD5FILE);
            if (f != null) {
                BufferedReader r = new BufferedReader(new FileReader(f));
                new StringBuilder();
                String md5 = r.readLine();
                String nmd5 = FileUtil.md5FromList(dirs);
                if (md5 != null && nmd5 != null && nmd5.equals(md5)) {
                    return restoreListformCache(fls);
                }
                return fls;
            }
            return fls;
        } catch (Exception e) {
            return fls;
        }
    }

    public static void saveCache(List<OptionDesc> fls, List<File> dirs) {
        String md5 = FileUtil.md5FromList(dirs);
        if (md5 != null) {
            try {
                File f = new File(ContextCompat.getDataDir(ePSXeApplication.getApplication()), CACHEMD5FILE);
                FileWriter writer = new FileWriter(f);
                writer.append((CharSequence) md5);
                writer.flush();
                writer.close();
                saveCacheList(fls);
            } catch (Exception e) {
            }
        }
    }
}
