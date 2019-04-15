/**
 * Licensed to Big Data Genomics (BDG) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The BDG licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bdgenomics.convert.htsjdk;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Guice;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMProgramRecord;
import htsjdk.samtools.SAMReadGroupRecord;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMSequenceRecord;
import htsjdk.samtools.ValidationStringency;

import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLine;

import htsjdk.variant.variantcontext.VariantContext;

import org.junit.Before;
import org.junit.Test;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.AlignmentRecord;
import org.bdgenomics.formats.avro.Genotype;
import org.bdgenomics.formats.avro.ProcessingStep;
import org.bdgenomics.formats.avro.ReadGroup;
import org.bdgenomics.formats.avro.Reference;
import org.bdgenomics.formats.avro.Sample;
import org.bdgenomics.formats.avro.Variant;

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
        assertNotNull(target.getSamRecordToAlignmentRecord());
        assertNotNull(target.getSamProgramRecordToProcessingStep());
        assertNotNull(target.getSamHeaderToProcessingSteps());
        assertNotNull(target.getSamReadGroupRecordToReadGroup());
        assertNotNull(target.getSamHeaderToReadGroups());
        assertNotNull(target.getSamSequenceRecordToReference());
        assertNotNull(target.getSamHeaderToReferences());
        assertNotNull(target.getVcfHeaderToReferences());
        assertNotNull(target.getVcfHeaderToSamples());
        assertNotNull(target.getVcfHeaderToVcfHeaderLines());

        SAMFileHeader samFileHeader = new SAMFileHeader();
        assertNotNull(target.getAlignmentRecordToSamRecordFactory());
        assertNotNull(target.getAlignmentRecordToSamRecordFactory().create(samFileHeader));

        VCFHeader vcfHeader = new VCFHeader();
        assertNotNull(target.getGenotypesToVariantContextFactory());
        assertNotNull(target.getGenotypesToVariantContextFactory().create(vcfHeader));
        assertNotNull(target.getVariantToVariantContextFactory());
        assertNotNull(target.getVariantToVariantContextFactory().create(vcfHeader));
        assertNotNull(target.getVariantContextToGenotypesFactory());
        assertNotNull(target.getVariantContextToGenotypesFactory().create(vcfHeader));
        assertNotNull(target.getVariantContextToVariantsFactory());
        assertNotNull(target.getVariantContextToVariantsFactory().create(vcfHeader));
    }

    /**
     * Injection target.
     */
    static class Target {
        final Converter<ConversionStringency, ValidationStringency> conversionStringencyToValidationStringency;
        final Converter<ValidationStringency, ConversionStringency> validationStringencyToConversionStringency;
        final Converter<SAMRecord, AlignmentRecord> samRecordToAlignmentRecord;
        final Converter<SAMProgramRecord, ProcessingStep> samProgramRecordToProcessingStep;
        final Converter<SAMFileHeader, List<ProcessingStep>> samHeaderToProcessingSteps;
        final Converter<SAMReadGroupRecord, ReadGroup> samReadGroupRecordToReadGroup;
        final Converter<SAMFileHeader, List<ReadGroup>> samHeaderToReadGroups;
        final Converter<SAMSequenceRecord, Reference> samSequenceRecordToReference;
        final Converter<SAMFileHeader, List<Reference>> samHeaderToReferences;
        final Converter<VCFHeader, List<Reference>> vcfHeaderToReferences;
        final Converter<VCFHeader, List<Sample>> vcfHeaderToSamples;
        final Converter<VCFHeader, List<VCFHeaderLine>> vcfHeaderToVcfHeaderLines;
        final AlignmentRecordToSamRecordFactory alignmentRecordToSamRecordFactory;
        final GenotypesToVariantContextFactory genotypesToVariantContextFactory;
        final VariantToVariantContextFactory variantToVariantContextFactory;
        final VariantContextToGenotypesFactory variantContextToGenotypesFactory;
        final VariantContextToVariantsFactory variantContextToVariantsFactory;

        @Inject
        Target(final Converter<ConversionStringency, ValidationStringency> conversionStringencyToValidationStringency,
               final Converter<ValidationStringency, ConversionStringency> validationStringencyToConversionStringency,
               final Converter<SAMRecord, AlignmentRecord> samRecordToAlignmentRecord,
               final Converter<SAMProgramRecord, ProcessingStep> samProgramRecordToProcessingStep,
               final Converter<SAMFileHeader, List<ProcessingStep>> samHeaderToProcessingSteps,
               final Converter<SAMReadGroupRecord, ReadGroup> samReadGroupRecordToReadGroup,
               final Converter<SAMFileHeader, List<ReadGroup>> samHeaderToReadGroups,
               final Converter<SAMSequenceRecord, Reference> samSequenceRecordToReference,
               final Converter<SAMFileHeader, List<Reference>> samHeaderToReferences,
               final Converter<VCFHeader, List<Reference>> vcfHeaderToReferences,
               final Converter<VCFHeader, List<Sample>> vcfHeaderToSamples,
               final Converter<VCFHeader, List<VCFHeaderLine>> vcfHeaderToVcfHeaderLines,
               final AlignmentRecordToSamRecordFactory alignmentRecordToSamRecordFactory,
               final GenotypesToVariantContextFactory genotypesToVariantContextFactory,
               final VariantToVariantContextFactory variantToVariantContextFactory,
               final VariantContextToGenotypesFactory variantContextToGenotypesFactory,
               final VariantContextToVariantsFactory variantContextToVariantsFactory) {

            this.conversionStringencyToValidationStringency = conversionStringencyToValidationStringency;
            this.validationStringencyToConversionStringency = validationStringencyToConversionStringency;
            this.samRecordToAlignmentRecord = samRecordToAlignmentRecord;
            this.samProgramRecordToProcessingStep = samProgramRecordToProcessingStep;
            this.samHeaderToProcessingSteps = samHeaderToProcessingSteps;
            this.samReadGroupRecordToReadGroup = samReadGroupRecordToReadGroup;
            this.samHeaderToReadGroups = samHeaderToReadGroups;
            this.samSequenceRecordToReference = samSequenceRecordToReference;
            this.samHeaderToReferences = samHeaderToReferences;
            this.vcfHeaderToReferences = vcfHeaderToReferences;
            this.vcfHeaderToSamples = vcfHeaderToSamples;
            this.vcfHeaderToVcfHeaderLines = vcfHeaderToVcfHeaderLines;
            this.alignmentRecordToSamRecordFactory = alignmentRecordToSamRecordFactory;
            this.genotypesToVariantContextFactory = genotypesToVariantContextFactory;
            this.variantToVariantContextFactory = variantToVariantContextFactory;
            this.variantContextToGenotypesFactory = variantContextToGenotypesFactory;
            this.variantContextToVariantsFactory = variantContextToVariantsFactory;
        }

        Converter<ConversionStringency, ValidationStringency> getConversionStringencyToValidationStringency() {
            return conversionStringencyToValidationStringency;
        }

        Converter<ValidationStringency, ConversionStringency> getValidationStringencyToConversionStringency() {
            return validationStringencyToConversionStringency;
        }

        Converter<SAMRecord, AlignmentRecord> getSamRecordToAlignmentRecord() {
            return samRecordToAlignmentRecord;
        }

        Converter<SAMProgramRecord, ProcessingStep> getSamProgramRecordToProcessingStep() {
            return samProgramRecordToProcessingStep;
        }

        Converter<SAMFileHeader, List<ProcessingStep>> getSamHeaderToProcessingSteps() {
            return samHeaderToProcessingSteps;
        }

        Converter<SAMReadGroupRecord, ReadGroup> getSamReadGroupRecordToReadGroup() {
            return samReadGroupRecordToReadGroup;
        }

        Converter<SAMFileHeader, List<ReadGroup>> getSamHeaderToReadGroups() {
            return samHeaderToReadGroups;
        }

        Converter<SAMSequenceRecord, Reference> getSamSequenceRecordToReference() {
            return samSequenceRecordToReference;
        }

        Converter<SAMFileHeader, List<Reference>> getSamHeaderToReferences() {
            return samHeaderToReferences;
        }

        Converter<VCFHeader, List<Reference>> getVcfHeaderToReferences() {
            return vcfHeaderToReferences;
        }

        Converter<VCFHeader, List<Sample>> getVcfHeaderToSamples() {
            return vcfHeaderToSamples;
        }

        Converter<VCFHeader, List<VCFHeaderLine>> getVcfHeaderToVcfHeaderLines() {
            return vcfHeaderToVcfHeaderLines;
        }

        AlignmentRecordToSamRecordFactory getAlignmentRecordToSamRecordFactory() {
            return alignmentRecordToSamRecordFactory;
        }

        GenotypesToVariantContextFactory getGenotypesToVariantContextFactory() {
            return genotypesToVariantContextFactory;
        }

        VariantToVariantContextFactory getVariantToVariantContextFactory() {
            return variantToVariantContextFactory;
        }

        VariantContextToGenotypesFactory getVariantContextToGenotypesFactory() {
            return variantContextToGenotypesFactory;
        }

        VariantContextToVariantsFactory getVariantContextToVariantsFactory() {
            return variantContextToVariantsFactory;
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
