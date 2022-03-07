package lpoo.gui.receita;

import lpoo.Controle;
import lpoo.model.Consulta;
import lpoo.model.Produto;
import lpoo.model.Receita;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class JPanelReceitaForm extends JPanel implements ActionListener {
    private Controle controle;

    private JPanel pnlSul;
    private JPanel pnlDadosCadastrais;
    private JPanel pnlProdutos;
    private JPanelReceita pnlReceita;

    private JTextField orientacaoField;
    private JLabel orientacaoLabel;

    private JLabel consultasLabel;
    private JComboBox cbxConsultas;

    private JButton btnGravar;
    private JButton btnCancelar;

    private JButton btnLeft;
    private JButton btnRight;

    private BorderLayout borderLayout;
    private GridBagLayout gridBagLayoutDadosCadastrais;

    private SimpleDateFormat formato;

    private JTabbedPane tbpAbas;

    private JScrollPane scpListagemConsultas;
    private JTable tblListagemConsultas;

    private DefaultTableModel modeloTabelaConsultas;

    private JLabel tblEsquerdaLabel;
    private JTable tblListagemConsultasBD;

    private JLabel tblDireitaLabel;

    private Receita receita;
    private List<Produto> produtosBD = new  ArrayList<>();

    public JPanelReceitaForm(JPanelReceita pnlReceita, Controle controle){
        this.controle = controle;
        this.pnlReceita = pnlReceita;
        initComponents();
    }

    public void populaComboConsultas(){
        cbxConsultas.removeAllItems();//zera o combo

        DefaultComboBoxModel model =  (DefaultComboBoxModel) cbxConsultas.getModel();

        model.addElement("Selecione"); //primeiro item
        try {

            List<Consulta> listConsulta = controle.getConexaoJDBC().findAll(Consulta.class);
            for(Consulta consulta : listConsulta){
                model.addElement(consulta);
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Erro ao listar Consultas -:"+ex.getLocalizedMessage(), "Consultas", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    public void populaTabelaProdutosBD(){
        populaTabelaProdutosBD("");
    }
    public void populaTabelaProdutosBD(String filter){
        if(produtosBD.isEmpty()){
            try {
                produtosBD = controle.getConexaoJDBC().findAll(Produto.class,filter);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar Funcionarios -:"+ex.getLocalizedMessage(), "Funcionarios", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        setListTable(produtosBD);
    }

    private void changeTableRowsHandler(JTable remove, JTable add){
        int index = remove.getSelectedRow();
        if(index >- 1) {
            DefaultTableModel model = (DefaultTableModel) remove.getModel();

            Vector linha = (Vector) model.getDataVector().get(index);
            Produto produto = (Produto) linha.get(0);

            model.removeRow(index);

            model = (DefaultTableModel) add.getModel();
            model.addRow(new Object[]{produto, produto.getValor(), produto.getTipoProduto().toString()});

        }else{
            JOptionPane.showMessageDialog(this, "Selecione uma linha para mover!", "Mover", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setListTable(List<Produto> listProduto){
        DefaultTableModel model =  (DefaultTableModel) tblListagemConsultasBD.getModel();
        model.setRowCount(0);

        for(Produto produto : listProduto){
            model.addRow(new Object[]{produto, produto.getValor(), produto.getTipoProduto().toString()});
        }

    }
    private void populaTabelaProdutos(List<Produto> listProdutos){
        DefaultTableModel modelBD =  (DefaultTableModel) tblListagemConsultasBD.getModel();
        DefaultTableModel model =  (DefaultTableModel) tblListagemConsultas.getModel();

        for(int i = 0; i<modelBD.getRowCount();i++){
            Vector linha = (Vector) modelBD.getDataVector().get(i);
            Produto produto = (Produto) linha.get(0);

            for(Produto produtoSelected : listProdutos){
                if(produto.equals(produtoSelected)){
                    modelBD.removeRow(i);
                    model.addRow(new Object[]{produtoSelected, produtoSelected.getValor(), produtoSelected.getTipoProduto().toString()});
                }
            }


        }
    }

    private List<Produto> getSelectedProdutos() {
        DefaultTableModel model = (DefaultTableModel) tblListagemConsultas.getModel();
        List<Produto> produtosSelected = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Vector linha = (Vector) model.getDataVector().get(i);
            Produto produto = (Produto) linha.get(0);

            produtosSelected.add(produto);
        }
        return produtosSelected;
    }

    public void setReceitaForm(Receita r){
        if(r == null){
            orientacaoField.setText("");
            cbxConsultas.setSelectedIndex(0);
            DefaultTableModel model =  (DefaultTableModel) tblListagemConsultas.getModel();
            model.setRowCount(0);
            receita = null;
        }else{
            receita = r;
            orientacaoField.setText(r.getOrientacao());
            cbxConsultas.getModel().setSelectedItem(r.getConsulta());
            populaTabelaProdutos(r.getProdutos());
        }

    }
    public Receita getReceitaByForm() {

        if (orientacaoField.getText().trim().length() > 3 &&
            cbxConsultas.getSelectedIndex() != 0) {
            Receita r = new Receita();
            r.setOrientacao(orientacaoField.getText().trim());
            r.setConsulta((Consulta) cbxConsultas.getSelectedItem());
            r.setProdutos(getSelectedProdutos());
            if (receita != null) {
                r.setId(receita.getId());
            }

            return r;
        }
        return null;
    }

    public void initComponents(){

        borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        tbpAbas = new JTabbedPane();
        this.add(tbpAbas, BorderLayout.CENTER);

        pnlDadosCadastrais = new JPanel();
        tbpAbas.add("Dados Cadastrais", pnlDadosCadastrais);
        pnlProdutos = new JPanel();
        tbpAbas.add("Produtos", pnlProdutos);

        pnlSul = new JPanel();
        pnlSul.setLayout(new FlowLayout());

        //BOTAO GRAVAR-----------------------------------------------------------
        btnGravar = new JButton("Gravar");
        btnGravar.addActionListener(this);
        btnGravar.setFocusable(true);
        btnGravar.setToolTipText("btnGravarFuncionario");
        btnGravar.setMnemonic(KeyEvent.VK_G);
        btnGravar.setActionCommand("botao_gravar_formulario_funcionario");
        pnlSul.add(btnGravar);

        //BOTAO CANCELAR---------------------------------------------------------
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(this);
        btnCancelar.setFocusable(true);
        btnCancelar.setToolTipText("btnCancelarFuncionario");
        btnCancelar.setActionCommand("botao_cancelar_formulario_funcionario");
        pnlSul.add(btnCancelar);

        this.add(pnlSul,BorderLayout.SOUTH);

        formato = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        initComponentsDadosCadastrais();
        initComponentsConsultas();

    }

    private void initComponentsDadosCadastrais(){
        gridBagLayoutDadosCadastrais = new GridBagLayout();
        pnlDadosCadastrais.setLayout(gridBagLayoutDadosCadastrais);

        //ORIENTACAO-----------------------------------------------------------
        orientacaoLabel = new JLabel("Orientação:");
        GridBagConstraints posicionador = new GridBagConstraints();
        posicionador.gridy = 0;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(orientacaoLabel,posicionador);

        orientacaoField = new JTextField(20);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 0;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(orientacaoField,posicionador);
        //CONSULTA---------------------------------------------------------
        consultasLabel = new JLabel("Consultas:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 4;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(consultasLabel,posicionador);

        cbxConsultas = new JComboBox();
        posicionador = new GridBagConstraints();
        posicionador.gridy = 4;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(cbxConsultas,posicionador);
    }

    private void initComponentsConsultas(){
        //PAINEL FINAL-------------------------------------------------------------
        pnlProdutos.setLayout(new BorderLayout());

        GridBagLayout gridBagLayoutBotoes = new GridBagLayout();
        JPanel pnlBotoes = new JPanel();
        pnlBotoes.setLayout(gridBagLayoutBotoes);
        //PAINEL CENTRO-----------------------------------------------------------
        GridBagLayout gridBagLayoutDadosCadastraisConsulta = new GridBagLayout();
        JPanel pnlDadosCadastraisConsulta;
        pnlDadosCadastraisConsulta = new JPanel();
        pnlDadosCadastraisConsulta.setLayout(gridBagLayoutDadosCadastraisConsulta);
        //TABELA ESQUERDA-------------------------------
        tblEsquerdaLabel = new JLabel("Todos produtos");
        GridBagConstraints posicionador = new GridBagConstraints();
        posicionador = new GridBagConstraints();
        posicionador.gridy = 0;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.CENTER;
        pnlDadosCadastraisConsulta.add(tblEsquerdaLabel,posicionador);

        JScrollPane scpListagemConsultasBD = new JScrollPane();
        tblListagemConsultasBD = new JTable();

        DefaultTableModel modeloTabelaConsultasBD = new DefaultTableModel(
                new String[]{
                        "Nome", "Valor", "Tipo Produto"
                }, 0);


        tblListagemConsultasBD.setModel(modeloTabelaConsultasBD);
        scpListagemConsultasBD.setViewportView(tblListagemConsultasBD);

        posicionador = new GridBagConstraints();
        posicionador.gridy = 1;
        posicionador.gridx = 0;
        posicionador.gridheight = 3;
        posicionador.anchor = GridBagConstraints.CENTER;
        pnlDadosCadastraisConsulta.add(scpListagemConsultasBD,posicionador);

        //BOTAO ESQUERDA---------------------------------
        btnLeft = new JButton("<");
        btnLeft.addActionListener(this);
        btnLeft.setFocusable(true);
        btnLeft.setToolTipText("btnLeft");
        btnLeft.setMnemonic(KeyEvent.VK_G);
        btnLeft.setActionCommand("botao-passar-para-esquerda");


        posicionador = new GridBagConstraints();
        posicionador.gridy = 1;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlBotoes.add(btnLeft,posicionador);

        //BOTAO DIREITA-----------------------------------
        btnRight = new JButton(">");
        btnRight.addActionListener(this);
        btnRight.setFocusable(true);
        btnRight.setToolTipText("btnRight");
        btnRight.setActionCommand("botao-passar-para-direita");

        posicionador = new GridBagConstraints();
        posicionador.gridy = 2;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;

        pnlBotoes.add(btnRight,posicionador);

        //BOTOES---------------------------------
        posicionador = new GridBagConstraints();
        posicionador.gridy = 3;
        posicionador.gridx = 1;
        pnlDadosCadastraisConsulta.add(pnlBotoes,posicionador);
        //TABELA DIREITA--------------------------------------
        pnlProdutos.setLayout(new BorderLayout());

        tblDireitaLabel = new JLabel("Produtos da receita");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 0;
        posicionador.gridx = 3;
        posicionador.anchor = GridBagConstraints.CENTER;
        pnlDadosCadastraisConsulta.add(tblDireitaLabel,posicionador);

        scpListagemConsultas = new JScrollPane();
        tblListagemConsultas = new JTable();

        DefaultTableModel modeloTabelaConsultas = new DefaultTableModel(
                new String[]{
                        "Nome", "Valor", "Tipo Produto"
                }, 0);

        tblListagemConsultas.setModel(modeloTabelaConsultas);
        scpListagemConsultas.setViewportView(tblListagemConsultas);

        posicionador = new GridBagConstraints();
        posicionador.gridy = 1;
        posicionador.gridx = 3;
        posicionador.gridheight = 5;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastraisConsulta.add(scpListagemConsultas,posicionador);

        pnlProdutos.add(pnlDadosCadastraisConsulta,borderLayout.CENTER);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(btnGravar.getActionCommand())) {
            Receita r = getReceitaByForm();

            if(r != null){

                try {

                    pnlReceita.getControle().getConexaoJDBC().persist(r);

                    JOptionPane.showMessageDialog(this, "Receita armazenada!", "Salvar", JOptionPane.INFORMATION_MESSAGE);

                    pnlReceita.showTela("tela_receita_listagem");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar Receita! : "+ex.getMessage(), "Salvar", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

            }else{

                JOptionPane.showMessageDialog(this, "Preencha o formulário corretamente!", "Edição", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (e.getActionCommand().equals(btnCancelar.getActionCommand())) {
            pnlReceita.showTela("tela_receita_listagem");
        } else if (e.getActionCommand().equals(btnRight.getActionCommand())) {
            changeTableRowsHandler(tblListagemConsultasBD, tblListagemConsultas);

        } else if (e.getActionCommand().equals(btnLeft.getActionCommand())) {
            changeTableRowsHandler(tblListagemConsultas, tblListagemConsultasBD);
        }
    }


}
