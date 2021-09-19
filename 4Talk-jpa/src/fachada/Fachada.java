package fachada;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import modelo.Administrador;
import modelo.Log;
import modelo.Mensagem;
import modelo.Usuario;

import dao.DAO;
import dao.DAOLog;
import dao.DAOMensagem;
import dao.DAOUsuario;


public class Fachada {
	private static DAOUsuario daousuario = new DAOUsuario();  
	private static DAOMensagem daomensagem = new DAOMensagem();  
	private static DAOLog daolog = new DAOLog();  

	private static Usuario usuariologado=null;

	public static void inicializar() {
		DAO.open();
	}

	public static void finalizar(){
		DAO.close();
	}

	public static List<Usuario> listarUsuarios() {
		// nao precisa estar logado
		return daousuario.readAll();	
	}
	public static List<Mensagem> listarMensagens() {
		// nao precisa estar logado
		return daomensagem.readAll();	
	}

	public static List<Log> listarLogs() {
		// nao precisa estar logado
		return daolog.readAll();	
	}
	public static List<Mensagem> buscarMensagens(String termo) throws  Exception{

		return daomensagem.readByTermo(termo);
	}

	public static Usuario criarUsuario(String nome, String senha) throws  Exception{
		// nao precisa estar logado
		DAO.begin();	
		Usuario u = daousuario.read(nome+"/"+senha);	
		if(u != null) {
			DAO.rollback();	
			throw new Exception("criar usuario - usuario existente:" + nome);
		}

		u = new Usuario(nome+"/"+senha);
		daousuario.create(u);		
		DAO.commit();
		return u;
	}


	public static void login(String nome, String senha) throws Exception{		
		//verificar se ja existe um usuario logada
		if(usuariologado!=null)
			throw new Exception ("ja existe um usuario logado "+getLogado().getNome());

		DAO.begin();	
		Usuario u = daousuario.read(nome+"/"+senha);	
		if(u == null) {
			DAO.rollback();	
			throw new Exception("login - usuario inexistente:" + nome);
		}
		if(!u.ativo()) {
			DAO.rollback();	
			throw new Exception("login - usuario nao ativo:" + nome);
		}
		usuariologado = u;		//altera o logado na fachada
		Log log = new Log(usuariologado.getNome() + " - login");
		daolog.create(log);
		DAO.commit();
	}
	public static void logoff() {		
		DAO.begin();
		Log log = new Log(usuariologado.getNome() + " - logoff");
		daolog.create(log);
		DAO.commit();

		usuariologado = null; 		//altera o logado na fachada
	}

	public static Usuario getLogado() {
		return usuariologado;
	}

