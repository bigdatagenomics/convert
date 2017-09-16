package org.bdgenomics.convert.ga4gh;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

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

    @Override
    public ga4gh.Variants.Variant convert(final org.bdgenomics.adam.models.VariantContext variantContext,
                                          final ConversionStringency stringency,
                                          final Logger logger) throws ConversionException {
        return ga4gh.Variants.Variant.newBuilder().build();
        
    }
}
