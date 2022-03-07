package model.dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaJDBC implements InterfacePersistencia {
    private final String DRIVER = "org.postgresql.Driver";
    private final String USER = "postgres";
    private final String SENHA = "1234";
    public static final String URL = "jdbc:postgresql://localhost:5434/Clinica-Veterinaria";
    private Connection con = null;

    public PersistenciaJDBC() throws Exception {

        Class.forName(DRIVER);
        System.out.println("Tentando estabelecer conexao JDBC com : " + URL + " ...");

        this.con = (Connection) DriverManager.getConnection(URL, USER, SENHA);

    }

    @Override
    public Boolean conexaoAberta() {

        try {
            if (con != null)
                return !con.isClosed();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void fecharConexao() {
        try {
            this.con.close();
            System.out.println("Fechou conexao JDBC");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object find(Class c, Object id) throws Exception {
        if (c == Receita.class) {
            PreparedStatement ps = this.con.prepareStatement("SELECT id,orientacao,consulta_id FROM receita WHERE id = ?");
            ps.setInt(1, Integer.parseInt(id.toString()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Receita receita = new Receita();
                receita.setId(rs.getLong("id"));
                receita.setOrientacao(rs.getString("orientacao"));
                Consulta consulta = new Consulta();
                consulta.setId(rs.getLong("consulta_id"));
                receita.setConsulta(consulta);
                PreparedStatement ps2 = this.con.prepareStatement("SELECT p.id, p.nome, p.quantidade, p.tipoproduto, p.valor, p.fornecedor_cpf " +
                        "FROM tb_receita_produto rp, produto p WHERE p.id = rp.produto_id AND rp.receita_id = ?");
                ps2.setInt(1, Integer.parseInt(id.toString()));
                ResultSet rs2 = ps2.executeQuery();
                List produtos = new ArrayList<Produto>();

                while (rs2.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs2.getLong("id"));
                    produto.setNome(rs2.getString("nome"));
                    produto.setQuantidade(rs2.getFloat("quantidade"));
                    TipoProduto tipoProduto = TipoProduto.getTipoProduto(rs2.getString("tipoproduto"));
                    produto.setTipoProduto(tipoProduto);
                    produto.setValor(rs2.getFloat("valor"));
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setCpf(rs2.getString("fornecedor_cpf"));
                    produto.setFornecedor(fornecedor);

                    produtos.add(produto);

                }
                receita.setProdutos(produtos);

                ps2.close();
                ps.close();

                return receita;

            }
        } else {
            if (c == Produto.class) {
                PreparedStatement ps = this.con.prepareStatement("SELECT id,nome,quantidade,tipoproduto,valor,fornecedor_cpf FROM produto WHERE id = ?");
                ps.setInt(1, Integer.parseInt(id.toString()));

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getLong("id"));
                    produto.setQuantidade(rs.getFloat("quantidade"));
                    produto.setNome(rs.getString("nome"));
                    produto.setValor(rs.getFloat("valor"));
                    TipoProduto tipoProduto = TipoProduto.getTipoProduto(rs.getString("tipoproduto"));
                    produto.setTipoProduto(tipoProduto);
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setCpf(rs.getString("fornecedor_cpf"));
                    produto.setFornecedor(fornecedor);

                    return produto;
                }
            }
        }
        return null;
    }

    @Override
    public Object persist(Object o) throws Exception {
        if (o instanceof Receita) {

            Receita receita = (Receita) o;

            if (receita.getId() == null) {

                PreparedStatement ps = this.con.prepareStatement("INSERT INTO receita (id,orientacao,consulta_id)" +
                        "values (nextval('seq_receita_id'), ?, ?)RETURNING receita.id");
                ps.setString(1, receita.getOrientacao());
                ps.setLong(2, receita.getConsulta().getId());

                ResultSet rs = ps.executeQuery();

                Long id = null;
                if (rs.next()) {
                    id = rs.getLong("id");
                }
                ps.close();
                rs.close();


                if(!receita.getProdutos().isEmpty()) insertProdutosReceita(id,receita.getProdutos());

                return id;
            } else {

                PreparedStatement ps = this.con.prepareStatement("UPDATE receita set "
                        + "orientacao = ?"
                        + "consulta_id = ?"
                        + "WHERE id = ? RETURNING receita.id");
                ps.setString(1, receita.getOrientacao());
                ps.setLong(2, receita.getConsulta().getId());
                ps.setLong(3, receita.getId());



                ResultSet rs = ps.executeQuery();

                Long id = receita.getId();
                if (rs.next()) {
                    id = rs.getLong("id");
                }
                ps.close();
                rs.close();

                List<Produto> produtosOld = getProdutosReceita(id);

                ProdutosToAddAndRemove produtosToAddAndRemove = VerDiferencaProdutos(produtosOld,receita.getProdutos());

                if(!produtosToAddAndRemove.produtosToAdd.isEmpty()){
                    insertProdutosReceita(id,produtosToAddAndRemove.produtosToAdd);
                }

                if(!produtosToAddAndRemove.produtosToRemove.isEmpty()){
                    deleteProdutosReceita(id,produtosToAddAndRemove.produtosToRemove);
                }

                return id;
            }
        }
        if (o instanceof Produto) {
            Produto produto = (Produto) o;

            if(produto.getId() == null){

                PreparedStatement ps = this.con.prepareStatement("INSERT INTO produto (id,nome,quantidade,tipoproduto,valor,fornecedor_cpf)" +
                        "values (nextval('seq_produto_id'), ?, ?, ?, ?, ?)RETURNING produto.id");
                ps.setString(1, produto.getNome());
                ps.setFloat(2, produto.getQuantidade());
                ps.setString(3, produto.getTipoProduto().toString());
                ps.setFloat(4,produto.getValor());
                ps.setString(5,produto.getFornecedor().getCpf());

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getLong("id");
                }
            }else{

                PreparedStatement ps = this.con.prepareStatement("UPDATE produto set "
                        + "nome = ?"
                        + "quantidade = ?"
                        + "tipoproduto = ?"
                        + "valor = ?"
                        + "fornecedor_cpf = ?"
                        + "WHERE id = ? RETURNING produto.id");
                ps.setString(1, produto.getNome());
                ps.setFloat(2, produto.getQuantidade());
                ps.setString(3, produto.getTipoProduto().toString());
                ps.setFloat(4,produto.getValor());
                ps.setString(5,produto.getFornecedor().getCpf());
                ps.setLong(6,produto.getId());


                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        if (o instanceof Fornecedor){
            Fornecedor fornecedor = (Fornecedor) o;

            PreparedStatement ps = this.con.prepareStatement("INSERT INTO pessoa "
                    + "(cpf, complemento, cep, data_nascimento, email, endereco, nome, numero_celular, rg, senha, tipo) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
            ps.setString(1, fornecedor.getCpf());
            ps.setString(2, fornecedor.getComplemento());
            ps.setString(3, fornecedor.getCep());
            ps.setTimestamp(4,new Timestamp(fornecedor.getData_nascimento().getTimeInMillis()));
            ps.setString(5, fornecedor.getEmail());
            ps.setString(6, fornecedor.getEndereco());
            ps.setString(7, fornecedor.getNome());
            ps.setString(8, fornecedor.getNumero_celular());
            ps.setString(9, fornecedor.getRg());
            ps.setString(10, fornecedor.getSenha());
            ps.setString(11, "FO");

            ps.execute();
            ps.close();

            ps = this.con.prepareStatement("INSERT INTO fornecedor "
                    + "(cnpj, ie, cpf) VALUES "
                    + "(?, ?, ?) RETURNING cpf");
            ps.setString(1, fornecedor.getCnpj());
            ps.setString(2, fornecedor.getIe());
            ps.setString(3, fornecedor.getCpf());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getString("cpf");
            }
        }
        if (o instanceof Consulta){
            Consulta consulta = (Consulta) o;

            PreparedStatement ps = this.con.prepareStatement("INSERT INTO consulta (id, data, data_retorno, observacao, valor) VALUES"
                    +"(nextval('seq_consulta_id'), ?, ?, ?, ? ) RETURNING id");

            ps.setTimestamp(1, new Timestamp(consulta.getData().getTimeInMillis()));
            ps.setTimestamp(2, new Timestamp(consulta.getData_retorno().getTimeInMillis()));
            ps.setString(3,consulta.getObservacao());
            ps.setFloat(4,consulta.getValor());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getLong("id");
            }
        }

        return null;
    }

    @Override
    public Object remover(Object o) throws Exception {
        if (o instanceof Receita) {

            Receita receita = (Receita) o;

            deleteProdutosReceita(receita.getId(),receita.getProdutos());

            PreparedStatement ps = this.con.prepareStatement("DELETE FROM receita WHERE id = ?");
            ps.setLong(1, receita.getId());
            ps.execute();

            return receita.getId();

        }
        if (o instanceof Produto) {

            Produto produto = (Produto) o;

            PreparedStatement ps = this.con.prepareStatement("DELETE FROM produto WHERE id = ?");
            ps.setLong(1, produto.getId());
            ps.execute();

            return produto.getId();
        }
        if (o instanceof Fornecedor){

            Fornecedor fornecedor = (Fornecedor) o;

            PreparedStatement ps = this.con.prepareStatement("DELETE FROM fornecedor WHERE cpf = ?");
            ps.setString(1,fornecedor.getCpf());
            ps.execute();
            ps.close();

            ps = this.con.prepareStatement("DELETE FROM pessoa WHERE cpf = ?");
            ps.setString(1,fornecedor.getCpf());
            ps.execute();

            return fornecedor.getCpf();
        }
        if (o instanceof Consulta){
            Consulta consulta = (Consulta) o;

            PreparedStatement ps = this.con.prepareStatement("DELETE FROM consulta WHERE id = ?");
            ps.setLong(1,consulta.getId());
            ps.execute();

            return consulta.getId();
        }

        return null;
    }

    @Override
    public List findAll(Class c) throws Exception {
        if (c == Receita.class) {
            List<Receita> lista = new ArrayList<>();

            PreparedStatement ps = this.con.prepareStatement("SELECT id,orientacao,consulta_id FROM receita ORDER BY id ASC");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Receita receita = new Receita();
                receita.setId(rs.getLong("id"));
                receita.setOrientacao(rs.getString("orientacao"));
                Consulta consulta = new Consulta();
                consulta.setId(rs.getLong("consulta_id"));
                receita.setConsulta(consulta);
                PreparedStatement ps2 = this.con.prepareStatement("SELECT p.id, p.nome, p.quantidade, p.tipoproduto, p.valor, p.fornecedor_cpf " +
                        "FROM tb_receita_produto rp, produto p WHERE p.id = rp.produto_id AND rp.receita_id = ?");
                ps2.setInt(1, Integer.parseInt(receita.getId().toString()));
                ResultSet rs2 = ps2.executeQuery();
                List produtos = new ArrayList<Produto>();
                while (rs2.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs2.getLong("id"));
                    produto.setNome(rs2.getString("nome"));
                    produto.setQuantidade(rs2.getFloat("quantidade"));
                    TipoProduto tipoProduto = TipoProduto.getTipoProduto(rs2.getString("tipoproduto"));
                    produto.setTipoProduto(tipoProduto);
                    produto.setValor(rs2.getFloat("valor"));
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setCpf(rs2.getString("fornecedor_cpf"));
                    produto.setFornecedor(fornecedor);

                    produtos.add(produto);

                }
                receita.setProdutos(produtos);

                lista.add(receita);

            }
            return lista;


        }else{
            if(c == Produto.class){
                List<Produto> lista = new ArrayList<>();
                PreparedStatement ps = this.con.prepareStatement("SELECT id,nome,quantidade,tipoproduto,valor,fornecedor_cpf FROM produto ORDER BY id ASC");

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getLong("id"));
                    produto.setQuantidade(rs.getFloat("quantidade"));
                    produto.setNome(rs.getString("nome"));
                    produto.setValor(rs.getFloat("valor"));
                    TipoProduto tipoProduto = TipoProduto.getTipoProduto(rs.getString("tipoproduto"));
                    produto.setTipoProduto(tipoProduto);
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setCpf(rs.getString("fornecedor_cpf"));
                    produto.setFornecedor(fornecedor);

                    lista.add(produto);
                }
                return lista;
            }
        }
        return null;
    }

    private ProdutosToAddAndRemove VerDiferencaProdutos(List<Produto> older, List<Produto> newer){
        boolean add = true;
        List<Produto> produtosToAdd = new ArrayList<>();
        List<Produto> produtosToRemove = older;

        for(Produto newProduto : newer){
            for(Produto oldproduto : older){
                if(oldproduto == newProduto){
                    produtosToRemove.remove(older);
                    add = false;
                }
            }
            if(add){
                produtosToAdd.add(newProduto);
            }
        }
        ProdutosToAddAndRemove produtosToAddAndRemove = new ProdutosToAddAndRemove(produtosToAdd,produtosToRemove);
        return produtosToAddAndRemove;
    }
    private List<Produto> getProdutosReceita(Long receita_id) throws Exception{
        PreparedStatement ps = this.con.prepareStatement("SELECT receita_id, produto_id FROM tb_receita_produto WHERE receita_id = ?");
        ps.setLong(1,receita_id);

        ResultSet rs = ps.executeQuery();

        List<Produto> produtos = new ArrayList<>();
        while(rs.next()){
            Produto produto = new Produto();
            produto.setId(rs.getLong("produto_id"));
            produtos.add(produto);
        }
        ps.close();
        rs.close();
        return produtos;
    }
    private void insertProdutosReceita(Long receita_id, List<Produto> produtos) throws Exception{
        for(Produto produto : produtos){

            PreparedStatement ps = this.con.prepareStatement("INSERT INTO tb_receita_produto (receita_id,produto_id)" +
                    "values (?, ?)");
            ps.setLong(1, receita_id);
            ps.setLong(2, produto.getId());

            ps.execute();
            ps.close();
        }
    }
    private void deleteProdutosReceita(Long receita_id,List<Produto> produtos) throws Exception{
        for(Produto produto : produtos){
            PreparedStatement ps = this.con.prepareStatement("DELETE FROM tb_receita_produto WHERE receita_id = ? AND produto_id = ?");

            ps.setLong(1,receita_id);
            ps.setLong(2,produto.getId());

            ps.execute();
            ps.close();
        }
    }
    private class ProdutosToAddAndRemove {
        private List<Produto> produtosToAdd;
        private List<Produto> produtosToRemove;

        public ProdutosToAddAndRemove(List<Produto> produtosToAdd, List<Produto> produtosToRemove) {
            this.produtosToAdd = produtosToAdd;
            this.produtosToRemove = produtosToRemove;
        }
    }
}
