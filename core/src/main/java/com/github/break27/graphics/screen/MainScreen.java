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
import com.github.break27.graphics.ui.Widgets;
import com.github.break27.graphics.ui.window.*;
import com.github.break27.system.shell.KotlinShell;

/**
 *
 * @author break27
 */
public class MainScreen extends AbstractScreen {
    
    public MainScreen(Game3 game) {
        super(game);
    }

    KotlinShell shell;

    ViewpointWindow window;
    TestWindow window2;
    HtmlViewerWindow window3;
    BrowserWindow window4;
    ConsoleWindow window5;
    
    @Override
    public void show() {
        Widgets.init(parent.AssetManager);

        window = new ViewpointWindow(300, 300);
        window2 = new TestWindow("Test Window 中文测试 - 01234");
        window3 = new HtmlViewerWindow(500, 500);
        window4 = new BrowserWindow(500, 500);
        window5 = new ConsoleWindow(500, 500);
        window.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
        stage = new Stage(defaultViewport);

        /*
        // debug
        shell = new KotlinShell();
        shell.setOutputCallback(result -> {
            System.out.println(result);
        });
        shell.run();

        //shell.send("println(\"Hello World! test = \" + (2 + 4) )");
        shell.send("println(\"Hello World! test = \" + (5 + 9) )");
        shell.send("Math.pow(10.0, 5.0)");
        shell.send(":help");
         */

        window.append(stage);
        window2.append(stage);
        window3.append(stage);
        window4.append(stage);
        window5.append(stage);
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
        //shell.halt();
    }
}

class MultiWindowScreen extends MainScreen {

    public MultiWindowScreen(Game3 game) {
        super(game);
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
        super.dispose();
    }
}

class FullScreen extends MainScreen {

    public FullScreen(Game3 game) {
        super(game);
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
        super.dispose();
    }
}