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

package com.github.break27.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.github.break27.game.entity.Entity;
import com.github.break27.game.entity.Profiles;

/**
 * @author break27
 */
public class Player extends Entity {

    public FirstPersonCameraController Controller;
    public PerspectiveCamera Camera;

    public Player(FileHandle saveFile) {
        super(new PlayerProfiles(saveFile));
        // setup camera
        Camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Camera.near = 0.5f;
        Camera.far = 1000;

        Controller = new FirstPersonCameraController(Camera);
    }

    public void update() {
        Camera.update();
    }

    public static class PlayerProfiles extends EntityProfiles {

        public PlayerProfiles(FileHandle saveFile) {

        }
    }
}