package br.com.project.converters;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.project.bean.geral.ObjetoCampoConsulta;

/**
 * Responsavel por efetuar a convers�o nos combos de campos de consulta
 * 
 * @author alex
 * 
 */
@FacesConverter(forClass = ObjetoCampoConsulta.class)
public class ObjetoCampoConsultaConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	public Object getAsObject(FacesContext arg0, UIComponent components,
			String value) {
		if (value != null && !value.isEmpty()) {
			String[] vals = value.split("\\*");
			ObjetoCampoConsulta objetoCampoConsulta = new ObjetoCampoConsulta();
			objetoCampoConsulta.setCampoBanco(vals[0]);
			objetoCampoConsulta.setTipoClass(vals[1]);
			return objetoCampoConsulta;
		} else {
			return null;
		}
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value != null) {
			ObjetoCampoConsulta c = (ObjetoCampoConsulta) value;
			return c.getCampoBanco() + "*" + c.getTipoClass();
		} else {
			return "N�o foi poss�vel estabeler o valor";
		}
	}

}
