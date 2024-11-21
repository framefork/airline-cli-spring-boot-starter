package org.framefork.cli.airline.spring;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseOptionConversionException;
import com.github.rvesse.airline.types.DefaultTypeConverter;
import com.github.rvesse.airline.types.TypeConverter;
import com.github.rvesse.airline.types.numerics.NumericTypeConverter;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;

import java.util.Objects;

public final class FrameforkCliTypeConverter extends DefaultTypeConverter
{

    private final ConversionService springConversionService;

    public FrameforkCliTypeConverter(
        final ConversionService springConversionService,
        @Nullable final NumericTypeConverter numericConverter
    )
    {
        super(numericConverter);
        this.springConversionService = springConversionService;
    }

    @Override
    public Object convert(
        final String name,
        final Class<?> type,
        final String value
    )
    {
        try {
            return super.convert(name, type, value);

        } catch (ParseOptionConversionException e) {
            try {
                return Objects.requireNonNull(
                    springConversionService.convert(value, type),
                    "conversion result must not be null"
                );

            } catch (ConversionException | IllegalArgumentException ex) {
                e.addSuppressed(ex);
                throw e;
            }
        }
    }

    @Override
    public <T> TypeConverter getTypeConverter(
        final OptionMetadata option,
        final ParseState<T> state
    )
    {
        return this;
    }

    @Override
    public <T> TypeConverter getTypeConverter(
        final ArgumentsMetadata arguments,
        final ParseState<T> state
    )
    {
        return this;
    }

}
