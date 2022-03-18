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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.break27.graphics.ui.button.CollapseButton;
import com.kotcrab.vis.ui.widget.VisImageButton;

/**
 *
 * @author break27
 */
public abstract class CollapsibleWindow extends SerializableWindow {
    
    VisImageButton collapseButton;
    private boolean Collapsed = false;

    int labelHeight;
    float width;
    float height;
    
    public CollapsibleWindow(String name, float width, float height) {
        super(name);
        this.width = width;
        this.height = height;
        labelHeight = (int) getMinHeight();
        
        setWidth(width);
        setHeight(height + labelHeight);
        getContentTable().setSize(width, height);
    }
    
    public boolean isCollapsed() {
        return this.Collapsed;
    }
    
    public void addCollapseButton() {
        collapseButton = new CollapseButton();
        addTitleTableButton(collapseButton);
        collapseButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                collapse();
            }
        });
    }

    /** Replaced by {@code resize(int, int)}.
     *  @param width
     *  @param height
     */
    public void resize(int width, int height) {
        super.setSize(width, height);
        this.width = width;
        this.height = height;
    }
    
    /** Make window collapsed.
     */
    public void collapse() {
        contentTable.setVisible(Collapsed);
        if(!Collapsed) {
            setPosition(getX(), getY() + height);
            setHeight(labelHeight);
        } else {
            setPosition(getX(), getY() - height);
            setHeight(height + labelHeight);
        }
        Collapsed = !Collapsed;
    }

    /** Replaced by {@code resize(int,int)}.
     *  @param width
     *  @param height
     */
    @Deprecated
    @Override
    public void setSize(float width, float height) {
    }
}