package org.framefork.cli.airline;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.parser.ParseResult;
import com.github.rvesse.airline.parser.errors.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class CliExecutor
{

    private static final Logger log = LoggerFactory.getLogger(CliExecutor.class);

    private final Cli<ExecutableCommand> cli;
    private final CliErrorHandler errorHandler;

    public CliExecutor(
        final Cli<ExecutableCommand> cli,
        final CliErrorHandler errorHandler
    )
    {
        this.cli = cli;
        this.errorHandler = errorHandler;
    }

    public int execute(final String[] args) throws Exception
    {
        var parsed = parse(args);
        switch (parsed) {
            case Parsed.Failure failure -> {
                return errorHandler.handleError(args, failure.error(), failure.exitCode());
            }
            case Parsed.Success success -> {
                log.debug("Executing `{}`", success.commandName());
                success.executableCommand().execute();
                return 0;
            }
        }
    }

    public Parsed parse(final String[] args)
    {
        if (args.length == 0) {
            return new Parsed.Failure(new IllegalArgumentException("No arguments"), 2);
        }

        try {
            ParseResult<ExecutableCommand> result = cli.parseWithResult(args);
            if (!result.wasSuccessful()) {
                Deque<ParseException> errors = new ArrayDeque<>(result.getErrors());
                ParseException firstError = errors.removeFirst();
                errors.forEach(firstError::addSuppressed);

                return new Parsed.Failure(firstError, 1);
            }

            return new Parsed.Success(
                Objects.requireNonNull(result.getCommand(), "result.getCommand() must not be null"),
                result.getState().getCommand().getName()
            );

        } catch (ParseException e) {
            return new Parsed.Failure(e, 1);
        }
    }

    public sealed interface Parsed
    {

        record Success(ExecutableCommand executableCommand, String commandName) implements Parsed
        {

        }

        record Failure(Throwable error, int exitCode) implements Parsed
        {

        }

    }

}
