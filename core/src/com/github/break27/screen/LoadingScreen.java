/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.break27.GameAssets;
import com.github.break27.TodoGame;

/**
 *
 * @author break27
 */
public class LoadingScreen implements Screen{
    private TodoGame parent;
    public LoadingScreen(TodoGame game) {
        parent = game;
    }
    
    // 声明舞台
    private Stage stage;
    
    float stateTime;
    
    @Override
    public void show() {
        // 设定默认资源管理器
        GameAssets.setManager(parent.Manager);
        // 载入游戏资源
        GameAssets.load();
    }

    @Override
    public void render(float delta) {
        // 将背景设为黑色
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // 更新资源管理器
        parent.Manager.update();
        stateTime += Gdx.graphics.getDeltaTime();
        // To be removed
        if (stateTime > 1) parent.changeScreen(parent.MENU);
    }

    @Override
    public void resize(int width, int height) {
        parent.Viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    
}
