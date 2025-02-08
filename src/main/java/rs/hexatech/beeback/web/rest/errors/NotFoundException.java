package rs.hexatech.beeback.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause;

public class NotFoundException extends ErrorResponseException {

  private static final long serialVersionUID = 1L;

  public NotFoundException() {
    super(
        HttpStatus.NOT_FOUND,
        ProblemDetailWithCause.ProblemDetailWithCauseBuilder.instance()
            .withStatus(HttpStatus.NOT_FOUND.value())
            .withType(ErrorConstants.NOT_FOUND)
            .withTitle("Not Found")
            .build(),
        null
    );

  }
}
