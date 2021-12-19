/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.break27.graphics.HtmlRenderer;
import com.github.break27.graphics.ui.widget.AlterScrollPane;

/**
 *
 * @author break27
 */
public class HtmlViewerWindow extends CollapsibleWindow {
    
    HtmlRenderer renderer;
    
    public HtmlViewerWindow(String name, int width, int height) {
        super(name);
        super.setSize(width, height);
        addCollapseButton();
        renderer = new HtmlRenderer(width, height, false);
    }
    
    public void resizeViewer(int width, int height) {
        renderer.resize(width, height);
        super.setSize(width, height);
    }
    
    @Override
    public int getType() {
        return WindowType.HTML;
    }

    @Override
    public void create() {
        renderer.load(Gdx.files.internal("misc/html/about.html").readString("UTF-8"));
        renderer.render();
        Image image = renderer.getImage();
        AlterScrollPane scrollpane = new AlterScrollPane(image);
        setContent(scrollpane);
        this.padBottom(5f);
    }

    @Override
    public void destroy() {
        
    }

    @Override
    public void styleApply() {
        setTitleImage(getAlterSkin().getDrawable("icon20-info-wb"));
    }

    @Override
    public void localeApply() {
    }
    
    /** Replaced by {@code resize(int, int)}.
     *  @param width
     *  @param height
     */
    @Deprecated
    @Override
    public void setSize(float width, float height) {
    }
}
