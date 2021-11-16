package com.github.break27;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.assets.AssetManager;
import com.github.break27.graphics.screen.BannerScreen;
import com.github.break27.graphics.screen.FinalizingScreen;
import com.github.break27.graphics.screen.LoadingScreen;
import com.github.break27.launcher.LauncherAdapter;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class TodoGame3 extends Game {
    public AssetManager Asset;
    public LauncherAdapter Launcher;
    public int Width;
    public int Height;
    
    public TodoGame3(LauncherAdapter launcher) {
        this.Launcher = launcher;
    }
    
    @Override
    public void create() {
        initialize();
        //setScreen(new BannerScreen(game));
        setScreen(new LoadingScreen(this));
    }
    
    private void initialize() {
        this.Width = Gdx.graphics.getWidth();
        this.Height = Gdx.graphics.getHeight();
        this.Asset = new AssetManager();
        
        Gdx.app.addLifecycleListener(new GameExitEventListener(this));
    }
}

class GameExitEventListener implements LifecycleListener {
    TodoGame3 parent;
    
    public GameExitEventListener(TodoGame3 parent) {
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