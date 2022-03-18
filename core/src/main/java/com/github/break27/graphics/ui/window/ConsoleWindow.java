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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.github.break27.system.console.CommandExecutor;
import com.github.break27.system.console.Console;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.highlight.*;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.VisTextField;

/**
 * @author break27
 */
public class ConsoleWindow extends CollapsibleWindow {
    Console console;
    ConsoleTextArea textArea;
    VisTextField textField;
    ScrollPane scrollPane;

    float width, height;

    public ConsoleWindow(float width, float height) {
        super("Console", width, height);
        console = new Console();
        console.setCommandExecutor(new DefaultCommandExecutor());
        console.setApplicationLogger(Gdx.app.getApplicationLogger());

        textArea = new ConsoleTextArea();
        scrollPane = textArea.createCompatibleScrollPane();
        scrollPane.setScrollingDisabled(true, false);
        textField = new VisTextField();

        console.printMOTD();
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        getContentTable().add(scrollPane).prefWidth(width).prefHeight(height);
        processInput();
    }

    private void processInput() {
        textArea.addListener(new InputListener() {
            boolean emptyInput = true;

            @Override
            public boolean keyTyped(InputEvent event, char c) {
                if(c == 10) {
                    String[] lines = textArea.getTextLines();
                    if(lines.length >= 1) {
                        String line = emptyInput ? "" : lines[lines.length - 1];
                        if(textArea.getCursorLine() == lines.length) {
                            console.execCommand(line);
                            emptyInput = true;
                        }
                    }
                } else emptyInput = false;
                return true;
            }
        });
    }

    @Override
    public void localeApply() {
        super.localeApply();
    }

    @Override
    public void destroy() {
        console.dispose();
    }

    public class DefaultCommandExecutor extends CommandExecutor {
        @Override
        public synchronized void result(ResultStatus status, String message) {
            switch(status) {
                case DEFAULT:
                    break;
                case FAILED:
                    message = "\033\011" + message + "\033\011";
                default:
                    message += "\n";
            }
            textArea.appendText(message);
        }

        @Override
        public void clear() {
            textArea.clearText();
        }

        @Override
        public void exit() {
            close();
        }
    }

    public static class ConsoleTextArea extends HighlightTextArea {

        public ConsoleTextArea() {
            this("");
        }

        public ConsoleTextArea(String text) {
            super(text, VisUI.getSkin().get(ConsoleTextAreaStyle.class));
            setHighlighter(createHighlighter());
            clearListeners();
            addListener(new ConsoleTextAreaClickListener());
        }

        public String[] getTextLines() {
            return getText().split("\n");
        }

        public BaseHighlighter createHighlighter() {
            BaseHighlighter highlighter = new BaseHighlighter();
            highlighter.addRule(new RegexHighlightRule(Color.WHITE, "\033\010([^\033\010]*)\033\010"));
            highlighter.addRule(new RegexHighlightRule(Color.RED, "\033\011([^\033\011]*)\033\011"));
            highlighter.addRule(new RegexHighlightRule(Color.GREEN, "\033\012([^\033\012]*)\033\012"));
            highlighter.addRule(new RegexHighlightRule(Color.BLUE, "\033\013([^\033\013]*)\033\013"));
            highlighter.addRule(new RegexHighlightRule(Color.YELLOW, "\033\014([^\033\014]*)\033\014"));
            highlighter.addRule(new RegexHighlightRule(Color.ORANGE, "\033\015([^\033\015]*)\033\015"));
            highlighter.addRule(new RegexHighlightRule(Color.PINK, "\033\016([^\033\016]*)\033\016"));
            highlighter.addRule(new RegexHighlightRule(Color.PURPLE, "\033\017([^\033\017]*)\033\017"));
            highlighter.addRule(new RegexHighlightRule(Color.CYAN, "\033\020([^\033\018]*)\033\020"));
            highlighter.addRule(new RegexHighlightRule(Color.FOREST, "\033\021([^\033\011]*)\033\021"));
            return highlighter;
        }

        public class ConsoleTextAreaClickListener extends TextAreaListener {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                return super.keyTyped(event, character);
            }
        }

        public static class ConsoleTextAreaStyle extends VisTextFieldStyle {
        }
    }
}