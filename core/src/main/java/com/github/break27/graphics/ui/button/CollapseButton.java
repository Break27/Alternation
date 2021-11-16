/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.VisImageButton;

/**
 *
 * @author break27
 */
public class CollapseButton extends VisImageButton {
    
    ButtonStyle style;
    Drawable empty = new Image().getDrawable();
    
    /** Compatible with VisImageButton
     * @param icon
     */
    public CollapseButton(Drawable icon) {
        super(icon);
        style  = super.getStyle();
        // there should be no focus when the button is pressed.
        this.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                focusLost();
            }
        });
    }
}
