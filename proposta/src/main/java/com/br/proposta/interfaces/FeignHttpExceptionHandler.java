package com.br.proposta.interfaces;

import feign.Response;

public interface FeignHttpExceptionHandler {
    Exception handle(Response response);

}
