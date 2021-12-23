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

package com.github.break27.graphics.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.github.break27.graphics.ui.window.SerializableWindow;
import com.kotcrab.vis.ui.widget.VisScrollPane;

/**
 *
 * @author break27
 */
public class AlterScrollPane extends VisScrollPane implements AlternativeWidget {
    
    SerializableWindow window;
    
    public AlterScrollPane(Actor widget) {
        super(widget);
        setScrollbarsOnTop(true);
        setupFadeScrollBars(0.75f, 1);
        
        setStyleEnabled();
    }
    
    public void listenTo(SerializableWindow window) {
        this.window = window;
    }
    
    @Override
    public void styleApply() {
    }

    @Override
    public void localeApply() {
    }
    
    @Override
    public void destroy() {
    }
}
