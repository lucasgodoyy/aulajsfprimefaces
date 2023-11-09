package br.com.project.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.geral.controller.CidadeController;
import br.com.project.model.classes.Cidade;


@Controller
@Scope("session")
@ManagedBean(name = "cidadeBeanView")
public class CidadeBeanView extends BeanManagedViewAbstract{

	
	private static final long serialVersionUID = 1L;

	private String url = "/cadastro/cad_cidade.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/find_cidade.jsf?faces-redirect=true";
	
	
	private Cidade objetoSelecionado = new Cidade();	
	
	@Autowired
	private CidadeController cidadeController;
	
	private List<Cidade> list = new ArrayList<Cidade>();

	
	public List<Cidade> getList() throws Exception {
		list = cidadeController.finList(getClassImplement());
		return list;
	}
	
	@Override
	public String editar() throws Exception {
		list.clear();
		return url;
	}


	@Override
	public String save() throws Exception {
		objetoSelecionado = cidadeController.merge(objetoSelecionado);	
		
		return "";
	}
	
	
	
	@Override
	public void saveNotReturn() throws Exception { 
		list.clear();
		objetoSelecionado = cidadeController.merge(objetoSelecionado);	
		list.add(objetoSelecionado);
		objetoSelecionado = new Cidade();
		sucesso();
	}
	
	@Override
	public void saveEdit() throws Exception {
		saveNotReturn();
	}
	
	
	@Override
	public String novo() throws Exception {
		setarVariaveisNulas();
		return url;
	}
	
	@Override
	public void excluir() throws Exception {
		objetoSelecionado = (Cidade) cidadeController.getSession
				().get(getClassImplement(), objetoSelecionado.getCid_codigo());
			cidadeController.delete(objetoSelecionado);
			list.remove(objetoSelecionado);
			objetoSelecionado = new Cidade();
			sucesso();	
		}
	
	
	@Override
	public void setarVariaveisNulas() throws Exception {
		list.clear();
		objetoSelecionado = new Cidade();
	}
	
	
	/**
	 * Ivocado pelo bot�o consultar
	 */
	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return urlFind;
	}
	
	
	/**@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_cidade");
		super.setNomeRelatorioSaida("report_cidade");
		super.setListDataBeanColletionReport(cidadeController.finList(getClassImplement()));
		return super.getArquivoReport();
	}*/
	
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_cidade");
		super.setNomeRelatorioSaida("report_cidade");
		List<?> list = cidadeController.finListOrderByProperty(Cidade.class, "estado.est_nome");
		super.setListDataBeanColletionReport(list); 
		return super.getArquivoReport();
	}
	
	
	
	public void setObjetoSelecionado(Cidade objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	
	public void setCidadeController(CidadeController cidadeController) {
		this.cidadeController = cidadeController;
	}

	public Cidade getObjetoSelecionado() {
		return objetoSelecionado;
	}
	
	public CidadeController getCidadeController() {
		return cidadeController;
	}


	public void setList(List<Cidade> list) {
		this.list = list;
	}

	@Override
	protected Class<Cidade> getClassImplement() {
		return Cidade.class;
	}

	@Override
	protected InterfaceCrud<Cidade> getController() {
		return cidadeController;
	}
	
	
	
	
	
}
