/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.button;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 *
 * @author break27
 */
public abstract class TitleButton extends AlternativeButton {
    
    public TitleButton() {
        super(null);
        // there should be no focus when the button is pressed.
        this.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                focusLost();
                return true;
            }
        });
    }
    
    @Override
    public void localeApply() {
    }
}