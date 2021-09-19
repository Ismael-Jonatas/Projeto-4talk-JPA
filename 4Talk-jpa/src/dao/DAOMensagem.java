package dao;

import modelo.Mensagem;


import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.List;

public class DAOMensagem extends DAO<Mensagem>{

	@Override
	public Mensagem read(Object chave) {
		try {
			Integer me = (Integer) chave;
			TypedQuery<Mensagem> q = manager.createQuery
					("select m from mensagem_20182370045 m where m.id=:i", Mensagem.class);
			q.setParameter("i", me);
			return q.getSingleResult();
		}catch(NoResultException e) {
			return null;
		}
	}
	
	
	public List<Mensagem> readAll(){
		TypedQuery<Mensagem> q = manager.createQuery
				("select m from mensagem_20182370045 m", Mensagem.class);
		return q.getResultList();
	}

	
	public List<Mensagem> readByTermo(String termo){
		TypedQuery<Mensagem> q = manager.createQuery
				("select m from mensagem_20182370045 m where m.texto like '%"+ termo +"%' order by m.id", Mensagem.class);
		return q.getResultList();
	}
	
}




