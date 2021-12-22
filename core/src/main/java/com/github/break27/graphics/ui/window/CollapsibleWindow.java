/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.break27.graphics.ui.button.CollapseButton;
import com.kotcrab.vis.ui.widget.VisImageButton;

/**
 *
 * @author break27
 */
public abstract class CollapsibleWindow extends SerializableWindow {
    
    VisImageButton collapseButton;
    private boolean Collapsed = false;
    
    int labelHeight;
    int width;
    int height;
    
    public CollapsibleWindow(String name, int width, int height) {
        super(name);
        this.width = width;
        this.height = height;
        labelHeight = (int) getMinHeight();
        
        setWidth(width);
        setHeight(height + labelHeight);
        getContentTable().setSize(width, height);
    }
    
    public boolean isCollapsed() {
        return this.Collapsed;
    }
    
    public void addCollapseButton() {
        collapseButton = new CollapseButton();
        addTitleTableButton(collapseButton);
        collapseButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                collapse();
            }
        });
    }
    
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    /** Make window collapsed.
     */
    public void collapse() {
        int footerHeight = (int) getFooterTable().getHeight();
        int menuHeight = (int) getMenuTable().getHeight();
        contentTable.setVisible(Collapsed);
        if(!Collapsed) {
            setPosition(getX(), getY() + height + menuHeight + footerHeight);
            setHeight(labelHeight);
        } else {
            setPosition(getX(), getY() - height - menuHeight - footerHeight);
            setHeight(height + labelHeight + menuHeight + footerHeight);
        }
        Collapsed = !Collapsed;
    }
    
    /** Replaced by {@code resize(int,int)}.
     *  @param width
     *  @param height
     */
    @Deprecated
    @Override
    public void setSize(float width, float height) {
    }
}