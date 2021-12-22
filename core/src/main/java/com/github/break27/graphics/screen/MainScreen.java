/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.break27.TodoGame3;
import com.github.break27.graphics.Viewpoint;
import com.github.break27.graphics.ui.window.BrowserWindow;
import com.github.break27.graphics.ui.window.HtmlViewerWindow;
import com.github.break27.system.Resource;
import com.github.break27.graphics.ui.window.TestWindow;
import com.github.break27.graphics.ui.window.ViewpointWindow;

/**
 *
 * @author break27
 */
public class MainScreen extends AbstractScreen {
    
    public MainScreen(TodoGame3 game) {
        super(game);
    }

    ViewpointWindow window;
    TestWindow window2;
    HtmlViewerWindow window3;
    BrowserWindow window4;
    Viewpoint viewpoint;
    
    @Override
    public int getId() {
        return ScreenType.MAIN;
    }
    
    @Override
    public void show() {
        window = new ViewpointWindow("Main View 中T文E测S试T", 300, 300);
        window2 = new TestWindow("Test Window 中文测试 - 0123456789");
        window3 = new HtmlViewerWindow("HTML Viewer 中文测试", 500, 500);
        window4 = new BrowserWindow("Browser", 500, 500);
        window.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        
        stage = new Stage(defaultViewport);
        window.append(stage);
        window2.append(stage);
        window3.append(stage);
        window4.append(stage);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        state += Gdx.graphics.getDeltaTime();
        //window.update();
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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
