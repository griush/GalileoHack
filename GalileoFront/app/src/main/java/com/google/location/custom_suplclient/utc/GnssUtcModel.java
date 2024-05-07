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

package com.google.location.custom_suplclient.utc;

/** A class representing the GNSS UTC model */
public abstract class GnssUtcModel {

    /** The IE GNSS-ID-Bitmap is used to indicate several GNSSs using a bit map*/
    public final int gnssId;

    /** The IE GNSS-SignalID is used to indicate a specific GNSS signal type. The interpretation of GNSS-SignalID depends
     on the GNSS-ID.*/
    public final int gnssSignalId;

    protected GnssUtcModel(GnssUtcModel.Builder<?> builder) {
        gnssId = builder.gnssId;
        gnssSignalId = builder.gnssSignalId;

    }

    /** Builder for {@link GnssUtcModel} */
    public abstract static class Builder<T extends GnssUtcModel.Builder<T>> {

        private int gnssId;
        private int gnssSignalId;

        public Builder() {}

        public abstract T getThis();

        /** Sets the {@link #gnssId}. */
        public T setGnssId(int gnssId) {
            this.gnssId = gnssId;
            return getThis();
        }

        /** Sets the {@link #gnssSignalId}. */
        public T setGnssSignalId(int gnssSignalId) {
            this.gnssSignalId = gnssSignalId;
            return getThis();
        }
    }
}
