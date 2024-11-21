package org.framefork.cli.airline;

import org.jspecify.annotations.Nullable;

public class CommandExitException extends RuntimeException
{

    private final int exitCode;

    public CommandExitException(
        final int exitCode,
        @Nullable final String message
    )
    {
        super(message);
        this.exitCode = exitCode;
    }

    public CommandExitException(
        final int exitCode,
        @Nullable final String message,
        @Nullable final Throwable cause
    )
    {
        super(message, cause);
        this.exitCode = exitCode;
    }

    public CommandExitException(
        final int exitCode,
        @Nullable final Throwable cause
    )
    {
        super(cause);
        this.exitCode = exitCode;
    }

    public int getExitCode()
    {
        return exitCode;
    }

}
