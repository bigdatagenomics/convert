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
