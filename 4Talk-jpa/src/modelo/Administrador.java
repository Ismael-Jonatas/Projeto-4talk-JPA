
package modelo;


import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.UniqueConstraint;


@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nomesenha", "email"}))

@Entity(name="administrador_20182370045")
@Cacheable(false) 
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Administrador extends Usuario {
	
	private String email;			

	public Administrador(){}
	
	public Administrador(String nomesenha, String email) {
		super(nomesenha);
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		String texto = "Nome=" + getNome().split("/")[0] + ", email=" + email;

		texto += "\n  lista de Mensagens: ";
		if (getMensagens().isEmpty())
			texto += "sem mensagens";
		else 	
			for(Mensagem m: getMensagens()) 
				texto += "\n  --> " + m ;

		return texto ;
	}

}