/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.button;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.kotcrab.vis.ui.widget.VisImageButton;

/**
 *
 * @author break27
 */
public abstract class AlternativeButton extends VisImageButton implements AlternativeWidget {
    
    Image image;
    
    public AlternativeButton(Drawable icon) {
        super(icon);
        enableStyle();
    }
    
    public void setImage(Drawable drawable) {
        if(image == null) {
            image = new Image(drawable);
            getImageCell().setActor(image);
        } else {
            image.setDrawable(drawable);
        }
    }
}
