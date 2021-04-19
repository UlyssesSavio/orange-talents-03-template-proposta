package com.br.proposta.anotacoes;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

@ConstraintComposition(CompositionType.OR)
@CNPJ
@CPF
@Documented
@Constraint(validatedBy = {})
@Target({ FIELD })
@Retention(RUNTIME)
public @interface CpfCnpj {

	String message() default "Documento invaildo.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
