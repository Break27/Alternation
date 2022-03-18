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

package com.github.break27.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.github.break27.Game3;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.github.break27.launcher.LauncherAdapter;
import com.kotcrab.vis.ui.VisUI;

import java.util.Locale;

/**
 *
 * @author break27
 */
public abstract class Resource {

    static XmlReader reader = new XmlReader();

    public static void loadDefault(AlterAssetManager manager) {
        createLoader().loadManifest(manager, Gdx.files.internal("manifest.xml"), new Array<>());
        manager.finishLoading();
        loadVisUI(manager);
    }

    public static ResourceLoader createLoader() {
        return new ResourceLoader();
    }

    private static void loadVisUI(AlterAssetManager manager) {
        VisUI.load(manager.getSkin());
    }

    protected static class ResourceLoader implements Disposable {
        private boolean read = false;

        public void loadManifest(AlterAssetManager manager, FileHandle manifest, final Array<FileHandle> files) {
            findResources(manifest).forEach(element -> {
                String name = element.getAttribute("name", "default");
                String type = element.getAttribute("type", null);
                FileHandle sibling;
                // Only manifest with location attribute is a valid one.
                if (element.hasAttribute("location"))
                    sibling = manifest.sibling(element.getAttribute("location"));
                else throw new GdxRuntimeException(manifest.path() + ": Invalid Manifest: " +
                        "\"location\" Attribute is not found.");
                if (type.equals("manifest")) {
                    // prevent from infinite recursive loop
                    if (sibling.path().contains("../")) {
                        throw new GdxRuntimeException(manifest.path() + ": Unsupported Operation: No access to parent directory: " + sibling);
                    } else if (files.contains(sibling, false) || sibling.equals(manifest)) {
                        throw new GdxRuntimeException(manifest.path() + ": Unsupported Operation: Manifest has already loaded: " + sibling);
                    } else {
                        files.add(sibling);
                        loadManifest(manager, sibling, files);
                    }
                } else {
                    /* Skin */
                    if (type.equals("skin")) loadSkin(manager, name, sibling);
                    /* I18N */
                    if (type.equals("i18n")) loadI18N(name, sibling, getLocale());
                }
            });
        }

        private void loadSkin(AlterAssetManager manager, String name, FileHandle skin) {
            if (skin.exists())
                manager.load(name, skin.path(), AlternativeSkin.class);
            else
                Gdx.app.log(Resource.class.getName(), "Skin file \"" + skin.path() +
                        "\" does not exist! Skin \"" + name + "\" is not loaded.");
        }

        private void loadI18N(String name, FileHandle bundleFile, Locale locale) {
            // ensure using UTF-8 encoding.
            Locales.putBundle(name, I18NBundle.createBundle(bundleFile, locale, "UTF-8"));
        }

        private Array<Element> findResources(FileHandle file) {
            Element element = reader.parse(file);
            loadMetaInfo(element.getChildByName("meta"));
            return element.getChildByName("list").getChildrenByNameRecursively("resource");
        }

        private void loadMetaInfo(Element metainfo) {
            if (!read) {
                /* Name */
                Element E_name = metainfo.getChildByName("name");
                String name = E_name == null ? "Resource Set" : E_name.getText();
                /* Version */
                Element E_version = metainfo.getChildByName("version");
                if (E_version != null) {
                    String val = E_version.getText().replace(".", "");
                    // Only numbers are accepted.
                    if (val.matches("^[0-9]+$")) {
                        int version = Integer.parseInt(val);
                        int gamever = Integer.parseInt(LauncherAdapter.VERSION.replace(".", ""));
                        if (version < gamever) Gdx.app.log(Resource.class.getName(), "Currently loading a resource set "
                                + "from an older version of the game. Some texture might be unable to display.");
                        if (version > gamever) Gdx.app.log(Resource.class.getName(), "Currently loading a resource set "
                                + "from a newer version of the game.");
                    } else {
                        Gdx.app.log(Resource.class.getName(), "Version element of the resource set is unreadable, ignored.");
                    }
                }
                /* Authors */
                Element E_authors = metainfo.getChildByName("authors");
                if (E_authors != null) {
                    Array<String> authors = new Array<>();
                    E_authors.getChildrenByNameRecursively("author").forEach(author -> {
                        authors.add(author.getText());
                    });
                }
                read = true;
            }
        }

        @Override
        public void dispose() {

        }

        private static Locale getLocale() {
            Preferences prefs = Gdx.app.getPreferences(Game3.Launcher.tmpDataPath());
            String language = "zh", country = "CN", variant = "";
            if (prefs.contains("locale")) {
                String[] localeName = prefs.getString("locale").split("_", 3);
                if (localeName.length >= 2) {
                    language = localeName[0];
                    country = localeName[1];
                }
                if (localeName.length == 3) variant = localeName[2];
            }
            return new Locale(language, country, variant);
        }
    }
}