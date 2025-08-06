package com.epsxe.ePSXe;

import android.content.Context;
import android.util.Log;
import com.epsxe.ePSXe.jni.libgamedetect;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/* loaded from: classes.dex */
public final class GameInfo {

    /* renamed from: c */
    private Context f127c;

    /* renamed from: d */
    private libgamedetect f128d;
    private String ginfo;

    public GameInfo(Context context, libgamedetect detect) {
        this.f128d = detect;
        this.f127c = context;
        initGinfo();
    }

    private void initGinfo() {
        try {
            InputStream is = this.f127c.getResources().openRawResource(R.raw.ginfo);
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringBuilder gb = new StringBuilder();
            while (true) {
                String line = r.readLine();
                if (line != null) {
                    gb.append(line + ";");
                } else {
                    this.ginfo = gb.toString();
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    private String getPadType(int pos) {
        int init = pos;
        for (int i = 0; i < 12; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    private String getNumPlayers(int pos) {
        int init = pos;
        for (int i = 0; i < 11; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    private String getMultitap(int pos) {
        int init = pos;
        for (int i = 0; i < 10; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return end != init ? "yes" : "no";
    }

    private String getDiscNumber(int pos) {
        int init = pos;
        for (int i = 0; i < 4; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    private String getTotalDiscNumber(int pos) {
        int init = pos;
        for (int i = 0; i < 5; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    private String getType(int pos) {
        int init = pos;
        for (int i = 0; i < 7; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    private String getLang(int pos) {
        int init = pos;
        for (int i = 0; i < 9; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    private String getCompany(int pos) {
        int init = pos;
        for (int i = 0; i < 8; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    private String getCountry(int pos) {
        int init = pos;
        for (int i = 0; i < 6; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    private String getName(int pos) {
        int init = this.ginfo.indexOf(59, pos) + 1;
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    private String getNameJP(int pos) {
        int init = pos;
        for (int i = 0; i < 2; i++) {
            init = this.ginfo.indexOf(59, init) + 1;
        }
        int end = this.ginfo.indexOf(59, init);
        return this.ginfo.substring(init, end);
    }

    public List<OptionDesc> addFileDropbox(String mcode, List<OptionDesc> fls) {
        String name;
        String country;
        String nameJP;
        String lang;
        String company;
        String type;
        String multitap;
        String numplayers;
        String padtype;
        Log.e("iso", "" + mcode);
        if (this.ginfo != null) {
            int entry = this.ginfo.indexOf(mcode, 0);
            if (entry != -1) {
                nameJP = getNameJP(this.ginfo.indexOf(mcode, 0));
                String discNumber = getDiscNumber(this.ginfo.indexOf(mcode, 0));
                String totalDiscs = getTotalDiscNumber(this.ginfo.indexOf(mcode, 0));
                name = !discNumber.equals("") ? getName(this.ginfo.indexOf(mcode, 0)) + " (CD" + discNumber + "/" + totalDiscs + ")" : getName(this.ginfo.indexOf(mcode, 0));
                country = getCountry(this.ginfo.indexOf(mcode, 0));
                company = getCompany(this.ginfo.indexOf(mcode, 0));
                lang = getLang(this.ginfo.indexOf(mcode, 0));
                type = getType(this.ginfo.indexOf(mcode, 0));
                multitap = getMultitap(this.ginfo.indexOf(mcode, 0));
                numplayers = getNumPlayers(this.ginfo.indexOf(mcode, 0));
                padtype = getPadType(this.ginfo.indexOf(mcode, 0));
            } else {
                name = mcode;
                country = mcode.substring(2, 3).contains("U") ? "NTSC" : "PAL";
                nameJP = "";
                lang = "";
                company = "";
                type = "";
                multitap = "";
                numplayers = "";
                padtype = null;
            }
            fls.add(new OptionDesc(name, nameJP, mcode, company + "/" + type + "/" + lang, "NONE", mcode + "/" + country, "NONE", multitap, numplayers, padtype, 0, null));
        }
        return fls;
    }

    public List<OptionDesc> addFile(File ff, List<OptionDesc> fls, int tracescan) {
        String name;
        String country;
        String nameJP;
        String lang;
        String company;
        String type;
        String multitap;
        String numplayers;
        String padtype;
        Log.e("iso", "" + ff.getAbsolutePath());
        String code = this.f128d.getCode(ff.getAbsolutePath(), tracescan).toUpperCase();
        Log.e("iso", "" + code);
        if (code != null && !code.equals("NONE") && this.ginfo != null) {
            if (code.equals("ECM")) {
                fls.add(new OptionDesc(ff.getName(), "", "ECM-NOINDEXED", "Ecm ROM no indexed!", ff.getName(), "Unknown yet!", ff.getAbsolutePath(), "", "", null, 0, null));
            } else if (this.ginfo != null) {
                int entry = this.ginfo.indexOf(code, 0);
                if (entry != -1) {
                    nameJP = getNameJP(this.ginfo.indexOf(code, 0));
                    String discNumber = getDiscNumber(this.ginfo.indexOf(code, 0));
                    String totalDiscs = getTotalDiscNumber(this.ginfo.indexOf(code, 0));
                    name = !discNumber.equals("") ? getName(this.ginfo.indexOf(code, 0)) + " (CD" + discNumber + "/" + totalDiscs + ")" : getName(this.ginfo.indexOf(code, 0));
                    country = getCountry(this.ginfo.indexOf(code, 0));
                    company = getCompany(this.ginfo.indexOf(code, 0));
                    lang = getLang(this.ginfo.indexOf(code, 0));
                    type = getType(this.ginfo.indexOf(code, 0));
                    multitap = getMultitap(this.ginfo.indexOf(code, 0));
                    numplayers = getNumPlayers(this.ginfo.indexOf(code, 0));
                    padtype = getPadType(this.ginfo.indexOf(code, 0));
                } else {
                    name = ff.getName();
                    country = code.substring(2, 3).contains("U") ? "NTSC" : "PAL";
                    nameJP = "";
                    lang = "";
                    company = "";
                    type = "";
                    multitap = "";
                    numplayers = "";
                    padtype = null;
                }
                fls.add(new OptionDesc(name, nameJP, code, company + "/" + type + "/" + lang, ff.getName(), code + "/" + country, ff.getAbsolutePath(), multitap, numplayers, padtype, 0, null));
            }
        }
        return fls;
    }

    public List<OptionDesc> addFile(File ff, List<OptionDesc> fls, String name, int slot, int tracescan) {
        String country;
        String nameJP;
        String lang;
        String company;
        String type;
        String multitap;
        String numplayers;
        String padtype;
        Log.e("isopbpb", "" + ff.getAbsolutePath());
        String code = this.f128d.getCode(ff.getAbsolutePath(), tracescan).toUpperCase();
        if (!code.equals("NONE") && this.ginfo != null) {
            int entry = this.ginfo.indexOf(code, 0);
            if (entry != -1) {
                nameJP = getNameJP(this.ginfo.indexOf(code, 0));
                country = getCountry(this.ginfo.indexOf(code, 0));
                company = getCompany(this.ginfo.indexOf(code, 0));
                lang = getLang(this.ginfo.indexOf(code, 0));
                type = getType(this.ginfo.indexOf(code, 0));
                multitap = getMultitap(this.ginfo.indexOf(code, 0));
                numplayers = getNumPlayers(this.ginfo.indexOf(code, 0));
                padtype = getPadType(this.ginfo.indexOf(code, 0));
            } else {
                country = code.substring(2, 3).contains("U") ? "NTSC" : "PAL";
                nameJP = "";
                lang = "";
                company = "";
                type = "";
                multitap = "";
                numplayers = "";
                padtype = null;
            }
            fls.add(new OptionDesc(name, nameJP, code, company + "/" + type + "/" + lang, ff.getName(), code + "/" + country, ff.getAbsolutePath(), multitap, numplayers, padtype, slot, null));
        }
        return fls;
    }
}
