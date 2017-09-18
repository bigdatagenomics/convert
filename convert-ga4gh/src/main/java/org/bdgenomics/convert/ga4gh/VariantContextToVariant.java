package org.bdgenomics.convert.ga4gh;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.protobuf.ListValue;
import com.google.protobuf.Value;
import ga4gh.Common.Position;
import ga4gh.Common.Strand;

import ga4gh.Reads.CigarUnit;
import ga4gh.Reads.LinearAlignment;
import ga4gh.Reads.ReadAlignment;

import htsjdk.samtools.Cigar;
import htsjdk.samtools.TextCigarCodec;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.AlignmentRecord;
import org.bdgenomics.formats.avro.Genotype;


import org.bdgenomics.formats.avro.Genotype;
import org.bdgenomics.formats.avro.GenotypeAllele;
import org.slf4j.Logger;

import org.bdgenomics.adam.models.VariantContext;

import ga4gh.Variants;

import ga4gh.Variants.Call;



/**
 * Created by paschalj on 9/16/17.
 */

@Immutable
final class VariantContextToVariant extends AbstractConverter<org.bdgenomics.adam.models.VariantContext, ga4gh.Variants.Variant> {


    VariantContextToVariant() {
        super(org.bdgenomics.adam.models.VariantContext.class, ga4gh.Variants.Variant.class);
    }


    String toGA4GHAllele(org.bdgenomics.formats.avro.GenotypeAllele genotypeAllele) {
        if (genotypeAllele == GenotypeAllele.REF || genotypeAllele == GenotypeAllele.OTHER_ALT) return "0";
        else if (genotypeAllele == GenotypeAllele.ALT ) return "1";
        else return ".";

    }

    ga4gh.Variants.Call genotypeToCall(org.bdgenomics.formats.avro.Genotype genotype) {

        java.util.List<GenotypeAllele> inputAlleles = genotype.getAlleles();
        String alleleFirst = toGA4GHAllele(inputAlleles.get(0));
        String alleleSecond = "";
        if (inputAlleles.size() == 2) {
            alleleSecond = toGA4GHAllele(inputAlleles.get(1));
        }
        else {
            alleleSecond = ".";
        }


        com.google.protobuf.Value alleleFirstValue = com.google.protobuf.Value.newBuilder().setStringValue(alleleFirst).build();
        com.google.protobuf.Value alleleSecondtValue = com.google.protobuf.Value.newBuilder().setStringValue(alleleSecond).build();

        com.google.protobuf.ListValue calls = ListValue.newBuilder().addValues(alleleFirstValue).addValues(alleleSecondtValue).build();

        java.util.List<Double> gl = genotype.getGenotypeLikelihoods();

        ga4gh.Variants.Call.Builder builder = ga4gh.Variants.Call.newBuilder().setCallSetName(genotype.getSampleId())
                                              .setCallSetId("NA").setGenotype(calls);

        if (genotype.getPhased()) {
            builder = builder.setPhaseset(genotype.getPhaseSetId().toString());

        }
        if (!gl.isEmpty()) {
            builder = builder.addAllGenotypeLikelihood(gl);
        }




        return ga4gh.Variants.Call.newBuilder().build();
    }

    @Override
    public ga4gh.Variants.Variant convert(final org.bdgenomics.adam.models.VariantContext variantContext,
                                          final ConversionStringency stringency,
                                          final Logger logger) throws ConversionException {


        return ga4gh.Variants.Variant.newBuilder().build();

    }
}
