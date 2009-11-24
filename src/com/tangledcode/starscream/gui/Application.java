package com.tangledcode.starscream.gui;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;

import com.tangledcode.starscream.Layer;
import com.tangledcode.starscream.Level;
import com.tangledcode.starscream.Texture;
import com.tangledcode.starscream.TextureManager;
import com.tangledcode.starscream.entities.Text;


public class Application extends JFrame {
    private static final long serialVersionUID = 5280460402425917164L;
    
    private boolean running;
    private int width;
    private int height;
    
    private Timer timer;
    private float frames;
    private float timeDiff;
    private float currentTime;
    private float fps;
    
    private Layer debugUI;
    
    public Application() {
        this.running = true;
        this.width = 800;
        this.height = 600;
        
        this.frames = this.timeDiff = this.currentTime = this.fps = 0;
        
        this.timer = new Timer();
        this.debugUI = new Layer();
        
        this.createWindow();
    }
    
    public void run() {
        // load level
//        Level level = new Level();
        
        TextureManager man = new TextureManager();
        Texture t = man.loadTexture("/Users/malone/Projects/starscream/data/images/upgrade.png");

        float pos = 0;
        
        while(this.running) {
            // step time
            this.stepOn();
            
            if (Display.isVisible()) {
                // update
//                level.update(this.timeDiff);
                this.debugUI.update(timeDiff);
                
                pos += 0.1f;
                
                // check collision
//                level.checkCollision();
                
                // render
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glPushMatrix();
                
                GL11.glTranslatef(400, 300, 0);
                
                t.bind();
                GL11.glBegin(GL11.GL_QUADS);
                    GL11.glTexCoord2f(Texture.TextureCoord.RIGHT, Texture.TextureCoord.TOP);
                    GL11.glVertex2f(t.getWidth(), t.getHeight());
                    
                    GL11.glTexCoord2f(Texture.TextureCoord.LEFT, Texture.TextureCoord.TOP);          
                    GL11.glVertex2f(0, t.getHeight());        
               
                    GL11.glTexCoord2f(Texture.TextureCoord.LEFT, Texture.TextureCoord.BOTTOM);
                    GL11.glVertex2f(0, 0);
                        
                    GL11.glTexCoord2f(Texture.TextureCoord.RIGHT, Texture.TextureCoord.BOTTOM);
                    GL11.glVertex2f(t.getWidth(), 0);
                GL11.glEnd();
                
                GL11.glPopMatrix();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                
//                this.debugUI.render();
            } else {
                if (Display.isDirty()) {
//                    level.render();
                    this.debugUI.render();
                }
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) { }
            }
            
            if (Display.isCloseRequested()) {
                this.running = false;
            }
            
            Display.update();
        }
        
        man.destroy();
        
        Display.destroy();
        System.exit(0);
    }

    private void stepOn() {
        Timer.tick();
        
        float lastTime = this.currentTime;
        this.currentTime = this.timer.getTime();
        
        this.timeDiff = this.currentTime - lastTime;
        
        this.frames++;
        if (this.frames >= 50) {
            this.fps = this.frames/ this.timeDiff;
            this.frames = 0;
        }
    }

    private void createWindow() {
        try {
            Display.setDisplayMode(new DisplayMode(this.width, this.height));
            Display.setTitle("Starscream");
            
            Display.create();
        } catch(LWJGLException e) {
            System.err.println("could not set display mode");
            System.exit(1);
        }
        
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
        GL11.glClearColor(0, 0, 0, 0);
    }
}
