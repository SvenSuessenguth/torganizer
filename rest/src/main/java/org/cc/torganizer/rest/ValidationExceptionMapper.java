package org.cc.torganizer.rest;

import javax.json.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Set;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(ConstraintViolationException e) {
    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

    JsonObject jsonObject = toJsonObject(violations);

    return Response
      .status(Response.Status.BAD_REQUEST)
      .entity(jsonObject)
      .type(MediaType.APPLICATION_JSON)
      .build();
  }

  protected JsonObject toJsonObject(Set<ConstraintViolation<?>> constraintViolations){
      JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
      JsonObjectBuilder objectBuilder = factory.createObjectBuilder();
      objectBuilder.add("violations-count", constraintViolations.size());

      JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
      for(ConstraintViolation violation : constraintViolations){
          arrayBuilder.add(
                  Json.createObjectBuilder()
                          .add("message", violation.getMessage())
                          .add("propertyPath", violation.getPropertyPath().toString())
                          .add("invalildValue", violation.getInvalidValue().toString()));
      }
      objectBuilder.add("violations", arrayBuilder);

      return objectBuilder.build();
  }
}
