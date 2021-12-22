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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 *
 * @author break27
 */
public class Viewpoint {
    
    protected FrameBuffer fbo;
    protected Image image;
    protected Renderer renderer;
    protected TextureRegion texture;
    protected TextureRegionDrawable drawable;
    
    protected int imageWidth;
    protected int imageHeight;
    
    public boolean isHdpiSupported = false;
    
    boolean hasDepth;
    boolean disabled;
    
    public Viewpoint(int width, int height, int imageWidth, int imageHeight, boolean hasDepth) {
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, hasDepth);
        this.hasDepth = hasDepth;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    public void setImageSize(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
    }
    
    public Image getImage() {
        if(image == null) update();
        return image;
    }
    
    public boolean update() {
        if(disabled) return false;
        if(isHdpiSupported) HdpiUtils.setMode(HdpiMode.Pixels);
        fbo.begin();
        renderer.render();
        fbo.end();
        if(isHdpiSupported) HdpiUtils.setMode(HdpiMode.Logical);
        
        // trying to avoid performance issues
        if(texture == null) {
            texture = new TextureRegion();
            drawable = new TextureRegionDrawable();
            image = new Image();
            // it doesn't do anything except passing the parameters for ui design
            image.setWidth(imageWidth);
            image.setHeight(imageHeight);
        }
        
        texture.setRegion(fbo.getColorBufferTexture());
        texture.setRegionWidth(imageWidth);
        texture.setRegionHeight(imageHeight);
        texture.flip(false, true);
        
        drawable.setRegion(texture);
        image.setDrawable(drawable);
        return true;
    }
    
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
    
    public void resize(int width, int height, int imageWidth, int imageHeight) {
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, hasDepth);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    /** Toggling the support in HDPI mode.
     * <b> Warning: this may cause performance issues. </b>
     * @param supported
     */
    public void setHdpiSupport(boolean supported) {
        this.isHdpiSupported = supported;
    }
    
    public void destory() {
        fbo.dispose();
        image.remove();
        texture = null;
        drawable = null;
        disabled = true;
    }
}
