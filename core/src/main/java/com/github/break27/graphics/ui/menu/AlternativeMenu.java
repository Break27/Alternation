/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 *
 * @author break27
 */
public abstract class AlternativeMenu extends VisWindow implements AlternativeWidget {
    
    AlternativePopupMenu menu;
    boolean isEntered = false;
    
    public AlternativeMenu(String name, Stage stage) {
        super(name, false);
        setStage(stage);
        menu = new AlternativePopupMenu();
        getTitleTable().clear();
        pad(0);
        setVisible(false);
        pack();
    }
    
    public abstract void listenTo(Table parent);
    public abstract void update();
    
    protected abstract void create(PopupMenu menu);
    
    public void createListeners() {
        getStage().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!isEntered) hide();
                // update input
                update();
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
        if(Gdx.input.getY() < getHeight()) y -= getHeight();
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
        create(this.menu);
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


