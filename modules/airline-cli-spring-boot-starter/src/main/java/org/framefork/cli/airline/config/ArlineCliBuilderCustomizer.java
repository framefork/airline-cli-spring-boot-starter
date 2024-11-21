package org.framefork.cli.airline.config;

import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.builder.ParserBuilder;
import org.framefork.cli.airline.ExecutableCommand;

@FunctionalInterface
public interface ArlineCliBuilderCustomizer
{

    void customize(
        final CliBuilder<ExecutableCommand> builder,
        final ParserBuilder<ExecutableCommand> parser
    );

}
