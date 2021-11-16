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
public class TestWindow extends CollapsibleWindow {
    
    public TestWindow(String name) {
        super(name);
        Image image = new Image(new Texture(Gdx.files.internal("banner/libgdx.png")));
        addContent(image);
        pack();
    }
    
    @Override
    public void create() {
        setTitleImage(new Image(atlas.createSprite("icon20-info-wb")));
    }
    
    @Override
    public int getWindowType() {
        return TEST;
    }
    
}
