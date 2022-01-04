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

import com.badlogic.gdx.utils.Array;

/**
 *
 * @author break27
 */
public interface AlternativeWidget {

    /** Register a widget to Map.
     */
    default void register() {
        if(Widgets.Map.containsKey(this.getClass())) {
            Widgets.Map.get(this.getClass()).add(this);
        } else {
            Widgets.Map.put(this.getClass(), new Array<>(new AlternativeWidget[]{this}));
        }
        // apply immediately if available
        if(Widgets.isAssetsSet())
            Widgets.applyAll(Widgets.temporaryAssets);
    }

    /** Destroy widget. Note: Normally it would not
     * remove the widget from the stage directly.
     * */
    void destroy();
}