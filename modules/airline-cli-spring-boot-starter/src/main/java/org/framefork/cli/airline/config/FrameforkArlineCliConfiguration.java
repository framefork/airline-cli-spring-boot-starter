package org.framefork.cli.airline.config;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.CommandFactory;
import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.parser.errors.handlers.CollectAll;
import com.github.rvesse.airline.types.TypeConverter;
import org.framefork.cli.airline.CliErrorHandler;
import org.framefork.cli.airline.CliExecutor;
import org.framefork.cli.airline.ExecutableCommand;
import org.framefork.cli.airline.command.HelpCommand;
import org.framefork.cli.airline.spring.FrameforkCliTypeConverter;
import org.framefork.cli.airline.spring.SpringContextCommandFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;

@AutoConfiguration
@Import({
    HelpCommand.class,
})
@ConditionalOnNotWebApplication
public class FrameforkArlineCliConfiguration
{

    @Bean
    public CliExecutor airlineCliExecutor(
        final Cli<ExecutableCommand> airlineCli,
        final CliErrorHandler errorHandler
    )
    {
        return new CliExecutor(airlineCli, errorHandler);
    }

    @Bean
    public Cli<ExecutableCommand> airlineCli(
        final ListableBeanFactory listableBeanFactory,
        final ConfigurableBeanFactory configurableBeanFactory,
        final CommandFactory<ExecutableCommand> commandFactory,
        final TypeConverter typeConverter,
        @Value("${spring.application.name:'cli'}") String springApplicationName,
        final List<ArlineCliBuilderCustomizer> customizers
    )
    {
        CliBuilder<ExecutableCommand> builder = Cli.builder(springApplicationName);

        builder
            .withDefaultRestrictions();

        var parser = builder.withParser()
            .withErrorHandler(new CollectAll())
            .withCommandFactory(commandFactory)
            .withTypeConverter(typeConverter);

        String[] commandBeanNames = listableBeanFactory.getBeanNamesForType(ExecutableCommand.class);
        for (String commandBeanName : commandBeanNames) {
            BeanDefinition beanDefinition = configurableBeanFactory.getMergedBeanDefinition(commandBeanName);

            Class<?> beanClass = getBeanClass(beanDefinition);
            if (!ExecutableCommand.class.isAssignableFrom(beanClass)) {
                throw new IllegalStateException(String.format("Expected command bean '%s' to be a type of %s", commandBeanName, ExecutableCommand.class.getName()));
            }

            @SuppressWarnings("unchecked")
            var commandClass = (Class<? extends ExecutableCommand>) beanClass;

            builder.withCommand(commandClass);
        }

        builder.withCommand(HelpCommand.class);

        customizers.forEach(customizer -> customizer.customize(builder, parser));

        return builder.build();
    }

    @ConditionalOnMissingBean(CliErrorHandler.class)
    @Bean
    public CliErrorHandler.DefaultErrorHandler airlineCliErrorHandler(
        final Cli<ExecutableCommand> airlineCli
    )
    {
        return new CliErrorHandler.DefaultErrorHandler(airlineCli);
    }

    @Bean
    public SpringContextCommandFactory airlineCliSpringContextCommandFactory(
        final ApplicationContext applicationContext
    )
    {
        return new SpringContextCommandFactory(applicationContext);
    }

    @ConditionalOnMissingBean(TypeConverter.class)
    @Bean
    public FrameforkCliTypeConverter airlineCliTypeConverter(
        @Qualifier(ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME) final Optional<ConversionService> springConversionService,
        final ConfigurableListableBeanFactory beanFactory
    )
    {
        var conversionService = Optional.<ConversionService>empty()
            .or(() -> springConversionService)
            .or(() -> Optional.ofNullable(beanFactory.getConversionService()))
            .orElseThrow(() -> new IllegalStateException("No suitable %s found, please register it as a '%s' bean yourself.".formatted(
                ConversionService.class.getName(),
                ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME
            )));

        return new FrameforkCliTypeConverter(conversionService, null);
    }

    private static Class<?> getBeanClass(final BeanDefinition beanDefinition)
    {
        String beanClassName = beanDefinition.getBeanClassName();
        if (beanClassName != null) {
            try {
                return Class.forName(beanClassName);

            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(String.format("Your reflection hack is broken: %s", e.getMessage()), e);
            }
        }

        Class<?> resolvedClass = beanDefinition.getResolvableType().resolve();
        if (resolvedClass != null) {
            return resolvedClass;
        }

        throw new IllegalStateException("Cannot resolve bean class from definition: %s".formatted(beanDefinition));
    }

}
