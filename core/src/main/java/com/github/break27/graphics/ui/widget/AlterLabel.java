/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.github.break27.system.Resource;
import com.kotcrab.vis.ui.widget.VisLabel;

/**
 *
 * @author break27
 */
public class AlterLabel extends VisLabel implements AlternativeWidget {
    
    public AlterLabel() {
        this("");
    }
    
    public AlterLabel(String text) {
        super(text);
        setStyleEnabled();
    }
    
    @Override
    public void styleApply() {
        setStyle(new LabelStyle(getAlterFont(), Color.WHITE));
    }

    @Override
    public void localeApply() {
    }
    
    @Override
    public void destroy() {
    }
}