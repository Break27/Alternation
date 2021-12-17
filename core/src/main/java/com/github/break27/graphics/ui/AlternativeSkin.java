/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.break27.system.SerializableResource;

/**
 *
 * @author break27
 */
public class AlternativeSkin extends Skin implements SerializableResource {
    
    public AlternativeSkin() {
    }
    
    public AlternativeSkin(AlternativeSkin parent) {
    }
    
    public AlternativeSkin(FileHandle file) {
        super(file);
    }
    
    public AlternativeSkin(FileHandle file, TextureAtlas atlas) {
        super(file, atlas);
    }

    @Override
    public void dispose() {
        
    }
}