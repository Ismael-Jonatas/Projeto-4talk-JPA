package dao;




import modelo.Administrador;
import modelo.Usuario;

//import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;

import java.util.List;


public class DAOUsuario extends DAO<Usuario>{

	@Override
	public Usuario read(Object chave) {
		try{
			String nomesenha = (String) chave;
			TypedQuery<Usuario> q = manager.createQuery
					("select u from usuario_20182370045 u where u.nomesenha=:n", Usuario.class);
			q.setParameter("n", nomesenha);
			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	
	public List<Usuario> readAll() {
		TypedQuery<Usuario> q = manager.createQuery
				("select u from usuario_20182370045 u order by u.id", Usuario.class);
		return q.getResultList();
	}
	
	
	public Usuario read2(String chave) {
		try{
			String nome = chave;
			TypedQuery<Usuario> q = manager.createQuery
					("select u from usuario_20182370045 u where u.nomesenha like :n", Usuario.class);
			q.setParameter("n", nome + "/%");
			return q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	

}

