/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 *
 * @author break27
 */
public abstract class AbstractMenu extends VisWindow {
    
    TextureAtlas atlas;
    AlternativePopupMenu menu;
    boolean isEntered = false;
    
    public AbstractMenu(String name, Stage stage) {
        this(name, null, stage);
    }
    
    public AbstractMenu(String name, TextureAtlas atlas, Stage stage) {
        super(name, false);
        setStage(stage);
        menu = new AlternativePopupMenu();
        this.atlas = atlas;
        getTitleTable().clear();
        pad(0);
        setVisible(false);
        pack();
    }
    
    public abstract void listenTo(Table parent);
    
    protected abstract void create(PopupMenu menu, TextureAtlas atlas);
    
    public void createListeners() {
        getStage().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!isEntered) hide();
                return true;
            }
        });
        menu.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hide();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isEntered = true;
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isEntered = false;
            }
        });
    }
    
    public void show (float x, float y) {
        if((Gdx.graphics.getHeight() - y) < getHeight()) y -= getHeight();
        setPosition(x, y);
        setVisible(true);
    }
    
    public void hide() {
        setVisible(false);
    }
    
    public void setFocus() {
        this.toFront();
    }
    
    @Override
    public void pack() {
        create(this.menu, this.atlas);
        add(menu).expand().fill();
        createListeners();
        super.pack();
    }
    
    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.addActor(this);
    }
}

class AlternativePopupMenu extends PopupMenu {
    public AlternativePopupMenu() {
    }

    @Override
    public boolean remove() {
        return false;
    }

    public boolean removeWidget() {
        return super.remove();
    }
}


