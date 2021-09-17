
package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Mensagem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String datahora;
    private Usuario criador;
    private String texto;	//formato "yyyyMMdd"
           
    public Mensagem() {
    	
    }
    
    public Mensagem(Usuario criador, String texto) {
		this.criador = criador;
		this.texto = texto;
		this.datahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
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
        return datahora;
    }
    public void setData(String data) {
        this.datahora = data;
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
                ", datahora=" + datahora +"\ntexto=" + texto ;
    }   
}
