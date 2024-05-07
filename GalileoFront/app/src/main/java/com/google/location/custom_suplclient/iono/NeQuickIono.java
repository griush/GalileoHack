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

/** A container for fields in NeQuick Iono Model */
public class NeQuickIono extends GnssIonoModel {

    /** Satellite user range accuracy (meters) */
    public final double[] ai = new double[3];

    /** The group delay term (seconds) */
    public final double[] ionoStormFlag = new double[5];

    private NeQuickIono(Builder builder) {
        super(builder);
        for (int i = 0; i < ai.length; ++i) {
            ai[i] = builder.ai[i];
        }
        for (int i = 0; i < ionoStormFlag.length; ++i) {
            ionoStormFlag[i] = builder.ionoStormFlag[i];
        }
    }

    /** Creates a builder with no fields set. */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Builder for {@link NeQuickIono} */
    public static class Builder extends GnssIonoModel.Builder<Builder> {

        // For documentation, see corresponding fields in {@link GpsEphemeris}.
        private double[] ai = new double[3];
        private double[] ionoStormFlag = new double[5];

        private Builder() {}

        @Override
        public Builder getThis() {
            return this;
        }

        /** Sets the NeQuick ai0, ai1, or ai2. */
        public Builder addAi(double ai, int idx) {
            this.ai[idx] = ai;
            return getThis();
        }

        /** Sets the NeQuick Iono Storm Flags. */
        public Builder addIonoStormFlag(double ionoStormFlag, int idx) {
            this.ionoStormFlag[idx] = ionoStormFlag;
            return getThis();
        }

        /** Builds a {@link NeQuickIono} object as specified by this builder. */
        public NeQuickIono build() {
            return new NeQuickIono(this);
        }
    }
}
