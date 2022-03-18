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

package com.github.break27.graphics.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.break27.graphics.ui.LocalizableWidget;
import com.github.break27.graphics.ui.StyleAppliedWidget;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 *
 * @author break27
 */
public abstract class AlternativeMenu extends VisWindow
        implements StyleAppliedWidget, LocalizableWidget {

    private final AlterPopupMenu menu;
    private boolean isEntered = false;

    protected boolean shown = false;
    
    public AlternativeMenu(String name, Stage stage) {
        super(name, false);
        menu = new AlterPopupMenu();
        setStage(stage);
        stage.addActor(this);
        
        getTitleTable().clear();
        pad(0);
        setVisible(false);
        pack();
        
        register();
    }
    
    public abstract void listenTo(Table parent);
    
    public abstract void update();
    
    protected abstract void create(PopupMenu menu);
    
    protected void listeners() {
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
    
    public void show(float x, float y) {
        if(Gdx.input.getY() < getHeight()) y -= getHeight();
        setPosition(x, y);
        setVisible(true);
        shown = true;
    }
    
    public void hide() {
        setVisible(false);
        getStage().unfocus(this);
        setPosition(0, 0);
        shown = false;
    }
    
    @Override
    public void pack() {
        create(this.menu);
        add(menu).expand().fill();
        listeners();
        super.pack();
    }
    
    @Override
    public void destroy() {
    }
}

class AlterPopupMenu extends PopupMenu {
    
    public AlterPopupMenu() {
    }

    @Override
    @Deprecated
    public boolean remove() {
        return false;
    }

    public boolean removeWidget() {
        return super.remove();
    }
}