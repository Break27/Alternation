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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JEditorPane;

/**
 *
 * @author break27
 */
public class HtmlRenderer {
    JEditorPane jep;
    Graphics graphics;
    ByteArrayOutputStream out;
    
    BufferedImage buffImage;
    TextureRegion region;
    Image s2dImage;

    int RegionWidth;
    int RegionHeight;
    boolean isClipped;
    
    public HtmlRenderer(int width, int height) {
        this(width, height, false);
    }
    
    public HtmlRenderer(int width, int height, boolean isClipped) {
        jep = new JEditorPane();
        out = new ByteArrayOutputStream();
        s2dImage = new Image();
        region = new TextureRegion();
        // utf8 support
        jep.getDocument().putProperty("Ignore-charset", "true");
        jep.setContentType("text/html;charset=utf-8");
        jep.putClientProperty("charset", "utf-8");
        jep.setEditable(false);
        
        this.RegionWidth = width;
        this.RegionHeight = height;
        this.isClipped = isClipped;
        s2dImage.setSize(width, height);
        // set image size to screen size
        //createGraphics(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        createGraphics(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
    }
    
    public void render() {
        // calls JEditorPane to render
        jep.print(graphics);
        try {
            ImageIO.write(buffImage, "png", out);
        } catch(IOException e) {
            Gdx.app.error(getClass().getName(), "Error rendering image.", e);
        }
        region.setRegion(new Texture(new Pixmap(out.toByteArray(), 0, out.size())));
        // clip the texture if necessary
        if(isClipped) region.setRegion(0, 0, RegionWidth, RegionHeight);
        else s2dImage.setSize(region.getRegionWidth(), region.getRegionHeight());
        s2dImage.setDrawable(new TextureRegionDrawable(region));
    }
    
    public void load(String html) {
        jep.setText(html);
    }
    
    public Image getImage() {
        return s2dImage;
    }
    
    public void resize(int width, int height) {
        jep.setSize(width, height);
        createGraphics(width, height);
    }
    
    public void setClipped(boolean isClipped) {
        this.isClipped = isClipped;
    }
    
    private void createGraphics(int width, int height) {
        jep.setSize(width, height);
        buffImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(width, height);
        graphics = buffImage.createGraphics();
    }
}
