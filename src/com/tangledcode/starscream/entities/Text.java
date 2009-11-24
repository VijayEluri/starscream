package com.tangledcode.starscream.entities;

import org.lwjgl.opengl.GL11;

import com.tangledcode.starscream.Texture;


public class Text extends Entity {
    private String text;
    private Alignment alignment;
    
    
    public Text() {
        this(new String());
    }
    
    public Text(String text) {
        this.text = text;
        this.alignment = Alignment.LEFT;
    }

    @Override
    public void draw() {
        GL11.glLoadIdentity();
        GL11.glTranslated(this.position.x, this.position.y, Entity.DEFAULT_Z);
        
        char[] chars = this.text.toCharArray();
        if (this.alignment == Alignment.RIGHT) {
            for(int i = chars.length-1; i >= 0 ; i++) {
                this.drawChar(chars[i]);
                GL11.glTranslatef(-this.size.width, 0, Entity.DEFAULT_Z);
            }
//        } else if (this.alignment == Alignment.CENTER) { // @todo implement center positioning for text
//            for(int i = chars.length-1; i >= 0 ; i++) {
//                this.drawChar(chars[i]);
//                GL11.glTranslatef(this.size.width, 0, Entity.Z_INDEX);
//            }
        } else {
            for(int i = 0; i < chars.length ; i++) {
                this.drawChar(chars[i]);
                GL11.glTranslatef(this.size.width, 0, Entity.DEFAULT_Z);
            }
        }
    }

    private void drawChar(char c) {
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(Texture.TextureCoord.RIGHT, Texture.TextureCoord.TOP);
            GL11.glVertex2f(this.size.width, -this.size.height);
            
            GL11.glTexCoord2f(Texture.TextureCoord.LEFT, Texture.TextureCoord.TOP);          
            GL11.glVertex2f(-this.size.width, -this.size.height);        
       
            GL11.glTexCoord2f(Texture.TextureCoord.LEFT, Texture.TextureCoord.BOTTOM);
            GL11.glVertex2f(-this.size.width, this.size.height);
                
            GL11.glTexCoord2f(Texture.TextureCoord.RIGHT, Texture.TextureCoord.BOTTOM);
            GL11.glVertex2f(this.size.width, this.size.height);
        GL11.glEnd();
    }

    @Override
    public void update(float timeDiff) {
        
    }
    
    // SETTER
    public void setText(String text) {
        this.text = text;
    }
    
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }
}
