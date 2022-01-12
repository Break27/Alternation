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

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.LogLevel;

/**
 * @author break27
 */
public class AlterConsole extends Table implements Console {

    public AlterConsole() {

    }

    @Override
    public void setMaxEntries(int numEntries) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void setSize(int width, int height) {

    }

    @Override
    public void setLoggingToSystem(Boolean log) {

    }

    @Override
    public void setSizePercent(float wPct, float hPct) {

    }

    @Override
    public void setPosition(int x, int y) {

    }

    @Override
    public void setPositionPercent(float xPosPct, float yPosPct) {

    }

    @Override
    public void resetInputProcessing() {

    }

    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }

    @Deprecated
    @Override
    public void draw() {
    }

    @Override
    public void refresh() {

    }

    @Override
    public void refresh(boolean retain) {

    }

    @Override
    public void log(String msg, LogLevel level) {

    }

    @Override
    public void log(String msg) {

    }

    @Override
    public void log(Throwable exception) {

    }

    @Override
    public void log(Throwable exception, LogLevel level) {

    }

    @Override
    public void printLogToFile(String file) {

    }

    @Override
    public void printLogToFile(FileHandle fh) {

    }

    @Override
    public void printCommands() {

    }

    @Override
    public void printHelp(String command) {

    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public void setDisabled(boolean disabled) {

    }

    @Override
    public int getDisplayKeyID() {
        return 0;
    }

    @Override
    public void setDisplayKeyID(int code) {

    }

    @Override
    public void setCommandExecutor(CommandExecutor commandExec) {

    }

    @Override
    public void execCommand(String command) {

    }

    @Override
    public boolean hitsConsole(float screenX, float screenY) {
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public void setExecuteHiddenCommands(boolean enabled) {

    }

    @Override
    public void setDisplayHiddenCommands(boolean enabled) {

    }

    @Override
    public boolean isExecuteHiddenCommandsEnabled() {
        return false;
    }

    @Override
    public boolean isDisplayHiddenCommandsEnabled() {
        return false;
    }

    @Override
    public void setConsoleStackTrace(boolean enabled) {

    }

    @Override
    public void select() {

    }

    @Override
    public void deselect() {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setHoverAlpha(float alpha) {

    }

    @Override
    public void setNoHoverAlpha(float alpha) {

    }

    @Override
    public void setHoverColor(Color color) {

    }

    @Override
    public void setNoHoverColor(Color color) {

    }

    @Override
    public void enableSubmitButton(boolean enable) {

    }

    @Override
    public void setSubmitText(String text) {

    }

    @Deprecated
    @Override
    public Window getWindow() {
        return null;
    }

    public static class AlterCommandExecutor extends CommandExecutor {

    }
}