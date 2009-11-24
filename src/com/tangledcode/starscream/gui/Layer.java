package com.tangledcode.starscream.gui;

import java.util.Vector;

import com.tangledcode.starscream.entities.Entity;
import com.tangledcode.starscream.entities.Updatable;


public class Layer implements Drawable, Updatable {
    private Vector<Entity> entities;
    
    public Layer() {
        this.entities = new Vector<Entity>();
    }
    
    public void add(Entity entity) {
        this.entities.add(entity);
    }
    
    public void remove(Entity entity) {
        this.entities.add(entity);
    }
    
    public void update(float timeDiff) {
        for(Entity entity : this.entities) {
            entity.update(timeDiff);
        }
    }

    public void draw() {
        for(Entity entity : this.entities) {
            entity.draw();
        }
    }
}
