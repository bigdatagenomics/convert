package org.bdgenomics.convert.htsjdk;

import static org.bdgenomics.convert.ConversionStringency.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Guice;

import htsjdk.samtools.ValidationStringency;

import org.junit.Before;
import org.junit.Test;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for ValidationStringencyToConversionStringency.
 */
public final class ValidationStringencyToConversionStringencyTest {
    private final Logger logger = LoggerFactory.getLogger(ValidationStringencyToConversionStringencyTest.class);

    Converter<ValidationStringency, ConversionStringency> converter;

    @Before
    public void setUp() {
        converter = new ValidationStringencyToConversionStringency();
    }

    @Test
    public void testConstructor() {
        assertNotNull(converter);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullSourceStrict() {
        converter.convert(null, STRICT, logger);
    }

    @Test
    public void testConvertNullSourceLenient() {
        // should see warning, or use mocks
        assertEquals(null, converter.convert(null, LENIENT, logger));
    }

    @Test
    public void testConvertNullSourceSilent() {
        assertEquals(null, converter.convert(null, SILENT, logger));
    }

    @Test
    public void testConvert() {
        assertEquals(STRICT, converter.convert(ValidationStringency.STRICT, SILENT, logger));
        assertEquals(LENIENT, converter.convert(ValidationStringency.LENIENT, SILENT, logger));
        assertEquals(SILENT, converter.convert(ValidationStringency.SILENT, SILENT, logger));
    }
}
