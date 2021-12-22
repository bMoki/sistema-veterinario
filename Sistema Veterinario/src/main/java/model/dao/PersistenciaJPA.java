package model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenciaJPA implements InterfacePersistencia{
    public EntityManagerFactory factory;
    public EntityManager entityManager;

    public PersistenciaJPA(){
        factory = Persistence.createEntityManagerFactory("Clinica-Veterinaria");
        entityManager = factory.createEntityManager();
    }

    @Override
    public Boolean conexaoAberta() {
        return entityManager.isOpen();
    }

    @Override
    public void fecharConexao() {
        entityManager.close();
    }

    @Override
    public Object find(Class c, Object id) throws Exception {
        return null;
    }

    @Override
    public void persist(Object o) throws Exception {

    }

    @Override
    public void remover(Object o) throws Exception {

    }
}
