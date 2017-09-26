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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import javax.annotation.concurrent.Immutable;

import com.google.protobuf.ListValue;
import com.google.protobuf.Value;

import ga4gh.Variants;
import ga4gh.Variants.Call;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.formats.avro.AlignmentRecord;
import org.bdgenomics.formats.avro.Genotype;

import org.bdgenomics.formats.avro.Genotype;
import org.bdgenomics.formats.avro.GenotypeAllele;
import org.slf4j.Logger;

@Immutable
public class BdgenomicsGenotypeToGa4ghCall extends AbstractConverter<org.bdgenomics.formats.avro.Genotype, ga4gh.Variants.Call> {

    private final Converter<GenotypeAllele, String> genotypeAlleleConverter;


    BdgenomicsGenotypeToGa4ghCall(final Converter<GenotypeAllele,String> genotypeAlleleConverter) {
        super(org.bdgenomics.formats.avro.Genotype.class, ga4gh.Variants.Call.class);
        checkNotNull(genotypeAlleleConverter);
        this.genotypeAlleleConverter = genotypeAlleleConverter;
    }

    @Override
    public ga4gh.Variants.Call convert(final org.bdgenomics.formats.avro.Genotype genotype,
                                       final ConversionStringency stringency,
                                       final Logger logger) throws ConversionException {
        if (genotype == null) {
            warnOrThrow(genotype, "must not be null", null, stringency, logger);
            return null;
        }

        java.util.List<GenotypeAllele> inputAlleles = genotype.getAlleles();
        String alleleFirst = genotypeAlleleConverter.convert(inputAlleles.get(0), stringency, logger);
        String alleleSecond = ".";
        if (inputAlleles.size() == 2) {
            alleleSecond = genotypeAlleleConverter.convert(inputAlleles.get(1), stringency, logger);
        }

        com.google.protobuf.Value alleleFirstValue = com.google.protobuf.Value.newBuilder().setStringValue(alleleFirst).build();
        com.google.protobuf.Value alleleSecondValue = com.google.protobuf.Value.newBuilder().setStringValue(alleleSecond).build();
        com.google.protobuf.ListValue calls = ListValue.newBuilder().addValues(alleleFirstValue).addValues(alleleSecondValue).build();

        java.util.List<Double> gl = genotype.getGenotypeLikelihoods();

        ga4gh.Variants.Call.Builder builder = ga4gh.Variants.Call.newBuilder();
        if(genotype.getSampleId() != null) {
            builder.setCallSetName(genotype.getSampleId());
        }

        builder.setGenotype(calls);

        if (genotype.getPhased() && genotype.getPhaseSetId() != null) {
            builder = builder.setPhaseset(genotype.getPhaseSetId().toString());

        }
        if (!gl.isEmpty()) {
            builder = builder.addAllGenotypeLikelihood(gl);
        }

        builder.setCallSetId(genotype.getSampleId());
        return builder.build();
    }
}
