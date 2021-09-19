package aplicacaoConsole;

import javax.swing.JOptionPane;

import fachada.Fachada;

public class Listar2User {
	
	
	public Listar2User() {
		Fachada.inicializar();
		try {
			String nome = JOptionPane.showInputDialog("digite o nome");
			String senha = JOptionPane.showInputDialog(null,"digite a senha","123");
			Fachada.login(nome, senha);
			System.out.println("Todas Mensagem do usuario \n" + Fachada.getLogado());
			System.out.println("Total de Mensgens do usuario: " + Fachada.totalMensagensUsuario());
			System.out.println("Usuario logado:"+ Fachada.getLogado().getNome());
			Fachada.logoff();
		}catch (Exception e) 	{
			System.out.println(e.getMessage());
		}
		
		Fachada.finalizar();
		System.out.println("fim do programa");
	}		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Listar2User();
	}

}
