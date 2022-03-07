package model.dao;

import java.util.List;

public interface InterfacePersistencia {

    Boolean conexaoAberta();

    void fecharConexao();

    public Object find(Class c, Object id)throws Exception;

    public Object persist(Object o) throws Exception;

    public  Object remover (Object o)throws Exception;

    public List findAll(Class c) throws Exception;

}
