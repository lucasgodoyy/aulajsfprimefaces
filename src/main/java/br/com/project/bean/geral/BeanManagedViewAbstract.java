package br.com.project.bean.geral;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.annotation.IdentificaCampoPesquisa;
import br.com.project.report.util.BeanReportView;
import br.com.project.util.all.UtilitariaRegex;
import br.project.enums.CondicaoPesquisa;

/**
 * Responsavel pela rotina de consulta e abstracao de metodos de CRUD e outros
 * padroes
 * 
 */
@Component
public abstract class BeanManagedViewAbstract extends BeanReportView {

	private static final long serialVersionUID = 1L;
	public String novo;

	protected abstract Class<?> getClassImplement();
	
	protected abstract InterfaceCrud<?> getController();
	
	public abstract String condicaoAndParaPesquisa() throws Exception;
	
	public ObjetoCampoConsulta objetoCampoConsultaSelecionado;

	public List<SelectItem> listaCampoPesquisa;
	
	public List<SelectItem> listaCondicaoPesquisa;
	
	public CondicaoPesquisa condicaoPesquisaSelecionado;
	
	public String valorPesquisa;
	
	
	

	public CondicaoPesquisa getCondicaoPesquisaSelecionado() {
		return condicaoPesquisaSelecionado;
	}

	

	public void setObjetoCampoConsultaSelecionado(
			ObjetoCampoConsulta objetoCampoConsultaSelecionado) {
		if (objetoCampoConsultaSelecionado != null) {
			for (Field field : getClassImplement().getDeclaredFields()) {
				if (field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {
					if (objetoCampoConsultaSelecionado.getCampoBanco()
							.equalsIgnoreCase(field.getName())) {
						String descricaoCampo = field.getAnnotation(
								IdentificaCampoPesquisa.class).descricaoCampo();
						objetoCampoConsultaSelecionado.setDescricao(descricaoCampo);
						objetoCampoConsultaSelecionado.setTipoClass(field.getType().getCanonicalName());
						objetoCampoConsultaSelecionado.setPrincipal(field.getAnnotation(IdentificaCampoPesquisa.class).principal());
						break;
						}
					}
				}
			}
	}

	

	/**
	 * 
	 * @return List<SelectItem> para combo de campo da tela de pesquisa
	 */
	public List<SelectItem> getListaCampoPesquisa() {
		listaCampoPesquisa = new ArrayList<SelectItem>();
		List<ObjetoCampoConsulta> listTemp = new ArrayList<ObjetoCampoConsulta>();
		for (Field field : getClassImplement().getDeclaredFields()) {
			if (field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {
				String descricaoCampo = field.getAnnotation(
						IdentificaCampoPesquisa.class).descricaoCampo();
				
				String descricaoCampoConsulta = field.getAnnotation(
						IdentificaCampoPesquisa.class).campoConsulta();
				
				int isPrincipal = field.getAnnotation(
						IdentificaCampoPesquisa.class).principal();

				ObjetoCampoConsulta objetoCampoConsulta = new ObjetoCampoConsulta();
				objetoCampoConsulta.setDescricao(descricaoCampo);
				objetoCampoConsulta.setCampoBanco(descricaoCampoConsulta);
				objetoCampoConsulta.setTipoClass(field.getType()
						.getCanonicalName());
				objetoCampoConsulta.setPrincipal(isPrincipal);

				listTemp.add(objetoCampoConsulta);
			}
		}
		ordernarReverse(listTemp);
		
		for (ObjetoCampoConsulta objetoCampoConsulta : listTemp) {
			listaCampoPesquisa.add(new SelectItem(objetoCampoConsulta));
		}
		
		return listaCampoPesquisa;
	
	
}

	/**
	 * 
	 * @return List<SelectItem> para o combo de condi��o de pesquisa
	 */
	public List<SelectItem> getListaCondicaoPesquisa() {
		listaCondicaoPesquisa = new ArrayList<SelectItem>();
		for (CondicaoPesquisa condicaoPesquisa : CondicaoPesquisa.values()) {
			listaCondicaoPesquisa
					.add(new SelectItem(condicaoPesquisa, condicaoPesquisa.toString()));
		}
		return listaCondicaoPesquisa; 
	}
	
	
	private void ordernarReverse(List<ObjetoCampoConsulta> listTemp) {
		Collections.sort(listTemp, new Comparator<ObjetoCampoConsulta>() {
			
			@Override
			public int compare(ObjetoCampoConsulta objeto1,
					ObjetoCampoConsulta objeto2) {
				return objeto1.isPrincipal().compareTo(objeto2.isPrincipal());
			}
		});

		// Collections.reverse(listTemp);
	}
	
	public void setCondicaoPesquisaSelecionado(CondicaoPesquisa condicaoPesquisaSelecionado) {
		this.condicaoPesquisaSelecionado = condicaoPesquisaSelecionado;
	}

	public ObjetoCampoConsulta getObjetoCampoConsultaSelecionado() {
		return objetoCampoConsultaSelecionado;
	}
	
	public String getValorPesquisa() {
		return valorPesquisa != null ? new UtilitariaRegex().retiraAcentos(valorPesquisa.trim() ) : "" ;
	}

	public void setValorPesquisa(String valorPesquisa) {
		this.valorPesquisa = valorPesquisa;
	}

	public String getSqlLazyQuery() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select entity from ");
		sql.append(getQueryConsulta());
		sql.append(" order by entity.");
		sql.append(objetoCampoConsultaSelecionado.getCampoBanco());
		return sql.toString();
	}

	public String getQueryConsulta() throws Exception {
		valorPesquisa = new  UtilitariaRegex().retiraAcentos(valorPesquisa);
		StringBuilder sql = new StringBuilder();
		sql.append(getClassImplement().getSimpleName());
		sql.append(" entity where ");

		sql.append(" retira_acentos(upper(cast(entity.");
		sql.append(objetoCampoConsultaSelecionado.getCampoBanco());
		sql.append(" as text))) ");

		if (condicaoPesquisaSelecionado.name().equals(
				CondicaoPesquisa.IGUAL_A.name())) {
			sql.append(" = retira_acentos(upper('");
			sql.append(valorPesquisa);
			sql.append("'))");
		} else if (condicaoPesquisaSelecionado.name().equals(
				CondicaoPesquisa.CONTEM.name())) {
			sql.append(" like retira_acentos(upper('%");
			sql.append(valorPesquisa);
			sql.append("%'))");
		} else if (condicaoPesquisaSelecionado.name().equals(
				CondicaoPesquisa.INICIA_COM.name())) {
			sql.append(" like retira_acentos(upper('");
			sql.append(valorPesquisa);
			sql.append("%'))");
		} else if (condicaoPesquisaSelecionado.name().equals(
				CondicaoPesquisa.TERMINA_COM.name())) {
			sql.append(" like retira_acentos(upper('%");
			sql.append(valorPesquisa);
			sql.append("'))");
		}
		sql.append(" ");
		sql.append(condicaoAndParaPesquisa());
		return sql.toString();
	}

	public int totalRegistroConsulta() throws Exception {
		Query query = getController().obterQuery(
				"select count(entity) from " + getQueryConsulta());
		Number result = (Number) query.uniqueResult();
		return result.intValue();
	}
	
}
