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
import com.github.break27.TodoGame3;

/**
 *
 * @author break27
 */
public abstract class AbstractScreen extends InputAdapter implements Screen {
    public static final int BANNER = 0;
    public static final int LOADING = 1;
    public static final int MAIN = 2;
    public static final int FINAL = 3;
    
    TodoGame3 parent;
    SpriteBatch batch;
    Stage stage;
    float delta;
    float state;
    
    AbstractScreen(TodoGame3 game) {
        this.parent = game;
    }
    
    public abstract int getId();
    void changeTo(AbstractScreen screen) {
        this.parent.setScreen(screen);
    }
}
