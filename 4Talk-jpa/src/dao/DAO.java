/** 
 * IFPB - TSI - PERSISTENCIA DE OBJETOS
 * @author Prof Fausto Ayres
 * 
 * 
 * ver configuracoes do DAO no arquivo dados.properties
 */


package dao;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public abstract class DAO<T> implements DAOInterface<T> {
	protected static EntityManager manager;
	protected static EntityManagerFactory factory;

	//usado para adicionar mensagens no log.txt
	protected static final Log log = LogFactory.getLog(DAO.class);


	public DAO(){}

	public static void open(){
		if(manager==null){
			/*****************************************************************************
			 * 	Determinar o nome da unidade de persistencia a ser processada no persistence.xml
			 *  Este nome � a concatenacao dos nomes provedor+sgbd, lidos do arquivo dados.properties
			 *****************************************************************************/
			String nomeUnidadePersistencia="";
			String provedor="";
			String sgbd="";
			String banco="";
			String ip="";
			Properties dados = new Properties();		
			try {
				//				dados.load(new FileInputStream("src/dados.properties"));
				dados.load(DAO.class.getResourceAsStream("/dados.properties"));
				provedor = dados.getProperty("provedor");
				sgbd = dados.getProperty("sgbd");
				nomeUnidadePersistencia = provedor +"-"+ sgbd;
				log.info("processando a unidade de persistencia: "+ nomeUnidadePersistencia);
			}
			catch (Exception e) {
				log.info("DAO open() - problema na conex�o: "+ e.getMessage());
				System.exit(0);
			} 

			/*****************************************************************************
			 * 	Substituir o ip do persistence.xml pelo ip do dados.properties
			 *****************************************************************************/
			ip = dados.getProperty("ip");
			banco = dados.getProperty("banco");
			Properties prop = new Properties();
			if(sgbd.equals("postgres"))
				prop.setProperty("javax.persistence.jdbc.url", "jdbc:postgresql://"+ip+":5432/"+banco);
			if(sgbd.equals("mysql"))
				prop.setProperty("javax.persistence.jdbc.url", "jdbc:mysql://"+ip+":3306/"+banco+"?createDatabaseIfNotExist=true");

			log.info("url de conexao= "+prop.getProperty("javax.persistence.jdbc.url"));
			
			/*****************************************************************************
			 * 	desativar o cache de objetos para todas as classes
			 *  cada classe habilitada individualmente com @Cacheable
			 *****************************************************************************/
			prop.setProperty("javax.persistence.sharedCache.mode", "DISABLE_SELECTIVE");

			factory = Persistence.createEntityManagerFactory(nomeUnidadePersistencia, prop);
			manager = factory.createEntityManager();	
		}
	}
	public static void close(){
		if(manager != null && manager.isOpen()) {
			manager.close();
			factory.close();
			manager=null;
		}
	}
	public void create(T obj){
		manager.persist(obj);
	}

	public abstract T read(Object chave);

	public T update(T obj){
		return manager.merge(obj);
	}
	public void delete(T obj) {
		manager.remove(obj);
	}


	@SuppressWarnings("unchecked")
	public List<T> readAll(){
		Class<T> type = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		TypedQuery<T> query = manager.createQuery("select x from " + type.getSimpleName() + " x",type);
		return  query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> readAllPagination(int firstResult, int maxResults) {
		Class<T> type = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		return manager.createQuery("select x from " + type.getSimpleName() + " x",type)
				.setFirstResult(firstResult - 1)
				.setMaxResults(maxResults)
				.getResultList();
	}

	public void deleteAll(){
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		String tabela = type.getSimpleName();
		Query query = manager.createQuery("delete from " + tabela);
		query.executeUpdate();

		//resetar a sequencia de ids depende do SGBD
		String nomesgbd ="";
		try {
			Connection con = getConnection();
			if(con==null)
				throw new RuntimeException("DAO - falha ao obter conexao");

			nomesgbd = con.getMetaData().getDatabaseProductName() ;

			if(nomesgbd.equalsIgnoreCase("postgresql"))
				query = manager.createNativeQuery("ALTER SEQUENCE "+tabela+"_id_seq RESTART WITH 1"); 
			else
				if(nomesgbd.equalsIgnoreCase("mysql"))
					query = manager.createNativeQuery("ALTER TABLE "+tabela+" AUTO_INCREMENT = 1");
				else
					throw new Exception("DAO - Nome de SGBD invalido:"+ nomesgbd);

			query.executeUpdate();
		}	
		catch (Exception ex) {
			log.info("problema no deleteAll: "+ex.getMessage());
		}
	}

	public static Connection getConnection() {
		try {
			String driver = (String) manager.getProperties().get("javax.persistence.jdbc.driver");
			String url = (String)	manager.getProperties().get("javax.persistence.jdbc.url");
			String user = (String)	manager.getProperties().get("javax.persistence.jdbc.user");
			String pass = (String)	manager.getProperties().get("javax.persistence.jdbc.password");
			Class.forName(driver);
			return DriverManager.getConnection(url, user, pass);
		} 
		catch (Exception ex) {
			return null;
		}
	}

	//----------------------- TRANSA��O   ----------------------
	public static void begin(){
		if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
	}
	public static void commit(){
		if(manager.getTransaction().isActive()){
			manager.getTransaction().commit();
			manager.clear();		// ---- esvazia o cache de objetos ----
		}
	}
	public static void rollback(){
		if(manager.getTransaction().isActive())
			manager.getTransaction().rollback();
	}

	public void lock(T obj) {
		//usado somente no controle de concorrencia persimista
		manager.lock(obj,LockModeType.PESSIMISTIC_WRITE); 
	}


	public static void clear() {
		Query q;
		int cont=0;

		begin();
		log.debug("esvaziando o banco: ");


		q = manager.createQuery("delete from Log_20182370045");
		cont =  q.executeUpdate();
		log.debug("Os registros de entrada e saida foram apagados: "+ cont);
		
		q = manager.createQuery("delete from Mensagem_20182370045");
		cont =  q.executeUpdate();
		log.debug("deletou as Mensagens: "+ cont);

		q = manager.createQuery("delete from Usuario_20182370045");
		cont =  q.executeUpdate();
		log.debug("deletou os Usuarios: "+ cont);
		log.debug("");
		
		q = manager.createQuery("delete from Administrador_20182370045");
		cont =  q.executeUpdate();
		log.debug("deletou o ADM: "+ cont);

		commit();
	}
}

