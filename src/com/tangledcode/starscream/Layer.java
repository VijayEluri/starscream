package com.tangledcode.starscream;

import java.util.Vector;

import com.tangledcode.starscream.entities.Entity;


public class Layer {
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

    public void render() {
        for(Entity entity : this.entities) {
            entity.draw();
        }
    }
}
