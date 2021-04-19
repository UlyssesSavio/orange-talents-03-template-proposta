package com.br.proposta.validacoes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MeuHandlerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroPadronizado> handle(MethodArgumentNotValidException methodArgumentNotValidException) {
		Collection<String> mensagens = new ArrayList<>();
		BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		fieldErrors.forEach(fieldError -> {
			String message = String.format("Campo %s %s", fieldError.getField(), fieldError.getDefaultMessage());
			mensagens.add(message);
		});

		ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = { IllegalStateException.class })
	public ErroPadronizado handleIlegal(IllegalStateException exception) {

		ErroPadronizado response = new ErroPadronizado(exception.getMessage());

		return response;
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = { MissingServletRequestParameterException.class })
	public ErroPadronizado handleMissing(MissingServletRequestParameterException exception) {

		ErroPadronizado response = new ErroPadronizado(exception.getMessage());

		return response;
	}

	@ExceptionHandler(ApiErroException.class)
	public ResponseEntity<ErroPadronizado> handleApiErroException(ApiErroException apiErroException) {
		Collection<String> mensagens = new ArrayList<>();
		mensagens.add(apiErroException.getReason());

		ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
		return ResponseEntity.status(apiErroException.getHttpStatus()).body(erroPadronizado);
	}

}