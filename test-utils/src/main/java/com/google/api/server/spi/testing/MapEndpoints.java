/*
 * Copyright 2018 Google Inc. All Rights Reserved.
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
package com.google.api.server.spi.testing;

import java.util.List;
import java.util.Map;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

/**
 * Tests the proper caching of schema for Maps with array values
 */
public class MapEndpoints {
	
	@Api(name = "api1")
	public class Api1 {
		@ApiMethod
		public Resource resource() {
			return null;
		}
	}
	
	@Api(name = "api2")
	public class Api2 {
		@ApiMethod
		public Resource resource() {
			return null;
		}
	}
	
	public static class Resource {
		private Map<String, List<TestEnum>> mapOfEnums;
		
		public Map<String, List<TestEnum>> getMapOfEnums() {
			return mapOfEnums;
		}
	}
	
}
