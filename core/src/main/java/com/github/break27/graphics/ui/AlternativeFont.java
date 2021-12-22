/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.github.break27.system.Resource.SerializableResource;

/**
 *
 * @author break27
 */
public class AlternativeFont extends BitmapFont implements SerializableResource {
    
    public AlternativeFont(FileHandle fontFile) {
        super(fontFile);
    }
    
    public AlternativeFont(BitmapFont font) {
        this(font.getData(), font.getRegions(), font.usesIntegerPositions());
    }
    
    public AlternativeFont(BitmapFontData data, TextureRegion region, boolean integer) {
        super(data, region, integer);
    }
    
    public AlternativeFont(BitmapFontData data, Array<TextureRegion> regions, boolean integer) {
        super(data, regions, integer);
    }
}
