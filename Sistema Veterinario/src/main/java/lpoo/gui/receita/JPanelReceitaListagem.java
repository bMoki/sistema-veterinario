package lpoo.gui.receita;

import lpoo.Controle;
import lpoo.model.Receita;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class JPanelReceitaListagem extends JPanel implements ActionListener {
    private JPanelReceita pnlReceita;
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

    public JPanelReceitaListagem(JPanelReceita pnlReceita, Controle controle){
        this.pnlReceita = pnlReceita;
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
                        "ID", "Observação", "Consulta"
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
            List<Receita> listReceita = controle.getConexaoJDBC().findAll(Receita.class,filter);

            setListTable(listReceita);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar Receitas -:"+ex.getLocalizedMessage(), "Receitas", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void populaTable(){
        populaTable("");
    }

    private void setListTable(List<Receita> listReceita){
        DefaultTableModel model =  (DefaultTableModel) tblListagem.getModel();
        model.setRowCount(0);

        for(Receita receita : listReceita){
            model.addRow(new Object[]{receita, receita.getOrientacao(),receita.getConsulta()});
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(btnNovo.getActionCommand())){
            pnlReceita.showTela("tela_receita_form");
            pnlReceita.getFormulario().setReceitaForm(null);
            pnlReceita.getFormulario().populaTabelaProdutosBD();
        }else if(e.getActionCommand().equals(btnAlterar.getActionCommand())){
            int index = tblListagem.getSelectedRow();
            if(index >-1){
                DefaultTableModel model = (DefaultTableModel) tblListagem.getModel();
                Vector linha = (Vector) model.getDataVector().get(index);

                Receita receita = (Receita) linha.get(0);

                pnlReceita.showTela("tela_receita_form");
                pnlReceita.getFormulario().setReceitaForm(receita);
            }else{
                JOptionPane.showMessageDialog(this, "Selecione uma linha para editar!", "Edição", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(e.getActionCommand().equals(btnRemover.getActionCommand())){
            int index = tblListagem.getSelectedRow();
            if(index >- 1){
                DefaultTableModel model = (DefaultTableModel) tblListagem.getModel();
                Vector linha = (Vector) model.getDataVector().get(index);
                Receita receita = (Receita) linha.get(0);

                try {
                    pnlReceita.getControle().getConexaoJDBC().remover(receita);
                    JOptionPane.showMessageDialog(this, "Receita removida!", "Deletar", JOptionPane.INFORMATION_MESSAGE);
                    pnlReceita.showTela("tela_receita_listagem");
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(this, "Erro ao remover Receita -:"+ex.getLocalizedMessage(), "Receitas", JOptionPane.ERROR_MESSAGE);
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

