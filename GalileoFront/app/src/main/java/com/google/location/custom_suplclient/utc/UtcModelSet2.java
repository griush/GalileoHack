package com.google.location.custom_suplclient.utc;

public class UtcModelSet2 extends GnssUtcModel{
    /** UTC model parameter  A0-n, bias coefficient of GNSS time scale relative to UTC time scale (seconds).
     Scale factor 2-35 seconds */
    public final double utcA0;
    /** UTC model parameter  A1-n, drift coefficient of GNSS time scale relative to UTC time scale (sec/sec).
     Scale factor 2-51 seconds/second */
    public final double utcA1;
    /** UTC model parameter A2-n, drift rate correction coefficient of GNSS time scale relative to UTC time scale (sec/sec2
     ). Scale factor 2-68 seconds/second2 */
    public final double utcA2;
    /** UTC model parameter  Parameter ΔtLS, current or past leap second count (seconds). Scale factor 1 second.   */
    public final double utcDeltaTls;
    /** UTC model parameter tot, time data reference time of week (seconds).
     Scale factor 24
     seconds. */
    public final double utcTot;
    /** UTC model parameter  WNot, time data reference week number (weeks) .
     Scale factor 1 week. */
    public final double utcWnot;
    /** UTC model parameter  WNLSF, leap second reference week number (weeks) .
     Scale factor 1 week */
    public final double utcWnLsf;
    /** UTC model parameter  DN, leap second reference day number (days).
     Scale factor 1 day */
    public final double utcDn;
    /** UTC model parameter   ΔtLSF, current or future leap second count (seconds) .
     Scale factor 1 second*/
    public final double utcDeltaTlsf;

    private UtcModelSet2(Builder builder) {
        super(builder);
        utcA0 = builder.utcA0;
        utcA1 = builder.utcA1;
        utcA2 = builder.utcA2;
        utcDeltaTls = builder.utcDeltaTls;
        utcTot = builder.utcTot;
        utcWnot = builder.utcWnot;
        utcWnLsf = builder.utcWnLsf;
        utcDn = builder.utcDn;
        utcDeltaTlsf = builder.utcDeltaTlsf;
    }

    /** Creates a builder with no fields set. */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Builder for {@link UtcModelSet2} */
    public static class Builder extends GnssUtcModel.Builder<Builder> {

        private double utcA0;
        private double utcA1;
        private double utcA2;
        private double utcDeltaTls;
        private double utcTot;
        private double utcWnot;
        private double utcWnLsf;
        private double utcDn;
        private double utcDeltaTlsf;

        private Builder() {}

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder setUtcA0(double utcA0) {
            this.utcA0 = utcA0;
            return getThis();
        }

        public Builder setUtcA1(double utcA1) {
            this.utcA1 = utcA1;
            return getThis();
        }

        public Builder setUtcA2(double utcA2) {
            this.utcA2 = utcA2;
            return getThis();
        }

        public Builder setUtcTot(double utcTot) {
            this.utcTot = utcTot;
            return getThis();
        }

        public Builder setUtcWnot(double utcWnot) {
            this.utcWnot = utcWnot;
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

        /** Builds a {@link UtcModelSet2} object as specified by this builder. */
        public UtcModelSet2 build() {
            return new UtcModelSet2(this);
        }
    }
}
