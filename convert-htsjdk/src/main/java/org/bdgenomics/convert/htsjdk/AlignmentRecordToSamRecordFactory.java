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

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;

import org.bdgenomics.convert.Converter;

import org.bdgenomics.formats.avro.AlignmentRecord;

/**
 * Factory for creating AlignmentRecord to htsjdk SAMRecord converters, which
 * require late binding for a SAMFileHeader.
 *
 * Thus instead of a converter instance, a converter factory is available via injection:
 * <pre>
 * final class MyClass {
 *   private final AlignmentRecordToSamRecordFactory alignmentRecordToSamRecordFactory;
 *
 *   @Inject
 *   MyClass(final AlignmentRecordToSamRecordFactory alignmentRecordToSamRecordFactory) {
 *     this.alignmentRecordToSamRecordFactory = alignmentRecordToSamRecordFactory;
 *   }
 *
 *   void doIt() {
 *     Converter&lt;AlignmentRecord, SAMRecord&gt; converter = alignmentRecordToSamRecordFactory.create(header);
 *     SAMRecord record = converter.convert(alignmentRecord, stringency, logger);
 *     // ...
 * </pre>
 */
public interface AlignmentRecordToSamRecordFactory {

    /**
     * Create a new AlignmentRecord to htsjdk SAMRecord converter with the specified header.
     *
     * @param header header, must not be null
     */
    Converter<AlignmentRecord, SAMRecord> create(SAMFileHeader header);
}
