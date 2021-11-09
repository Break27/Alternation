/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.lwjgl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author break27
 */
public class LwjglAdapter {
    
    private int width;
    private int height;
    public LwjglAdapter(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    boolean doInit = true;
    public void start() {
        // initialize OpenGL
        if(doInit) {
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, width, 0, height, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            doInit = false;
        }
        // start render
        render();
        update();
    }
    
    void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        // set color
        GL11.glColor3f(0.5f, 0.5f, 1.0f);
        // Render a quad
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(100, 100);
        GL11.glVertex2d(100+200, 100);
        GL11.glVertex2d(100+200, 100+200);
        GL11.glVertex2d(100, 100+200);
        GL11.glEnd();
    }
    
    void update() {
        
    }
}
