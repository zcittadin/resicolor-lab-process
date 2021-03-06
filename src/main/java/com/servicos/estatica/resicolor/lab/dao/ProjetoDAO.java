package com.servicos.estatica.resicolor.lab.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.servicos.estatica.resicolor.lab.model.Projeto;
import com.servicos.estatica.resicolor.lab.model.Prova;
import com.servicos.estatica.resicolor.lab.util.HibernateUtil;

public class ProjetoDAO {

	private static ProvaDAO provaDAO = new ProvaDAO();

	public void saveProjeto(Projeto projeto) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(projeto);
		session.getTransaction().commit();
		session.clear();
		session.close();
	}

	public void updateProjeto(Projeto projeto) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.update(projeto);
		session.getTransaction().commit();
		session.clear();
		session.close();
	}

	public void updateDataFinal(Projeto projeto) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Projeto set dtFinal = :dtFim WHERE id = :id");
		query.setParameter("dtFim", Calendar.getInstance().getTime());
		query.setParameter("id", projeto.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void reopenProjeto(Projeto projeto) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Projeto set dtFinal = :dtFim WHERE id = :id");
		query.setParameter("dtFim", null);
		query.setParameter("id", projeto.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void removeProjeto(Projeto projeto) {
		removeChildren(projeto);
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.remove(projeto);
		session.getTransaction().commit();
		session.close();
	}

	private void removeChildren(Projeto projeto) {
		List<Prova> provas = provaDAO.findByProjeto(projeto);
		if (provas != null && !provas.isEmpty()) {
			for (Prova prova : provas) {
				provaDAO.removeProva(prova);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Projeto> findLastProjetos() {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT p FROM Projeto p ORDER BY id DESC");
		query.setMaxResults(15);
		List<Projeto> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Projeto> findProjetoByNome(String nome) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT p FROM Projeto p WHERE nome LIKE '%" + nome + "%' ORDER BY id DESC");
		List<Projeto> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}
}
