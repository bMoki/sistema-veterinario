package model.dao;

public interface InterfacePersistencia {

    Boolean conexaoAberta();

    void fecharConexao();

    public Object find(Class c, Object id)throws Exception;

    public void persist(Object o) throws Exception;

    public  void remover (Object o)throws Exception;

    //public List<Receita> listReceitas();
}
