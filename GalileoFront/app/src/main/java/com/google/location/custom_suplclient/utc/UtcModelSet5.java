package com.google.location.custom_suplclient.utc;

public class UtcModelSet5 extends GnssUtcModel{
    /** UTC model parameter A0UTC, BDS clock bias relative to UTC, seconds [23].
     Scale factor 2-30 seconds. */
    public final double utcA0;
    /** UTC model parameter A1UTC, BDS clock rate relative to UTC, sec/sec [23].
     Scale factor 2-50 sec/sec */
    public final double utcA1;
    /** UTC model parameter   ΔtLS, delta time due to leap seconds before the new leap second effective, seconds [23].
     Scale factor 1 second.  */
    public final double utcDeltaTls;
    /** UTC model parameter  WNLSF, week number of the new leap second, weeks [23].
     Scale factor 1 week */
    public final double utcWnLsf;
    /** UTC model parameter  DN, day number of week of the new leap second, days [23].
     Scale factor 1 day.  */
    public final double utcDn;
    /** UTC model parameter  ΔtLSF, delta time due to leap seconds after the new leap second effective, seconds [23].
     Scale factor 1 second. */
    public final double utcDeltaTlsf;

    private UtcModelSet5(Builder builder) {
        super(builder);
        utcA1 = builder.utcA1;
        utcA0 = builder.utcA0;
        utcDeltaTls = builder.utcDeltaTls;
        utcWnLsf = builder.utcWnLsf;
        utcDn = builder.utcDn;
        utcDeltaTlsf = builder.utcDeltaTlsf;
    }

    /** Creates a builder with no fields set. */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Builder for {@link UtcModelSet5} */
    public static class Builder extends GnssUtcModel.Builder<Builder> {

        private double utcA1;
        private double utcA0;
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

        /** Builds a {@link UtcModelSet5} object as specified by this builder. */
        public UtcModelSet5 build() {
            return new UtcModelSet5(this);
        }
    }
}
