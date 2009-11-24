package com.tangledcode.starscream;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureManager {

    private Hashtable<String, Texture> textures;

    public TextureManager() {
        this.textures = new Hashtable<String, Texture>();
    }

    public Texture loadTexture(String path) {
        return this.loadTexture(path, false);
    }
    
    public Texture loadTexture(String path, boolean flip) {
        if(this.textures.contains(path)) {
            return this.textures.get(path);
        }

        // load image
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch(IOException e) {
            System.err.println(path);
            e.printStackTrace(); // @todo log error; does the player need to know?
        }

        // flip image @malone how does this really work?
//        if(flip) {
//            AffineTransform trans = AffineTransform.getScaleInstance(1, -1);
//            trans.translate(0, -image.getHeight());
//            image = new AffineTransformOp(trans, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(image, null);
//        }

        // convert to bytes for OpenGL; make sure it's the size of an power of two number
        ByteBuffer buffer = this.getImageBuffer(image, flip);
        
        // create the OpenGL part
//        IntBuffer imageHandlerBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer imageHandlerBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        
        GL11.glGenTextures(imageHandlerBuffer);                                         // create a image handler for this texture
        int imageHandler = imageHandlerBuffer.get(0);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, imageHandler);                           // bind image to an 2D texture
        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        
        // revert OpenGL binding state
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        return new Texture(imageHandler, image.getWidth(), image.getHeight());          // return our new texture obj with the OpenGL reference
    }

    private ByteBuffer getImageBuffer(BufferedImage image, boolean flip) {
        int textureWidth = this.getNextPowerOfTwo(image.getWidth());
        int textureHeight = this.getNextPowerOfTwo(image.getHeight());

        ByteBuffer buffer = null;
        byte[] data = null;
        
        if(image.getWidth() != textureWidth || image.getHeight() != textureHeight) {
            image = image.getSubimage(0, 0, textureWidth, textureHeight);               // create new image if the size is not supported
        }

        // @malone why 4 * width * height?
//        buffer = BufferUtils.createByteBuffer(4 * image.getWidth() * image.getHeight());   // create buffer from (resized)image
        buffer = ByteBuffer.allocateDirect(4 * image.getWidth() * image.getHeight()).order(ByteOrder.nativeOrder());
//        data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();          // get data from our image
        data = (byte[]) image.getRaster().getDataElements(0, 0, image.getWidth(), image.getHeight(), null);

        buffer.clear();                                                                 // make sure everything is clear
        buffer.put(data);                                                               // save byte array data
        if (flip) {
            buffer.flip();
        } else {
            buffer.rewind();                                                                // @malone not sure yet
        }

        return buffer;
    }
    
    public void destroy() {
        for(Texture texture : this.textures.values()) {
            texture.destroy();
        }
    }

    private int getNextPowerOfTwo(int fold) {
        int pow = 2;

        while(pow < fold) {
            pow *= 2;
        }

        return pow;
    }
}
