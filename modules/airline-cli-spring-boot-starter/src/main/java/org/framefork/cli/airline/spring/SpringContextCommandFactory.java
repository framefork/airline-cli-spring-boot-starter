package org.framefork.cli.airline.spring;

import com.github.rvesse.airline.CommandFactory;
import org.framefork.cli.airline.ExecutableCommand;
import org.springframework.context.ApplicationContext;

public class SpringContextCommandFactory implements CommandFactory<ExecutableCommand>
{

    private final ApplicationContext applicationContext;

    public SpringContextCommandFactory(
        final ApplicationContext applicationContext
    )
    {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ExecutableCommand createInstance(final Class<?> type)
    {
        return applicationContext.getBean((Class<? extends ExecutableCommand>) type);
    }

}
