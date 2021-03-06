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

package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.github.break27.graphics.ui.LocalizableWidget;
import com.github.break27.graphics.ui.StyleAppliedWidget;
import com.github.break27.graphics.ui.button.CloseButton;
import com.github.break27.graphics.ui.dialog.WindowResizeDialog;
import com.github.break27.graphics.ui.widget.AlterLabel;
import com.github.break27.system.AlterAssetManager;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 *
 * @author break27
 */
public abstract class AlternativeWindow extends VisWindow
        implements StyleAppliedWidget, LocalizableWidget {
    VisTable menuTable;
    VisTable contentTable;
    VisTable footerTable;
    VisTable titleBarButtonsTable;

    Label subTitleLabel;
    Image titleImage;

    WindowResizeDialog resizeDialog;

    private boolean windowCreated = false;
    
    public AlternativeWindow(String name) {
        super(name);
        titleBarButtonsTable = new VisTable();
        subTitleLabel = new AlterLabel();
        // default pad style
        padLeft(4f);
        padRight(4f);
        // reset table
        getTitleTable().clearChildren();
        getTitleTable().add(getTitleLabel());
        // labels
        getTitleTable().add(subTitleLabel).fillX().expandX().left();
        getTitleTable().add(titleBarButtonsTable).padRight(-getPadRight() + 0.7f).right();
        // panel
        add(createPanel());
        resizeDialog = new WindowResizeDialog(this);
        
        register();
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

    public void setSubTitleLabel(Label label, float width) {
        getTitleTable().getCell(getSubTitleLabel()).setActor(label).width(width);
        subTitleLabel = label;
    }

    public Label getSubTitleLabel() {
        return subTitleLabel;
    }

    public void appendResizeDialog() {
        resizeDialog.show(getStage());
    }
    
    public void setTitleImage(Drawable drawable) {
        if(titleImage == null) {
            titleImage = new Image(drawable);
            getTitleTable().padLeft(titleImage.getWidth() + 5f);
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
    public void styleApply(AlternativeSkin skin) {
        // window style
        setStyle(skin.get(WindowStyle.class));
    }

    @Override
    public void localeApply() {
        getTitleLabel().setText(translate("TITLE"));
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