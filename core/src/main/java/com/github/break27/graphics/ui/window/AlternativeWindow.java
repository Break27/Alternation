/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.github.break27.graphics.ui.button.CloseButton;
import com.github.break27.graphics.ui.widget.AlterLabel;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 *
 * @author break27
 */
public abstract class AlternativeWindow extends VisWindow implements AlternativeWidget {
    VisTable menuTable;
    VisTable contentTable;
    VisTable footerTable;
    VisTable titleBarButtonsTable;
    VisTable subTitleTable;
    
    Image titleImage;
    AlterLabel titleLabel;

    private boolean windowCreated = false;
    
    public AlternativeWindow(String name) {
        super(name);
        titleBarButtonsTable = new VisTable();
        subTitleTable = new VisTable();
        // default pad style
        padLeft(4f);
        padRight(4f);
        // reset the default Title Table
        getTitleTable().clear();
        // replace the old label
        subTitleTable.add(titleLabel);
        // tables
        getTitleTable().add(subTitleTable).expand().left();
        getTitleTable().add(titleBarButtonsTable).padRight(-getPadRight() + 0.7f).right();
        // panel
        add(createPanel());
        
        setStyleEnabled();
    }
    
    public abstract void create();
    
    public abstract void setListeners();
    
    public Table getMenuTable() {
        return menuTable;
    }
    
    public Table getContentTable() {
        return contentTable;
    }
    
    public Table getFooterTable() {
        return footerTable;
    }
    
    public Table getSubTitleTable() {
        return subTitleTable;
    }
    
    @Override
    public Label getTitleLabel() {
        if(titleLabel == null) titleLabel = new AlterLabel(super.getTitleLabel().getText().toString());
        return titleLabel;
    }
    
    public void setTitleImage(Drawable drawable) {
        if(titleImage == null) {
            titleImage = new Image(drawable);
            subTitleTable.padLeft(titleImage.getWidth() + 5f);
            getTitleTable().addActorAt(0, titleImage);
        } else {
            titleImage.setDrawable(drawable);
        }
    }
    
    public void addTitleTableButton(VisImageButton button) {
        titleBarButtonsTable.add(button);
        if (getTitleLabel().getLabelAlign() == Align.center && getTitleTable().getChildren().size == 2)
                getTitleTable().getCell(getTitleLabel()).padLeft(button.getWidth() * 2);
    }
    
    public void append(Stage stage) {
        if(!windowCreated) {
            setStage(stage);
            setListeners();
            create();
            pack();
            stage.addActor(this);
            windowCreated = true;
        }
    }
    
    @Override
    public void addCloseButton() {
        CloseButton closeButton = new CloseButton();
        addTitleTableButton(closeButton);
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
        windowCreated = false;
        destroy();
        super.close();
    }
    
    private VisTable createPanel() {
        VisTable panel = new VisTable();
        menuTable = new VisTable();
        panel.add(menuTable).expand().fill();
        panel.row();
        
        contentTable = new VisTable();
        panel.add(contentTable).expand().fill();
        panel.row();
        
        footerTable = new VisTable();
        panel.add(footerTable).expand().fill();
        return panel;
    }
}