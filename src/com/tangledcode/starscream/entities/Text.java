package com.tangledcode.starscream.entities;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;

import com.tangledcode.starscream.gui.Texture;
import com.tangledcode.starscream.gui.TextureManager;

public class Text extends Entity {

    private char[] text;
    private Alignment alignment;
    private Texture[] font;

    public Text() {
        this(new String());
    }

    public Text(String text) {
        this.setText(text);
        this.alignment = Alignment.LEFT;

        String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "data/images/font.png";
        System.out.println(path);
        this.size = new Dimension(32, 32);
        this.font = new TextureManager().loadAnimation(path, 16, 16, this.size);
    }

    public void draw() {
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslated(this.position.x, this.position.y, Texture.DEFAULT_Z);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        if(this.alignment == Alignment.RIGHT) {
            for(int i = this.text.length - 1; i >= 0; i++) {
                this.drawChar(this.text[i]);
                GL11.glTranslatef(-this.size.getWidth(), 0, Texture.DEFAULT_Z);
            }
            // } else if (this.alignment == Alignment.CENTER) { // @todo implement center positioning for text
            // for(int i = chars.length-1; i >= 0 ; i++) {
            // this.drawChar(chars[i]);
            // GL11.glTranslatef(this.size.width, 0, Entity.Z_INDEX);
            // }
        } else {
            for(int i = 0; i < this.text.length; i++) {
                this.drawChar(this.text[i]);
                GL11.glTranslatef(this.size.getWidth(), 0, Texture.DEFAULT_Z);
            }
        }
        
        GL11.glPopMatrix();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    private void drawChar(char c) {
        this.font[(int) c].draw();
    }

    public void update(float timeDiff) { }

    // SETTER
    public void setText(String text) {
        this.text = text.toCharArray();
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }
}
