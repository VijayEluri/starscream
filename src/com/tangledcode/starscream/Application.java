package com.tangledcode.starscream;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Timer;
import org.lwjgl.util.vector.Vector2f;

import com.tangledcode.starscream.entities.Text;
import com.tangledcode.starscream.gui.AnimatedTexture;
import com.tangledcode.starscream.gui.Layer;
import com.tangledcode.starscream.gui.Level;
import com.tangledcode.starscream.gui.TextureManager;


public class Application extends JFrame {

    private static final long serialVersionUID = 5280460402425917164L;

    private boolean running;
    private int width;
    private int height;

    private Timer timer;
    private float frames;
    private float timeDiff;
    private float timebase;
    private float currentTime;
    private float fps;

    private Layer debugUI;

    public Application() {
        this.running = true;
        this.width = 800;
        this.height = 600;

        this.frames = this.timeDiff = this.timebase = this.currentTime = this.fps = 0;

        this.timer = new Timer();
        this.debugUI = new Layer();

        this.createWindow();
    }

    public void run() {
        // load level
        Level level = new Level();

        TextureManager man = new TextureManager();
        
        String path = "/Users/malone/Projects/starscream/data/images/bulletHit.png";
        String path2 = "/Users/malone/Projects/starscream/data/images/upgrade.png";
        
        Dimension dimension = new Dimension(32, 32);
        AnimatedTexture anim = new AnimatedTexture(7, 8, man.loadAnimation(path, 4, 2, dimension));
        AnimatedTexture anim2 = new AnimatedTexture(7, 8, man.loadAnimation(path2, 4, 2, dimension));
        AnimatedTexture anim3 = new AnimatedTexture(7, 12, man.loadAnimation(path2, 4, 2, dimension));
        AnimatedTexture anim4 = new AnimatedTexture(7, 16, man.loadAnimation(path2, 4, 2, dimension));
        
        Text fpsText = new Text();
        fpsText.spawn(new Vector2f(10, 558));
        
        this.debugUI.add(fpsText);
        
        while(this.running) {
            // step time
            this.stepOn();

            if(Display.isVisible()) {
                fpsText.setText("FPS: " + (int) this.fps);
                
                // update
                level.update(this.timeDiff);
                this.debugUI.update(timeDiff);

                anim.update(timeDiff);
                anim2.update(timeDiff);
                anim3.update(timeDiff);
                anim4.update(timeDiff);
                
                // check collision
                level.checkCollision();

                // render
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glPushMatrix();

                level.draw();
                this.debugUI.draw();
                
                GL11.glTranslated(200, 300, 0);
                anim.draw();
                
                GL11.glTranslated(100, 0, 0);
                anim2.draw();
                
                GL11.glTranslated(100, 0, 0);
                anim3.draw();
                
                GL11.glTranslated(100, 0, 0);
                anim4.draw();

                GL11.glPopMatrix();
            } else {
                if(Display.isDirty()) {
                    level.draw();
                    this.debugUI.draw();
                }

                try {
                    Thread.sleep(100);
                } catch(InterruptedException ex) {
                }
            }

            if(Display.isCloseRequested()) {
                this.running = false;
            }

            Display.update();
        }

        Display.destroy();
        System.exit(0);
    }

    private void stepOn() {
        Timer.tick();

        float lastTime = this.currentTime;
        this.currentTime = this.timer.getTime();

        this.timeDiff = this.currentTime - lastTime;

        this.frames++;
        if(this.currentTime - this.timebase > 1.f) {
            this.fps = this.frames / (this.currentTime - this.timebase); // @malone not sure if this is correct
            this.timebase = this.currentTime;
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

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
    }
}
