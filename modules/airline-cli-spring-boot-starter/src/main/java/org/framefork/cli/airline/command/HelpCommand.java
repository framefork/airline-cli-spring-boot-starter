package org.framefork.cli.airline.command;

import com.github.rvesse.airline.help.Help;
import org.framefork.cli.airline.CliCommand;
import org.framefork.cli.airline.ExecutableCommand;

@CliCommand
public class HelpCommand extends Help<ExecutableCommand> implements ExecutableCommand
{

    @Override
    public void execute()
    {
        run();
    }

}
