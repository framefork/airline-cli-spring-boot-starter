package org.framefork.cli.airline;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static com.ginsberg.junit.exit.assertions.SystemExitAssertion.assertThatCallsSystemExit;

@ExtendWith(OutputCaptureExtension.class)
final class CliMainClassTest
{

    @Test
    public void testNoCommand(final CapturedOutput capturedOutput)
    {
        assertThatCallsSystemExit(() -> TestingSpringApplicationMock.main(new String[]{}))
            .withExitCode(2);

        Assertions.assertThat(capturedOutput.getAll())
            .contains("Starting TestingSpringApplicationMock using Java")
            .contains("Started TestingSpringApplicationMock")
            .containsPattern("org.framefork.cli.airline.CliExecutor.*?:.*?No arguments")
            .containsPattern("usage: 'cli' <command> \\[\\s+<args>\\s+]");
    }

    @Test
    public void testUnknownCommand(final CapturedOutput capturedOutput)
    {
        assertThatCallsSystemExit(() -> TestingSpringApplicationMock.main(new String[]{"unknown-command"}))
            .withExitCode(1);

        Assertions.assertThat(capturedOutput.getAll())
            .contains("Starting TestingSpringApplicationMock using Java")
            .contains("Started TestingSpringApplicationMock")
            .containsPattern("org.framefork.cli.airline.CliExecutor.*?:.*?Command 'unknown-command' not recognized");
    }

    @Test
    public void testHelpCommand(final CapturedOutput capturedOutput)
    {
        assertThatCallsSystemExit(() -> TestingSpringApplicationMock.main(new String[]{"help"}))
            .withExitCode(0);

        Assertions.assertThat(capturedOutput.getAll())
            .contains("Starting TestingSpringApplicationMock using Java")
            .contains("Started TestingSpringApplicationMock")
            .containsPattern("org.framefork.cli.airline.CliExecutor.*?:.*?Executing `help`");
    }

}
