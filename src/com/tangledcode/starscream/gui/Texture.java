package com.tangledcode.starscream.gui;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


public class Texture implements Drawable {

    public class TextureCoord {

        public static final int TOP = 1;
        public static final int BOTTOM = 0;
        public static final int RIGHT = 1;
        public static final int LEFT = 0;
    }
    public static final int DEFAULT_Z = 0;
    
    protected int id;
    protected int width;
    protected int height;

    public Texture(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    }
    
    public void draw() {
        GL11.glPushMatrix();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        this.bind();
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(Texture.TextureCoord.RIGHT, Texture.TextureCoord.TOP);
            GL11.glVertex2f(this.width, this.height);
    
            GL11.glTexCoord2f(Texture.TextureCoord.LEFT, Texture.TextureCoord.TOP);
            GL11.glVertex2f(0, this.height);
    
            GL11.glTexCoord2f(Texture.TextureCoord.LEFT, Texture.TextureCoord.BOTTOM);
            GL11.glVertex2f(0, 0);
    
            GL11.glTexCoord2f(Texture.TextureCoord.RIGHT, Texture.TextureCoord.BOTTOM);
            GL11.glVertex2f(this.width, 0);
        GL11.glEnd();
        
        GL11.glPopMatrix();
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
