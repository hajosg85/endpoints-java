package com.google.api.server.spi.testing;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.types.SimpleDate;

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
		public Optional<Date> getOptionalDate() {
			return null;
		}
		public Optional<SimpleDate> getOptionalSimpleDate() {
			return null;
		}
		
		//primitive optionals
		public OptionalInt getOptionalInt() {
			return null;
		}
		public OptionalLong getOptionalLong() {
			return null;
		}
		public OptionalDouble getOptionalDouble() {
			return null;
		}
		
		//numbers
		public Optional<Integer> getOptionalInteger() {
			return null;
		}
		public Optional<Long> getOptionalLongObject() {
			return null;
		}
		public Optional<Float> getOptionalFloatObject() {
			return null;
		}
		public Optional<Double> getOptionalDoubleObject() {
			return null;
		}
		
		//enums
		public Optional<TestEnum> getOptionalEnum() {
			return null;
		}
		public List<TestEnum> getEnums() {
			return null;
		}
		
		//objects
		public Optional<Foo> getOptionalFoo() {
			return null;
		}
		public List<Foo> getFoos() {
			return null;
		}
		
		//can't be resolved, must be skipped
		public Optional<?> getOptionalAny() {
			return null;
		}
	}
	
}
