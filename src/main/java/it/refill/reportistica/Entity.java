package it.refill.reportistica;

import it.refill.entities.Allievi;
import it.refill.entities.Docenti;
import it.refill.entities.Path;
import it.refill.entities.SoggettiAttuatori;
import it.refill.entities.Tracking;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.apache.commons.collections4.map.HashedMap;


public class Entity {
	EntityManagerFactory emf;
	EntityManager em;
	
	public Entity() {
		 this.emf = Persistence.createEntityManagerFactory("microcredito");
	        this.em = this.emf.createEntityManager();
	        this.em.clear();
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void begin() {
		this.em.getTransaction().begin();
	}

	public void persist(Object o) {
		this.em.persist(o);
	}

	public void merge(Object o) {
		this.em.merge(o);
	}

	public void commit() {
		this.em.getTransaction().commit();
	}

	public void rollBack() {
		this.em.getTransaction().rollback();
	}

	public void flush() {
		this.em.flush();
	}

	public void close() {
		this.em.close();
		this.emf.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findAll(Class c) {
		return this.em.createQuery("Select a FROM " + c.getSimpleName() + " a", c).getResultList();
	}

	public void insertTracking(String id_user, String action) {
		if (!this.em.getTransaction().isActive()) {
			this.begin();
		}

		if (id_user == null || id_user.equals("")) {
			id_user = "Microcredito";
		}
		this.persist(new Tracking(id_user, action));
		this.commit();
	}

	public String getPath(String id) {
		return this.em.find(Path.class, id).getUrl();
	}

	public List<Docenti> getDocentiIdMaxOf(int id) {
		TypedQuery<Docenti> q = this.em.createNamedQuery("d.byIdMaxOf", Docenti.class).setParameter("id", Long.parseLong(String.valueOf(id)));
		return q.getResultList().size() > 0 ? q.getResultList() : new ArrayList<>();
	}

	public List<Allievi> getAllieviIdMaxOf(int id) {
		TypedQuery<Allievi> q = this.em.createNamedQuery("a.byIdMaxOf", Allievi.class).setParameter("id", Long.parseLong(String.valueOf(id)));
		return q.getResultList().size() > 0 ? q.getResultList() : new ArrayList<>();
	}
	
	public List<Allievi> getAllieviConclusi() {
		TypedQuery<Allievi> q = this.em.createNamedQuery("a.ProgettiConclusi", Allievi.class);
		return q.getResultList().size() > 0 ? q.getResultList() : new ArrayList<>();
	}

	public HashedMap<String, SoggettiAttuatori> getSoggettiMap() {
		HashedMap<String, SoggettiAttuatori> out = new HashedMap<>();
		@SuppressWarnings("unchecked")
		List<SoggettiAttuatori> soggetti = this.findAll(SoggettiAttuatori.class);
		soggetti.forEach(s -> {
			if (s.getPiva() != null && !s.getPiva().equals("")) {
				out.put(s.getPiva(), s);
			}
			if (s.getCodicefiscale() != null && !s.getCodicefiscale().equals("")) {
				out.put(s.getCodicefiscale(), s);
			}
		});
		return out;
	}
}
