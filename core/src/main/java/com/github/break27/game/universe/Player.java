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

package com.github.break27.game.universe;

import com.badlogic.gdx.files.FileHandle;

/**
 * @author break27
 */
public class Player {

    float x, y, z;
    Profiles profiles;

    public Player() {

    }

    public Player(FileHandle saveFile) {

    }

    public Player(float x, float y, float z, Profiles profiles) {

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Profiles getProfiles() {
        return profiles;
    }

    public class Profiles {

    }
}
