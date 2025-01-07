package rs.hexatech.beeback.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;

public class CustomInstantSerializer extends JsonSerializer<Instant> {

  private static final DateTimeFormatter formatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS") // Custom pattern with 10 fractional digits
      .withChronology(IsoChronology.INSTANCE)
      .withZone(java.time.ZoneOffset.UTC);

  @Override
  public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    String formatted = formatter.format(value);
    gen.writeString(formatted);
  }
}
