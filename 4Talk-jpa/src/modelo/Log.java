package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;




@Entity (name="log_20182370045")
@Cacheable(false) 

public class Log {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String datahora;
	private String nome;
	
	public Log() {}
	
	public Log(String nome) {
		this.nome = nome;
		this.datahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyMMdd HH:mm:ss"));
	}

	

	public String getDatahora() {
		return datahora;
	}



	public String getNome() {
		return nome;
	}



	@Override
	public String toString() {
		return "Log [datahora=" + datahora + ", nome=" + nome + "]";
	}
	
	
	
	
}
