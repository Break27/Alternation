package com.github.break27.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.break27.TodoGame;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.utils.ScreenUtils;

public class DesktopLauncher implements Launcher {
    
    private static String[] arguments;
    public static void main (String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        //LwjglApplicationConfiguration config = getPrefs(); todo
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new TodoGame(), config);
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
}
