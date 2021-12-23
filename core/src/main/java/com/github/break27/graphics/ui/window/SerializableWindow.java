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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.util.HashMap;

/**
 *
 * @author break27
 */
public abstract class SerializableWindow extends AlternativeWindow {
    public static final class WindowType {
        public static final int TEST = 0;
        public static final int VIEW = 1;
        public static final int HTML = 2;
        public static final int BROW = 3;
    }
    
    private boolean ignoreFocus = false;
    private boolean Focused = false;
    
    protected static HashMap<Integer,SerializableWindow> windows;
    protected final int ID;
    
    public SerializableWindow(String name) {
        super(name);
        ID = this.hashCode();
        if(windows == null) windows = new HashMap<>();
        windows.put(ID, this);
    }
    
    public abstract int getType();
    
    public int getId() {
        return this.ID;
    }
    
    public boolean isFocused() {
        return this.Focused;
    }

    public void setFocus() {
        this.Focused = true;
        this.getColor().a = 1f;
        this.titleBarButtonsTable.getColor().a = 1f;
    }
    
    public void setFocusLost() {
        // it never "loses focus" if true
        if(!ignoreFocus) {
            this.Focused = false;
            this.getColor().a = 0.75f;
            this.titleBarButtonsTable.getColor().a = 0;
        }
    }
    
    public void setIgnoreFocus(boolean ignore) {
        this.ignoreFocus = ignore;
    }
    
    @Override
    public void setListeners() {
        this.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                focusProcess();
                return true;
            }
        });
        titleBarButtonsTable.addListener(new ClickListener() {
             @Override
             public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                 focusProcess();
                 event.cancel();
                 return true;
             }
         });
    }
    
    @Deprecated
    @Override
    public void pack() {
        if(!Focused) this.setFocusLost();
        super.pack();
    }
    
    private void focusProcess() {
        windows.values().forEach(window -> {
            if(window.ID != this.ID) {
                window.setFocusLost();
            } else {
                window.setFocus();
            }
        });
    }
}
