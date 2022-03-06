/**************************************************************************
 * Copyright (c) 2021-2022 Breakerbear
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

package com.github.break27.game.world;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.break27.game.entity.AlterEntity;
import com.github.break27.game.entity.Player;

/**
 * @author break27
 */
public abstract class World implements Disposable {

    Array<AlterEntity> Entities;
    Save save;

    public World(Save save) {
        // resolve save file
        this.save = save;
        Entities = new Array<>();
        // create world
        initialize();
        create();
    }

    public abstract void initialize();

    public abstract void create();

    /** Update current world.
     * @param delta delta time in seconds.
     * */
    public void update(float delta) {
        save.uptime += (long)(delta * 1000);
    }

    public void save(FileHandle saveFile) {

    }
}