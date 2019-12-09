package io.darkbytes.blogapp.entity.response;

import java.io.IOException;

import io.darkbytes.blogapp.utit.JsonToObjectConverter;
import okhttp3.ResponseBody;

public class ErrorResponse {
    private Boolean error;
    private String message;

    public ErrorResponse(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ErrorResponse fromErrorBody(ResponseBody response) {
        try {
            ErrorResponse errorResponse = new JsonToObjectConverter<ErrorResponse>()
                    .convertToObject(response.string(), ErrorResponse.class);

            return errorResponse;
        } catch (IOException e) {
            return null;
        }
    }
}
