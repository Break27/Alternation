/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.break27.TodoGame3;
import com.github.break27.graphics.Viewpoint;
import com.github.break27.graphics.ui.window.TestWindow;
import com.github.break27.graphics.ui.window.ViewpointWindow;

/**
 *
 * @author break27
 */
public class MainScreen extends AbstractScreen{
    
    public MainScreen(TodoGame3 game) {
        super(game);
    }
    
    ViewpointWindow window;
    TestWindow window2;
    Viewpoint viewpoint;
    
    @Override
    public int getId() {
        return MAIN;
    }
    
    @Override
    public void show() {
        TextureAtlas atlas = parent.Asset.get("ui/visui/skin.atlas");
        window = new ViewpointWindow("Main View", atlas, 250, 181);
        window2 = new TestWindow("Test Window");
        window2.setTextureAtlas(atlas);
        window.setPosition(parent.Width/2, parent.Height/2);
        stage = new Stage();
        window.setStage(stage);
        window2.setStage(stage);
        
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        state += Gdx.graphics.getDeltaTime();
        window.update();
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
    
    @Override
    public boolean keyDown(int keycode) {
        
        return false;
    }
}
