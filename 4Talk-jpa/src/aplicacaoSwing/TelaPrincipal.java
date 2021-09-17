package aplicacaoSwing;
/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Pesist~encia de Objetos
 * Prof. Fausto Maranhão Ayres
 **********************************/

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fachada.Fachada;
import modelo.Administrador;

public class TelaPrincipal {
	private JFrame frame;
	private JMenu mnUsuario;
	private JMenuItem mntmListarUsuario;
	private JMenuItem mntmCriarUsuario;
	private JMenuItem mntmSairGrupo;
	private JMenuItem mntmEntrarGrupo;
	private JMenuItem mntmApagarUsuario;
	private JMenu mnMensagem;
	private JMenu mnLogar;
	private JMenuItem mntmLogin;
	private JMenuItem mntmLogoff;
	private JLabel label;
	private ImageIcon imagem;		//label de fundo
	private JMenu mnLog;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal window = new TelaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TelaPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Fachada.inicializar();
				
				try {
					Administrador ad = Fachada.criarAdministrador("admin", "123", "seuemail@gmail.com");
				}
				catch(Exception e) {}
				
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				label.setIcon(imagem);
			}
			@Override
			public void windowClosing(WindowEvent e) {
				Fachada.finalizar();
				//		JOptionPane.showMessageDialog(frame, "banco fechado !");
			}
		});
		frame.setTitle("4TALK - Sem Usuario logado");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		label = new JLabel("");
		label.setFont(new Font("Tahoma", Font.PLAIN, 26));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText("Inicializando...");
		label.setBounds(0, 0, 450, 249);
		imagem = new ImageIcon(getClass().getResource("/imagens/imagem.png"));
		imagem = new ImageIcon(imagem.getImage().getScaledInstance(label.getWidth(),label.getHeight(), Image.SCALE_DEFAULT));//		label.setIcon(fotos);
		frame.getContentPane().add(label);
		frame.setResizable(false);


		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		mnUsuario = new JMenu("Usu\u00E1rio");
		menuBar.add(mnUsuario);

		mntmCriarUsuario = new JMenuItem("Criar");
		mntmCriarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaUsuario tela = new TelaUsuario();
			}
		});
		mnUsuario.add(mntmCriarUsuario);

		mntmListarUsuario = new JMenuItem("Listar");
		mntmListarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaUsuario tela = new TelaUsuario();
			}
		});
		mnUsuario.add(mntmListarUsuario);

		mntmSairGrupo = new JMenuItem("Sair do grupo");
		mntmSairGrupo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Fachada.sairDoGrupo();
					frame.setTitle("4TALK - Sem Usuario logado");
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage());
				}
			}
		});
		mnUsuario.add(mntmSairGrupo);

		mntmEntrarGrupo = new JMenuItem("Entrar no grupo");
		mntmEntrarGrupo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = pegarNome();
					String senha = pegarSenha();
					Fachada.solicitarAtivacao(nome, senha);
					JOptionPane.showMessageDialog(frame, "solicitação enviada ao administrador");
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage());
				}
			}
		});
		mnUsuario.add(mntmEntrarGrupo);

		mntmApagarUsuario = new JMenuItem("Apagar");
		mntmApagarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = pegarNome();
					String senha = pegarSenha();
					Fachada.solicitarExclusao(nome, senha);
					JOptionPane.showMessageDialog(frame, "solicitação enviada ao administrador");
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage());
				}
			}
		});
		mnUsuario.add(mntmApagarUsuario);


		//-----------------------------------------------------------------

		mnLogar = new JMenu("Logar");
		menuBar.add(mnLogar);

		mntmLogin = new JMenuItem("Login");
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TelaLogin telalogin = new TelaLogin();
				if(Fachada.getLogado()!=null)
					frame.setTitle("4TALK - Usuario logado=" + Fachada.getLogado().getNome());
			}
		});
		mnLogar.add(mntmLogin);

		mntmLogoff = new JMenuItem("Logoff");
		mntmLogoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Fachada.logoff();
				frame.setTitle("4TALK - Sem Usuario logado");
			}
		});
		mnLogar.add(mntmLogoff);



		//-----------------------------------------------------------------
		mnMensagem = new JMenu("Mensagem");
		mnMensagem.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TelaMensagem tela = new TelaMensagem();
			}
		});
		menuBar.add(mnMensagem);

		mnLog = new JMenu("Logs");
		mnLog.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TelaLogs tela = new TelaLogs();
			}
		});
		menuBar.add(mnLog);
	}
	
	public static String pegarNome(){
		JTextField textField = new JTextField(10);
		JPanel painel = new JPanel();
		painel.add(new JLabel("Entre com o seu nome:"));
		painel.add(textField);
		JOptionPane.showMessageDialog(null, painel, "Nome", JOptionPane.PLAIN_MESSAGE);
		String texto = textField.getText();
		return texto.trim() ;
	}
	public static String pegarSenha(){
		JPasswordField field = new JPasswordField(10);
		field.setEchoChar('*'); 
		JPanel painel = new JPanel();
		painel.add(new JLabel("Entre com a sua senha:"));
		painel.add(field);
		JOptionPane.showMessageDialog(null, painel, "Senha", JOptionPane.PLAIN_MESSAGE);
		String texto = new String(field.getPassword());
		return texto.trim();
	}
}
