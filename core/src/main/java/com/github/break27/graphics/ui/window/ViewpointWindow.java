/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.break27.graphics.Viewpoint;
import com.github.break27.graphics.ui.menu.WindowTitleMenu;

/**
 *
 * @author break27
 */
public class ViewpointWindow extends CollapsibleWindow {
    public ViewpointWindow(String name, TextureAtlas atlas, int width, int height) {
        super(name, atlas);
        viewpoint = new Viewpoint(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), width, height);
        createViewpoint();
        
        image = viewpoint.getFrameImage();
        addContent(image);
        this.padBottom(5f);
        pack();
    }
    
    Viewpoint viewpoint;
    Image image;
    SpriteBatch batch;
    Image titleImage;
    
    public void update() {
        if(!isCollapsed()) {
            viewpoint.update();
            image.setDrawable(viewpoint.getFrameImage().getDrawable());
        }
    }
    
    @Override
    public void create() {
        setTitleImage(new Image(atlas.createSprite("icon20-game-map")));
        WindowTitleMenu menu = new WindowTitleMenu(getStage(), this, atlas);
        menu.listenTo(this.getTitleTable());
        // default value: focused
        setFocus();
    }
    
    @Override
    public int getWindowType() {
        return VIEW;
    }
    
    public void setViewpoint(Viewpoint viewpoint) {
        this.viewpoint = viewpoint;
    }
    
    private void createViewpoint() {
        batch = new SpriteBatch();
        Sprite spr = new Sprite(new Texture(Gdx.files.internal("banner/snapshot.png")));
        viewpoint.setRenderer(() -> {
            Gdx.gl.glClearColor(1, 1, 1, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            batch.draw(spr, 0, 0);
            batch.end();
        });
    }
}
