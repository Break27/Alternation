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

package com.github.break27;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.assets.AssetManager;
import com.github.break27.graphics.screen.FinalizingScreen;
import com.github.break27.graphics.screen.LoadingScreen;
import com.github.break27.launcher.LauncherAdapter;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Game3 extends Game {
    public AssetManager Asset;
    public LauncherAdapter Launcher;
    
    public Game3() {
    }
    
    public Game3(LauncherAdapter launcher) {
        this.Launcher = launcher;
    }
    
    @Override
    public void create() {
        initialize();
        //setScreen(new BannerScreen(this));
        setScreen(new LoadingScreen(this));
        //setScreen(new TestScreen(this));
    }
    
    private void initialize() {
        this.Asset = new AssetManager();
        Gdx.app.addLifecycleListener(new GameExitEventListener(this));
    }
}

class GameExitEventListener implements LifecycleListener {
    Game3 parent;
    
    public GameExitEventListener(Game3 parent) {
        this.parent = parent;
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void dispose() {
        parent.setScreen(new FinalizingScreen(parent));
    }
}   