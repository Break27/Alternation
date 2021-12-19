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
    
    public CollapsibleWindow(String name) {
        super(name);
    }
    
    public boolean isCollapsed() {
        return this.Collapsed;
    }
    
    public void addCollapseButton() {
        collapseButton = new CollapseButton();
        addTitleBarButton(collapseButton);
        collapseButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                collapse();
            }
        });
    }
    
    public void collapse() {
        contentTable.setVisible(Collapsed);
        if(!Collapsed) {
            super.setHeight(labelHeight);
            super.setPosition(super.getX(), super.getY() + contentHeight - labelHeight);
        } else {
            super.setHeight(contentHeight);
            super.setPosition(super.getX(), super.getY() - contentHeight + labelHeight);
        }
        Collapsed = !Collapsed;
    }
}
