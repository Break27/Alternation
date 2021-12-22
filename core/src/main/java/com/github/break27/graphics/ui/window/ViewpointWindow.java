/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.github.break27.graphics.Viewpoint;
import com.github.break27.graphics.ui.dialog.WindowResizeDialog;
import com.github.break27.graphics.ui.menu.TitleMenu;
import com.github.break27.system.Resource;

/**
 *
 * @author break27
 */
public class ViewpointWindow extends CollapsibleWindow {
    public ViewpointWindow(String name, int width, int height) {
        super(name, width, height);
        this.padBottom(5f);
        this.viewpointWidth = width;
        this.viewpointHeight = height;
        
        addCollapseButton();
    }
    
    WindowResizeDialog resizeDialog;
    Viewpoint viewpoint;
    Image image;
    
    int viewpointWidth;
    int viewpointHeight;
    boolean destroyed = false;
    
    public void update() {
        if(!isCollapsed() && !destroyed) {
            viewpoint.update();
            image.setDrawable(viewpoint.getImage().getDrawable());
        }
    }
    
    @Override
    public void create() {
        this.viewpoint = new Viewpoint(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), 
                viewpointWidth, viewpointHeight, false);
        createViewpoint();
        image = viewpoint.getImage();
        getContentTable().add(image);

        TitleMenu menu = new TitleMenu(this);
        menu.listenTo(getTitleTable());
        
        resizeDialog = new WindowResizeDialog("Resize");
        // default value: focused
        setFocus();
    }
    
    @Override
    public void styleApply() {
        setTitleImage(getAlterSkin().getDrawable("icon20-game-map"));
    }
    
    @Override
    public void localeApply() {
    }
    
    @Override
    public void destroy() {
        viewpoint.destory();
        destroyed = true;
    }
    
    @Override
    public int getType() {
        return WindowType.VIEW;
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.viewpoint.resize(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), width, height);
    }
    
    public void setViewpoint(Viewpoint viewpoint) {
        this.viewpoint = viewpoint;
    }
    
    public void resizeDialogAppend() {
        resizeDialog.show(getStage());
    }
    
    SpriteBatch batch = new SpriteBatch();
    Sprite sprite = new Sprite(getTitleLabel().getStyle().font.getRegion());
    
    private void createViewpoint() {
        viewpoint.setRenderer(() -> {
            batch.begin();
            sprite.draw(batch);
            batch.end();
        });
    }
}
