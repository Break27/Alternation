/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.github.break27.graphics.ui.window.SerializableWindow;
import com.kotcrab.vis.ui.widget.VisScrollPane;

/**
 *
 * @author break27
 */
public class AlterScrollPane extends VisScrollPane implements AlternativeWidget {
    
    SerializableWindow window;
    
    public AlterScrollPane(Actor widget) {
        super(widget);
        setStyleEnabled();
    }
    
    public void listenTo(SerializableWindow window) {
        this.window = window;
    }
    
    @Override
    public void styleApply() {
        setScrollbarsOnTop(true);
        setupFadeScrollBars(0.75f, 1);
    }

    @Override
    public void localeApply() {
    }
    
    @Override
    public void destroy() {
    }
}