	public static Mensagem criarMensagem(String texto) throws Exception{

		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
		}else {
			DAO.begin();
			Mensagem me = new Mensagem(usuariologado, texto);
			daomensagem.create(me);
			usuariologado.adicionar(me);
			daousuario.update(usuariologado);
			DAO.commit();
			return me;
		}
		
	}



	public static List<Mensagem> listarMensagensUsuario() throws Exception{
		/*
		 * tem que esta logado
		 * retorna todas as mensagens do usuario logado
		 * 
		 */

		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
		}else {
			return usuariologado.getMensagens();
		}
		
		
		
	}

	public static void apagarMensagens(int... ids) throws  Exception{
		/*
		 * tem que esta logado
		 * recebe uma lista de numeros de id 
		 * (id é um numero entre 1 a N, onde N é a quatidade atual de mensagens do grupo)
		 * validar se ids são de mensagens criadas pelo usuario logado
		 * (um usuario nao pode apagar mensagens de outros usuarios)
		 * 
		 * remover cada mensagem da lista de mensagens do usuario logado
		 * apagar cada mensagem do banco 
		 */
		
		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
			
		}else {
			DAO.begin();
			Boolean verificar = false;
			List<Mensagem> mensUsuario = usuariologado.getMensagens();
			for (int i:ids) {
				verificar = false;
				for(Mensagem mens: mensUsuario) {
					
						if(mens.getId() == i) {
							verificar = true;
							usuariologado.remover(mens);
							daousuario.update(usuariologado);
							DAO.commit();
							System.out.println("Mensagem Excluida");
						}
						
				}

			}
			if(!verificar) {
				System.out.println("Mensagem não encontrada!");
			}
		}
	}
		
		

	public static void sairDoGrupo() throws  Exception{
		/*
		 * tem que esta logado
		 * 
		 * criar a mensagem "fulano saiu do grupo"
		 * desativar o usuario logado e fazer logoff dele
		 */
		
		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
			
		}else {
			DAO.begin();
			String texto = Fachada.getLogado().getNome() + " Saiu do Grupo";
			Fachada.criarMensagem(texto);
			usuariologado.setStatus(false);
			daousuario.update(usuariologado);
			DAO.commit();
			logoff();
		}	
		
		
	}

	
	public static int totalMensagensUsuario() throws Exception{

		
		if (usuariologado == null) {
			throw new Exception("É necessário estar logado!");
			
		}else {		
			List<Mensagem> mensUsuario = usuariologado.getMensagens();
			return mensUsuario.size();
			}
		
	}

	public static void esvaziar() throws Exception{
		DAO.clear();
	}

	/**************************************************************
	 * 
	 * NOVOS MÉTODOS DA FACHADA PARA O PROJETO 2
	 * 
	 **************************************************************/

	public static Administrador criarAdministrador(String nome, String senha, String email) throws  Exception{
		// nao precisa estar logado
		DAO.begin();	
		Usuario u = daousuario.read(nome+"/"+senha);	
		if(u != null) {
			DAO.rollback();	
			throw new Exception("criar administrador - usuario ja existe:" + nome);
		}

		Administrador ad = new Administrador(nome+"/"+senha, email);
		daousuario.create(ad);		
		DAO.commit();
		return ad;
	}

	public static void solicitarAtivacao(String nome, String senha, String loginEmail, String senhaEmail) throws  Exception{
		/*
		 * o usuario (nome+senha) tem que estar desativado
		 *  
		 * enviar um email para o administrador com a mensagem "nome solicita ativação"
		 * usar o método Fachada.enviarEmail(...) 
		 * 
		 */
		
		DAO.begin();	
		Usuario u = daousuario.read(nome+"/"+senha);	
		
		if(u == null) {
			DAO.rollback();	
			throw new Exception("Usuário inexistente!");
		}

		
		if (u.getStatus() == true) {
			DAO.rollback();	
			throw new Exception("O Usuário precisa estar desativado!");
		}
	
		String assunto = "Permição para entrar no grupo";
		String mensagem = nome + " solicita ativiação";
		Fachada.enviarEmail(assunto, mensagem, loginEmail, senhaEmail);
		
		
		
	}

	public static void solicitarExclusao(String nome, String senha, String loginEmail, String senhaEmail) throws  Exception{
		/*
		 * o usuario (nome+senha) tem que estar desativado
		 *  
		 * enviar um email para o administrador com a mensagem "nome solicita exclusão"
		 * usar o método Fachada.enviarEmail(...) 
		 * 
		 */
		
		DAO.begin();	
		Usuario u = daousuario.read(nome+"/"+senha);	
		
		if(u == null) {
			DAO.rollback();	
			throw new Exception("Usuário inexistente!");
		}

		
		if (u.getStatus() == true) {
			DAO.rollback();	
			throw new Exception("O Usuário precisa estar desativado!");
		}


		String assunto = "Permição para Exclusão";
		String mensagem = nome + " solicita Exclusão";
		Fachada.enviarEmail(assunto, mensagem, loginEmail, senhaEmail);
		
		
	}

	public static void ativarUsuario(String nome) throws  Exception{
		/*
		 * o usuario logado tem que ser um administrador  e o usuario a 
		 * ser ativado (nome) tem que estar desativado 
		 *  
		 * ativar o usuario 
		 * criar a mensagem "nome entrou no grupo"
		 * 
		 */
		
		DAO.begin();	
		Usuario u = daousuario.read2(nome);
		
		if(!(usuariologado instanceof Administrador)) {
			DAO.rollback();	
			throw new Exception("Usuario Logado não possui privilegios!");
		}
		
		if(u == null) {
			DAO.rollback();	
			throw new Exception("Usuário inexistente!");
		}

		if (u.getStatus() == true) {
			DAO.rollback();	
			throw new Exception("O Usuário precisa estar desativado!");
		}else {
			u.setStatus(true);
			daousuario.update(u);
			DAO.commit();
		}
		

		
	}

	public static void apagarUsuario(String nome) throws  Exception{
		/*
		 * o usuario logado tem que ser um administrador  e o usuario a 
		 * ser apagado tem que estar desativado (e não pode ser do tipo Administrador)
		 *  
		 * apagar as mensagens do usuario e apagar o usuario 
		 * criar a mensagem "nome foi excluido do sistema"
		 */
		
		DAO.begin();	
		Usuario u = daousuario.read2(nome);
		
		if(!(usuariologado instanceof Administrador)) {
			DAO.rollback();	
			throw new Exception("Usuario Logado não possui privilegios!");
		}
		
		if(u == null) {
			DAO.rollback();	
			throw new Exception("Usuário inexistente!");
		}

		if (u.getStatus() == true) {
			DAO.rollback();	
			throw new Exception("O Usuário precisa estar desativado!");
		}else {
			daousuario.delete(u);
			DAO.commit();
		}
		
		
		
		
	}


	/**************************************************************
	 * 
	 * MÉTODO PARA ENVIAR EMAIL, USANDO UMA CONTA (SMTP) DO GMAIL
	 * ELE ABRE UMA JANELA PARA PEDIR A SENHA DO EMAIL DO EMITENTE
	 * ELE USA A BIBLIOTECA JAVAMAIL (ver pom.xml)
	 * Lembrar de: 
	 * 1. desligar antivirus e de 
	 * 2. ativar opcao "Acesso a App menos seguro" na conta do gmail
	 * 
	 **************************************************************/
	public static void enviarEmail(String assunto, String mensagem, String login, String senha) {
		try {
			/*
			 * ********************************************************
			 * Obs: lembrar de desligar antivirus e 
			 * de ativar "Acesso a App menos seguro" na conta do gmail
			 * 
			 * pom.xml contem a dependencia javax.mail
			 * 
			 * ********************************************************
			 */
			//configurar emails
			String emailorigem = login;
			String senhaorigem = senha;
			String emaildestino = "joonnatasfernanndes@gmail.com";
			

			//Gmail
			Properties props = new Properties();
			props.put("mail.smtp.user", emailorigem); 
	        props.put("mail.smtp.host", "smtp.gmail.com"); 
	        props.put("mail.smtp.port", "25"); 
	        props.put("mail.debug", "true"); 
	        props.put("mail.smtp.auth", "true"); 
	        props.put("mail.smtp.starttls.enable","true"); 
	        props.put("mail.smtp.EnableSSL.enable","true");
	        props.put("mail.smtp.localhost", "192.168.0.16");
	        
	        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
	        props.setProperty("mail.smtp.socketFactory.fallback", "false");   
	        props.setProperty("mail.smtp.port", "465");   
	        props.setProperty("mail.smtp.socketFactory.port", "465");

			//Autenticar o login no email
			Session session;
			session = Session.getInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailorigem, senhaorigem);
				}
			});

			//session.setDebug(true);
			
			//Prepara a mensagem para enviar
			MimeMessage message = new MimeMessage(session);		
			message.setFrom(new InternetAddress(emailorigem));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emaildestino));
			message.setSubject(assunto);
			message.setText(mensagem);   // usar "\n" para quebrar linhas
			Transport.send(message);

			//System.out.println("enviado com sucesso");

		} catch (MessagingException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * JANELA PARA DIGITAR A SENHA DO EMAIL
	 */
	/*public static String pegarSenha(){
		JPasswordField field = new JPasswordField(10);
		field.setEchoChar('*'); 
		JPanel painel = new JPanel();
		painel.add(new JLabel("Entre com a senha do email:"));
		painel.add(field);
		JOptionPane.showMessageDialog(null, painel, "Senha", JOptionPane.PLAIN_MESSAGE);
		String texto = new String(field.getPassword());
		return texto.trim();
	}*/
}

