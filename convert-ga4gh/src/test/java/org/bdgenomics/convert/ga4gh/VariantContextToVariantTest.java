package org.bdgenomics.convert.ga4gh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.protobuf.util.JsonFormat;

import ga4gh.Common.Strand;

import ga4gh.Reads.CigarUnit;
import ga4gh.Reads.CigarUnit.Operation;

import ga4gh.Reads.ReadAlignment;

import ga4gh.ReadServiceOuterClass.SearchReadsResponse;

import htsjdk.samtools.Cigar;
import htsjdk.samtools.CigarOperator;

import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;
import org.bdgenomics.convert.Converter;

import org.bdgenomics.formats.avro.*;

import org.junit.Before;
import org.junit.Test;

import ga4gh.Variants;
import ga4gh.Variants.Call;
import org.bdgenomics.adam.models.VariantContext;

import org.bdgenomics.adam.models.VariantContext;
import org.bdgenomics.adam.models.SequenceDictionary;
import org.bdgenomics.adam.models.SequenceRecord;

import org.bdgenomics.formats.avro.AlignmentRecord;
import org.bdgenomics.formats.avro.Variant;
import org.bdgenomics.formats.avro.Genotype;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Predef;
import scala.collection.Seq;
import scala.collection.JavaConverters;

/**
 * Created by paschalj on 9/16/17.
 */
public final class VariantContextToVariantTest {
    private final Logger logger = LoggerFactory.getLogger(VariantContextToVariantTest.class);
    private Converter<org.bdgenomics.adam.models.VariantContext, ga4gh.Variants.Variant> variantContextConverter;
    private org.bdgenomics.adam.models.VariantContext variantContext;



    @Before
    public void setUp() {
        variantContextConverter = new VariantContextToVariant();

        org.bdgenomics.formats.avro.Variant v0 = Variant.newBuilder()
                .setContigName("chr11")
                .setStart(17409572L)
                .setEnd(17409573L)
                .setReferenceAllele("T")
                .setAlternateAllele("C")
                .setNames(Arrays.asList("rs3131972", "rs201888535"))
                .setFiltersApplied(true)
                .setFiltersPassed(true)
                .build();

              org.bdgenomics.formats.avro.Genotype g0 = Genotype.newBuilder().setVariant(v0)
                .setSampleId("NA12878")
                .setAlleles(Arrays.asList(GenotypeAllele.REF, GenotypeAllele.ALT))
                .build();


        List<Genotype> g1 = new ArrayList<>();
        g1.add(g0);

        Seq<Genotype> g1_scala = JavaConverters.asScalaBufferConverter(g1).asScala().toSeq();


        org.bdgenomics.adam.models.VariantContext variantContext = org.bdgenomics.adam.models.VariantContext.buildFromGenotypes(g1_scala);


    }


    @Test
    public void testConvert() {

        ga4gh.Variants.Variant variant = variantContextConverter.convert(variantContext, ConversionStringency.STRICT, logger);


    }
/*
    @Test
    public void testConstructor() {
        assertNotNull(variantContextConverter);
    }

*/

}
