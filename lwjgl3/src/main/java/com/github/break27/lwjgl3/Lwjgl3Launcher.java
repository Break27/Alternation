package com.github.break27.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.break27.TodoGame3;
import com.github.break27.launcher.LauncherAdapter;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        createApplication(new Launcher());
    }

    private static Lwjgl3Application createApplication(Launcher launcher) {
        return new Lwjgl3Application(new TodoGame3(launcher), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("TodoGame3");
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        configuration.setWindowedMode(640, 480);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}

class Launcher implements LauncherAdapter {
    
    @Override
    public String getGameDataLocation() {
        String osName = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");
        if (osName.contains("windows")) return home + "/Documents/MyLibGdxGame/";
        // For Linux, OSX and other Unix-like OSs.
        return home + "/.config/MyLibGdxGame/";
    }
}