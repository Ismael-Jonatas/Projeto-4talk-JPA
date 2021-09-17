package aplicacaoConsole;
/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 *
 */

import javax.swing.JOptionPane;

import fachada.Fachada;

public class CadastrarMensagem {

	public CadastrarMensagem(){
		Fachada.inicializar();
		try {
			String nome = JOptionPane.showInputDialog("digite o nome");
			String senha = JOptionPane.showInputDialog(null,"digite a senha","123");
			String texto = JOptionPane.showInputDialog("digite o texto");
			Fachada.login(nome, senha);
			System.out.println("Usuario logado:"+ Fachada.getLogado().getNome());
			Fachada.criarMensagem(texto);
			Fachada.logoff();
		} catch (Exception e) 	{
			System.out.println(e.getMessage());
		}

		Fachada.finalizar();
		System.out.println("fim do programa");
	}



	//=================================================
	public static void main(String[] args) {
		new CadastrarMensagem();
	}
}


