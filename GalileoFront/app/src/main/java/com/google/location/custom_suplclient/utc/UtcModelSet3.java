package com.google.location.custom_suplclient.utc;

public class UtcModelSet3 extends GnssUtcModel{
    /** UTC model parameter NA, callendar day number within four-year period beginning since the leap year (days) [9].
     Scale factor 1 day */
    public final double nA;
    /** UTC model parameter τc, GLONASS time scale correction to UTC(SU) (seconds) [9].
     Scale factor 2-31 seconds.  */
    public final double tauC;
    /** UTC model parameter  B1, coefficient to determine ΔUT1 (seconds) [9].
     Scale factor 2-10 seconds */
    public final double b1;
    /** UTC model parameter  B2, coefficient to determine ΔUT1 (seconds/msd) [9].
     Scale factor 2-16 seconds/msd. */
    public final double b2;
    /** UTC model parameter   KP, notification of expected leap second correction (dimensionless)  */
    public final double kp;

    private UtcModelSet3(Builder builder) {
        super(builder);
        nA = builder.nA;
        tauC = builder.tauC;
        b1 = builder.b1;
        b2 = builder.b2;
        kp = builder.kp;
    }

    /** Creates a builder with no fields set. */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Builder for {@link UtcModelSet3} */
    public static class Builder extends GnssUtcModel.Builder<Builder> {

        private double nA;
        private double tauC;
        private double b1;
        private double b2;
        private double kp;

        private Builder() {}

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder setNA(double nA) {
            this.nA = nA;
            return getThis();
        }

        public Builder setTauC(double tauC) {
            this.tauC = tauC;
            return getThis();
        }

        public Builder setB1(double b1) {
            this.b1 = b1;
            return getThis();
        }

        public Builder setB2(double b2) {
            this.b2 = b2;
            return getThis();
        }

        public Builder setKp(double kp) {
            this.kp = kp;
            return getThis();
        }

        /** Builds a {@link UtcModelSet3} object as specified by this builder. */
        public UtcModelSet3 build() {
            return new UtcModelSet3(this);
        }
    }
}
