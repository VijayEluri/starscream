package com.tangledcode.starscream;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


public class Texture {
    public class TextureCoord {
        public static final int TOP = 0;
        public static final int BOTTOM = 1;
        public static final int RIGHT = 1;
        public static final int LEFT = 0;
    }
    
    private int id;
    private int width;
    private int height;
    private float widthRatio;
    private float heightRatio;
    private int textureWidth;
    private int textureHeight;
    
    public Texture(int id, int width, int height) {
        this(id, width, height, 1, 1, width, height);
    }
    
    public Texture(int id, int width, int height, float widthRatio, float heightRatio, int textureWidth, int textureHeight) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }
    
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    }
    
    public void destroy() {
        IntBuffer buf = BufferUtils.createIntBuffer(1);
        buf.put(0, this.id);
        GL11.glDeleteTextures(buf);
    }

    // GETTER
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
}
