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

import com.badlogic.gdx.Version;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.break27.Game3;
import com.github.break27.system.exception.BadArgumentException;
import com.github.break27.system.exception.TooManyArgException;
import com.github.break27.system.exception.UnknownCommandException;
import com.github.break27.system.shell.KotlinShell;
import com.github.break27.system.shell.ShellHandler;
import com.github.break27.system.shell.KShell;
import org.jetbrains.annotations.Nullable;

/**
 * @author break27
 */
public abstract class CommandExecutor implements Disposable {
    private final OrderedMap<String, Command> RegCommands = new OrderedMap<>();
    private final OrderedMap<String, Command> AlsCommands = new OrderedMap<>();

    protected boolean echoEnabled = true;
    private String defaultCommand;
    private String labelName;

    public CommandExecutor() {
        this("", true);
    }

    public CommandExecutor(String name, boolean useDefaultCommands) {
        setName(name);
        setDefaultCommand("");
        if(useDefaultCommands) {
            register("help", new HelpCommand());
            alias("help", "?");
            register("kotlin", new KotlinCommand());
            register("clear", new ClearCommand());
            register("label", new LabelCommand());
            register("print", new PrintCommand());
            register("exit", new ExitCommand());
        }
    }

    public void success(String message) {
        result(ResultStatus.SUCCESS, message);
    }

    public void failed(String message) {
        result(ResultStatus.FAILED, message);
    }

    public void feed() {
        feed(getLabel() + " ");
    }

    public void feed(String message) {
        result(ResultStatus.DEFAULT, message);
    }

    public void setName(String name) {
        labelName = name;
    }

    public String getLabel() {
        return labelName + ">";
    }

    public void setEchoEnabled(boolean enabled) {
        echoEnabled = enabled;
    }

    public abstract void result(ResultStatus status, String message);

    public abstract void clear();

    public abstract void exit();

    protected void printVersion() {
        success("ALTERNATION GAME CONSOLE");
        success("VERSION: " + Game3.Launcher.VERSION + "/" + Version.VERSION);
    }

    protected void parseAndExec(String command) throws Exception {
        if (command.startsWith(getLabel()))
            command = command.replaceFirst(getLabel(), "").trim();
        command = defaultCommand + " " + command;
        if(!command.trim().isEmpty()) {
            String[] raw = parseCommand(command);
            Command com = findCommand(raw[0], true);
            com.execute(raw);
        }
    }

    protected String[] parseCommand(String command) {
        return command.trim().split(" ");
    }

    protected void setDefaultCommand(String command) {
        defaultCommand = command;
    }

    public @Null Command findCommand(String command, boolean strict) throws UnknownCommandException {
        Command retval = RegCommands.get(command);
        if(retval == null) retval = AlsCommands.get(command);
        if(retval == null && strict) throw new UnknownCommandException(command);
        return retval;
    }

    public boolean alias(String sourceCommand, String aliasCommand) {
        Command command = RegCommands.get(sourceCommand);
        if(command != null) {
            AlsCommands.put(aliasCommand, command);
            return true;
        }
        return false;
    }

    public void register(String baseCommand, Command command) {
        RegCommands.put(baseCommand, command);
    }

    public boolean remove(String baseCommand) {
        return remove(baseCommand, true);
    }

