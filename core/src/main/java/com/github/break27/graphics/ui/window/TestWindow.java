/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 *
 * @author break27
 */
public class TestWindow extends SerializableWindow {
    Image image;
    Texture texture;
    
    public TestWindow(String name) {
        super(name);
        addCloseButton();
    }
    
    @Override
    public int getType() {
        return WindowType.TEST;
    }
    
    @Override
    public void create() {
        texture = new Texture(Gdx.files.internal("banner/libgdx.png"));
        image = new Image(texture);
        getContentTable().add(image);
    }
    
    @Override
    public void destroy() {
        image.remove();
        texture.dispose();
    }
    
    @Override
    public void styleApply() {
        setTitleImage(getAlterSkin().getDrawable("icon20-application"));
    }
    
    @Override
    public void localeApply() {
        
    }
}
