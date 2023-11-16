package br.com.project.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(value = { java.lang.annotation.ElementType.FIELD })
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public abstract @interface IdentificaCampoPesquisa {

	String descricaoCampo(); //descricao do campo para a tela
	String campoConsulta();  // campo do banco
	int principal() default 10000;  // posição que aparecerá no combo
	
	
}
