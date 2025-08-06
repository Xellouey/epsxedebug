package com.epsxe.ePSXe;

/* loaded from: classes.dex */
public class TextureRegion {

    /* renamed from: u1 */
    public float f142u1;

    /* renamed from: u2 */
    public float f143u2;

    /* renamed from: v1 */
    public float f144v1;

    /* renamed from: v2 */
    public float f145v2;

    public TextureRegion(float texWidth, float texHeight, float x, float y, float width, float height) {
        this.f142u1 = x / texWidth;
        this.f144v1 = y / texHeight;
        this.f143u2 = this.f142u1 + (width / texWidth);
        this.f145v2 = this.f144v1 + (height / texHeight);
    }
}
