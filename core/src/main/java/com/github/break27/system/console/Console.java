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

package com.github.break27.system.console;

import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.break27.system.console.exception.BadArgumentException;
import com.github.break27.system.console.exception.UnknownCommandException;

/**
 * @author break27
 */
public class Console implements Disposable {
    protected final Array<String> historyCommands = new Array<>();
    private int maxEntries = 80;

    protected CommandExecutor exec;
    protected ApplicationLogger logger;

    public Console() {
        this(null);
    }

    public Console(CommandExecutor exec) {
        this(exec, new DumbApplicationLogger());
    }

    public Console(CommandExecutor exec, ApplicationLogger logger) {
        this.exec = exec;
        this.logger = logger;
    }

    public void setMaxEntries(int maxEntries) {
        if(maxEntries >= 80) {
            if(maxEntries < this.maxEntries)
                historyCommands.removeRange(0, (this.maxEntries - maxEntries));
            else
                historyCommands.size = maxEntries;
            this.maxEntries = maxEntries;
        } else {
            logger.error(getClass().getName(),
                    "Console.MaxEntries=" + maxEntries + " is too less. (at least 80)");
        }
    }

    public void setApplicationLogger(ApplicationLogger logger) {
        if(logger != null)
            this.logger = logger;
    }

    public void setCommandExecutor(CommandExecutor executor) {
        exec = executor;
    }

    public void printMOTD() {
        if(exec != null)  {
            exec.printVersion();
            exec.feed();
        }
    }

    public void execCommand(String command) {
        if(exec != null) {
            try {
                exec.parseAndExec(command);
            } catch (ArrayIndexOutOfBoundsException e1) {
                //todo
                logger.error(getClass().getName(), "", e1);
            } catch (UnknownCommandException e2) {
                exec.failed("Unknown command. Use 'help' for more information.");
                logger.error(getClass().getName(), "Command Not Found: " + e2.getMessage());
            } catch (BadArgumentException e3) {
                exec.failed(e3.getLocalizedMessage());
            } catch (Exception ex) {
                exec.failed("Error: " + ex.getLocalizedMessage() +
                        "\nCheck the log for more detailed information if available.");
                logger.error(getClass().getName(),
                        "An error occurred while executing command '" + command + "'.", ex);
            }

            if(exec.echoEnabled) exec.feed();
            historyCommands.add(command);
            if (historyCommands.size > maxEntries)
                historyCommands.removeIndex(0);
        } else {
            logger.error(getClass().getName(),
                    "No CommandExecutor available. Ignored.");
        }
    }

    @Override
    public void dispose() {
        if(exec != null) exec.dispose();
    }

    private static final class DumbApplicationLogger implements ApplicationLogger {
        @Override
        public void log(String tag, String message) {
        }
        @Override
        public void log(String tag, String message, Throwable exception) {
        }
        @Override
        public void error(String tag, String message) {
        }
        @Override
        public void error(String tag, String message, Throwable exception) {
        }
        @Override
        public void debug(String tag, String message) {
        }
        @Override
        public void debug(String tag, String message, Throwable exception) {
        }
    }
}
