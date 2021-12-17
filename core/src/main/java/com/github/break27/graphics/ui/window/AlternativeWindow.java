/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.github.break27.graphics.ui.button.CloseButton;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 *
 * @author break27
 */
public abstract class AlternativeWindow extends VisWindow implements AlternativeWidget {
    Table menuTable;
    Table contentTable;
    Table footerTable;
    Table titleBarButtonsTable;
    
    Image titleImage;

    int labelHeight;
    int contentHeight;

    private boolean windowCreated = false;
    
    public AlternativeWindow(String name) {
        super(name);
        contentTable = new VisTable();
        titleBarButtonsTable = new VisTable();
        getTitleTable().add(titleBarButtonsTable).padRight(-getPadRight() + 0.7f).right();
        
        setMenuTable();
        add(contentTable).expand().fill().top();
        setFooterTable();
        
        enableStyle();
    }
    
    public abstract void create();
    
    public abstract void destroy();
    
    public abstract void setListeners();
    
    public Cell setContent(Actor actor) {
        contentTable.setHeight(actor.getHeight());
        return contentTable.add(actor);
    }
    
    public Table getContentTable() {
        return contentTable;
    }
    
    public Table getFooterTable() {
        return footerTable;
    }
    
    public void setTitleImage(Drawable drawable) {
        if(titleImage == null) {
            titleImage = new Image(drawable);
            getTitleTable().getCell(getTitleLabel()).padLeft(titleImage.getWidth() + 5f);
            getTitleTable().addActorAt(0, titleImage);
        } else {
            titleImage.setDrawable(drawable);
        }
    }
    
    public void addTitleBarButton(VisImageButton button) {
        titleBarButtonsTable.add(button);
        if (getTitleLabel().getLabelAlign() == Align.center && getTitleTable().getChildren().size == 2)
                getTitleTable().getCell(getTitleLabel()).padLeft(button.getWidth() * 2);
    }
    
    public void append(Stage stage) {
        setStage(stage);
        createWindow();
        stage.addActor(this);
    }
    
    @Override
    public void addCloseButton() {
        CloseButton closeButton = new CloseButton();
        addTitleBarButton(closeButton);
        closeButton.addListener(new ChangeListener() {
             @Override
             public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                 close();
             }
         });
    }
    
    @Override
    public void close() {
        titleBarButtonsTable.getColor().a = 0;
        super.close();
        destroy();
    }
    
    @Override
    public void pack() {
        labelHeight = (int)super.getMinHeight();
        contentHeight = (int)contentTable.getHeight();
        super.pack();
    }
    
    private void setFooterTable() {
        row();
        footerTable = new Table();
        add(footerTable).expand().fill().bottom();
    }
    
    private void setMenuTable() {
        row();
        menuTable = new Table();
        add(menuTable).expand().fill().bottom();
    }
    
    private void createWindow() {
        if(!windowCreated) {
            setListeners();
            create();
            pack();
            windowCreated = true;
        }
    }
}