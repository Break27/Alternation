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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.github.break27.system.console.CommandExecutor;
import com.github.break27.system.console.Console;
import com.github.break27.util.ColorMarkup;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.highlight.*;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

/**
 * @author break27
 */
public class ConsoleWindow extends CollapsibleWindow {
    Console console;
    ConsoleTextArea textArea;
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

        console.printMOTD();
        this.width = width;
        this.height = height;
    }

    public void update() {
        textArea.updateDisplay();
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
        textArea.dispose();
    }

    public class DefaultCommandExecutor extends CommandExecutor {
        @Override
        public synchronized void result(ResultStatus status, String message) {
            switch(status) {
                case DEFAULT:
                    break;
                case FAILED:
                    message = ColorMarkup.RED.wrap(message);
                default:
                    message += "\n";
            }
            textArea.queue(message);
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

    public static class ConsoleTextArea extends HighlightTextArea implements Disposable {
        private final Queue<String> queue = new Queue<>();
        private boolean active = true;

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

        public void queue(String text) {
            if(active) queue.addFirst(text);
        }

        public void updateDisplay() {
            if(active && !queue.isEmpty())
                appendText(queue.removeLast());
        }

        public BaseHighlighter createHighlighter() {
            BaseHighlighter highlighter = new BaseHighlighter();
            for(ColorMarkup markup : ColorMarkup.values()) {
                highlighter.addRule(new ColorMarkupHighlightRule(markup));
            }
            return highlighter;
        }

        @Override
        public void dispose() {
            active = false;
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

    public static class ColorMarkupHighlightRule extends RegexHighlightRule {

        public ColorMarkupHighlightRule(ColorMarkup markup) {
            super(markup.getColor(), markup.wrap("([^" + markup.getMark() + "]*)"));
        }
    }
}