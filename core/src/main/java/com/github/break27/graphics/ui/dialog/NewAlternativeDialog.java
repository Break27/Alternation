/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.dialog;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.break27.graphics.ui.window.AlternativeWindow;

/**
 *
 * @author break27
 */
public abstract class NewAlternativeDialog extends AlternativeWindow {
    
    NewAlternativeDialog(String name) {
        super(name);
        initialize();
    }
    
    private void initialize() {
        setModal(true);
        
    }
}
