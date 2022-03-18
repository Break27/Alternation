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

package com.github.break27.graphics.screen;

import com.github.break27.Game3;
import com.github.break27.graphics.ui.Widgets;
import com.github.break27.system.Resource;
import com.kotcrab.vis.ui.VisUI;

/**
 *
 * @author break27
 */
public class FinalizingScreen extends AbstractScreen {
    
    public FinalizingScreen(Game3 game) {
        super(game);
    }

    @Override
    public void show() {
        /* FINALIZING */
        try {
            Widgets.destroyAll();
            VisUI.dispose();
        } catch(Exception ignored) {
        }
    }

    @Override
    public void render(float f) {
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
