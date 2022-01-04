/**************************************************************************
 * Copyright (c) 2022 Breakerbear
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

import com.badlogic.gdx.graphics.Color;
import com.github.break27.graphics.ui.StyleAppliedWidget;
import com.github.break27.system.AlterAssetManager;
import com.kotcrab.vis.ui.widget.VisLabel;

/**
 * @author break27
 */
public class AlterLabel extends VisLabel implements StyleAppliedWidget {

    public AlterLabel() {
        this("");
    }

    public AlterLabel(CharSequence text) {
        this(text, Color.WHITE);
    }

    public AlterLabel(CharSequence text, Color color) {
        super(text, color);
        register();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void styleApply(AlterAssetManager assets) {
        LabelStyle style = assets.getSkin().get(LabelStyle.class);
        style.font = assets.getSkin().getDefaultFont();
        setStyle(style);
    }
}
