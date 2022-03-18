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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.break27.graphics.Viewpoint;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.github.break27.graphics.ui.menu.TitleMenu;
import com.github.break27.system.AlterAssetManager;

/**
 *
 * @author break27
 */
public class ViewpointWindow extends CollapsibleWindow {
    public ViewpointWindow(int width, int height) {
        super("Main View", width, height);
        this.padBottom(5f);
        this.viewpointWidth = width;
        this.viewpointHeight = height;
        
        addCollapseButton();
    }

    Viewpoint viewpoint;
    Image image;
    
    int viewpointWidth;
    int viewpointHeight;
    boolean destroyed = false;
    
    public void update() {
        if(!isCollapsed() && !destroyed) {
            viewpoint.update();
            image.setDrawable(viewpoint.getImage().getDrawable());
        }
    }
    
    @Override
    public void create() {
        this.viewpoint = new Viewpoint(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), 
                viewpointWidth, viewpointHeight, false);
        createViewpoint();
        image = viewpoint.getImage();
        getContentTable().add(image);

        TitleMenu menu = new TitleMenu(this);
        menu.listenTo(getTitleTable());

        // default value: focused
        setFocused(true);
    }
    
    @Override
    public void styleApply(AlternativeSkin skin) {
        super.styleApply(skin);
        setTitleImage(skin.getDrawable("icon20-game-map"));
    }
    
    @Override
    public void destroy() {
        viewpoint.destory();
        destroyed = true;
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewpoint.resize(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), width, height);
    }
    
    public void setViewpoint(Viewpoint viewpoint) {
        this.viewpoint = viewpoint;
    }
    
    SpriteBatch batch = new SpriteBatch();
    Sprite sprite = new Sprite(getTitleLabel().getStyle().font.getRegion());
    
    private void createViewpoint() {
        viewpoint.render(() -> {
            batch.begin();
            sprite.draw(batch);
            batch.end();
        });
    }
}
