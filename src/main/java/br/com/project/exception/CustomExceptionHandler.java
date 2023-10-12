package br.com.project.exception;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;

import br.com.framework.hibernate.session.HibernateUtil;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	//classe para tratar erros
	
	private ExceptionHandler wrapped;

	// Obt�m uma inst�ncia do FacesContext
	final FacesContext facesContext = FacesContext.getCurrentInstance();

	// Obt�m um mapa do FacesContext
	final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

	// Obt�m o estado atual da navega��o entre p�ginas do JSF
	final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

	// Declara o construtor que recebe uma exception do tipo ExceptionHandler
	// como par�metro
	CustomExceptionHandler(ExceptionHandler exception) {
		this.wrapped = exception;
	}

	// Sobrescreve o m�todo ExceptionHandler que retorna a "pilha" de exce��es

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	// Sobrescreve o m�todo handle que � respons�vel por manipular as exce��es
	// do JSF
	@Override
	public void handle() throws FacesException {
		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

		while (iterator.hasNext()) {
			ExceptionQueuedEvent event = iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			// Recupera a exce��o do contexto
			Throwable exception = context.getException();

			// Aqui tentamos tratar a exe��o
			try {

				requestMap.put("exceptionMessage", exception.getMessage());

				if (exception != null && exception.getMessage() != null
						&& exception.getMessage().indexOf("ConstraintViolationException") != -1) {

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Registro n�o pode ser removido por estar associado.", ""));

				} else if (exception != null && exception.getMessage() != null
						&& exception.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1) {

					
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Registro foi atualizado ou exclu�do por outro usu�rio. Consulte novamente.", ""));

				} else {
					// Avisa o usu�rio do erro
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"O sistema se recuperou de um erro inesperado.", ""));

					// Tranquiliza o usu�rio para que ele continue usando o sistema
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Voc� pode continuar usando o sistema normalmente!", ""));

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"O erro foi causado por:\n" + exception.getMessage(), ""));

					// SETA A NAVEGA��O PARA UMA P�GINA PADR�O - REDIRECIONA PARA UMA PAGINA DE
					// ERRO.
					// esse alert apenas � exibio se a pagina n�o redirecionar
					RequestContext.getCurrentInstance()
							.execute("alert('O sistema se recuperou de um erro inesperado.')");

					RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Erro", "O sistema se recuperou de um erro inesperado.."));

					navigationHandler.handleNavigation(facesContext, null,
							"/error/error.jsf?faces-redirect=true&expired=true");
				}

				// Renderiza a pagina de erro e exibe as mensagens
				facesContext.renderResponse();
			} finally {

				SessionFactory sf = HibernateUtil.getSessionFactory();

				if (sf.getCurrentSession().getTransaction().isActive()) {
					sf.getCurrentSession().getTransaction().rollback();
				}
				// imprime exce��o no console
				exception.printStackTrace();

				// Remove a exe��o da fila
				iterator.remove();
			}
		}
		// Manipula o erro
		getWrapped().handle();

	}

}
