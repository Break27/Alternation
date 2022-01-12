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

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.break27.Game3;

/**
 *
 * @author break27
 */
public abstract class AbstractScreen extends InputAdapter implements Screen {
    
    final Game3 parent;
    final ScreenViewport defaultViewport;

    float delta;
    float state;
    
    SpriteBatch batch;
    Stage stage;
    
    AbstractScreen(Game3 game) {
        this.parent = game;
        this.defaultViewport = new ScreenViewport();
    }
    
    void change(AbstractScreen screen) {
        this.parent.setScreen(screen);
    }
    
    @Override
    public void resize(int width, int height) {
        this.defaultViewport.update(width, height);
    }
}
