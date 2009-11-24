package com.tangledcode.starscream.gui;

import com.tangledcode.starscream.entities.Updatable;


public class AnimatedTexture implements Drawable, Updatable {
    private int frames;
    private int currentFrame;
    private int fps;
    private float timebase;
    private Texture[] textures;

    public AnimatedTexture(int frames, int fps, Texture[] textures) {
        this.frames = frames;
        this.fps = fps;
        this.textures = textures;
        
        this.currentFrame = 0;
        this.timebase = 0;
    }

    public void draw() {
        this.textures[this.currentFrame].draw();
    }

    public void update(float timeDiff) {
        this.timebase += timeDiff;
        
        if(this.timebase > 1.f/(this.fps)) {
            this.currentFrame = (this.currentFrame + 1) % this.frames;
            this.timebase = 0;
        }
    }

}
