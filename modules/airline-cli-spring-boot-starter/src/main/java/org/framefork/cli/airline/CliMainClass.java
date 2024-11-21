package org.framefork.cli.airline;

import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.function.Consumer;

/**
 * This class simplifies introducing a CLI entrypoint into your project.
 *
 * <pre><code>
 * &#64;SpringBootApplication
 * public class MyConsoleApplication
 * {
 *     public static void main(final String[] args)
 *     {
 *         CliMainClass.runOneCommand(MyConsoleApplication.class, args);
 *     }
 * }
 * </code></pre>
 */
@SuppressWarnings({"unused", "SystemExitOutsideMain"})
public final class CliMainClass
{

    private CliMainClass()
    {
    }

    public static void runOneCommand(
        final Class<?> mainSpringClass,
        final String[] args
    )
    {
        runOneCommand(
            mainSpringClass,
            args,
            builder -> {
            }
        );
    }

    public static void runOneCommand(
        final Class<?> mainSpringClass,
        final String[] args,
        final Consumer<SpringApplicationBuilder> customizer
    )
    {
        try {
            var applicationBuilder = new SpringApplicationBuilder(mainSpringClass)
                .registerShutdownHook(true)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.NONE);

            customizer.accept(applicationBuilder);

            var context = applicationBuilder.run(args);

            var exitCode = context.getBean(CliExecutor.class).execute(args);

            System.exit(SpringApplication.exit(context, () -> exitCode));

        } catch (CommandExitException e) {
            LoggerFactory.getLogger(mainSpringClass).warn(e.getMessage(), e);
            System.exit(e.getExitCode());

        } catch (Exception e) {
            LoggerFactory.getLogger(mainSpringClass).error(e.getMessage(), e);
            System.exit(1);
        }
    }

}
