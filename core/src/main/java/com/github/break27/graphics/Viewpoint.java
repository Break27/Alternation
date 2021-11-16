/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 *
 * @author break27
 */
public class Viewpoint {
    
    protected FrameBuffer fbo;
    protected Image image;
    protected Renderer renderer;
    protected TextureRegion texture;
    
    protected int imageWidth;
    protected int imageHeight;
    
    public boolean isHdpiSupported = false;
    
    public Viewpoint(int width, int height, int imageWidth, int imageHeight) {
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    public void setImageSize(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
    }
    
    public Image getFrameImage() {
        if(image == null) update();
        return image;
    }
    
    public void update() {
        if(isHdpiSupported) HdpiUtils.setMode(HdpiMode.Pixels);
        fbo.begin();
        renderer.render();
        fbo.end();
        if(isHdpiSupported) HdpiUtils.setMode(HdpiMode.Logical);
        // trying to avoid performance issues
        if(image != null || texture != null) {
            image = null;
            texture = null;
            System.gc();
        }
        texture = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, imageWidth, imageHeight);
        texture.flip(false, true);
        image = new Image(texture);
    }
    
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
    
    /** Toggling the support in HDPI mode.
     * <b> Warning: this may cause performance issues. </b>
     * @param supported
     */
    public void setHdpiSupport(boolean supported) {
        this.isHdpiSupported = supported;
    }
}
