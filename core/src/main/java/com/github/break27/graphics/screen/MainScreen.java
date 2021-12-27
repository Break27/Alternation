/**************************************************************************
 * Copyright (c) 2021 Breakerbear
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *************************************************************************/

package com.github.break27.graphics.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.break27.Game3;
import com.github.break27.graphics.ui.window.BrowserWindow;
import com.github.break27.graphics.ui.window.HtmlViewerWindow;
import com.github.break27.graphics.ui.window.TestWindow;
import com.github.break27.graphics.ui.window.ViewpointWindow;

/**
 *
 * @author break27
 */
public class MainScreen extends AbstractScreen {
    
    public MainScreen(Game3 game) {
        super(game);
    }

    ViewpointWindow window;
    TestWindow window2;
    HtmlViewerWindow window3;
    BrowserWindow window4;
    
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

class MultiWindowScreen extends AbstractScreen {

    public MultiWindowScreen(Game3 game) {
        super(game);
    }

    @Override
    public int getId() {
        return ScreenType.MULTI;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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

class SingleWindowScreen extends AbstractScreen {

    public SingleWindowScreen(Game3 game) {
        super(game);
    }

    @Override
    public int getId() {
        return ScreenType.SINGLE;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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