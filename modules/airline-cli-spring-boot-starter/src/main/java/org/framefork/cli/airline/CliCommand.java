package org.framefork.cli.airline;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class is a "ConsoleCommand".
 * <p>
 * <p>This annotation serves as a specialization of {@link Service},
 * allowing for implementation classes to be autodetected through classpath scanning.
 *
 * @see Component
 * @see Service
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public @interface CliCommand
{

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any
     */
    @AliasFor(annotation = Service.class, attribute = "value")
    String value() default "";

}
