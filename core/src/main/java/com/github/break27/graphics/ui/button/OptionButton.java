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

package com.github.break27.graphics.ui.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.github.break27.system.AlterAssetManager;
import com.kotcrab.vis.ui.widget.PopupMenu;

/**
 *
 * @author break27
 */
public class OptionButton extends TitleButton {

    PopupMenu menu;

    public OptionButton() {
        this(null);
    }

    public OptionButton(PopupMenu menu) {
        this.menu = menu;
        createListeners();
    }

    public void setPopupMenu(PopupMenu menu) {
        this.menu = menu;
    }

    public PopupMenu getPopupMenu() {
        if(menu == null) menu = new PopupMenu();
        return menu;
    }
    
    @Override
    public void styleApply(AlternativeSkin skin) {
        super.styleApply(skin);
        setImage(skin.getDrawable("icon-bars-horizontal"));
    }

    private void createListeners() {
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menu.toFront();
                return true;
            }
        });
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menu.showMenu(actor.getStage(), actor);
            }
        });
    }
}