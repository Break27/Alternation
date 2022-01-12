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

package com.github.break27.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.break27.graphics.ui.AlternativeSkin;

/**
 * @author break27
 */
public class AlterSkinLoader extends AsynchronousAssetLoader<AlternativeSkin, AlterSkinLoader.SkinParameter> {

    public AlterSkinLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkinParameter parameter) {
        Array<AssetDescriptor> deps = new Array<>();
        if (parameter != null && parameter.textureAtlasPath != null) {
            deps.add(new AssetDescriptor<>(parameter.textureAtlasPath, TextureAtlas.class));
        }
        return deps;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {
    }

    @Override
    public AlternativeSkin loadSync(AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {
        ObjectMap<String, Object> resources = null;
        if (parameter != null) {
            if (parameter.resources != null) {
                resources = parameter.resources;
            }
        }

        AlternativeSkin skin = new AlternativeSkin(file);

        if (resources != null) {
            for (ObjectMap.Entry<String, Object> entry : resources.entries()) {
                skin.add(entry.key, entry.value);
            }
        }

        return skin;
    }

    public static class SkinParameter extends AssetLoaderParameters<AlternativeSkin> {
        public final String textureAtlasPath;
        public final ObjectMap<String, Object> resources;

        public SkinParameter () {
            this(null, null);
        }

        public SkinParameter (ObjectMap<String, Object> resources) {
            this(null, resources);
        }

        public SkinParameter (String textureAtlasPath) {
            this(textureAtlasPath, null);
        }

        public SkinParameter (String textureAtlasPath, ObjectMap<String, Object> resources) {
            this.textureAtlasPath = textureAtlasPath;
            this.resources = resources;
        }
    }
}