package org.bdgenomics.convert.htsjdk;

import htsjdk.samtools.ValidationStringency;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionStringency;

/**
 * Guice module for the org.bdgenomics.convert.htsjdk package.
 */
public final class HtsjdkModule extends AbstractModule {
    @Override
    protected void configure() {
        // empty
    }

    @Provides @Singleton
    Converter<ConversionStringency, ValidationStringency> createConversionStringencyToValidationStringency() {
        return new ConversionStringencyToValidationStringency();
    }

    @Provides @Singleton
    Converter<ValidationStringency, ConversionStringency> createValidationStringencyToConversionStringency() {
        return new ValidationStringencyToConversionStringency();
    }
}
