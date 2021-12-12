/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.screen;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.break27.TodoGame3;

/**
 *
 * @author break27
 */
public abstract class AbstractScreen extends InputAdapter implements Screen {
    public static final class ScreenType {
        public static final int BANNER = 0;
        public static final int LOADING = 1;
        public static final int MAIN = 2;
        public static final int FINAL = 3;
        public static final int TEST = 4;
    }
    
    final TodoGame3 parent;
    final ScreenViewport defaultViewport;
    float delta;
    float state;
    
    SpriteBatch batch;
    Stage stage;
    
    AbstractScreen(TodoGame3 game) {
        this.parent = game;
        this.defaultViewport = new ScreenViewport();
    }
    
    public abstract int getId();
    
    void change(AbstractScreen screen) {
        this.parent.setScreen(screen);
    }
    
    @Override
    public void resize(int width, int height) {
        this.defaultViewport.update(width, height);
    }
}
