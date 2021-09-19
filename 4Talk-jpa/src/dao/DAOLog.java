package dao;

import modelo.Log;
import modelo.Mensagem;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class DAOLog extends DAO<Log> {

	@Override
	public Log read(Object chave) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Log> readAll(){
		TypedQuery<Log> q = manager.createQuery
				("select l from log_20182370045 l", Log.class);
		return q.getResultList();
	}


}
