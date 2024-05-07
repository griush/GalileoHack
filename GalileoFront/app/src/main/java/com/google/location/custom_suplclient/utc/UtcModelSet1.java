package com.google.location.custom_suplclient.utc;

public class UtcModelSet1 extends GnssUtcModel{
    /** UTC model parameter A1, scale factor 2-50 seconds/second */
    public final double utcA1;
    /** UTC model parameter A0, scale factor 2-30 seconds */
    public final double utcA0;
    /** UTC model parameter T_ot, scale factor 2^12 seconds*/
    public final double utcTot;
    /** UTC model parameter  WNt, scale factor 1 week*/
    public final double utcWn;
    /** UTC model parameter  ΔtLS, scale factor 1 second  */
    public final double utcDeltaTls;
    /** UTC model parameter WNLSF, scale factor 1 week */
    public final double utcWnLsf;
    /** UTC model parameter  DN, scale factor 1 day */
    public final double utcDn;
    /** UTC model parameter  ΔtLSF, scale factor 1 second*/
    public final double utcDeltaTlsf;

    private UtcModelSet1(Builder builder) {
        super(builder);
        utcA1 = builder.utcA1;
        utcA0 = builder.utcA0;
        utcTot = builder.utcTot;
        utcWn = builder.utcWn;
        utcDeltaTls = builder.utcDeltaTls;
        utcWnLsf = builder.utcWnLsf;
        utcDn = builder.utcDn;
        utcDeltaTlsf = builder.utcDeltaTlsf;
    }

    /** Creates a builder with no fields set. */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Builder for {@link UtcModelSet1} */
    public static class Builder extends GnssUtcModel.Builder<Builder> {

        private double utcA1;
        private double utcA0;
        private double utcTot;
        private double utcWn;
        private double utcDeltaTls;
        private double utcWnLsf;
        private double utcDn;
        private double utcDeltaTlsf;

        private Builder() {}

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder setUtcA1(double utcA1) {
            this.utcA1 = utcA1;
            return getThis();
        }

        public Builder setUtcA0(double utcA0) {
            this.utcA0 = utcA0;
            return getThis();
        }

        public Builder setUtcTot(double utcTot) {
            this.utcTot = utcTot;
            return getThis();
        }

        public Builder setUtcWn(double utcWn) {
            this.utcWn = utcWn;
            return getThis();
        }

        public Builder setUtcDeltaTls(double utcDeltaTls) {
            this.utcDeltaTls = utcDeltaTls;
            return getThis();
        }

        public Builder setUtcWnLsf(double utcWnLsf) {
            this.utcWnLsf = utcWnLsf;
            return getThis();
        }

        public Builder setUtcDn(double utcDn) {
            this.utcDn = utcDn;
            return getThis();
        }

        public Builder setUtcDeltaTlsf(double utcDeltaTlsf) {
            this.utcDeltaTlsf = utcDeltaTlsf;
            return getThis();
        }

        /** Builds a {@link UtcModelSet1} object as specified by this builder. */
        public UtcModelSet1 build() {
            return new UtcModelSet1(this);
        }
    }
}
