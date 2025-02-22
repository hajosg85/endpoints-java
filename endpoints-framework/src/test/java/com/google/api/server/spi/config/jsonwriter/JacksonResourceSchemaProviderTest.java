/*
 * Copyright 2016 Google Inc. All Rights Reserved.
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
package com.google.api.server.spi.config.jsonwriter;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.api.server.spi.TypeLoader;

/**
 * Tests for {@link JacksonResourceSchemaProvider}.
 */
@RunWith(JUnit4.class)
public class JacksonResourceSchemaProviderTest extends ResourceSchemaProviderTest {
  @Override
  public ResourceSchemaProvider getResourceSchemaProvider() {
    try {
      return new JacksonResourceSchemaProvider(new TypeLoader(JacksonResourceSchemaProviderTest.class.getClassLoader()));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
