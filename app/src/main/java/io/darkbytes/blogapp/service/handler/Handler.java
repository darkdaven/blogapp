package io.darkbytes.blogapp.service.handler;

import io.darkbytes.blogapp.entity.response.ErrorResponse;

public interface Handler<T> {
    void success(T response);

    void failure(ErrorResponse errorBody);
}
