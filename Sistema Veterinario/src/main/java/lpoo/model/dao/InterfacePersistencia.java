package lpoo.model.dao;

import lpoo.model.Pessoa;

import java.util.List;

public interface InterfacePersistencia {

    Boolean conexaoAberta();

    void fecharConexao();

    public Object find(Class c, Object id)throws Exception;

    public Object persist(Object o) throws Exception;

    public  Object remover (Object o)throws Exception;

    public List findAll(Class c) throws Exception;

    public Pessoa doLogin(String email, String senha) throws Exception;

}
