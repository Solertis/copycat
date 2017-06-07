/*
 * Copyright 2017-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.copycat.protocol;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.util.Assert;

import java.util.Objects;

/**
 * Open session request.
 */
public class OpenSessionRequest extends AbstractRequest {
  public static final String NAME = "open-session";

  /**
   * Returns a new open session request builder.
   *
   * @return A new open session request builder.
   */
  public static Builder builder() {
    return new Builder(new OpenSessionRequest());
  }

  /**
   * Returns an open session request builder for an existing request.
   *
   * @param request The request to build.
   * @return The open session request builder.
   * @throws NullPointerException if {@code request} is null
   */
  public static Builder builder(OpenSessionRequest request) {
    return new Builder(request);
  }

  private String client;
  private String name;
  private String type;
  private long timeout;

  /**
   * Returns the client identifier.
   *
   * @return The client identifier.
   */
  public String client() {
    return client;
  }

  /**
   * Returns the state machine name.
   *
   * @return The state machine name.
   */
  public String name() {
    return name;
  }

  /**
   * Returns the state machine type;
   *
   * @return The state machine type.
   */
  public String type() {
    return type;
  }

  /**
   * Returns the session timeout.
   *
   * @return The session timeout.
   */
  public long timeout() {
    return timeout;
  }

  @Override
  public void readObject(BufferInput<?> buffer, Serializer serializer) {
    super.readObject(buffer, serializer);
    client = buffer.readString();
    name = buffer.readString();
    type = buffer.readString();
    timeout = buffer.readLong();
  }

  @Override
  public void writeObject(BufferOutput<?> buffer, Serializer serializer) {
    super.writeObject(buffer, serializer);
    buffer.writeString(client);
    buffer.writeString(name);
    buffer.writeString(type);
    buffer.writeLong(timeout);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), name, type, timeout);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof OpenSessionRequest) {
      OpenSessionRequest request = (OpenSessionRequest) object;
      return request.name.equals(name) && request.type.equals(type) && request.timeout == timeout;
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("%s[name=%s, type=%s, timeout=%d]", getClass().getSimpleName(), name, type, timeout);
  }

  /**
   * Open session request builder.
   */
  public static class Builder extends AbstractRequest.Builder<Builder, OpenSessionRequest> {
    protected Builder(OpenSessionRequest request) {
      super(request);
    }

    /**
     * Sets the client identifier.
     *
     * @param client The client identifier.
     * @return The open session request builder.
     * @throws NullPointerException if {@code client} is {@code null}
     */
    public Builder withClient(String client) {
      request.client = Assert.notNull(client, "client");
      return this;
    }

    /**
     * Sets the state machine name.
     *
     * @param name The state machine name.
     * @return The open session request builder.
     * @throws NullPointerException if {@code name} is {@code null}
     */
    public Builder withName(String name) {
      request.name = Assert.notNull(name, "name");
      return this;
    }

    /**
     * Sets the state machine type.
     *
     * @param type The state machine type.
     * @return The open session request builder.
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public Builder withType(String type) {
      request.type = Assert.notNull(type, "type");
      return this;
    }

    /**
     * Sets the session timeout.
     *
     * @param timeout The session timeout.
     * @return The open session request builder.
     * @throws IllegalArgumentException if {@code timeout} is not positive
     */
    public Builder withTimeout(long timeout) {
      request.timeout = Assert.argNot(timeout, timeout < 0, "timeout must be positive");
      return this;
    }

    /**
     * @throws IllegalStateException is session is not positive
     */
    @Override
    public OpenSessionRequest build() {
      super.build();
      Assert.notNull(request.client, "client");
      Assert.notNull(request.name, "name");
      Assert.notNull(request.type, "type");
      return request;
    }
  }
}
