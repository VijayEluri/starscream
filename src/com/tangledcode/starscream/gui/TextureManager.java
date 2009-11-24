package com.tangledcode.starscream.gui;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.vector.Vector2f;

public class TextureManager {

    private Hashtable<String, BufferedImage> images;

    public TextureManager() {
        this.images = new Hashtable<String, BufferedImage>();
    }

    // public Texture loadTexture(String path) {
    // this.loadTexture(path)
    // }

    public Texture[] loadAnimation(String path, int cols, int rows, Dimension dimension) {
        return loadAnimation(path, cols, rows, new Vector2f(0, 0), dimension);
    }

    public Texture[] loadAnimation(String path, int cols, int rows, Vector2f offset, Dimension dimension) {
        Texture[] textures = new Texture[cols * rows];
        System.out.print("loading animation ... ");
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                Vector2f position = new Vector2f(col * (float) dimension.getWidth(), row * (float) dimension.getHeight());
                textures[col + row * cols] = loadTexture(path, offset, position, dimension);
            }
        }
        System.out.println("done");

        return textures;
    }

    public Texture loadTexture(String path, Vector2f offset, Vector2f position, Dimension dimension) {
        // check parameters (power of two, ...)

        // load image
        BufferedImage image = null;
        if(this.images.contains(path)) {
            image = this.images.get(path);
        } else {
            try {
                image = ImageIO.read(new File(path));
                this.images.put(path, image);
            } catch(IOException e) {
                System.err.println(path);
                e.printStackTrace();                                                            // @todo log error; does the player need to know?
            }
        }

        // convert to bytes for OpenGL; make sure it's the size of an power of two number
        ByteBuffer buffer = this.getImageBuffer(image, offset, position, dimension);

        // create the OpenGL part
        IntBuffer imageHandler = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();

        GL11.glGenTextures(imageHandler);                                                       // create a image handler for this texture

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, imageHandler.get(0));                            // bind image to an 2D texture

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, dimension.getWidth(), dimension.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // revert OpenGL binding state
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        return new Texture(imageHandler.get(0), dimension.getWidth(), dimension.getHeight());           // return our new texture obj with the OpenGL reference
    }

    private ByteBuffer getImageBuffer(BufferedImage image, Vector2f offset, Vector2f position, Dimension dimension) {
        int bytesPerPixel = image.getColorModel().getPixelSize() / 8;
        int size = bytesPerPixel * dimension.getWidth() * dimension.getHeight();
        ByteBuffer buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());     // create buffer from (resized)image
        DataBufferByte data = (DataBufferByte) image.getRaster().getDataBuffer();               // get data from our image

        buffer.clear();                                                                         // make sure everything is clear

        int height = dimension.getHeight();
        int length = dimension.getWidth() * bytesPerPixel;
        for(int i = 0; i < height; i++) {
            int x = (int) (offset.getX() + position.getX());
            int y = (int) (offset.getY() + position.getY());
            
            int dataOffset = (int) (x + (y + (height-1-i)) * image.getWidth()) * bytesPerPixel;
            
            buffer.put(data.getData(), dataOffset, length);
        }

        buffer.rewind();                                                                        // reset buffer pointer

        return buffer;
    }
}
