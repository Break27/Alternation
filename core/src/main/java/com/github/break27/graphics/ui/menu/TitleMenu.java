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

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.break27.graphics.ui.window.CollapsibleWindow;
import com.github.break27.graphics.ui.window.ViewpointWindow;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;

/**
 *
 * @author break27
 */
public class TitleMenu extends AlternativeMenu {
    
    CollapsibleWindow window;
    // items
    MenuItem window_restore;
    MenuItem window_resize;
    MenuItem window_collapse;
    MenuItem window_maximize;
    MenuItem window_close;
    // icons
    Drawable icon_restore;
    Drawable icon_collapse;
    Drawable icon_extend;
    Drawable icon_maximize;
    Drawable icon_close;
    
    public TitleMenu(CollapsibleWindow window) {
        super("TitleMenu", window.getStage());
        this.window = window;
    }
    
    @Override
    public void update() {
        collapseProcess();
    }
    
        
    @Override
    public void listenTo(Table parent) {
        Group grand = parent.getParent();
        parent.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setFocus();
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // algorithm correction
                float modx = x + parent.getX() + grand.getX() + 2f;
                float mody = y + parent.getY() + grand.getY();
                if(button == Input.Buttons.RIGHT) show(modx, mody);
            }
        });
    }
    
    @Override
    protected void create(PopupMenu menu) {
        window_restore = new MenuItem("Restore", new Image(), new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("Restore!");
            }
        });
        window_resize = new MenuItem("Resize", new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                if(window instanceof ViewpointWindow)
                    ((ViewpointWindow)window).resizeDialogAppend();
            }
        });
        window_collapse = new MenuItem("Collapse", new Image(), new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                window.collapse();
            }
        });
        window_maximize = new MenuItem("Maximize", new Image(), new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("Maximize!");
            }
        });
        window_close = new MenuItem("Close", new Image(), new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                window.close();
            }
        });
        // layout
        menu.addItem(window_restore);
        menu.addItem(window_resize);
        menu.addItem(window_collapse);
        menu.addItem(window_maximize);
        menu.addSeparator();
        menu.addItem(window_close);
    }
    
    @Override
    public void styleApply() {
        window_restore.getImage().setDrawable(icon_restore = getAlterSkin().getDrawable("icon-window-restore"));
        window_collapse.getImage().setDrawable(icon_collapse = getAlterSkin().getDrawable("icon-window-collapse"));
        icon_extend = getAlterSkin().getDrawable("icon-window-extend");
        window_maximize.getImage().setDrawable(icon_maximize = getAlterSkin().getDrawable("icon-window-maximize"));
        window_close.getImage().setDrawable(icon_close = getAlterSkin().getDrawable("icon-window-close"));
    }
    
    @Override
    public void localeApply() {
        
    }

    private void collapseProcess() {
        window_restore.setDisabled(window.isCollapsed());
        window_resize.setDisabled(window.isCollapsed());
        if(window.isCollapsed()) {
            window_collapse.setText("Extend");
            window_collapse.getImage().setDrawable(icon_extend);
        } else {
            window_collapse.setText("Collapse");
            window_collapse.getImage().setDrawable(icon_collapse);
        }
    }
}
