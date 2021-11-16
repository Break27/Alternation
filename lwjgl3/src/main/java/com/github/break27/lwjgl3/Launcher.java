/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.lwjgl3;

import com.github.break27.launcher.LauncherAdapter;

/**
 *
 * @author break27
 */
public class Launcher implements LauncherAdapter {

    @Override
    public String getGameDataLocation() {
        String osName = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");
        if (osName.contains("windows")) return home + "/Documents/MyLibGdxGame/";
        // For Linux, OSX and other Unix-like OSs.
        return home + "/.config/MyLibGdxGame/";
    }
    
}
