package lpoo.gui.funcionario;

import lpoo.Controle;
import lpoo.model.Agenda;
import lpoo.model.Cargo;
import lpoo.model.Funcionario;
import lpoo.model.Venda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class JPanelFuncionarioForm extends JPanel implements ActionListener {
    private Controle controle;

    private JPanel pnlSul;
    private JPanel pnlDadosCadastrais;
    private JPanel pnlDadosVendas;
    private JPanel pnlDadosAgendas;
    private JPanelFuncionario pnlFuncionario;


    private JButton btnGravar;
    private JButton btnCancelar;

    private JTextField nomeField;
    private JTextField emailField;
    private JFormattedTextField cpfField;
    private JFormattedTextField rgField;
    private JFormattedTextField cepField;
    private JTextField enderecoField;
    private JFormattedTextField ctpsField;
    private JFormattedTextField pisField;
    private JPasswordField senhaField;
    private JFormattedTextField dataNascimentoField;
    private JFormattedTextField numeroCelularField;
    private JTextField complementoField;

    private JLabel CPFLabel;
    private JLabel nomeLabel;
    private JLabel emailLabel;
    private JLabel RGLabel;
    private JLabel CEPLabel;
    private JLabel enderecoLabel;
    private JLabel ctpsLabel;
    private JLabel pisLabel;
    private JComboBox cbxCargos;
    private JLabel cargoLabel;
    private JLabel senhaLabel;
    private JLabel dataNascimentoLabel;
    private JLabel numeroCelularLabel;
    private JLabel complementoLabel;

    private BorderLayout borderLayout;
    private GridBagLayout gridBagLayoutDadosCadastrais;

    private SimpleDateFormat formato;

    private JTabbedPane tbpAbas;

    private JScrollPane scpListagemVendas;
    private JTable tblListagemVendas;
    private DefaultTableModel modeloTabelaVendas;

    private JScrollPane scpListagemAgendas;
    private JTable tblListagemAgendas;
    private DefaultTableModel modeloTabelaAgendas;

    private Funcionario funcionario;

    public JPanelFuncionarioForm(JPanelFuncionario pnlFuncionario, Controle controle){

        this.controle = controle;
        this.pnlFuncionario = pnlFuncionario;
        initComponents();

    }


    public void populaComboCargo(){
        cbxCargos.removeAllItems();
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbxCargos.getModel();

        model.addElement("Selecione");

        for(Cargo cargo : Cargo.values()){
            model.addElement(cargo.toString());
        }
    }
    public void populaVendas(List<Venda> vendas){
        DefaultTableModel model =  (DefaultTableModel) tblListagemVendas.getModel();

        model.setRowCount(0);

        try {
            for(Venda venda : vendas){
                model.addRow(new Object[]{venda, formato.format(venda.getData().getTime()), venda.getValor_total(),venda.getPagamento().toString(), venda.getCliente().getCpf()});
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Erro ao listar Vendas -:"+ex.getLocalizedMessage(), "Vendas", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    public void populaAgendas(List<Agenda> agendas){
        DefaultTableModel model =  (DefaultTableModel) tblListagemAgendas.getModel();

        model.setRowCount(0);

        try {
            for(Agenda agenda : agendas){
                model.addRow(new Object[]{agenda, formato.format(agenda.getData_inicio().getTime()), formato.format(agenda.getData_fim().getTime()),
                        agenda.getObservacao(), agenda.getTipoProduto().toString(), agenda.getMedico().getCpf()});
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Erro ao listar Agendas -:"+ex.getLocalizedMessage(), "Agendas", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public Funcionario getFuncionarioByForm() throws ParseException {
        if( rgField.getText().trim().length() == 10 &&
            nomeField.getText().trim().length() > 3 &&
            emailField.getText().trim().length() > 4 &&
            cpfField.getText().trim().length() == 14 &&
            cbxCargos.getSelectedIndex() > 0 &&
            cepField.getText().trim().length() == 9 &&
            enderecoField.getText().trim().length() > 4 &&
            ctpsField.getText().trim().length() == 7 &&
            pisField.getText().trim().length() == 16 &&
            numeroCelularField.getText().trim().length() == 13 &&
            rgField.getText().trim().length() == 10 &&
            complementoField.getText().trim().length() != 0)
        {
            if(!(new String(senhaField.getPassword()).trim().length() > 3)){
                JOptionPane.showMessageDialog(this, "Senha deve conter mais do que 3 caracteres!", "Salvar", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            Calendar calendar = Calendar.getInstance();

            formato.setLenient(false);
            try {
                calendar.setTime(formato.parse(dataNascimentoField.getText()));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Data incorreta! : " + dataNascimentoField.getText(), "Salvar", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            Funcionario f = new Funcionario();
            f.setNome(nomeField.getText().trim());
            f.setEmail(emailField.getText().trim());
            f.setCpf(cpfField.getText().trim());
            String cargo = cbxCargos.getSelectedItem().toString();
            f.setCargo(Cargo.valueOf(cargo));
            f.setCep(cepField.getText().trim());
            f.setEndereco(enderecoField.getText().trim());
            f.setNumero_ctps(ctpsField.getText().trim());
            f.setNumero_pis(pisField.getText().trim());
            f.setSenha(new String(senhaField.getPassword()).trim());
            f.setData_nascimento(calendar);
            f.setNumero_celular(numeroCelularField.getText().trim());
            f.setRg(rgField.getText().trim());
            f.setComplemento(complementoField.getText().trim());
            if(funcionario != null){
                f.setData_cadastro(funcionario.getData_cadastro());
            }
            return f;

        }
        return null;
    }
    public void setFuncionarioForm(Funcionario f){
        if(f == null){
            nomeField.setText("");
            nomeField.setEditable(true);
            cpfField.setText("");
            cpfField.setEditable(true);
            emailField.setText("");
            rgField.setText("");
            ctpsField.setText("");
            pisField.setText("");
            cepField.setText("");
            enderecoField.setText("");
            cbxCargos.setSelectedIndex(0);
            senhaField.setText("");
            dataNascimentoField.setText("");
            numeroCelularField.setText("");
            complementoField.setText("");

            funcionario = null;
        }else{
            funcionario = f;
            nomeField.setEditable(false);
            cpfField.setEditable(false);
            nomeField.setText(funcionario.getNome());
            cpfField.setText(funcionario.getCpf());
            emailField.setText(funcionario.getEmail());
            rgField.setText(funcionario.getRg());
            ctpsField.setText(funcionario.getNumero_ctps());
            pisField.setText(funcionario.getNumero_pis());
            cepField.setText(funcionario.getCep());
            enderecoField.setText(funcionario.getEndereco());
            cbxCargos.getModel().setSelectedItem(funcionario.getCargo().toString());
            String dt = formato.format(funcionario.getData_nascimento().getTime());
            dataNascimentoField.setText(dt);
            senhaField.setText(funcionario.getSenha());
            numeroCelularField.setText(funcionario.getNumero_celular());
            complementoField.setText(funcionario.getComplemento());


            populaVendas(funcionario.getVendas());
            populaAgendas(funcionario.getAgendas());

        }
    }

    public void initComponents(){
        borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        tbpAbas = new JTabbedPane();
        this.add(tbpAbas, BorderLayout.CENTER);

        pnlDadosCadastrais = new JPanel();
        tbpAbas.add("Dados Cadastrais", pnlDadosCadastrais);
        pnlDadosVendas = new JPanel();
        tbpAbas.add("Vendas",pnlDadosVendas);
        pnlDadosAgendas = new JPanel();
        tbpAbas.add("Agendas",pnlDadosAgendas);

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
        initComponentsVendas();
        initComponentsAgendas();

    }
    private void initComponentsDadosCadastrais(){
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
        //CPF-----------------------------------------------------------
        CPFLabel = new JLabel("CPF:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 1;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(CPFLabel,posicionador);

        cpfField = new JFormattedTextField();
        cpfField.setColumns(10);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 1;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(cpfField,posicionador);
        //EMAIL---------------------------------------------------------
        emailLabel = new JLabel("Email:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 2;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(emailLabel,posicionador);

        emailField = new JTextField(20);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 2;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(emailField,posicionador);
        //RG-------------------------------------------------------------
        RGLabel = new JLabel("RG:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 3;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(RGLabel,posicionador);

        rgField = new JFormattedTextField();
        rgField.setColumns(10);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 3;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(rgField,posicionador);
        //DATA-NASCIMENTO--------------------------------------------------
        dataNascimentoLabel = new JLabel("Data de nascimento:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 4;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(dataNascimentoLabel,posicionador);

        dataNascimentoField = new JFormattedTextField();
        dataNascimentoField.setColumns(7);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 4;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(dataNascimentoField,posicionador);
        //CEP-------------------------------------------------------------
        CEPLabel = new JLabel("CEP:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 5;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(CEPLabel,posicionador);

        cepField = new JFormattedTextField();
        cepField.setColumns(7);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 5;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(cepField,posicionador);
        //ENDEREÇO------------------------------------------------------------
        enderecoLabel = new JLabel("Endereço:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 6;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(enderecoLabel,posicionador);

        enderecoField = new JTextField(20);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 6;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(enderecoField,posicionador);
        //COMPLEMENTO----------------------------------------------------------
        complementoLabel = new JLabel("Complemento:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 7;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(complementoLabel,posicionador);

        complementoField = new JTextField(20);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 7;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(complementoField,posicionador);

        //Numero-CTPS-----------------------------------------------------------
        ctpsLabel = new JLabel("Numero CTPS:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 8;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(ctpsLabel,posicionador);

        ctpsField = new JFormattedTextField();
        ctpsField.setColumns(7);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 8;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(ctpsField,posicionador);
        //Numero-PIS-------------------------------------------------------
        pisLabel = new JLabel("Numero PIS:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 9;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(pisLabel,posicionador);

        pisField = new JFormattedTextField();
        pisField.setColumns(10);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 9;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(pisField,posicionador);
        //Numero-Celualar-------------------------------------------------
        numeroCelularLabel = new JLabel("Numero Celular:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 10;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(numeroCelularLabel,posicionador);

        numeroCelularField = new JFormattedTextField();
        numeroCelularField.setColumns(10);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 10;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(numeroCelularField,posicionador);
        //CARGOS---------------------------------------------------------
        cargoLabel = new JLabel("Cargo:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 11;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(cargoLabel,posicionador);

        cbxCargos = new JComboBox();
        posicionador = new GridBagConstraints();
        posicionador.gridy = 11;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(cbxCargos,posicionador);
        //SENHA----------------------------------------------------------
        senhaLabel = new JLabel("Senha:");
        posicionador = new GridBagConstraints();
        posicionador.gridy = 12;
        posicionador.gridx = 0;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(senhaLabel,posicionador);

        senhaField = new JPasswordField(10);
        posicionador = new GridBagConstraints();
        posicionador.gridy = 12;
        posicionador.gridx = 1;
        posicionador.anchor = GridBagConstraints.LINE_START;
        pnlDadosCadastrais.add(senhaField,posicionador);
        //MASKS-----------------------------------------------------------------
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("##/##/####");
            mask.install(dataNascimentoField);

            mask = new MaskFormatter("###.###.###-##");
            mask.install(cpfField);

            mask = new MaskFormatter("(##)####-####");
            mask.install(numeroCelularField);

            mask = new MaskFormatter("##########");
            mask.install(rgField);

            mask = new MaskFormatter("#####-###");
            mask.install(cepField);

            mask = new MaskFormatter("#######");
            mask.install(ctpsField);

            mask = new MaskFormatter("###.######.##-##");
            mask.install(pisField);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void initComponentsVendas(){
        pnlDadosVendas.setLayout(new BorderLayout());
        scpListagemVendas = new JScrollPane();
        tblListagemVendas = new JTable();

        modeloTabelaVendas = new DefaultTableModel(
                new String[]{
                        "ID","Data", "Valor Total", "Pagamento", "CPF Cliente"
                }, 0);

        tblListagemVendas.setModel(modeloTabelaVendas);
        scpListagemVendas.setViewportView(tblListagemVendas);
        pnlDadosVendas.add(scpListagemVendas,borderLayout.CENTER);
    }
    private void initComponentsAgendas(){
        pnlDadosAgendas.setLayout(new BorderLayout());
        scpListagemAgendas = new JScrollPane();
        tblListagemAgendas = new JTable();

        modeloTabelaAgendas = new DefaultTableModel(
                new String[]{
                        "ID","Data Inicio", "Data Fim", "Observação", "Tipo Produto", "Medico CPF"
                }, 0);

        tblListagemAgendas.setModel(modeloTabelaAgendas);
        scpListagemAgendas.setViewportView(tblListagemAgendas);
        pnlDadosAgendas.add(scpListagemAgendas,borderLayout.CENTER);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(btnGravar.getActionCommand())){

            Funcionario funcionario = null;
            try {
                funcionario = getFuncionarioByForm();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            if(funcionario != null){

                try {

                    pnlFuncionario.getControle().getConexaoJDBC().persist(funcionario);

                    JOptionPane.showMessageDialog(this, "Funcionario armazenado!", "Salvar", JOptionPane.INFORMATION_MESSAGE);

                    pnlFuncionario.showTela("tela_funcionario_listagem");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar Funcionario! : "+ex.getMessage(), "Salvar", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

            }else{

                JOptionPane.showMessageDialog(this, "Preencha o formulário corretamente!", "Edição", JOptionPane.INFORMATION_MESSAGE);
            }


        }else if(e.getActionCommand().equals(btnCancelar.getActionCommand())){


            pnlFuncionario.showTela("tela_funcionario_listagem");

        }
    }
}
