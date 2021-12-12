/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.dialog;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;

/**
 *
 * @author break27
 */
public abstract class AlternativeDialog extends VisDialog {
    
    TextureAtlas atlas;
    VisImageButton closeButton;
    
    private boolean dialogCreated = false;
    
    public AlternativeDialog(String name) {
        this(name, null);
    }
    
    public AlternativeDialog(String name, TextureAtlas atlas) {
        super(name);
        this.atlas = atlas;
    }
    
    public abstract void create();
    
    public abstract void destroy();
    
    public void setTitleImage(String name) {
        setTitleImage(new Image(atlas.createSprite(name)));
    }
    
    public void setTitleImage(Image image) {
        getTitleTable().getCell(getTitleLabel()).padLeft(image.getWidth() + 5f);
        getTitleTable().addActorAt(0, image);
    }
    
    public void setTextureAtlas(TextureAtlas atlas) {
        this.atlas = atlas;
    }
    
    public TextureAtlas getTextureAtlas() {
        return this.atlas;
    }
    
    @Override
    public void addCloseButton() {
        closeButton = new VisImageButton("close-window");
        getTitleTable().add(closeButton).padRight(-getPadRight() + 0.7f);
        closeButton.addListener(new ChangeListener() {
             @Override
             public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                 close();
             }
         });
        
        if (getTitleLabel().getLabelAlign() == Align.center && getTitleTable().getChildren().size == 2)
                getTitleTable().getCell(getTitleLabel()).padLeft(closeButton.getWidth() * 2);
    }
    
    @Override
    public void close() {
        closeButton.getColor().a = 0;
        super.close();
        destroy();
    }
    
    @Override
    public void setStage(Stage stage) {
        if(stage != null) {
            super.setStage(stage);
            createDialog();
            stage.addActor(this);
        }
    }
    
    private void createDialog() {
        if(!dialogCreated) {
            create();
            pack();
            dialogCreated = true;
        }
    }
}
