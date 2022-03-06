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

package com.github.break27.graphics.ui.window;

import com.github.break27.system.console.AlterConsole;

/**
 * @author break27
 */
public class ConsoleWindow extends CollapsibleWindow {

    AlterConsole console;

    public ConsoleWindow(int width, int height) {
        super("Console", width, height);
    }

    @Override
    public void destroy() {
        console.dispose();
    }

    @Override
    public void create() {
        console = new AlterConsole();
    }
}