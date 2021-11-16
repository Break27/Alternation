/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.break27.graphics.ui.window.CollapsibleWindow;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;

/**
 *
 * @author break27
 */
public class WindowTitleMenu extends AbstractMenu {
    
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
    
    public WindowTitleMenu(Stage stage, CollapsibleWindow window, TextureAtlas atlas) {
        super("window", atlas, stage);
        this.window = window;
    }
    
    @Override
    protected void create(PopupMenu menu, TextureAtlas atlas) {
        createImage();
        window_restore = new MenuItem("Restore", icon_restore, new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("Restore!");
            }
        });
        window_resize = new MenuItem("Resize", new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                    System.out.println("Resize!");
            }
        });
        window_collapse = new MenuItem("Collapse", icon_collapse, new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                window.collapse();
                if(window.isCollapsed()) { 
                    window_collapse.setText("Extend");
                    window_collapse.getImage().setDrawable(icon_extend);
                } else {
                    window_collapse.setText("Collapse");
                    window_collapse.getImage().setDrawable(icon_collapse);
                }
            }
        });
        window_maximize = new MenuItem("Maximize", icon_maximize, new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("Maximize!");
            }
        });
        window_close = new MenuItem("Close", icon_close, new ChangeListener() {
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
    
    private void createImage() {
        icon_restore = new Image(atlas.createSprite("icon-window-restore")).getDrawable();
        icon_collapse = new Image(atlas.createSprite("icon-window-collapse")).getDrawable();
        icon_extend = new Image(atlas.createSprite("icon-window-extend")).getDrawable();
        icon_maximize = new Image(atlas.createSprite("icon-window-maximize")).getDrawable();
        icon_close = new Image(atlas.createSprite("icon-window-close")).getDrawable();
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
}
