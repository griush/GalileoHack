// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.location.custom_suplclient.iono;

/** A container for fields in Klobuchar Iono Model */
public class KlobucharIono extends GnssIonoModel {


    /** Klobuchar model data source ID, where '00': All, '01': BDS, '11': QZSS */
    public final byte[]  dataId = new byte[2];

    /** Ionospheric model value for alpha */
    public final double[] alpha = new double[4];

    /** The group delay term (seconds) */
    public final double[] beta = new double[4];

    private KlobucharIono(Builder builder) {
        super(builder);
        for (int i = 0; i < dataId.length; ++i) {
            dataId[i] = builder.dataId[i];
        }

        for (int i = 0; i < alpha.length; ++i) {
            alpha[i] = builder.alpha[i];
        }
        for (int i = 0; i < beta.length; ++i) {
            beta[i] = builder.beta[i];
        }
    }

    /** Creates a builder with no fields set. */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Builder for {@link KlobucharIono} */
    public static class Builder extends GnssIonoModel.Builder<Builder> {

        // For documentation, see corresponding fields in {@link GnssIonoModel}.
        private byte[] dataId = new byte[2];
        private double[] alpha = new double[4];
        private double[] beta = new double[4];

        private Builder() {}

        @Override
        public Builder getThis() {
            return this;
        }

        /** Sets the data source ID. */
        public Builder setDataId(byte[] dataId) {
            for(int i=0; i<dataId.length; i++){
                this.dataId[i] = dataId[i];
            }

            return getThis();
        }

        /** Sets the satellite user range accuracy (meters). */
        public Builder addAlpha(double alpha, int idx) {
            this.alpha[idx] = alpha;
            return getThis();
        }

        /** Sets the group delay term (seconds). */
        public Builder addBeta(double beta, int idx) {
            this.beta[idx] = beta;
            return getThis();
        }

        /** Builds a {@link KlobucharIono} object as specified by this builder. */
        public KlobucharIono build() {
            return new KlobucharIono(this);
        }
    }
}
