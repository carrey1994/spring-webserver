package com.jameswu.demo.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.Instant;

public class InstantSerializer extends StdSerializer<Instant> {

	public InstantSerializer(Class<Instant> t) {
		super(t);
	}

	public InstantSerializer() {
		this(Instant.class);
	}

	@Override
	public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeNumber(value.toEpochMilli());
	}
}
