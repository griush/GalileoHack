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

package com.google.location.custom_suplclient.supl;

import com.google.auto.value.AutoValue;

/**
 * Holds SUPL Connection request parameters.
 */
@AutoValue
public class SuplConnectionRequest {
  public String serverHost;
  public int serverPort;
  public boolean sslEnabled;
  public boolean loggingEnabled;
  public boolean messageLoggingEnabled;
  public String getServerHost() {
    return serverHost;}

  public int getServerPort() {
    return serverPort;
  }

  public boolean isSslEnabled() {
    return sslEnabled;
  }

  public boolean isLoggingEnabled() {
    return loggingEnabled;
  }

  public boolean isMessageLoggingEnabled() {
    return messageLoggingEnabled;
  }

  public static Builder builder() {
    return new SuplConnectionRequest.Builder()
        .setMessageLoggingEnabled(false)
        .setLoggingEnabled(false)
        .setSslEnabled(false);
  }

  public static class Builder {
    String host = SuplTester.serverHost;
    int port = SuplTester.serverPort;
    boolean ssl = SuplTester.sslEnabled;
    boolean logging = SuplTester.loggingEnabled;
    boolean messageLogging = SuplTester.messageLoggingEnabled;

    public Builder setServerHost(String host){
      this.host = host;
      return this;
    }
    public Builder setServerPort(int port){
      this.port = port;
      return this;
    }
    public Builder setSslEnabled(boolean enableSsl){
      ssl = enableSsl;
      return this;
    }
    public Builder setLoggingEnabled(boolean enableLogging){
      logging = enableLogging;
      return this;
    }
    public Builder setMessageLoggingEnabled(boolean enableMessageLogging){
      messageLogging = enableMessageLogging;
      return this;
    }
    public SuplConnectionRequest build()
    {
        SuplConnectionRequest request = new SuplConnectionRequest();
        request.serverHost = host;
        request.serverPort = port;
        request.sslEnabled = ssl;
        request.loggingEnabled = logging;
        request.messageLoggingEnabled = messageLogging;
        return request;
    }
  }
}
