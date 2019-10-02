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
package org.bdgenomics.convert.ga4gh;

import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import ga4gh.Common.Program;

import ga4gh.Reads.CigarUnit;
import ga4gh.Reads.CigarUnit.Operation;
import ga4gh.Reads.ReadAlignment;

import htsjdk.samtools.Cigar;
import htsjdk.samtools.CigarOperator;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.bdgenomics.convert.Converter;

import org.bdgenomics.formats.avro.Alignment;
import org.bdgenomics.formats.avro.GenotypeAllele;
import org.bdgenomics.formats.avro.ProcessingStep;

/**
 * Guice module for the org.bdgenomics.convert.ga4gh package.
 */
@Immutable
public final class Ga4ghModule extends AbstractModule {
    @Override
    protected void configure() {
        // empty
    }

    @Provides @Singleton
    Converter<CigarOperator, Operation> createCigarOperatorToOperation() {
        return new CigarOperatorToOperation();
    }

    @Provides @Singleton
    Converter<Cigar, List<CigarUnit>> createCigarToCigarUnits(final Converter<CigarOperator, Operation> operatorConverter) {
        return new CigarToCigarUnits(operatorConverter);
    }

    @Provides @Singleton
    Converter<GenotypeAllele, String> createGenotyepAlleleToString() {
        return new GenotypeAlleleToString();
    }

    @Provides @Singleton
    Converter<Alignment, ReadAlignment> createAlignmentToReadAlignment(Converter<Cigar, List<CigarUnit>> cigarConverter) {
        return new AlignmentToReadAlignment(cigarConverter);
    }

    @Provides @Singleton
    Converter<org.bdgenomics.formats.avro.Variant, ga4gh.Variants.Variant> createBdgenomicsVariantToGa4ghVariant() {
        return new BdgenomicsVariantToGa4ghVariant();
    }

    @Provides @Singleton
    Converter<org.bdgenomics.formats.avro.Genotype, ga4gh.Variants.Call> createBdgenomicsGenotypeToGa4ghCall(Converter<GenotypeAllele,String> genotypeAlleleConverter) {
        return new BdgenomicsGenotypeToGa4ghCall(genotypeAlleleConverter);
    }

    @Provides @Singleton
    Converter<ga4gh.Common.Strand, org.bdgenomics.formats.avro.Strand> createGa4ghStrandToBdgenomicsStrand() {
        return new Ga4ghStrandToBdgenomicsStrand();
    }

    @Provides @Singleton
    Converter<org.bdgenomics.formats.avro.Strand, ga4gh.Common.Strand> createBdgenomicsStrandToGa4ghStrand() {
        return new BdgenomicsStrandToGa4ghStrand();
    }

    @Provides @Singleton
    Converter<ga4gh.Common.OntologyTerm, org.bdgenomics.formats.avro.OntologyTerm> createGa4ghOntologyTermToBdgenomicsOntologyTerm() {
        return new Ga4ghOntologyTermToBdgenomicsOntologyTerm();
    }

    @Provides @Singleton
    Converter<String, ga4gh.Common.OntologyTerm> createFeatureTypeToOntologyTerm() {
        return new StringToOntologyTerm();
    }

    @Provides @Singleton
    Converter<org.bdgenomics.formats.avro.OntologyTerm, ga4gh.Common.OntologyTerm> createBdgenomicsOntologyTermToGa4ghOntologyTerm() {
        return new BdgenomicsOntologyTermToGa4ghOntologyTerm();
    }

    @Provides @Singleton
    Converter<Map<String, String>, ga4gh.Common.Attributes> createMapToGa4ghAttributes() {
        return new MapToGa4ghAttributes();
    }

    @Provides @Singleton
    Converter<ga4gh.Common.Attributes, Map<String, String>> createGa4ghAttributesToMap() {
        return new Ga4ghAttributesToMap();
    }

    @Provides @Singleton
    Converter<org.bdgenomics.formats.avro.Feature, ga4gh.SequenceAnnotations.Feature> createBdgenomicsFeatureToGa4ghFeature(final Converter<String, ga4gh.Common.OntologyTerm> featureTypeConverter,
                                                                                                                            final Converter<org.bdgenomics.formats.avro.Strand, ga4gh.Common.Strand> strandConverter,
                                                                                                                            final Converter<java.util.Map<String, String>, ga4gh.Common.Attributes> attributeConverter) {
        return new BdgenomicsFeatureToGa4ghFeature(featureTypeConverter, strandConverter, attributeConverter);
    }

    @Provides @Singleton
    Converter<Program, ProcessingStep> createProgramToProcessingStep() {
        return new ProgramToProcessingStep();
    }

    @Provides @Singleton
    Converter<ProcessingStep, Program> createProcessingStepToProgram() {
        return new ProcessingStepToProgram();
    }

    @Provides @Singleton
    Converter<ga4gh.Reads.ReadGroup, org.bdgenomics.formats.avro.ReadGroup> createGa4ghReadGroupToBdgenomicsReadGroup(final Converter<Program, ProcessingStep> programConverter) {
        return new Ga4ghReadGroupToBdgenomicsReadGroup(programConverter);
    }

    @Provides @Singleton
    Converter<org.bdgenomics.formats.avro.ReadGroup, ga4gh.Reads.ReadGroup> createBdgenomicsReadGroupToGa4ghReadGroup(final Converter<ProcessingStep, Program> processingStepConverter) {
        return new BdgenomicsReadGroupToGa4ghReadGroup(processingStepConverter);
    }
}
