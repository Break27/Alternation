/**************************************************************************
 * Copyright (c) 2021 Breakerbear
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *************************************************************************/

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
    public interface ViewpointRenderer {
        void render();
    }
    
    protected FrameBuffer fbo;
    protected Image image;
    protected ViewpointRenderer renderer;
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
    
    public void render(ViewpointRenderer renderer) {
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
