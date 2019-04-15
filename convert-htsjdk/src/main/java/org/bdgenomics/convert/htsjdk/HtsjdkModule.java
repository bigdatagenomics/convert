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

import java.util.List;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMProgramRecord;
import htsjdk.samtools.SAMReadGroupRecord;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMSequenceRecord;
import htsjdk.samtools.ValidationStringency;

import htsjdk.variant.variantcontext.VariantContext;

import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLine;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import com.google.inject.assistedinject.FactoryModuleBuilder;

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
 * Guice module for the org.bdgenomics.convert.htsjdk package.
 */
public final class HtsjdkModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
            .implement(new TypeLiteral<Converter<AlignmentRecord, SAMRecord>>() {}, AlignmentRecordToSamRecord.class)
            .build(AlignmentRecordToSamRecordFactory.class));

        install(new FactoryModuleBuilder()
            .implement(new TypeLiteral<Converter<List<Genotype>, VariantContext>>() {}, GenotypesToVariantContext.class)
            .build(GenotypesToVariantContextFactory.class));

        install(new FactoryModuleBuilder()
            .implement(new TypeLiteral<Converter<Variant, VariantContext>>() {}, VariantToVariantContext.class)
            .build(VariantToVariantContextFactory.class));

        install(new FactoryModuleBuilder()
            .implement(new TypeLiteral<Converter<VariantContext, List<Genotype>>>() {}, VariantContextToGenotypes.class)
            .build(VariantContextToGenotypesFactory.class));

        install(new FactoryModuleBuilder()
            .implement(new TypeLiteral<Converter<VariantContext, List<Variant>>>() {}, VariantContextToVariants.class)
            .build(VariantContextToVariantsFactory.class));
    }

    @Provides @Singleton
    Converter<ConversionStringency, ValidationStringency> createConversionStringencyToValidationStringency() {
        return new ConversionStringencyToValidationStringency();
    }

    @Provides @Singleton
    Converter<ValidationStringency, ConversionStringency> createValidationStringencyToConversionStringency() {
        return new ValidationStringencyToConversionStringency();
    }

    @Provides @Singleton
    Converter<SAMRecord, AlignmentRecord> createSamRecordToAlignmentRecord() {
        return new SamRecordToAlignmentRecord();
    }

    @Provides @Singleton
    Converter<SAMSequenceRecord, Reference> createSamSequenceRecordToReference() {
        return new SamSequenceRecordToReference();
    }

    @Provides @Singleton
    Converter<SAMFileHeader, List<Reference>> createSamHeaderToReferences(final Converter<SAMSequenceRecord, Reference> referenceConverter) {
        return new SamHeaderToReferences(referenceConverter);
    }

    @Provides @Singleton
    Converter<SAMReadGroupRecord, ReadGroup> createSamReadGroupRecordToReadGroups() {
        return new SamReadGroupRecordToReadGroup();
    }

    @Provides @Singleton
    Converter<SAMFileHeader, List<ReadGroup>> createSamHeaderToReadGroups(final Converter<SAMReadGroupRecord, ReadGroup> readGroupConverter) {
        return new SamHeaderToReadGroups(readGroupConverter);
    }

    @Provides @Singleton
    Converter<SAMProgramRecord, ProcessingStep> createSamProgramRecordToProcessingStep() {
        return new SamProgramRecordToProcessingStep();
    }

    @Provides @Singleton
    Converter<SAMFileHeader, List<ProcessingStep>> createSamHeaderToProcessingSteps(final Converter<SAMProgramRecord, ProcessingStep> processingStepConverter) {
        return new SamHeaderToProcessingSteps(processingStepConverter);
    }

    @Provides @Singleton
    Converter<VCFHeader, List<Reference>> createVcfHeaderToReferences(final Converter<SAMSequenceRecord, Reference> referenceConverter) {
        return new VcfHeaderToReferences(referenceConverter);
    }

    @Provides @Singleton
    Converter<VCFHeader, List<Sample>> createVcfHeaderToSamples() {
        return new VcfHeaderToSamples();
    }

    @Provides @Singleton
    Converter<VCFHeader, List<VCFHeaderLine>> createVcfHeaderToVcfHeaderLines() {
        return new VcfHeaderToVcfHeaderLines();
    }
}
