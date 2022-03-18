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
import com.github.break27.graphics.HtmlRenderer;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.kotcrab.vis.ui.widget.VisScrollPane;

/**
 *
 * @author break27
 */
public class HtmlViewerWindow extends CollapsibleWindow {
    HtmlRenderer renderer;
    VisScrollPane scrollPane;
    
    public HtmlViewerWindow(int width, int height) {
        super("HTML Viewer", width, height);
        padBottom(5f);
        addCollapseButton();
        renderer = new HtmlRenderer(width, height, false);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        renderer.resize(width, height);
    }

    @Override
    public void create() {
        renderer.load(Gdx.files.internal("misc/html/about.html").readString("UTF-8"));
        renderer.render();
        Image image = renderer.getImage();
        scrollPane = new VisScrollPane(image);
        scrollPane.setOverscroll(false, false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setupFadeScrollBars(0.75f, 1);
        getContentTable().add(scrollPane);
    }

    @Override
    public void destroy() {
        
    }

    @Override
    public void styleApply(AlternativeSkin skin) {
        super.styleApply(skin);
        setTitleImage(skin.getDrawable("icon20-info-wb"));
    }
}