    public boolean remove(String baseCommand, boolean removeAlias) {
        Command com = RegCommands.get(baseCommand);
        if(com != null) {
            RegCommands.remove(baseCommand);
            // remove alias
            AlsCommands.forEach(alias -> {
                if(alias.value == com)
                    AlsCommands.remove(alias.key);
            });
            // dispose
            com.dispose();
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        for(Command command : RegCommands.values())
            command.dispose();
        RegCommands.clear();
        AlsCommands.clear();
    }

    public enum ResultStatus {
        DEFAULT(),
        SUCCESS(),
        FAILED()
    }

    protected final class HelpCommand extends Command {
        @Override
        public void execute(String[] args) throws Exception {
            if(args.length == 1) {
                RegCommands.forEach(entry -> {
                    String desc = entry.value.getDescription();
                    success(entry.key + " - " + (desc == null ? "<No Description>" : desc));
                });
            } else if(args.length == 2) {
                Command com = findCommand(args[1], true);
                String message = "";
                message += com.getDescription() == null ? "" : "Description: " + com.getDescription();
                message += com.getSource() == null ? "" : "\nSource: " + com.getSource();
                message += com.getUsage() == null ? "" : "\nUsage: " + com.getUsage();
                if(message.isEmpty())
                    message = "No documentation found for command '" + args[1] + "'.";
                success(message);
            } else {
                throw new TooManyArgException("EXCEPTION.COMMAND.TMA");
            }
        }

        @Override
        public String getDescription() {
            return "Print help information.";
        }

        @Override
        public String getSource() {
            return null;
        }

        @Override
        public String getUsage() {
            return "help [command]";
        }

        @Override
        public void dispose() {
        }
    }

    protected final class KotlinCommand extends Command {
        private final KotlinShell shell = KShell.System.createKotlinShell();
        private boolean active = false;

        @Override
        public void execute(String[] args) {
            if(!shell.isRunning()) {
                clear();
                shell.create(new ShellHandler() {
                    @Override
                    public void feed() {
                        if(active) CommandExecutor.this.feed();
                    }

                    @Override
                    public void feed(@Nullable String message) {
                        if(active) CommandExecutor.this.feed(message);
                    }

                    @Override
                    public void success(@Nullable String message) {
                        if(active) CommandExecutor.this.success(message);
                    }

                    @Override
                    public void failed(@Nullable String message) {
                        if(active) CommandExecutor.this.failed(message);
                    }
                });
            }
            active = true;
            setEchoEnabled(false);
            setDefaultCommand("kotlin");
            setName(shell.getShellName());
            String command = "";

            if(args.length >= 2) {
                for(int i=1; i<args.length; i++)
                    command += args[i] + " ";
                if(args[1].equals(":quit") || args[1].equals(":q")) {
                    active = false;
                    setEchoEnabled(true);
                    clear();
                    setDefaultCommand("");
                    setName("");
                    printVersion();
                }
            }
            shell.execute(command);
        }

        @Override
        public String getDescription() {
            return "Start a Kotlin shell interface.";
        }

        @Override
        public String getSource() {
            return null;
        }

        @Override
        public String getUsage() {
            return null;
        }

        @Override
        public void dispose() {
            shell.destroy();
        }
    }

    protected final class LabelCommand extends Command {
        @Override
        public void execute(String[] args) throws Exception {
            if(args.length != 2)
                throw new BadArgumentException("Usage: " + getUsage());
            else {
                if(args[1].length() > 24)
                    throw new BadArgumentException("Too many characters. (Maximum of 24)");
                if(args[1].contains("\\"))
                    throw new BadArgumentException("Illegal Character(s): \\");
                setName(args[1].replace("\"", ""));
            }
        }

        @Override
        public String getDescription() {
            return "Set label of the console.";
        }

        @Override
        public String getSource() {
            return null;
        }

        @Override
        public String getUsage() {
            return "label <label name>";
        }

        @Override
        public void dispose() {
        }
    }

    protected final class ClearCommand extends Command {
        @Override
        public void execute(String[] args) {
            clear();
        }

        @Override
        public String getDescription() {
            return "Clean the screen.";
        }

        @Override
        public String getSource() {
            return null;
        }

        @Override
        public String getUsage() {
            return null;
        }

        @Override
        public void dispose() {
        }
    }

    protected final class PrintCommand extends Command {

        @Override
        public void execute(String[] args) throws Exception {
            if(args.length != 2)
                throw new BadArgumentException("Usage: " + getUsage());
            else {
                success(args[1]);
            }
        }

        @Override
        public String getDescription() {
            return "Print message on screen.";
        }

        @Override
        public String getSource() {
            return null;
        }

        @Override
        public String getUsage() {
            return "print <message>";
        }

        @Override
        public void dispose() {
        }
    }

    protected final class ExitCommand extends Command {

        @Override
        public void execute(String[] args) {
            exit();
        }

        @Override
        public String getDescription() {
            return "Quit console.";
        }

        @Override
        public String getSource() {
            return null;
        }

        @Override
        public String getUsage() {
            return null;
        }

        @Override
        public void dispose() {
        }
    }
}