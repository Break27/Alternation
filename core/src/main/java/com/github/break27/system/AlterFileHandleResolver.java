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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * @author break27
 */
public class AlterFileHandleResolver implements FileHandleResolver {

    @Override
    public FileHandle resolve(String fileName) {
        FileHandle retval = parse(fileName);
        if(retval == null) throw new GdxRuntimeException(fileName + ": Resource not found.");
        return retval;
    }

    private FileHandle parse(String filePath) {
        String[] val = filePath.split(":",2);
        switch(val.length) {
            case 2:
                String head = val[0].toLowerCase();
                String path = val[1];
                // resolve head
                switch (head) {
                    case "file":
                        return Gdx.files.absolute(path);
                    case "http":
                        //todo
                    case "https":
                    case "ftp":
                }
            case 1:
                // catcher
                FileHandle retval = Gdx.files.internal(filePath);
                if(retval == null) retval = Gdx.files.external(filePath);
                return retval;
            default:
                return null;
        }
    }
}