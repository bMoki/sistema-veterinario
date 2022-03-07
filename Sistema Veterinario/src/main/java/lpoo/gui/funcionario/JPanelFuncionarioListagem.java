package lpoo.gui.funcionario;

import lpoo.Controle;
import lpoo.model.Funcionario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class JPanelFuncionarioListagem extends JPanel implements ActionListener {

    private JPanelFuncionario pnlFuncionario;
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


    public JPanelFuncionarioListagem(JPanelFuncionario pnlFuncionario, Controle controle){
        this.pnlFuncionario = pnlFuncionario;
        this.controle = controle;

        initComponents();
    }

    public void populaTable(String filter){

        try {
            List<Funcionario> listFuncionarios = controle.getConexaoJDBC().findAll(Funcionario.class,filter);

            setListTable(listFuncionarios);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Erro ao listar Funcionarios -:"+ex.getLocalizedMessage(), "Funcionarios", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    public void populaTable(){
        populaTable("");
    }

    private void setListTable(List<Funcionario> listFuncionarios){
        DefaultTableModel model =  (DefaultTableModel) tblListagem.getModel();
        model.setRowCount(0);

        for(Funcionario funcionario : listFuncionarios){
            model.addRow(new Object[]{funcionario, funcionario.getNome(), funcionario.getEmail(), funcionario.getCargo(),
                    funcionario.getNumero_ctps(), funcionario.getNumero_pis()});
        }

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
                        "CPF", "Nome", "Email", "Cargo", "CTPS", "PIS"
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


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(btnNovo.getActionCommand())){
            pnlFuncionario.showTela("tela_funcionario_form");
            pnlFuncionario.getFormulario().setFuncionarioForm(null);
            pnlFuncionario.getFormulario().populaAgendas(new ArrayList<>());
            pnlFuncionario.getFormulario().populaVendas(new ArrayList<>());
        }else if(e.getActionCommand().equals(btnAlterar.getActionCommand())){
            int index = tblListagem.getSelectedRow();
            if(index >-1){
                DefaultTableModel model = (DefaultTableModel) tblListagem.getModel();
                Vector linha = (Vector) model.getDataVector().get(index);

                Funcionario funcionario = (Funcionario) linha.get(0);

                pnlFuncionario.showTela("tela_funcionario_form");
                pnlFuncionario.getFormulario().setFuncionarioForm(funcionario);
            }else{
                JOptionPane.showMessageDialog(this, "Selecione uma linha para editar!", "Edição", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(e.getActionCommand().equals(btnRemover.getActionCommand())){
            int index = tblListagem.getSelectedRow();
            if(index >- 1){
                DefaultTableModel model = (DefaultTableModel) tblListagem.getModel();
                Vector linha = (Vector) model.getDataVector().get(index);
                Funcionario funcionario = (Funcionario) linha.get(0);

                try {
                    pnlFuncionario.getControle().getConexaoJDBC().remover(funcionario);
                    JOptionPane.showMessageDialog(this, "Funcionario removido!", "Deletar", JOptionPane.INFORMATION_MESSAGE);
                    pnlFuncionario.showTela("tela_funcionario_listagem");
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(this, "Erro ao remover Funcionario -:"+ex.getLocalizedMessage(), "Funcionarios", JOptionPane.ERROR_MESSAGE);
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
