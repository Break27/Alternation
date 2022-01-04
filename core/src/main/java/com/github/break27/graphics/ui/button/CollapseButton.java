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

import com.github.break27.system.AlterAssetManager;

/**
 *
 * @author break27
 */
public class CollapseButton extends TitleButton {

    @Override
    public void styleApply(AlterAssetManager assets) {
        super.styleApply(assets);
        setImage(assets.getSkin().getDrawable("icon-collapse"));
    }
}