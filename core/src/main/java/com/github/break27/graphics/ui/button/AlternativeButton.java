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
        setStyleEnabled();
    }
    
    public void setImage(Drawable drawable) {
        if(image == null) {
            image = new Image(drawable);
            getImageCell().setActor(image);
        } else {
            image.setDrawable(drawable);
        }
    }
    
    @Override
    public void styleApply() {
        setStyle(getAlterSkin().get(VisImageButtonStyle.class));
    }
    
    @Override
    public void destroy() {
    }
}
