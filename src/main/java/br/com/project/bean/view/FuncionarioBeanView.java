package br.com.project.bean.view;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.carregamento.lazy.CarregamentoLazyListForObject;
import br.com.project.geral.controller.EntidadeController;
import br.com.project.model.classes.Entidade;
import br.com.project.util.all.Messagens;

@Controller
@Scope(value = "session")
@ManagedBean(name = "funcionarioBeanView")
public class FuncionarioBeanView extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private String url = "/cadastro/cad_funcionario.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/find_funcionario.jsf?faces-redirect=true";
	private CarregamentoLazyListForObject<Entidade> list = new CarregamentoLazyListForObject<Entidade>();
	private Entidade objetoSelecionado = new Entidade();
	

	@Autowired
	private ContextoBean contextoBean;

	@Autowired
	private EntidadeController entidadeController;


	

	@Override
	protected Class<?> getClassImplement() {
		return Entidade.class;
	}
	
	@Override
	public String novo() throws Exception {
		
		return url;
	}
	
	@Override
	public String editar() throws Exception {
		
		return url;
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		entidadeController.merge(objetoSelecionado);
		if (!objetoSelecionado.getAcessos().contains("USER")) {
			objetoSelecionado.getAcessos().add("USER");
			
		}
	}
	
	@Override
	public void excluir() throws Exception {
			if (objetoSelecionado.getEnt_codigo() != null
					&& objetoSelecionado.getEnt_codigo() > 0) {
				entidadeController.delete(objetoSelecionado);
				list.remove(objetoSelecionado);
				objetoSelecionado = new Entidade();
				sucesso();
			}
	}
	
	
	@Override
	public void saveEdit() throws Exception {
		entidadeController.merge(objetoSelecionado);
		Messagens.msgSeverityInfo("Atualizado com sucesso!");
	}
	
	
	@Override
	public void consultaEntidade() throws Exception {
			objetoSelecionado = new Entidade();
			list.clear();
			list.setTotalRegistroConsulta(super.totalRegistroConsulta(), super.getSqlLazyQuery());
	}
	
	public String redirecionarFindEntidade() throws Exception {
		return urlFind;
	}
	
	
	@Override
	protected InterfaceCrud<Entidade> getController() {
		return entidadeController;
	}
	
	
	
	public CarregamentoLazyListForObject<Entidade> getList() {
		return list;
	}

	public void setList(CarregamentoLazyListForObject<Entidade> list) {
		this.list = list;
	}
	

	public Entidade getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Entidade objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	@Override
	public String condicaoAndParaPesquisa() {
		return "and entity.tipoEntidade = 'FUNCIONARIO' ";
	}


	public ContextoBean getContextoBean() {
		return contextoBean;
	}


	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}


	
}
