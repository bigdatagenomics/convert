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

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.formats.avro.GenotypeAllele;

import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.GenotypeAllele;

import org.slf4j.Logger;

import javax.annotation.concurrent.Immutable;


@Immutable
final class GenotypeAlleleToString extends AbstractConverter<GenotypeAllele, String> {
    GenotypeAlleleToString() {
        super(org.bdgenomics.formats.avro.GenotypeAllele.class, String.class);
    }

    @Override
    public String convert(final org.bdgenomics.formats.avro.GenotypeAllele genotypeAllele,
    final ConversionStringency stringency,
    final Logger logger) throws ConversionException {
        if (genotypeAllele == GenotypeAllele.REF || genotypeAllele == GenotypeAllele.OTHER_ALT) return "0";
        else if (genotypeAllele == GenotypeAllele.ALT ) return "1";
        else return ".";
    }
}
