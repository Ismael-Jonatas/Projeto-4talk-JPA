
package modelo;


import java.text.SimpleDateFormat;
import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Cacheable;
import javax.persistence.Column;


@Table(indexes={@Index(name="indice1", columnList="criador, datahora")})

@Entity (name = "mensagem_20182370045")
@Cacheable(false) 

public class Mensagem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
	@Column(name =  "datahora", columnDefinition = "TIMESTAMP")
    private Date datahora; //formato "yyyyMMdd HH:mm:ss"
    @ManyToOne
    private Usuario criador;
    private String texto;	
           
    public Mensagem() {
    	
    }
    
    public Mensagem(Usuario criador, String texto) {
		this.criador = criador;
		this.texto = texto;
	}
                           
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

  
    public String getData() {
    	SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");    
        return fmt.format(this.datahora);
    }
    
    @PrePersist
    void datahora() {
    	this.datahora = new Date();
    }
    
    public Usuario getCriador() {
		return criador;
	}

	public void setCriador(Usuario criador) {
		this.criador = criador;
	}

	@Override
    public String toString() {
        return  "id=" + id + ", criador=" + criador.getNome() +
                ", datahora=" + this.getData() +"\ntexto=" + texto ;
    }   
}
