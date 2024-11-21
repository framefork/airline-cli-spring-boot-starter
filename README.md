# Airline CLI Spring Boot Starter

The [Airline CLI](https://rvesse.github.io/airline/guide/) is a Java library providing an annotation-based framework for parsing command line interfaces.
This project aims to integrate the Airline into Spring and serve as an application entrypoint.

## Installation

Install the latest `org.framefork:airline-cli-spring-boot-starter:*` and you're good to go.

## Usage

### 1. create an entrypoint

```java
import static org.framefork.cli.airline.CliMainClass.runOneCommand;

@SpringBootApplication
public class TestingSpringApplicationMock
{
    public static void main(final String[] args)
    {
        runOneCommand(TestingSpringApplicationMock.class, args);
    }
}
```

If you need to, you can use the third argument to customize the `SpringApplicationBuilder`

```java
public static void main(final String[] args)
{
    runOneCommand(TestingSpringApplicationMock.class, args, builder -> {
        builder.profiles("cli");
    });
}
```

### 2. define commands

The command must implement `org.framefork.cli.airline.ExecutableCommand`, so that there is a single main method that will be called for the command.
And be annotated with `org.framefork.cli.airline.CliCommand`, which makes it a Spring bean with prototype scope.  

```java
@CliCommand
public class MyCommand implements ExecutableCommand
{

    @Option(name = {"--dry-run"}, description = "An option that requires no values")
    private boolean dryRun = false;

    @Override
    public void execute()
    {
        if (dryRun) {
            // dry run
        } else {
            // real run
        }
    }

}
```

### 3. profit

Now you should be able to, using your new main class, execute one-off commands.

## How it works

When you execute a command

1. the application is started in non-web mode, the `runOneCommand()` takes care of that
2. the console arguments are parsed and validated
3. Airline uses the bridge in this project to ask spring for the command instance, which is created from the prototype bean
4. Airline binds all the options and arguments to the command, making them available to the command
5. the `execute()` method is called
6. if at any point the handling ends with an error, its printed 
