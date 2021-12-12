/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.break27.graphics.Viewpoint;
import com.github.break27.graphics.ui.StyleProvider;
import com.github.break27.graphics.ui.menu.TitleMenu;

/**
 *
 * @author break27
 */
public class ViewpointWindow extends CollapsibleWindow {
    public ViewpointWindow(String name, int width, int height) {
        super(name);
        provider.setStyle("title_icon", "alter::icon20-game-map");
        
        addCollapseButton();
        this.viewpointWidth = width;
        this.viewpointHeight = height;
    }
    
    Viewpoint viewpoint;
    Image image;
    
    int viewpointWidth;
    int viewpointHeight;
    boolean destroyed = false;
    
    public void update() {
        if(!isCollapsed() && !destroyed) {
            viewpoint.update();
            image.setDrawable(viewpoint.getFrameImage().getDrawable());
        }
    }
    
    @Override
    public void create() {
        this.viewpoint = new Viewpoint(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), viewpointWidth, viewpointHeight, false);
        createViewpoint();
        image = viewpoint.getFrameImage();
        setContent(image);
        this.padBottom(5f);

        TitleMenu menu = new TitleMenu(this);
        menu.listenTo(this.getTitleTable());
        // default value: focused
        setFocus();
    }
    
    @Override
    public void styleApply() {
        setTitleImage(provider.getStyle("title_icon"));
    }
    
    @Deprecated
    @Override
    public void setSize(float width, float height) {
    }
    
    @Override
    public void destroy() {
        viewpoint.destory();
    }
    
    @Override
    public int getWindowType() {
        return WindowType.VIEW;
    }
    
    public void setViewpoint(Viewpoint viewpoint) {
        this.viewpoint = viewpoint;
    }
    
    public void resize(int width, int height) {
        super.setSize(width, height);
        this.viewpoint.resize(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), width, height);
    }
    
    private void createViewpoint() {
        viewpoint.setRenderer(() -> {
            
        });
    }
}
