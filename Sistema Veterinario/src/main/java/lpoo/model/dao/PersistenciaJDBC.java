package lpoo.model.dao;

import lpoo.model.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
                        + "orientacao = ?,"
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
                        + "nome = ?,"
                        + "quantidade = ?,"
                        + "tipoproduto = ?,"
                        + "valor = ?,"
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

            if(fornecedor.getData_cadastro() == null){
                PreparedStatement ps = this.con.prepareStatement("INSERT INTO pessoa "
                        + "(cpf, complemento, cep, data_nascimento, email, endereco, nome, numero_celular, rg, senha, tipo, data_cadastro) VALUES "
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now()) ");
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
        if (o instanceof Funcionario){
            Funcionario funcionario = (Funcionario) o;

            if(funcionario.getData_cadastro() == null){
                PreparedStatement ps = this.con.prepareStatement("INSERT INTO pessoa "
                        + "(cpf, complemento, cep, data_nascimento, email, endereco, nome, numero_celular, rg, senha, tipo, data_cadastro) VALUES "
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,now()) ");
                ps.setString(1, funcionario.getCpf());
                ps.setString(2, funcionario.getComplemento());
                ps.setString(3, funcionario.getCep());
                ps.setTimestamp(4,new Timestamp(funcionario.getData_nascimento().getTimeInMillis()));
                ps.setString(5, funcionario.getEmail());
                ps.setString(6, funcionario.getEndereco());
                ps.setString(7, funcionario.getNome());
                ps.setString(8, funcionario.getNumero_celular());
                ps.setString(9, funcionario.getRg());
                ps.setString(10, funcionario.getSenha());
                ps.setString(11, "FU");

                ps.execute();
                ps.close();

                ps = this.con.prepareStatement("INSERT INTO funcionario "
                        + "(numero_ctps,numero_pis, cpf, cargo) VALUES "
                        + "(?, ?, ?, ?) RETURNING cpf");
                ps.setString(1, funcionario.getNumero_ctps());
                ps.setString(2, funcionario.getNumero_pis());
                ps.setString(3, funcionario.getCpf());
                ps.setString(4, funcionario.getCargo().toString());

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return rs.getString("cpf");
                }
            }else{
                PreparedStatement ps = this.con.prepareStatement("UPDATE pessoa SET"
                        +" complemento = ?, "
                        +" cep = ?, "
                        +" data_nascimento = ?, "
                        +" email = ?, "
                        +" endereco = ?, "
                        +" nome = ?, "
                        +" numero_celular = ?, "
                        +" rg = ?, "
                        +" senha = ? "
                        +"WHERE cpf = ?");

                ps.setString(1, funcionario.getComplemento());
                ps.setString(2, funcionario.getCep());
                ps.setTimestamp(3,new Timestamp(funcionario.getData_nascimento().getTimeInMillis()));
                ps.setString(4, funcionario.getEmail());
                ps.setString(5, funcionario.getEndereco());
                ps.setString(6, funcionario.getNome());
                ps.setString(7, funcionario.getNumero_celular());
                ps.setString(8, funcionario.getRg());
                ps.setString(9, funcionario.getSenha());
                ps.setString(10, funcionario.getCpf());

                ps.execute();
                ps.close();

                ps = this.con.prepareStatement("UPDATE funcionario SET"
                        + " numero_ctps = ?, "
                        +" numero_pis = ?, "
                        +" cargo = ? "
                        +"WHERE cpf = ? RETURNING funcionario.cpf");
                ps.setString(1, funcionario.getNumero_ctps());
                ps.setString(2, funcionario.getNumero_pis());
                ps.setString(3, funcionario.getCargo().toString());
                ps.setString(4, funcionario.getCpf());

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return rs.getString("cpf");
                }

            }
        }
        if (o instanceof Cliente){
            Cliente cliente = (Cliente) o;

            if(cliente.getData_cadastro() == null){
                PreparedStatement ps = this.con.prepareStatement("INSERT INTO pessoa "
                        + "(cpf, complemento, cep, data_nascimento, email, endereco, nome, numero_celular, rg, senha, tipo, data_cadastro) VALUES "
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,now()) ");
                ps.setString(1, cliente.getCpf());
                ps.setString(2, cliente.getComplemento());
                ps.setString(3, cliente.getCep());
                ps.setTimestamp(4,new Timestamp(cliente.getData_nascimento().getTimeInMillis()));
                ps.setString(5, cliente.getEmail());
                ps.setString(6, cliente.getEndereco());
                ps.setString(7, cliente.getNome());
                ps.setString(8, cliente.getNumero_celular());
                ps.setString(9, cliente.getRg());
                ps.setString(10, cliente.getSenha());
                ps.setString(11, "C");

                ps.execute();
                ps.close();

                ps = this.con.prepareStatement("INSERT INTO cliente "
                        + "(data_ultima_visita, cpf) VALUES "
                        + "(?, ?) RETURNING cpf");
                ps.setTimestamp(1,new Timestamp( cliente.getData_ultima_visita().getTimeInMillis()));
                ps.setString(2,cliente.getCpf());

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return rs.getString("cpf");
                }
            }else{
                PreparedStatement ps = this.con.prepareStatement("UPDATE pessoa SET"
                        +" complemento = ?, "
                        +" cep = ?, "
                        +" data_nascimento = ?, "
                        +" email = ?, "
                        +" endereco = ?, "
                        +" nome = ?, "
                        +" numero_celular = ?, "
                        +" rg = ?, "
                        +" senha = ? "
                        +"WHERE cpf = ?");

                ps.setString(1, cliente.getComplemento());
                ps.setString(2, cliente.getCep());
                ps.setTimestamp(3,new Timestamp(cliente.getData_nascimento().getTimeInMillis()));
                ps.setString(4, cliente.getEmail());
                ps.setString(5, cliente.getEndereco());
                ps.setString(6, cliente.getNome());
                ps.setString(7, cliente.getNumero_celular());
                ps.setString(8, cliente.getRg());
                ps.setString(9, cliente.getSenha());
                ps.setString(10, cliente.getCpf());

                ps.execute();
                ps.close();

                ps = this.con.prepareStatement("UPDATE cliente SET"
                        + " data_ultima_visita = ? "
                        +"WHERE cpf = ? RETURNING cliente.cpf");
                ps.setTimestamp(1,new Timestamp( cliente.getData_ultima_visita().getTimeInMillis()));
                ps.setString(2, cliente.getCpf());

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return rs.getString("cpf");
                }

            }
        }
        if (o instanceof Medico){
            Medico medico = (Medico) o;

            if(medico.getData_cadastro() == null){
                PreparedStatement ps = this.con.prepareStatement("INSERT INTO pessoa "
                        + "(cpf, complemento, cep, data_nascimento, email, endereco, nome, numero_celular, rg, senha, tipo, data_cadastro) VALUES "
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,now()) ");
                ps.setString(1, medico.getCpf());
                ps.setString(2, medico.getComplemento());
                ps.setString(3, medico.getCep());
                ps.setTimestamp(4,new Timestamp(medico.getData_nascimento().getTimeInMillis()));
                ps.setString(5, medico.getEmail());
                ps.setString(6, medico.getEndereco());
                ps.setString(7, medico.getNome());
                ps.setString(8, medico.getNumero_celular());
                ps.setString(9, medico.getRg());
                ps.setString(10, medico.getSenha());
                ps.setString(11, "C");

                ps.execute();
                ps.close();

                ps = this.con.prepareStatement("INSERT INTO medico "
                        + "(numero_crmv, cpf) VALUES "
                        + "(?, ?) RETURNING cpf");
                ps.setString(1,medico.getNumero_crmv());
                ps.setString(2,medico.getCpf());

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return rs.getString("cpf");
                }
            }
        }
        if (o instanceof Venda){
            Venda venda = (Venda) o;

            PreparedStatement ps = this.con.prepareStatement("INSERT INTO venda (id, data, observacao, pagamento, valor_total, cliente_cpf, funcionario_cpf) VALUES"
                    +"(nextval('seq_venda_id'), now(), ?, ?, ?, ? ,?  ) RETURNING id");

            ps.setString(1,venda.getObservacao());
            ps.setString(2,venda.getPagamento().toString());
            ps.setFloat(3,venda.getValor_total());
            ps.setString(4,venda.getCliente().getCpf());
            ps.setString(5,venda.getFuncionario().getCpf());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getLong("id");
            }
        }
        if (o instanceof Agenda){
            Agenda agenda = (Agenda) o;

            PreparedStatement ps = this.con.prepareStatement("INSERT INTO agenda (id, data_inicio, data_fim, observacao, tipoproduto, medico_cpf, funcionario_cpf) VALUES"
                    +"(nextval('seq_agenda_id'), ?, ?, ?, ?, ? ,?  ) RETURNING id");

            ps.setTimestamp(1,new Timestamp(agenda.getData_inicio().getTimeInMillis()));
            ps.setTimestamp(2,new Timestamp(agenda.getData_fim().getTimeInMillis()));
            ps.setString(3,agenda.getObservacao());
            ps.setString(4,agenda.getTipoProduto().toString());
            ps.setString(5,agenda.getMedico().getCpf());
            ps.setString(6,agenda.getFuncionario().getCpf());

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
        if (o instanceof Funcionario){
            Funcionario funcionario = (Funcionario) o;

            PreparedStatement ps = this.con.prepareStatement("DELETE FROM funcionario WHERE cpf = ?");
            ps.setString(1,funcionario.getCpf());
            ps.execute();
            ps.close();

            ps = this.con.prepareStatement("DELETE FROM pessoa WHERE cpf = ?");
            ps.setString(1,funcionario.getCpf());
            ps.execute();

            return funcionario.getCpf();
        }

        return null;
    }


    public List findAll(Class c, String filter) throws Exception {
        if (c == Receita.class) {
            List<Receita> lista = new ArrayList<>();

            PreparedStatement ps = this.con.prepareStatement("SELECT r.id,r.orientacao,r.consulta_id,c.observacao as consulta_observacao FROM receita r " +
                    "JOIN consulta c ON c.id = r.consulta_id " +
                    "WHERE r.orientacao LIKE ? "+
                    "ORDER BY id ASC");

            ps.setString(1,filter+"%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Receita receita = new Receita();
                receita.setId(rs.getLong("id"));
                receita.setOrientacao(rs.getString("orientacao"));
                Consulta consulta = new Consulta();
                consulta.setId(rs.getLong("consulta_id"));
                consulta.setObservacao(rs.getString("consulta_observacao"));
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
        }
        if (c == Produto.class) {
                List<Produto> lista = new ArrayList<>();
                PreparedStatement ps = this.con.prepareStatement("SELECT p.id, p.nome, p.quantidade, p.tipoproduto, p.valor, p.fornecedor_cpf, f.nome AS fornecedor_nome FROM produto p " +
                        "JOIN pessoa f ON f.cpf = p.fornecedor_cpf WHERE p.nome LIKE ? ORDER BY id ASC");

                ps.setString(1,filter+"%");
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
                    fornecedor.setNome(rs.getString("fornecedor_nome"));
                    produto.setFornecedor(fornecedor);

                    lista.add(produto);
                }
                return lista;
            }
        if (c == Funcionario.class) {
            List<Funcionario> lista = new ArrayList<>();
            PreparedStatement ps = this.con.prepareStatement("SELECT " +
                    "p.cpf, p.cep, p.complemento, p.data_nascimento, p.email, p.endereco, p.nome, p.numero_celular, p.rg,p.data_cadastro," +
                    "f.cargo, f.numero_ctps, f.numero_pis , p.senha " +
                    "FROM funcionario f " +
                    "JOIN pessoa p ON p.cpf = f.cpf "+
                    "WHERE p.nome LIKE ?");

            ps.setString(1,filter+"%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setCep(rs.getString("cep"));
                funcionario.setComplemento(rs.getString("complemento"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(rs.getDate("data_nascimento"));
                funcionario.setData_nascimento(calendar);
                funcionario.setEmail(rs.getString("email"));
                funcionario.setEndereco(rs.getString("endereco"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setNumero_celular(rs.getString("numero_celular"));
                funcionario.setRg(rs.getString("rg"));
                funcionario.setCargo(Cargo.valueOf(rs.getString("cargo")));
                funcionario.setNumero_ctps(rs.getString("numero_ctps"));
                funcionario.setNumero_pis(rs.getString("numero_pis"));
                funcionario.setSenha(rs.getString("senha"));
                calendar.setTime(rs.getDate("data_cadastro"));
                funcionario.setData_cadastro(calendar);

                PreparedStatement ps2 = this.con.prepareStatement("SELECT id, data, observacao, pagamento, valor_total, cliente_cpf " +
                                                                        " FROM venda WHERE funcionario_cpf = ?");
                ps2.setString(1, funcionario.getCpf());
                ResultSet rs2 = ps2.executeQuery();
                List vendas = new ArrayList<Venda>();
                while (rs2.next()) {
                    Venda venda = new Venda();
                    venda.setId(rs2.getLong("id"));
                    calendar = Calendar.getInstance();
                    calendar.setTime(rs2.getDate("data"));
                    venda.setData(calendar);
                    venda.setObservacao(rs2.getString("observacao"));
                    Pagamento pagamento = Pagamento.getPagamento(rs2.getString("pagamento"));
                    venda.setPagamento(pagamento);
                    venda.setValor_total(rs2.getFloat("valor_total"));
                    Cliente cliente = new Cliente();
                    cliente.setCpf(rs2.getString("cliente_cpf"));
                    venda.setCliente(cliente);

                    vendas.add(venda);

                }
                ps2.close();
                ps2 = this.con.prepareStatement("SELECT id, data_fim, data_inicio, observacao, tipoproduto, medico_cpf "+
                                                    " FROM agenda WHERE funcionario_cpf = ?");
                ps2.setString(1, funcionario.getCpf());
                rs2 = ps2.executeQuery();
                List agendas = new ArrayList();
                while(rs2.next()){
                    Agenda agenda =  new Agenda();
                    agenda.setId(rs2.getLong("id"));
                    calendar.setTime(rs2.getDate("data_fim"));
                    agenda.setData_fim(calendar);
                    calendar.setTime(rs2.getDate("data_inicio"));
                    agenda.setData_inicio(calendar);
                    agenda.setObservacao(rs2.getString("observacao"));
                    agenda.setTipoProduto(TipoProduto.getTipoProduto(rs2.getString("tipoproduto")));
                    Medico medico = new Medico();
                    medico.setCpf(rs2.getString("medico_cpf"));
                    agenda.setMedico(medico);

                    agendas.add(agenda);
                }

                funcionario.setVendas(vendas);
                funcionario.setAgendas(agendas);
                lista.add(funcionario);
            }
            return lista;
        }
        if (c == Fornecedor.class) {
            List<Fornecedor> lista = new ArrayList<>();
            PreparedStatement ps = this.con.prepareStatement("SELECT " +
                    "p.cpf, p.cep, p.complemento, p.data_nascimento, p.email, p.endereco, p.nome, p.numero_celular, p.rg," +
                    "f.cnpj,f.ie , p.senha " +
                    "FROM fornecedor f " +
                    "JOIN pessoa p ON p.cpf = f.cpf "+
                    "WHERE p.nome LIKE ?");

            ps.setString(1,filter+"%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setCpf(rs.getString("cpf"));
                fornecedor.setCep(rs.getString("cep"));
                fornecedor.setComplemento(rs.getString("complemento"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(rs.getDate("data_nascimento"));
                fornecedor.setData_nascimento(calendar);
                fornecedor.setEmail(rs.getString("email"));
                fornecedor.setEndereco(rs.getString("endereco"));
                fornecedor.setNome(rs.getString("nome"));
                fornecedor.setNumero_celular(rs.getString("numero_celular"));
                fornecedor.setRg(rs.getString("rg"));
                fornecedor.setSenha(rs.getString("senha"));
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.setIe(rs.getString("ie"));


                lista.add(fornecedor);
            }
            return lista;
        }
        if (c == Consulta.class){
            List<Consulta> lista = new ArrayList<>();
            PreparedStatement ps = this.con.prepareStatement("SELECT id,data,data_retorno,observacao,valor,medico_cpf,pet_id FROM consulta");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Consulta consulta = new Consulta();
                consulta.setId(rs.getLong("id"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(rs.getDate("data"));
                consulta.setData(calendar);
                calendar.setTime(rs.getDate("data_retorno"));
                consulta.setData_retorno(calendar);
                consulta.setObservacao(rs.getString("observacao"));
                consulta.setValor(rs.getFloat("valor"));
                Medico medico = new Medico();
                medico.setCpf(rs.getString("medico_cpf"));
                consulta.setMedico(medico);
                Pet pet = new Pet();
                pet.setId(rs.getLong("pet_id"));

                lista.add(consulta);
            }
            return lista;
        }

        return null;
    }

    @Override
    public List findAll(Class c) throws Exception{
        return findAll(c, "");
    }

    @Override
    public Pessoa doLogin(String email, String senha) throws Exception {


        Pessoa pessoa = null;

        PreparedStatement ps =
                this.con.prepareStatement("select p.nome, p.email, p.senha from pessoa p where p.email = ? and p.tipo = 'FU'");

        ps.setString(1, email);
//        ps.setString(2, senha);

        ResultSet rs = ps.executeQuery();//o ponteiro do REsultSet inicialmente está na linha -1

        if(rs.next()){//se a matriz (ResultSet) tem uma linha

            pessoa = new Pessoa();
            pessoa.setEmail(rs.getString("email"));
            pessoa.setNome(rs.getString("nome"));
            pessoa.setSenha(rs.getString("senha"));
        }

        ps.close();
        return pessoa;

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
