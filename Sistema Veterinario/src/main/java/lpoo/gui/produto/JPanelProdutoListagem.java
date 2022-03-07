package lpoo.gui.produto;

import lpoo.Controle;
import lpoo.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class JPanelProdutoListagem  extends JPanel implements ActionListener {
    private JPanelProduto pnlProduto;
    private Controle controle;

    private BorderLayout borderLayout;
    private JPanel pnlNorte;
    private JLabel lblFiltro;
    private JTextField filtroField;
    private JButton btnFiltro;

    private JPanel pnlCentro;
    private JScrollPane scpListagem;
    private JTable tblListagem;
    private DefaultTableModel modeloTabela;

    private JPanel pnlSul;
    private JButton btnNovo;
    private JButton btnAlterar;
    private JButton btnRemover;

    private SimpleDateFormat format;

    public JPanelProdutoListagem(JPanelProduto pnlProduto, Controle controle){
        this.pnlProduto = pnlProduto;
        this.controle = controle;

        initComponents();
    }

    public void initComponents(){
        borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        pnlNorte = new JPanel();
        pnlNorte.setLayout(new FlowLayout());

        lblFiltro = new JLabel("Filtrar por nome:");
        pnlNorte.add(lblFiltro);

        filtroField = new JTextField(20);
        pnlNorte.add(filtroField);

        btnFiltro = new JButton("Filtrar");
        btnFiltro.addActionListener(this);
        btnFiltro.setFocusable(true);
        btnFiltro.setToolTipText("btnFiltrar");
        btnFiltro.setActionCommand("botao_filtro");
        pnlNorte.add(btnFiltro);

        this.add(pnlNorte, BorderLayout.NORTH);

        pnlCentro = new JPanel();
        pnlCentro.setLayout(new BorderLayout());

        scpListagem = new JScrollPane();
        tblListagem =  new JTable();

        modeloTabela = new DefaultTableModel(
                new String [] {
                        "Nome", "Valor", "Quantidade", "Tipo produto", "Fornecedor CPF"
                }, 0);

        tblListagem.setModel(modeloTabela);
        scpListagem.setViewportView(tblListagem);

        pnlCentro.add(scpListagem, BorderLayout.CENTER);

        this.add(pnlCentro, BorderLayout.CENTER);

        pnlSul = new JPanel();
        pnlSul.setLayout(new FlowLayout());

        btnNovo = new JButton("Novo");
        btnNovo.addActionListener(this);
        btnNovo.setFocusable(true);
        btnNovo.setToolTipText("btnNovo");
        btnNovo.setMnemonic(KeyEvent.VK_N);
        btnNovo.setActionCommand("botao_novo");

        pnlSul.add(btnNovo);

        btnAlterar = new JButton("Editar");
        btnAlterar.addActionListener(this);
        btnAlterar.setFocusable(true);
        btnAlterar.setToolTipText("btnAlterar");
        btnAlterar.setActionCommand("botao_alterar");

        pnlSul.add(btnAlterar);

        btnRemover = new JButton("Remover");
        btnRemover.addActionListener(this);
        btnRemover.setFocusable(true);
        btnRemover.setToolTipText("btnRemvoer");
        btnRemover.setActionCommand("botao_remover");

        pnlSul.add(btnRemover);


        this.add(pnlSul, BorderLayout.SOUTH);

        format = new SimpleDateFormat("dd/MM/yyyy");
    }
    public void populaTable(String filter){

        try {
            List<Produto> listProdutos = controle.getConexaoJDBC().findAll(Produto.class,filter);

            setListTable(listProdutos);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar Funcionarios -:"+ex.getLocalizedMessage(), "Funcionarios", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void populaTable(){
        populaTable("");
    }

    private void setListTable(List<Produto> listProduto){
        DefaultTableModel model =  (DefaultTableModel) tblListagem.getModel();
        model.setRowCount(0);

        for(Produto produto : listProduto){
            model.addRow(new Object[]{produto, produto.getValor(),produto.getQuantidade(), produto.getTipoProduto().toString(),
                    produto.getFornecedor().getCpf()});
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(btnNovo.getActionCommand())){
            pnlProduto.showTela("tela_produto_form");
            pnlProduto.getFormulario().setProdutoForm(null);

        }else if(e.getActionCommand().equals(btnAlterar.getActionCommand())){
            int index = tblListagem.getSelectedRow();
            if(index >-1){
                DefaultTableModel model = (DefaultTableModel) tblListagem.getModel();
                Vector linha = (Vector) model.getDataVector().get(index);

                Produto produto = (Produto) linha.get(0);

                pnlProduto.showTela("tela_produto_form");
                pnlProduto.getFormulario().setProdutoForm(produto);
            }else{
                JOptionPane.showMessageDialog(this, "Selecione uma linha para editar!", "Edição", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(e.getActionCommand().equals(btnRemover.getActionCommand())){
            int index = tblListagem.getSelectedRow();
            if(index >- 1){
                DefaultTableModel model = (DefaultTableModel) tblListagem.getModel();
                Vector linha = (Vector) model.getDataVector().get(index);
                Produto produto = (Produto) linha.get(0);

                try {
                    pnlProduto.getControle().getConexaoJDBC().remover(produto);
                    JOptionPane.showMessageDialog(this, "Produto removido!", "Deletar", JOptionPane.INFORMATION_MESSAGE);
                    pnlProduto.showTela("tela_produto_listagem");
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(this, "Erro ao remover Produto -:"+ex.getLocalizedMessage(), "Produtos", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

            }else{
                JOptionPane.showMessageDialog(this, "Selecione uma linha para remover!", "Remoção", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(e.getActionCommand().equals(btnFiltro.getActionCommand())){
            populaTable(filtroField.getText().trim());
        }
    }
}

