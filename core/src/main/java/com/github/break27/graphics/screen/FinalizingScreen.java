/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.screen;

import com.github.break27.TodoGame3;
import com.kotcrab.vis.ui.VisUI;

/**
 *
 * @author break27
 */
public class FinalizingScreen extends AbstractScreen {
    
    public FinalizingScreen(TodoGame3 game) {
        super(game);
    }
    
    @Override
    public int getId() {
        return FINAL;
    }

    @Override
    public void show() {
        /* FINALIZING */
        parent.Asset.finishLoading();
        VisUI.dispose();
    }

    @Override
    public void render(float f) {
    }

    @Override
    public void resize(int i, int i1) {
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
