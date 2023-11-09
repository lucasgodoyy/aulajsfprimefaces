package br.com.project.bean.geral;

import org.springframework.stereotype.Component;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.report.util.BeanReportView;

/**
 * Responsavel pela rotina de consulta e abstracao de metodos de CRUD e outros
 * padroes
 * 
 * @author alex
 * 
 */
@Component
public abstract class BeanManagedViewAbstract extends BeanReportView {

	private static final long serialVersionUID = 1L;
	public String novo;

	protected abstract Class<?> getClassImplement();
	
	protected abstract InterfaceCrud<?> getController();
	
	

}

