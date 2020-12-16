package com.google.api.server.spi.testing;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import com.google.api.server.spi.config.Api;

/**
 * Checks that optional types are unwrapped properly.
 */
@Api
public class OptionalEndpoint {
	
	public OptionalResults getResult() {
		return null;
	}
	
	private class OptionalResults {
		public Optional<String> getOptionalString() {
			return null;
		}
		public Optional<Integer> getOptionalInteger() {
			return null;
		}
		public Optional<Foo> getOptionalFoo() {
			return null;
		}
		public OptionalInt getOptionalInt() {
			return null;
		}
		public OptionalLong getOptionalLong() {
			return null;
		}
		public OptionalDouble getOptionalDouble() {
			return null;
		}
		//can't be resolved, must be skipped
		public Optional<?> getOptionalAny() {
			return null;
		}
	}
	
}
