/**************************************************************************
 * Copyright (c) 2021 Breakerbear
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *************************************************************************/

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
