package lpoo.model.dao;

import lpoo.model.Pessoa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

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
    public Object persist(Object o) throws Exception {
        return null;
    }

    @Override
    public Object remover(Object o) throws Exception {
        return null;
    }

    @Override
    public List findAll(Class c) throws Exception {
        return null;
    }

    @Override
    public Pessoa doLogin(String email, String senha) throws Exception {
        return null;
    }

}
