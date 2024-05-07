package com.google.location.custom_suplclient.utc;

public class UtcModelSet4 extends GnssUtcModel{
    /** UTC model parameter A1WNT, sec/sec ([10], Message Type 12).
     Scale factor 2-50 seconds/second. */
    public final double utcA1wnt;
    /** UTC model parameter A0WNT, seconds ([10], Message Type 12).
     Scale factor 2-30 seconds */
    public final double utcA0wnt;
    /** UTC model parameter  tot, seconds ([10], Message Type 12).
     Scale factor 212 seconds*/
    public final double utcTot;
    /** UTC model parameter  WNt, weeks ([10], Message Type 12).
     Scale factor 1 week.*/
    public final double utcWnt;
    /** UTC model parameter   ΔtLS, seconds ([10], Message Type 12).
     Scale factor 1 second.   */
    public final double utcDeltaTls;
    /** UTC model parameter WNLSF, weeks ([10], Message Type 12).
     Scale factor 1 week.  */
    public final double utcWnLsf;
    /** UTC model parameter  DN, days ([10], Message Type 12).
     Scale factor 1 day */
    public final double utcDn;
    /** UTC model parameter  ΔtLSF, seconds ([10], Message Type 12).
     Scale factor 1 second. */
    public final double utcDeltaTlsf;
    /** If GNSS-ID indicates "sbas", this field indicates the UTC standard used for the SBAS network time indicated by
    SBAS-ID to UTC relation as defined in the table Value of UTC Standard ID to UTC Standard relation shown below. */
    public final double utcStandardId;

    private UtcModelSet4(Builder builder) {
        super(builder);
        utcA1wnt = builder.utcA1wnt;
        utcA0wnt = builder.utcA0wnt;
        utcTot = builder.utcTot;
        utcWnt = builder.utcWnt;
        utcDeltaTls = builder.utcDeltaTls;
        utcWnLsf = builder.utcWnLsf;
        utcDn = builder.utcDn;
        utcDeltaTlsf = builder.utcDeltaTlsf;
        utcStandardId = builder.utcStandardId;
    }

    /** Creates a builder with no fields set. */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Builder for {@link UtcModelSet4} */
    public static class Builder extends GnssUtcModel.Builder<Builder> {

        private double utcA1wnt;
        private double utcA0wnt;
        private double utcTot;
        private double utcWnt;
        private double utcDeltaTls;
        private double utcWnLsf;
        private double utcDn;
        private double utcDeltaTlsf;
        private double utcStandardId;

        private Builder() {}

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder setUtcA1wnt(double utcA1wnt) {
            this.utcA1wnt = utcA1wnt;
            return getThis();
        }

        public Builder setUtcA0wnt(double utcA0wnt) {
            this.utcA0wnt = utcA0wnt;
            return getThis();
        }

        public Builder setUtcTot(double utcTot) {
            this.utcTot = utcTot;
            return getThis();
        }

        public Builder setUtcWnt(double utcWnt) {
            this.utcWnt = utcWnt;
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

        public Builder setUtcStandardId(double utcStandardId) {
            this.utcStandardId = utcStandardId;
            return getThis();
        }

        /** Builds a {@link UtcModelSet4} object as specified by this builder. */
        public UtcModelSet4 build() {
            return new UtcModelSet4(this);
        }
    }
}
