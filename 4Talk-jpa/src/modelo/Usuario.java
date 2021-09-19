
package modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;



@Entity(name = "usuario_20182370045")
@Cacheable(false) 
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column(unique = true)
	private String nomesenha;			//  nome + / + senha
	@OneToMany (mappedBy = "criador",
			    orphanRemoval = true,
			    fetch = FetchType.LAZY,
			    cascade = {CascadeType.PERSIST,
					       CascadeType.MERGE})
	private List<Mensagem> mensagens = new ArrayList<>();   //criadas por ele
	private boolean ativo = true;
	
	public Usuario() {}
	
	public Usuario(String nomesenha) {
		this.nomesenha = nomesenha;
	}

	public String getNome() {
		return nomesenha.split("/")[0];
	}
	public void setNome(String nome) {
		this.nomesenha = nome;
	}

	public boolean ativo() {
		return ativo;
	}
	
	public void desativar() {
		ativo=false;
	}
	
	public Boolean getStatus() {
		return this.ativo;
	}
	
	public void setStatus(Boolean status) {
		this.ativo = status;
	}


	public List<Mensagem> getMensagens() {
		return mensagens;
	}
	
	public void adicionar(Mensagem me){
		mensagens.add(me);
	}
	public void remover(Mensagem me){
		mensagens.remove(me);
	}
		

	@Override
	public String toString() {
		String texto = "Nome=" + nomesenha ;

		texto += "\n  lista de Mensagens: ";
		if (mensagens.isEmpty())
			texto += "sem mensagens";
		else 	
			for(Mensagem m: mensagens) 
				texto += "\n  --> " + m ;

		return texto ;

	}

}