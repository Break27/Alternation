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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.break27.graphics.Browser;
import com.github.break27.graphics.ui.button.OptionButton;
import com.github.break27.graphics.ui.widget.AlterScrollPane;
import com.kotcrab.vis.ui.widget.PopupMenu;

/**
 *
 * @author break27
 */
public class BrowserWindow extends CollapsibleWindow {
    
    Browser browser;
    Image optionImage;
    
    public BrowserWindow(String name, int width, int height) {
        super(name, width, height);
        padBottom(5f);
        browser = new Browser(width, height, false);
        addOptionButton();
    }
    
    @Override
    public int getType() {
        return WindowType.BROW;
    }

    @Override
    public void create() {
        browser.load("https://www.bilibili.com");
        //browser.load("https://www.un.org/zh/about-us/universal-declaration-of-human-rights");
        //browser.load("https://e621.net/posts/2977638?q=monomasa");
        //browser.load("https://www.youtube.com");
        //browser.load(Gdx.files.internal("misc/html/about.html").file().toURI());
        Table table = browser.getBrowserTable();
        Image image = browser.getImage();
        AlterScrollPane scrollpane = new AlterScrollPane(image);
        scrollpane.setOverscroll(false, false);
        
        getSubTitleTable().add(browser.getBrowserLabel()).left();
        getContentTable().add(scrollpane);
        getFooterTable().add(table).expand().left();
    }

    @Override
    public void destroy() {
        browser.destroy();
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
    
    @Override
    public void styleApply() {
        super.styleApply();
        setTitleImage(getAlterSkin().getDrawable("icon20-application"));
    }

    @Override
    public void localeApply() {
    }
    
    private void addOptionButton() {
        OptionButton button = new OptionButton();
        addTitleTableButton(button);
    }
}

class BrowserPopupMenu extends PopupMenu {

}