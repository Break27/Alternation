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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.break27.graphics.Browser;
import com.github.break27.graphics.ui.LocalizableWidget;
import com.github.break27.graphics.ui.StyleAppliedWidget;
import com.github.break27.graphics.ui.button.OptionButton;
import com.github.break27.graphics.ui.menu.AlterMenuItem;
import com.github.break27.graphics.ui.menu.TitleMenu;
import com.github.break27.graphics.ui.widget.AlterScrollPane;
import com.kotcrab.vis.ui.widget.MenuItem;
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

    public void appendLinkDialog() {

    }

    @Override
    public int getType() {
        return WindowType.BROW;
    }

    @Override
    public void create() {
        //browser.load("https://www.bilibili.com");
        browser.load("https://www.un.org/zh/about-us/universal-declaration-of-human-rights");
        //browser.load("https://www.youtube.com");
        //browser.load(Gdx.files.internal("misc/html/about.html").file().toURI());
        Table table = browser.getBrowserTable();
        Image image = browser.getImage();
        AlterScrollPane scrollpane = new AlterScrollPane(image);
        scrollpane.setOverscroll(false, false);

        TitleMenu menu = new TitleMenu(this);
        menu.listenTo(getTitleTable());
        
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
    
    private void addOptionButton() {
        OptionButton button = new OptionButton(new BrowserOptionMenu());
        addTitleTableButton(button);
    }

    private class BrowserOptionMenu extends PopupMenu
            implements StyleAppliedWidget, LocalizableWidget {

        AlterMenuItem browser_link;
        AlterMenuItem browser_reload;
        AlterMenuItem window_close;

        public BrowserOptionMenu() {
            createMenu();
            register();
            pack();
        }

        private void createMenu() {
            browser_link = new AlterMenuItem("Link...", new Image(), new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    appendLinkDialog();
                }
            });
            browser_reload = new AlterMenuItem("Reload", new Image(), new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                }
            });
            window_close = new AlterMenuItem("Close", new Image(), new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    close();
                }
            });

            addItem(browser_link);
            addItem(browser_reload);
            addSeparator();
            addItem(window_close);
        }

        @Override
        public void styleApply() {
            window_close.getImage().setDrawable(getAlterSkin().getDrawable("icon-window-close"));
        }

        @Override
        public void localeApply() {
            browser_link.setText(translate("LINK"));
            browser_reload.setText(translate("RELOAD"));
            window_close.setText(translate("CLOSE"));
        }

        @Override
        public void destroy() {
        }
    }
}