/**************************************************************************
 * Copyright (c) 2022 Breakerbear
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

package com.github.break27.system;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.github.break27.loader.AlterSkinLoader;

import java.util.HashMap;

/**
 * @author break27
 */
public class AlterAssetManager extends AssetManager {

    private final HashMap<String, String> Table = new HashMap<>();

    public AlterAssetManager() {
    }

    public AlterAssetManager(FileHandleResolver resolver) {
        this(resolver, true);
    }

    public AlterAssetManager(FileHandleResolver resolver, boolean defaultLoaders) {
        super(resolver, defaultLoaders);
        // set default loaders
        if(defaultLoaders) {
            // freetype font loader
            setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
            setLoader(BitmapFont.class, new FreetypeFontLoader(resolver));
            // skin loader
            setLoader(AlternativeSkin.class, new AlterSkinLoader(resolver));
        }
    }

    public synchronized <T> void load(String name, String fileName, Class<T> type) {
        load(name, fileName, type, null);
    }

    public synchronized <T> void load(String name, String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
        Table.put(parse(name, type), fileName);
        super.load(fileName, type, parameter);
    }

    public synchronized AlternativeSkin getSkin() {
        return get("default", AlternativeSkin.class);
    }

    @Deprecated
    @Override
    public synchronized <T> void load(String fileName, Class<T> type) {
        load("default", fileName, type);
    }

    @Deprecated
    @Override
    public synchronized <T> void load(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
        load("default", fileName, type, parameter);
    }

    @Override
    public synchronized <T> T get(String name, Class<T> type) {
        return get(name, type, true);
    }

    @Override
    public synchronized <T> T get(String name, Class<T> type, boolean required) {
        return super.get(Table.get(parse(name, type)), type, required);
    }

    private String parse(String name, Class type) {
        return name + "@" + type;
    }
}