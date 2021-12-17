/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.github.break27.graphics.ui.button.CloseButton;
import com.kotcrab.vis.ui.widget.VisDialog;

/**
 *
 * @author break27
 */
public abstract class AlternativeDialog extends VisDialog implements AlternativeWidget {
    Image titleImage;
    CloseButton closeButton;
    
    public AlternativeDialog(String name) {
        super(name);
        enableStyle();
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
    
    @Override
    public void addCloseButton() {
        closeButton = new CloseButton();
        getTitleTable().add(closeButton).padRight(-getPadRight() + 0.7f).right();
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
    }
    
    @Override
    public VisDialog show(Stage stage) {
        closeButton.getColor().a = 1f;
        return super.show(stage);
    }
}
