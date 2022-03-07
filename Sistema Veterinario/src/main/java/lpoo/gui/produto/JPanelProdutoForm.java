package lpoo.gui.produto;

import lpoo.Controle;
import lpoo.model.Fornecedor;
import lpoo.model.Produto;
import lpoo.model.TipoProduto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class JPanelProdutoForm extends JPanel implements ActionListener {
    private Controle controle;

    private JPanel pnlSul;
    private JPanel pnlDadosCadastrais;
    private JPanelProduto pnlProduto;

    private JTextField nomeField;
    private JLabel nomeLabel;

    private JComboBox cbxTipoProdutos;
    private JLabel tipoProdutoLabel;

    private JComboBox cbxFornecedores;
    private JLabel fornecedoresLabel;

    private JFormattedTextField valorField;
    private JLabel valorLabel;

    private JFormattedTextField quantidadeField;
    private JLabel quantidadeLabel;


    private JButton btnGravar;
    private JButton btnCancelar;

    private BorderLayout borderLayout;
    private GridBagLayout gridBagLayoutDadosCadastrais;

    private SimpleDateFormat formato;

    private Produto produto;

    public JPanelProdutoForm(JPanelProduto pnlProduto, Controle controle){
        this.controle = controle;
        this.pnlProduto = pnlProduto;
        initComponents();
    }

    public void populaComboTipoProduto(){
        cbxTipoProdutos.removeAllItems();
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbxTipoProdutos.getModel();

        model.addElement("Selecione");

        for(TipoProduto tipoProduto : TipoProduto.values()){
            model.addElement(tipoProduto.toString());
        }
    }

    public void populaComboFornecedores(){
        cbxFornecedores.removeAllItems();

        DefaultComboBoxModel model =  (DefaultComboBoxModel) cbxFornecedores.getModel();

        model.addElement("Selecione");
        try {

            List<Fornecedor> listFornecedor = controle.getConexaoJDBC().findAll(Fornecedor.class);
            for(Fornecedor fornecedor : listFornecedor){
                model.addElement(fornecedor);
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Erro ao listar Fornecedores -:"+ex.getLocalizedMessage(), "Fornecedores", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public Produto getProdutoByForm(){
        if( nomeField.getText().trim().length() > 3 &&
            valorField.getText().trim().length() > 0 &&
            quantidadeField.getText().trim().length() > 0 &&
            cbxTipoProdutos.getSelectedIndex() != 0 &&
            cbxFornecedores.getSelectedIndex() != 0)
        {
            Produto p = new Produto();
            p.setNome(nomeField.getText().trim());
            try{
                p.setQuantidade(Float.parseFloat(quantidadeField.getText().trim()));
                p.setValor(Float.parseFloat(valorField.getText().trim()));
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(this, "Os campos quantidade e valor devem conter um numero! ", "Erro", JOptionPane.ERROR_MESSAGE);
            }

            p.setTipoProduto(TipoProduto.getTipoProduto(cbxTipoProdutos.getSelectedItem().toString()));
            p.setFornecedor((Fornecedor) cbxFornecedores.getSelectedItem());
            if(produto != null){
                p.setId(produto.getId());
            }

            return p;

        }

        return null;
    }
    public void setProdutoForm(Produto p){
        if(p == null){
            nomeField.setText("");
            valorField.setText("");
            quantidadeField.setText("");
            cbxFornecedores.setSelectedIndex(0);
            cbxTipoProdutos.setSelectedIndex(0);
            produto = null;
        }else{
            produto = p;
            nomeField.setText(p.getNome());
            valorField.setText(p.getValor().toString());
            quantidadeField.setText(p.getQuantidade().toString());
            cbxFornecedores.getModel().setSelectedItem(p.getFornecedor());
            cbxTipoProdutos.getModel().setSelectedItem(p.getTipoProduto().toString());
        }

    }

    public void initComponents(){
        borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        pnlDadosCadastrais = new JPanel();
        this.add(pnlDadosCadastrais, BorderLayout.CENTER);

        gridBagLayoutDadosCadastrais = new GridBagLayout();
        pnlDadosCadastrais.setLayout(gridBagLayoutDadosCadastrais);

        //NOME-----------------------------------------------------------
        nomeLabel = new JLabel("Nome:");
        GridBagConstraints posicionador = new GridBagConstraints();
        posicionador.gridy = 0;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(nomeLabel,posicionador);

        nomeField = new JTextField(20);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 0;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(nomeField,posicionador);
        //VALOR-----------------------------------------------------------
        valorLabel = new JLabel("Valor:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 1;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(valorLabel,posicionador);

        valorField = new JFormattedTextField();
        valorField.setColumns(5);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 1;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(valorField,posicionador);
        //QUANTIDADE-----------------------------------------------------------
        quantidadeLabel = new JLabel("Quantidade:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 2;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(quantidadeLabel,posicionador);

        quantidadeField = new JFormattedTextField();
        quantidadeField.setColumns(5);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 2;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(quantidadeField,posicionador);
        //TIPO PRODUTOS---------------------------------------------------------
        tipoProdutoLabel = new JLabel("Tipo Produto:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 3;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(tipoProdutoLabel,posicionador);

        cbxTipoProdutos = new JComboBox();
        posicionador = new GridBagConstraints();
        posicionador.gridy = 3;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(cbxTipoProdutos,posicionador);
        //FORNCEDORES---------------------------------------------------------
        fornecedoresLabel = new JLabel("Forncedores:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 4;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(fornecedoresLabel,posicionador);

        cbxFornecedores = new JComboBox();
        posicionador = new GridBagConstraints();
        posicionador.gridy = 4;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(cbxFornecedores,posicionador);

        pnlSul = new JPanel();
        pnlSul.setLayout(new FlowLayout());

        //BOTAO GRAVAR-----------------------------------------------------------
        btnGravar = new JButton("Gravar");
        btnGravar.addActionListener(this);
        btnGravar.setFocusable(true);
        btnGravar.setToolTipText("btnGravarProduto");
        btnGravar.setMnemonic(KeyEvent.VK_G);
        btnGravar.setActionCommand("botao_gravar_formulario_produto");
        pnlSul.add(btnGravar);

        //BOTAO CANCELAR---------------------------------------------------------
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(this);
        btnCancelar.setFocusable(true);
        btnCancelar.setToolTipText("btnCancelarProduto");
        btnCancelar.setActionCommand("botao_cancelar_formulario_produto");
        pnlSul.add(btnCancelar);

        this.add(pnlSul,BorderLayout.SOUTH);

        formato = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(btnGravar.getActionCommand())){

            Produto p = getProdutoByForm();

            if(p != null){

                try {

                    pnlProduto.getControle().getConexaoJDBC().persist(p);

                    JOptionPane.showMessageDialog(this, "Produto armazenado!", "Salvar", JOptionPane.INFORMATION_MESSAGE);

                    pnlProduto.showTela("tela_produto_listagem");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar Produto! : "+ex.getMessage(), "Salvar", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

            }else{

                JOptionPane.showMessageDialog(this, "Preencha o formulário corretamente!", "Edição", JOptionPane.INFORMATION_MESSAGE);
            }


        }else if(e.getActionCommand().equals(btnCancelar.getActionCommand())){


            pnlProduto.showTela("tela_produto_listagem");

        }
    }
}
