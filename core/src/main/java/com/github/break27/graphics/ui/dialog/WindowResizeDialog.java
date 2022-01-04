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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.break27.graphics.ui.button.AlterTextButton;
import com.github.break27.graphics.ui.widget.AlterLabel;
import com.github.break27.graphics.ui.window.AlternativeWindow;
import com.github.break27.graphics.ui.window.CollapsibleWindow;
import com.github.break27.system.AlterAssetManager;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

/**
 *
 * @author break27
 */
public class WindowResizeDialog extends AlternativeDialog {

    Table fieldsTable;
    AlterLabel widthLabel;
    AlterLabel heightLabel;
    AlterLabel sizeLabel;

    VisValidatableTextField widthField;
    VisValidatableTextField heightField;

    AlternativeWindow target;

    AlterTextButton button_cancel;
    AlterTextButton button_reset;
    AlterTextButton button_resize;

    int width = 0, height = 0;
    boolean isShown = false;

    public WindowResizeDialog(AlternativeWindow window) {
        super("Dialog");
        target = window;
        addCloseButton();
        setListeners();
    }

    @Override
    public VisDialog show(Stage stage) {
        updateFields();
        updateSize();
        isShown = true;
        return super.show(stage);
    }

    public void close() {
        super.close();
        isShown = false;
    }

    @Override
    public void styleApply(AlterAssetManager assets) {
        super.styleApply(assets);
        setTitleImage(assets.getSkin().getDrawable("icon20-application"));
    }
    
    @Override
    public void localeApply() {
        getTitleLabel().setText(translate("TITLE"));
        widthLabel.setText(translate("label", "WIDTH"));
        heightLabel.setText(translate("label", "HEIGHT"));
        button_cancel.setText(translate("button", "CANCEL"));
        button_reset.setText(translate("button", "RESET"));
        button_resize.setText(translate("button", "RESIZE"));
    }

    @Override
    public void create() {
        // validate input (int)
        widthField = new VisValidatableTextField(new IntegerValidator());
        heightField = new VisValidatableTextField(new IntegerValidator());
        sizeLabel = new AlterLabel();
        // text fields
        fieldsTable = new Table();
        fieldsTable.padTop(10f);
        fieldsTable.add(widthLabel = new AlterLabel("Width")).left().uniform();

        fieldsTable.add(widthField);
        fieldsTable.row();
        fieldsTable.add(heightLabel = new AlterLabel("Height")).left().uniform();

        fieldsTable.add(heightField);
        fieldsTable.row();
        // labels
        sizeLabel.setWrap(true);
        fieldsTable.add(sizeLabel).right();
        fieldsTable.padBottom(10f);
        getContentTable().add(fieldsTable);
        // buttons
        getButtonsTable().add(button_cancel = new AlterTextButton("Cancel")).space(0);
        getButtonsTable().add(button_reset = new AlterTextButton("Reset")).space(0);
        getButtonsTable().add(button_resize = new AlterTextButton("Resize")).space(0);
    }

    private void updateSize() {
        try {
            width = Float.valueOf(widthField.getText()).intValue();
            height = Float.valueOf(heightField.getText()).intValue();
        } catch(NumberFormatException ignored) { }
        sizeLabel.setText(width + " x " + height + " " + translate("label", "SIZE-UNIT-PIXEL"));
    }

    private void updateFields() {
        widthField.setText(String.valueOf((int) target.getWidth()));
        heightField.setText(String.valueOf((int) target.getHeight()));
    }

    private void setListeners() {
        button_cancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
        button_reset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateFields();
            }
        });
        button_resize.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(widthField.isInputValid() && heightField.isInputValid()) {
                    updateSize();
                    if (target instanceof CollapsibleWindow)
                        ((CollapsibleWindow) target).resize(width, height);
                    else target.setSize(width, height);
                    close();
                }
            }
        });
    }

    private class IntegerValidator implements InputValidator {

        @Override
        public boolean validateInput(String input) {
            if(isShown) {
                updateSize();
                // size limit
                if(width > Gdx.graphics.getWidth() || height > Gdx.graphics.getHeight())
                    return false;
            }
            try {
                Integer.parseInt(input);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}