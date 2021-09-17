package dao;


import modelo.Usuario;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;

import java.util.List;


public class DAOUsuario extends DAO<Usuario>{

	@Override
	public Usuario read(Object chave) {
		try {
			String nomesenha = (String) chave;
			TypedQuery<Usuario> q = manager.createQuery
					("select u from Usuario u where p.nomesenha=:o", Usuario.class);
			q.setParameter("o", nomesenha);
			return q.getSingleResult();
		}catch(NoResultException e) {
			return null;
		}
		
	}
	
	
	public List<Usuario> readAll() {
		TypedQuery<Usuario> q = manager.createQuery
				("select u from Usuario u order by u.id", Usuario.class);
		return q.getResultList();
	}
	
	

}
