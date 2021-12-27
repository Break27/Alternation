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

package com.github.break27.graphics.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.github.break27.graphics.ui.LocalizableWidget;
import com.github.break27.graphics.ui.StyleAppliedWidget;
import com.github.break27.graphics.ui.button.CloseButton;
import com.github.break27.graphics.ui.widget.AlterLabel;
import com.kotcrab.vis.ui.widget.VisDialog;

/**
 *
 * @author break27
 */
public abstract class AlternativeDialog extends VisDialog
        implements StyleAppliedWidget, LocalizableWidget {

    Image titleImage;
    CloseButton closeButton;
    AlterLabel titleLabel;
    
    public AlternativeDialog(String name) {
        super(name);
        getTitleTable().clear();
        getTitleTable().add(getTitleLabel()).expand().left();
        register();
    }
    
    public void setTitleImage(Drawable drawable) {
        if(titleImage == null) {
            titleImage = new Image(drawable);
            getTitleTable().getCell(getTitleLabel()).padLeft(titleImage.getWidth() + 2.5f);
            getTitleTable().addActorAt(0, titleImage);
        } else {
            titleImage.setDrawable(drawable);
        }
    }

    @Override
    public Label getTitleLabel() {
        if(titleLabel == null) titleLabel = new AlterLabel(super.getTitleLabel().getText().toString());
        return titleLabel;
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
        destroy();
        super.close();
    }
    
    @Override
    public VisDialog show(Stage stage) {
        closeButton.getColor().a = 1f;
        return super.show(stage);
    }
    
    @Override
    public void styleApply() {
        setStyle(getAlterSkin().get(WindowStyle.class));
    }
    
    @Override
    public void destroy() {
        closeButton.getColor().a = 0;
    }
}
