/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.launcher;

/**
 *
 * @author break27
 */
public interface LauncherAdapter {
    // package info
    public static final String VERSION = "0.0.3";
    public static final String RELEASE = "SNAPSHOT";
    
    // game filepaths
    public String getGameDataLocation();
}
