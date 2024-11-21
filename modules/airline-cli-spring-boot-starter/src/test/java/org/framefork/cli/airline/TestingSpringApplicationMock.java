package org.framefork.cli.airline;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.framefork.cli.airline.CliMainClass.runOneCommand;

@SpringBootApplication
@SuppressWarnings("PrivateConstructorForUtilityClass")
public class TestingSpringApplicationMock
{

    public static void main(final String[] args)
    {
        runOneCommand(TestingSpringApplicationMock.class, args);
    }

}
