package com.tangledcode.starscream.entities;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.vector.Vector2f;

import com.tangledcode.starscream.gui.Drawable;

public abstract class Entity implements Drawable, Updatable {

    public static enum Alignment {
        LEFT, RIGHT, CENTER, TOP, BOTTOM
    }

    protected Vector2f position;
    protected Dimension size;

    public Entity() {
        this.position = new Vector2f();
        this.size = new Dimension();
    }

    public void spawn(Vector2f position) {
        this.position = position;
    }
}
