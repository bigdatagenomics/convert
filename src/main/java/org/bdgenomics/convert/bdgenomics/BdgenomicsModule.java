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
package org.bdgenomics.convert.bdgenomics;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.bdgenomics.convert.Converter;

import org.bdgenomics.formats.avro.Dbxref;
import org.bdgenomics.formats.avro.OntologyTerm;
import org.bdgenomics.formats.avro.Strand;

/**
 * Guice module for the org.bdgenomics.convert.bdgenomics package.
 */
public final class BdgenomicsModule extends AbstractModule {
    @Override
    protected void configure() {
        // empty
    }

    @Provides @Singleton
    Converter<String, Dbxref> createStringToDbxref() {
        return new StringToDbxref();
    }

    @Provides @Singleton
    Converter<Dbxref, String> createDbxrefToString() {
        return new DbxrefToString();
    }

    @Provides @Singleton
    Converter<String, OntologyTerm> createStringToOntologyTerm() {
        return new StringToOntologyTerm();
    }

    @Provides @Singleton
    Converter<OntologyTerm, String> createOntologyTermToString() {
        return new OntologyTermToString();
    }

    @Provides @Singleton
    Converter<String, Strand> createStringToStrand() {
        return new StringToStrand();
    }

    @Provides @Singleton
    Converter<Strand, String> createStrandToString() {
        return new StrandToString();
    }
}
