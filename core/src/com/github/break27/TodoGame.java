package com.github.break27;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.github.break27.screen.DemoScreen;
import com.github.break27.screen.FinalizingScreen;
import com.github.break27.screen.InGameScreen;
import com.github.break27.screen.LoadingScreen;
import com.github.break27.screen.MenuScreen;
import com.github.break27.screen.StartupScreen;

public class TodoGame extends Game {
    // 声明游戏界面
    public StartupScreen startupScreen;
    public LoadingScreen loadingScreen;
    public MenuScreen menuScreen;
    public InGameScreen gameScreen;
    public DemoScreen demoScreen;
    public FinalizingScreen finalScreen;
    
    // 游戏界面数值常量
    public final int STARTUP = 0;
    public final int LOADING = 1;
    public final int MENU = 2;
    public final int GAME = 3;
    public final int DEMO = 4;
    public final int FINAL = 5;
    
    // 声明 Viewport
    public StretchViewport Viewport;
    // 声明资源加载器
    public AssetManager Manager;

    public void changeScreen(int screen) {
        switch(screen) {
            case STARTUP:
                if (startupScreen == null) startupScreen = new StartupScreen(this);
                this.setScreen(startupScreen);
                break;
            case LOADING:
                if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
                this.setScreen(loadingScreen);
                break;
            case MENU:
                if (menuScreen == null) menuScreen = new MenuScreen(this);
                this.setScreen(menuScreen);
                break;
            case GAME:
                if (gameScreen == null) gameScreen = new InGameScreen(this);
                this.setScreen(gameScreen);
                break;
            case DEMO:
                if (demoScreen == null) demoScreen = new DemoScreen(this);
                this.setScreen(demoScreen);
                break;
            case FINAL:
                if (finalScreen == null) finalScreen = new FinalizingScreen(this);
                this.setScreen(finalScreen);
                break;
        }
    }
    
    @Override
    public void create () {
        // 实例化 Viewport
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        Viewport = new StretchViewport(width, height);
        // 实例化资源加载器
        Manager = new AssetManager();
        // 设定游戏文件夹
        GameAssets.setDefaultPath(Gdx.files.getExternalStoragePath());
        // 进入初始界面
        changeScreen(STARTUP);
    }
}
