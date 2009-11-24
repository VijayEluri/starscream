package com.tangledcode.starscream.gui;

import com.tangledcode.starscream.entities.Updatable;


public class Level implements Drawable, Updatable {
    private Layer background;
    private Layer enemies;
    private Layer effects;
    private Layer player;
    private Layer forground;
    
    
    public Level() {
        this.background = new Layer();
        this.enemies = new Layer();
        this.effects = new Layer();
        this.player = new Layer();
        this.forground = new Layer();
    }

    public void update(float timeDiff) {
        this.background.update(timeDiff);
        this.enemies.update(timeDiff);
        this.effects.update(timeDiff);
        this.player.update(timeDiff);
        this.forground.update(timeDiff);
    }

    public void checkCollision() {
        // @todo let enemies and bullets collide
        
        // @todo let player collide with bullets
    }

    public void draw() {
        this.background.draw();
        this.enemies.draw();
        this.effects.draw();
        this.player.draw();
        this.forground.draw();
    }

}
