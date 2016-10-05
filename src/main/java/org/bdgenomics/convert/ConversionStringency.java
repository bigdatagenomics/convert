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
package org.bdgenomics.convert;

/**
 * Conversion stringency.
 */
public enum ConversionStringency {
    /** Silent validation, ignore conversion failures. */
    SILENT,

    /** Lenient validation, log conversion failures as warnings. */
    LENIENT,

    /** Strict validation, throw runtime ConversionExceptions on conversion failures. */
    STRICT;


    /**
     * Return true if this conversion stringency is <code>SILENT</code>.
     *
     * @return true if this conversion stringency is <code>SILENT</code>
     */
    public boolean isSilent() {
        return SILENT == this;
    }

    /**
     * Return true if this conversion stringency is <code>LENIENT</code>.
     *
     * @return true if this conversion stringency is <code>LENIENT</code>
     */
    public boolean isLenient() {
        return LENIENT == this;
    }

    /**
     * Return true if this conversion stringency is <code>STRICT</code>.
     *
     * @return true if this conversion stringency is <code>STRICT</code>
     */
    public boolean isStrict() {
        return STRICT == this;
    }
}
