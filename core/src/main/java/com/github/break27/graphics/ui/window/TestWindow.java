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

package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 *
 * @author break27
 */
public class TestWindow extends SerializableWindow {
    Image image;
    Texture texture;
    
    public TestWindow(String name) {
        super(name);
        addCloseButton();
    }
    
    @Override
    public int getType() {
        return WindowType.TEST;
    }
    
    @Override
    public void create() {
        texture = new Texture(Gdx.files.internal("banner/libgdx.png"));
        image = new Image(texture);
        getContentTable().add(image);
    }
    
    @Override
    public void destroy() {
        image.remove();
        texture.dispose();
    }
    
    @Override
    public void styleApply() {
        super.styleApply();
        setTitleImage(getAlterSkin().getDrawable("icon20-application"));
    }
    
    @Override
    public void localeApply() {
        
    }
}
