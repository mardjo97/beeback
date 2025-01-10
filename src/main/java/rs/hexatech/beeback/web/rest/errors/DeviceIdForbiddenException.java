package rs.hexatech.beeback.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause;

public class DeviceIdForbiddenException extends ErrorResponseException {

  private static final long serialVersionUID = 1L;

  public DeviceIdForbiddenException() {
    super(
        HttpStatus.FORBIDDEN,
        ProblemDetailWithCause.ProblemDetailWithCauseBuilder.instance()
            .withStatus(HttpStatus.FORBIDDEN.value())
            .withType(ErrorConstants.DEVICE_ID_FORBIDDEN)
            .withTitle("Device Id Forbidden")
            .build(),
        null
    );

  }
}
