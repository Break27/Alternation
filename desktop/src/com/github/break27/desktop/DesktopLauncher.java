package com.github.break27.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.break27.TodoGame;

<<<<<<< Updated upstream
public class DesktopLauncher {
	public static void main (String[] arg) {
                System.setProperty("file.encoding", "UTF-8");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new TodoGame(), config);
	}
=======
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.utils.ScreenUtils;

public class DesktopLauncher implements Launcher {
    
    public static fxApp app;
    private static String[] arguments;
    public static void main (String[] args) {
        /*
            System.setProperty("file.encoding", "UTF-8");
            //LwjglApplicationConfiguration config = getPrefs(); todo
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            new LwjglApplication(new TodoGame(), config);
        */
        arguments = args;
        app = new fxApp();
        System.setProperty("file.encoding", "UTF-8");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new TodoGame(), config);
        
        app.loopRun();
        fxAppThread.start();
        while(true) app.passFrameData(TodoGame.getPixelData());
    }

    public static Thread fxAppThread = new Thread(new Runnable() {
        @Override
        public void run() {
            app.launch(fxApp.class, arguments);
        }
    });
    
    public static boolean isAppOnClose() {
        return fxAppThread.isAlive();
    }
    
    /*
    private static LwjglApplicationConfiguration getPrefs() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        // 设定设置目录
        config.preferencesDirectory = getPrefsPath();
        // 读取设置文件
        Preferences platform = Gdx.app.getPreferences("platform");
        readPrefsPlatform(platform, config);
        
        Preferences options = Gdx.app.getPreferences("options");
        return config;
    }
    
    private static String getPrefsPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) return "Documents/TodoGame/";
        // For Linux, OSX or other Unix-like OS.
        return ".prefs/TodoGame/";
    }
    
    private static void readPrefsPlatform(Preferences platform, LwjglApplicationConfiguration config) {
        // 检测版本
        Gdx.app.log(DesktopLauncher.class.getName(), "Launcher has started up. Current version: " + VERSION);
        String appliedVersion = platform.getString("appliedVersion");
        
        if (appliedVersion != null) {
            int ver = Integer.valueOf(appliedVersion.split("-")[0]);
            Gdx.app.log(DesktopLauncher.class.getName(), "Loading preference, target version: " + appliedVersion);
            if (ver < Integer.valueOf(VERSION)) {
                Gdx.app.error(DesktopLauncher.class.getName(), "The config was created by an older version of the game!");
            } else if(ver > Integer.valueOf(VERSION)) {
                Gdx.app.error(DesktopLauncher.class.getName(), "The config was created by an newer version of the game!");
            }
        } else {
            Gdx.app.log(DesktopLauncher.class.getName(), "Preference file doesn't exist.");
            
        }
    }
    */
>>>>>>> Stashed changes
}
