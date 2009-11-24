package com.tangledcode.starscream.entities;

import java.awt.Dimension;

import org.lwjgl.util.vector.Vector2f;


public abstract class Entity {
    public static enum Alignment {
        LEFT,
        RIGHT,
        CENTER,
        TOP,
        BOTTOM
    }
    
    public static final int DEFAULT_Z = 0;
    
    protected Vector2f position;
    protected Dimension size;
    
    public abstract void draw();
    public abstract void update(float timeDiff);
    
    
    public Entity() {
        this.position = new Vector2f();
        this.size = new Dimension();
    }
    
    public void spawn(Vector2f position) {
        this.position = position;
    }
}
