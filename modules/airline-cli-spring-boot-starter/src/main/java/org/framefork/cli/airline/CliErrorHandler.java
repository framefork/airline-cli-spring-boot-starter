package org.framefork.cli.airline;

import com.github.rvesse.airline.Channels;
import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.help.Help;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public interface CliErrorHandler
{

    int handleError(String[] args, Throwable error, int exitCode) throws IOException;

    class DefaultErrorHandler implements CliErrorHandler
    {

        @SuppressWarnings("LoggerInitializedWithForeignClass")
        private static final Logger log = LoggerFactory.getLogger(CliExecutor.class);

        private final Cli<ExecutableCommand> cli;

        public DefaultErrorHandler(
            final Cli<ExecutableCommand> cli
        )
        {
            this.cli = cli;
        }

        @Override
        public int handleError(final String[] args, final Throwable error, final int exitCode) throws IOException
        {
            log.error(error.getMessage(), error);
            Channels.error().println('\n');

            Help.help(
                cli.getMetadata(),
                Arrays.asList(args),
                Channels.error()
            );

            return exitCode;
        }

    }

}
