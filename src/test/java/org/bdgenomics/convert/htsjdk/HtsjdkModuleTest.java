package org.bdgenomics.convert.htsjdk;

import static org.junit.Assert.assertNotNull;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Guice;

import htsjdk.samtools.ValidationStringency;

import org.junit.Before;
import org.junit.Test;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionStringency;

/**
 * Unit test for HtsjdkModule.
 */
public final class HtsjdkModuleTest {
    private HtsjdkModule module;

    @Before
    public void setUp() {
        module = new HtsjdkModule();
    }

    @Test
    public void testConstructor() {
        assertNotNull(module);
    }

    @Test
    public void testHtsjdkModule() {
        Injector injector = Guice.createInjector(module, new TestModule());
        Target target = injector.getInstance(Target.class);
        assertNotNull(target.getConversionStringencyToValidationStringency());
        assertNotNull(target.getValidationStringencyToConversionStringency());
    }

    /**
     * Injection target.
     */
    static class Target {
        final Converter<ConversionStringency, ValidationStringency> conversionStringencyToValidationStringency;
        final Converter<ValidationStringency, ConversionStringency> validationStringencyToConversionStringency;

        @Inject
        Target(final Converter<ConversionStringency, ValidationStringency> conversionStringencyToValidationStringency,
               final Converter<ValidationStringency, ConversionStringency> validationStringencyToConversionStringency) {
            this.conversionStringencyToValidationStringency = conversionStringencyToValidationStringency;
            this.validationStringencyToConversionStringency = validationStringencyToConversionStringency;
            
        }

        Converter<ConversionStringency, ValidationStringency> getConversionStringencyToValidationStringency() {
            return conversionStringencyToValidationStringency;
        }

        Converter<ValidationStringency, ConversionStringency> getValidationStringencyToConversionStringency() {
            return validationStringencyToConversionStringency;
        }
    }

    /**
     * Test module.
     */
    class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(Target.class);
        }
    }
}
