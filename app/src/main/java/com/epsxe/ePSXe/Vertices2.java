package com.epsxe.ePSXe;

import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class Vertices2 {
    static final int COLOR_CNT = 4;
    static final int INDEX_SIZE = 2;
    static final int NORMAL_CNT = 3;
    static final int POSITION_CNT_2D = 2;
    static final int POSITION_CNT_3D = 3;
    static final int TEXCOORD_CNT = 2;

    /* renamed from: gl */
    final GL10 f147gl;
    final boolean hasColor;
    final boolean hasNormals;
    final boolean hasTexCoords;
    final ShortBuffer indices;
    int mpadAlphaLoc;
    int mpadFrameLoc;
    int mpadPositionLoc;
    int mpadTexCoordLoc;
    public int numIndices;
    public int numVertices;
    int pProgram;
    float padAlpha;
    public final int positionCnt;
    final int[] tmpBuffer;
    public final int vertexSize;
    public final int vertexStride;
    final IntBuffer vertices;

    public Vertices2(GL10 gl, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords, boolean hasNormals, int pPro, int mpadPos, int mpadTex, int mpadFrm, int mpadAlp, float pAlpha) {
        this(gl, maxVertices, maxIndices, hasColor, hasTexCoords, hasNormals, false, pPro, mpadPos, mpadTex, mpadFrm, mpadAlp, pAlpha);
    }

    public Vertices2(GL10 gl, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords, boolean hasNormals, boolean use3D, int pPro, int mpadPos, int mpadTex, int mpadFrm, int mpadAlp, float pAlpha) {
        this.pProgram = pPro;
        this.mpadPositionLoc = mpadPos;
        this.mpadTexCoordLoc = mpadTex;
        this.mpadFrameLoc = mpadFrm;
        this.mpadAlphaLoc = mpadAlp;
        this.padAlpha = pAlpha;
        this.f147gl = gl;
        this.hasColor = hasColor;
        this.hasTexCoords = hasTexCoords;
        this.hasNormals = hasNormals;
        this.positionCnt = use3D ? 3 : 2;
        this.vertexStride = (hasNormals ? 3 : 0) + this.positionCnt + (hasColor ? 4 : 0) + (hasTexCoords ? 2 : 0);
        this.vertexSize = this.vertexStride * 4;
        ByteBuffer buffer = ByteBuffer.allocateDirect(this.vertexSize * maxVertices);
        buffer.order(ByteOrder.nativeOrder());
        this.vertices = buffer.asIntBuffer();
        if (maxIndices > 0) {
            ByteBuffer buffer2 = ByteBuffer.allocateDirect(maxIndices * 2);
            buffer2.order(ByteOrder.nativeOrder());
            this.indices = buffer2.asShortBuffer();
        } else {
            this.indices = null;
        }
        this.numVertices = 0;
        this.numIndices = 0;
        this.tmpBuffer = new int[(this.vertexSize * maxVertices) / 4];
    }

    public void setVertices(float[] vertices, int offset, int length) {
        this.vertices.clear();
        int last = offset + length;
        int i = offset;
        int j = 0;
        while (i < last) {
            this.tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
            i++;
            j++;
        }
        this.vertices.put(this.tmpBuffer, 0, length);
        this.vertices.flip();
        this.numVertices = length / this.vertexStride;
    }

    public void setIndices(short[] indices, int offset, int length) {
        this.indices.clear();
        this.indices.put(indices, offset, length);
        this.indices.flip();
        this.numIndices = length;
    }

    public void bind() {
        this.vertices.position(0);
        GLES20.glVertexAttribPointer(this.mpadPositionLoc, 2, 5126, false, 16, (Buffer) this.vertices);
        this.vertices.position(2);
        GLES20.glVertexAttribPointer(this.mpadTexCoordLoc, 2, 5126, false, 16, (Buffer) this.vertices);
        GLES20.glEnableVertexAttribArray(this.mpadPositionLoc);
        GLES20.glEnableVertexAttribArray(this.mpadTexCoordLoc);
        GLES20.glUniform1i(this.mpadFrameLoc, 0);
        GLES20.glUniform1f(this.mpadAlphaLoc, this.padAlpha);
    }

    public void draw(int primitiveType, int offset, int numVertices) {
        if (this.indices != null) {
            this.indices.position(offset);
            GLES20.glDrawElements(primitiveType, numVertices, 5123, this.indices);
        } else {
            GLES20.glDrawArrays(primitiveType, offset, numVertices);
        }
    }

    public void unbind() {
    }

    public void drawFull(int primitiveType, int offset, int numVertices) {
        bind();
        draw(primitiveType, offset, numVertices);
        unbind();
    }

    void setVtxPosition(int vtxIdx, float x, float y) {
        int index = vtxIdx * this.vertexStride;
        this.vertices.put(index + 0, Float.floatToRawIntBits(x));
        this.vertices.put(index + 1, Float.floatToRawIntBits(y));
    }

    void setVtxPosition(int vtxIdx, float x, float y, float z) {
        int index = vtxIdx * this.vertexStride;
        this.vertices.put(index + 0, Float.floatToRawIntBits(x));
        this.vertices.put(index + 1, Float.floatToRawIntBits(y));
        this.vertices.put(index + 2, Float.floatToRawIntBits(z));
    }

    void setVtxColor(int vtxIdx, float r, float g, float b, float a) {
        int index = (this.vertexStride * vtxIdx) + this.positionCnt;
        this.vertices.put(index + 0, Float.floatToRawIntBits(r));
        this.vertices.put(index + 1, Float.floatToRawIntBits(g));
        this.vertices.put(index + 2, Float.floatToRawIntBits(b));
        this.vertices.put(index + 3, Float.floatToRawIntBits(a));
    }

    void setVtxColor(int vtxIdx, float r, float g, float b) {
        int index = (this.vertexStride * vtxIdx) + this.positionCnt;
        this.vertices.put(index + 0, Float.floatToRawIntBits(r));
        this.vertices.put(index + 1, Float.floatToRawIntBits(g));
        this.vertices.put(index + 2, Float.floatToRawIntBits(b));
    }

    void setVtxColor(int vtxIdx, float a) {
        int index = (this.vertexStride * vtxIdx) + this.positionCnt;
        this.vertices.put(index + 3, Float.floatToRawIntBits(a));
    }

    void setVtxTexCoords(int vtxIdx, float u, float v) {
        int index = this.positionCnt + (this.vertexStride * vtxIdx) + (this.hasColor ? 4 : 0);
        this.vertices.put(index + 0, Float.floatToRawIntBits(u));
        this.vertices.put(index + 1, Float.floatToRawIntBits(v));
    }

    void setVtxNormal(int vtxIdx, float x, float y, float z) {
        int index = (this.hasColor ? 4 : 0) + this.positionCnt + (this.vertexStride * vtxIdx) + (this.hasTexCoords ? 2 : 0);
        this.vertices.put(index + 0, Float.floatToRawIntBits(x));
        this.vertices.put(index + 1, Float.floatToRawIntBits(y));
        this.vertices.put(index + 2, Float.floatToRawIntBits(z));
    }
}
