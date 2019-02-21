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

import java.util.Map;

import javax.annotation.concurrent.Immutable;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;

/**
 * Convert bdg-formats Feature to GA4GH Feature.
 */
@Immutable
final class BdgenomicsFeatureToGa4ghFeature extends AbstractConverter<org.bdgenomics.formats.avro.Feature, ga4gh.SequenceAnnotations.Feature> {
    /** Convert bdg-formats Feature.featureType as String to GA4GH OntologyTerm. */
    private final Converter<String, ga4gh.Common.OntologyTerm> featureTypeConverter;
    /** Convert bdg-formats Strand to GA4GH Strand. */
    private final Converter<org.bdgenomics.formats.avro.Strand, ga4gh.Common.Strand> strandConverter;
    /** Convert map of attributes to GA4GH Attributes. */
    private final Converter<Map<String, String>, ga4gh.Common.Attributes> attributeConverter;

    /**
     * Convert bdg-formats Feature to GA4GH Feature.
     *
     * @param featureTypeConverter feature type converter, must not be null
     * @param strandConverter strand converter, must not be null
     */
    BdgenomicsFeatureToGa4ghFeature(final Converter<String, ga4gh.Common.OntologyTerm> featureTypeConverter,
                                    final Converter<org.bdgenomics.formats.avro.Strand, ga4gh.Common.Strand> strandConverter,
                                    final Converter<Map<String, String>, ga4gh.Common.Attributes> attributeConverter) {
        super(org.bdgenomics.formats.avro.Feature.class, ga4gh.SequenceAnnotations.Feature.class);
        checkNotNull(featureTypeConverter);
        checkNotNull(strandConverter);
        checkNotNull(attributeConverter);
        this.featureTypeConverter = featureTypeConverter;
        this.strandConverter = strandConverter;
        this.attributeConverter = attributeConverter;
    }


    @Override
    public ga4gh.SequenceAnnotations.Feature convert(final org.bdgenomics.formats.avro.Feature feature,
                                                     final ConversionStringency stringency,
                                                     final Logger logger) throws ConversionException {

        if (feature == null) {
            warnOrThrow(feature, "must not be null", null, stringency, logger);
            return null;
        }

        ga4gh.SequenceAnnotations.Feature.Builder builder = ga4gh.SequenceAnnotations.Feature.newBuilder();
        if (feature.getStart() != null) {
           builder.setStart(feature.getStart());
        }
        if (feature.getEnd() != null) {
            builder.setEnd(feature.getEnd());
        }
        if (feature.getName() != null) {
            builder.setName(feature.getName());
        }
        if (feature.getStrand() != null) {
           builder.setStrand(strandConverter.convert(feature.getStrand(), stringency, logger));
        }
        if (feature.getContigName() != null) {
           builder.setReferenceName(feature.getContigName());
        }
        if (feature.getFeatureId() != null) {
            builder.setId(feature.getFeatureId());
        }
        if (feature.getGeneId() != null) {
            builder.setGeneSymbol(feature.getGeneId());
        }
        if (feature.getFeatureType() != null) {
            builder.setFeatureType(ga4gh.Common.OntologyTerm.newBuilder().setTermId(feature.getFeatureType()));
        }
        if (feature.getAttributes() != null) {
            builder.setAttributes(attributeConverter.convert(feature.getAttributes(), stringency, logger));
        }
        return builder.build();
    }
}
