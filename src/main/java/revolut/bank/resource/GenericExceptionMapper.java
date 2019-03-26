package revolut.bank.resource;

import revolut.bank.dto.ErrorMessage;
import revolut.bank.exception.EntityNotFoundException;
import com.google.inject.Singleton;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        setHttpStatus(ex, errorMessage);
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setDetailedMessage(ex.getCause() != null ? ex.getCause().getMessage() : null);

        return Response.status(errorMessage.getStatus())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
        if (ex instanceof WebApplicationException) {
            errorMessage.setStatus(((WebApplicationException)ex).getResponse().getStatus());
        } else if (ex instanceof IllegalArgumentException) {
            errorMessage.setStatus(HttpStatus.BAD_REQUEST_400);
        } else if (ex instanceof EntityNotFoundException) {
            errorMessage.setStatus(HttpStatus.NOT_FOUND_404);
        } else {
            errorMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }
}
