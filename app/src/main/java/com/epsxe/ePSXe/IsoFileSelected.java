package com.epsxe.ePSXe;

/* loaded from: classes.dex */
public class IsoFileSelected {
    private String mIsoName;
    private int mIsoSlot;

    public IsoFileSelected(String iso, int slot) {
        this.mIsoName = "";
        this.mIsoSlot = 0;
        this.mIsoName = iso;
        this.mIsoSlot = slot;
    }

    public String getmIsoName() {
        return this.mIsoName;
    }

    public int getmIsoSlot() {
        return this.mIsoSlot;
    }

    public void setmIsoName(String iso) {
        this.mIsoName = iso;
    }

    public void setmIsoSlot(int slot) {
        this.mIsoSlot = slot;
    }
}
