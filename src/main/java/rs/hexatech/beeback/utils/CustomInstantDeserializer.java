package rs.hexatech.beeback.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.time.Instant;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;

public class CustomInstantDeserializer extends JsonDeserializer<Instant> {

  private static final DateTimeFormatter formatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS") // Custom pattern with 6 fractional digits
      .withChronology(IsoChronology.INSTANCE)
      .withZone(java.time.ZoneOffset.UTC);

//    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

  @Override
  public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    String text = ((TextNode) parser.getCodec().readTree(parser)).asText();
    String test = getValidInstantString(text);
    Instant i = Instant.from(formatter.parse(getValidInstantString(text)));
    return i;
  }

  private String getValidInstantString(String dateString) {
    int plusIndex = dateString.indexOf('+');
    if (plusIndex > -1) {
      dateString = dateString.substring(0, plusIndex);
    }
    int dotIndex = dateString.indexOf('.');
    if (dateString.length() > 23) {
      dateString = dateString.substring(0, 24) + "000000000";
    } else if (dotIndex == -1) {
      dateString += ".000000000";
    } else {
      dateString += "000000000";
    }
    dotIndex = dateString.indexOf('.');
    return dateString.substring(0, dotIndex + 7);
  }
}