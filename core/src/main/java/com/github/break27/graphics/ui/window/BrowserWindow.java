/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.break27.graphics.Browser;
import com.github.break27.graphics.ui.button.OptionButton;
import com.github.break27.graphics.ui.widget.AlterScrollPane;

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
