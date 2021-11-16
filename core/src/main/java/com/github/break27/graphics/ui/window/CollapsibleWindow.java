/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.github.break27.graphics.ui.button.CollapseButton;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import java.util.HashMap;

/**
 *
 * @author break27
 */
public abstract class CollapsibleWindow extends VisWindow {
    public static final int VIEW = 1;
    public static final int TEST = 2;

    VisTable contentTable;
    Table titleTable;
    TextureAtlas atlas;
    VisImageButton collapseButton;

    int labelHeight;
    int contentHeight;
    boolean ignoreOnFocus = false;
    
    public String name;
    private boolean Collapsed = false;
    private boolean Focused = false;
    
    protected static HashMap<Integer,CollapsibleWindow> windows;
    protected final int ID;
    
    CollapsibleWindow(String name) {
        this(name, null);
    }
    
    CollapsibleWindow(String name, TextureAtlas atlas) {
        super(name);
        ID = this.hashCode();
        contentTable = new VisTable();
        titleTable = getTitleTable();
        add(contentTable).expand().fill();
        
        if(windows == null) windows = new HashMap<>();
        windows.put(ID, this);
        
        this.atlas = atlas;
        this.name = name;
    }
    
    public abstract int getWindowType();
    public abstract void create();
    
    public int getId() {
        return this.ID;
    }
    
    public boolean isCollapsed() {
        return this.Collapsed;
    }
    
    public boolean isFocused() {
        return this.Focused;
    }
    
    public Cell addContent(Actor actor) {
        contentTable.setHeight(actor.getHeight());
        return contentTable.add(actor);
    }
    
    public void setTitleImage(Image image) {
        titleTable.getCell(getTitleLabel()).padLeft(image.getWidth() + 5f);
        titleTable.addActorAt(0, image);
    }
    
    @Override
    public void addCloseButton() {
        Label titleLabel = getTitleLabel();
        if(getWindowType() != TEST) {
            collapseButton = new CollapseButton(new Image(atlas.createSprite("icon-collapse")).getDrawable());
        } else {
            // VisImageButton fallback
            collapseButton = new VisImageButton("close-window");
        }
        titleTable.add(collapseButton).padRight(-getPadRight() + 0.7f);
        collapseButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                // debug
                if(getWindowType() != TEST)
                    collapse();
                else 
                    close();
            }
        });
        collapseButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                focusProcess();
                event.cancel();
                return true;
            }
        });
        if (titleLabel.getLabelAlign() == Align.center && titleTable.getChildren().size == 2)
                titleTable.getCell(titleLabel).padLeft(collapseButton.getWidth() * 2);
    }
    
    @Override
    public void close() {
        collapseButton.getColor().a = 0;
        super.close();
    }
    
    public void collapse() {
        contentTable.setVisible(Collapsed);
        if(!Collapsed) {
            super.setHeight(labelHeight);
            super.setPosition(super.getX(), super.getY() + contentHeight);
        } else {
            super.setHeight(labelHeight + contentHeight);
            super.setPosition(super.getX(), super.getY() - contentHeight);
        }
        Collapsed = !Collapsed;
    }
    
    @Override
    public void setStage(Stage stage) {
        if(stage != null) {
            super.setStage(stage);
            stage.addActor(this);
        }
        create();
    }
    
    @Override
    public void pack() {
        labelHeight = (int)super.getMinHeight();
        contentHeight = (int)contentTable.getHeight();
        addCloseButton();
        setListener();
        if(!Focused) this.setFocusLost();
        super.pack();
    }
    
    public void setTextureAtlas(TextureAtlas atlas) {
        this.atlas = atlas;
    }
    
    public TextureAtlas getTextureAtlas() {
        return this.atlas;
    }
    
    public void setListener() {
        this.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                focusProcess();
                return true;
            }
        });
    }
    
    public void setFocus() {
        this.Focused = true;
        this.getColor().a = 1f;
        this.collapseButton.getColor().a = 1f;
    }
    
    public void setFocusLost() {
        // it never "loses focus" if true
        if(!ignoreOnFocus) {
            this.Focused = false;
            this.getColor().a = 0.75f;
            this.collapseButton.getColor().a = 0;
        }
    }
    
    public void setIgnoreOnFocus(boolean ignore) {
        this.ignoreOnFocus = ignore;
    }
    
    private void focusProcess() {
        for(CollapsibleWindow window : windows.values()) {
            if(window.ID != this.ID) {
                window.setFocusLost();
            } else {
                window.setFocus();
            }
        }
    }
}