package gui.telaPrincipal;

import gui.modelos.FuncionarioTableModel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import utils.MedidasPadroes;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import configurador.Funcionario;
import dao.FuncionarioDao;

/**
 *
 * @author Felps
 */
public class ConsultarFuncionario extends JPanel{
    
    private TelaPrincipal telaPrincipal;
    
    private JTable tabelaFuncionarios;
    
    private JTextField txtBuscarFuncionario;
    
    private JButton btnCadastrar;
    private JButton btnExcluir;
    private JButton btnEditar;
    private JButton btnCancelar;
    
    private List<Funcionario> funcionariosCadastrados;
    
    boolean isProprietario;
    
    private FuncionarioTableModel model;
    
    public ConsultarFuncionario(TelaPrincipal pai) {
        telaPrincipal = pai;
        isProprietario = TelaPrincipal.func.getTipo() == Funcionario.TIPO_PROPRIETARIO ? true : false;
        init();
    }
    
    private void init() {
        FormLayout layout = new FormLayout("30dlu, pref",
                                           "80dlu, pref, 3dlu, pref, 3dlu, pref");
        CellConstraints cc = new CellConstraints();
        setLayout(layout);
        setBackground(MedidasPadroes.COR_DE_FUNDO);
        JScrollPane scroll = new JScrollPane(getTabelaFuncionarios());
        scroll.setPreferredSize(new Dimension(720, 300));
        
        
        add(getPainelBusca(), cc.xy(2, 2));
        add(scroll, cc.xy(2, 4));
        add(getPainelBtns(), cc.xy(2, 6));
    }
    
    private JPanel getPainelBtns() {
        FormLayout layout = new FormLayout("265dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref",
                                           "pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        
        builder.add(getBtnCadastrar(), cc.xy(2, 1));
        builder.add(getBtnEditar(), cc.xy(4, 1));
        builder.add(getBtnExcluir(), cc.xy(6, 1));
        builder.add(getBtnCancelar(), cc.xy(8, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getPainelBusca() {
        FormLayout layout = new FormLayout("pref",
                                           "pref, 3dlu, pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        
        JLabel lbl = new JLabel("Digite o nome do funcionário:");
        lbl.setFont(MedidasPadroes.FONTE_LABELS);
        lbl.setForeground(MedidasPadroes.COR_LABEL);
        
        builder.add(lbl, cc.xy(1, 1));
        builder.add(getTxtBuscarFuncionario(), cc.xy(1, 3));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        
        return builder.getPanel();
    }
    
    private JButton getBtnCadastrar() {
        if (btnCadastrar == null) {
            btnCadastrar = new JButton("Cadastrar");
            btnCadastrar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    tabelaFuncionarios.clearSelection();
                    telaPrincipal.setPainel(ConsultarFuncionario.this, 
                            new PainelCadastrarEditarFuncionario(telaPrincipal), 
                            "cadastrarCliente");
                }
            });
            return btnCadastrar;
        } else {
            return btnCadastrar;
        }
    }
    
    private JButton getBtnEditar() {
        if (btnEditar == null) {
            btnEditar = new JButton("Editar");
            btnEditar.setEnabled(false);
            btnEditar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String codigo = tabelaFuncionarios.getModel().getValueAt(
                                            tabelaFuncionarios.getSelectedRow(), 0).toString();
                    
                    tabelaFuncionarios.clearSelection();
                    telaPrincipal.setPainel(ConsultarFuncionario.this, 
                            new PainelCadastrarEditarFuncionario(telaPrincipal, 
                                                                FuncionarioDao.selectPorCodigo(codigo)), 
                            "cadastrarCliente");
                }
            });
            return btnEditar;
        } else {
            return btnEditar;
        }
    }
    
    private JButton getBtnCancelar() {
        if (btnCancelar == null) {
            btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    telaPrincipal.setPainelPrincipal(ConsultarFuncionario.this);
                }
            });
            return btnCancelar;
        } else {
            return btnCancelar;
        }
    }
    
    private JButton getBtnExcluir() {
        if (btnExcluir == null) {
            btnExcluir = new JButton("Excluir");
            btnExcluir.setEnabled(false);
            btnExcluir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<Integer> linhas = new ArrayList<Integer>();
                    for (int i = 0; i < tabelaFuncionarios.getSelectedRowCount(); i++) { 
                        String tipo = tabelaFuncionarios.getModel().getValueAt(
                                tabelaFuncionarios.getSelectedRows()[i], 4).toString();
                        if (!tipo.equalsIgnoreCase("proprietário")) {
                            String codigo = tabelaFuncionarios.getModel().getValueAt(
                                    tabelaFuncionarios.getSelectedRows()[i], 0).toString();
                            
                            FuncionarioDao.apaga(codigo);
                            linhas.add(tabelaFuncionarios.getSelectedRows()[i]);
                        }
                    }
                    model.remove(linhas);
                    JOptionPane.showMessageDialog(ConsultarFuncionario.this, 
                            "Funcionário(s) removido(s) com sucesso!", "Sucesso", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });
            return btnExcluir;
        } else {
            return btnExcluir;
        }
    }
    
    private JTextField getTxtBuscarFuncionario() {
        if (txtBuscarFuncionario == null) {
            txtBuscarFuncionario = new JTextField();
            txtBuscarFuncionario.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_GRANDE);
            txtBuscarFuncionario.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    atualizaTabelaFuncionarios(txtBuscarFuncionario.getText().toLowerCase());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    atualizaTabelaFuncionarios(txtBuscarFuncionario.getText().toLowerCase());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    atualizaTabelaFuncionarios(txtBuscarFuncionario.getText().toLowerCase());
                }
            });
            return txtBuscarFuncionario;
        } else {
            return txtBuscarFuncionario;
        }
    }
    
    private JTable getTabelaFuncionarios() {
        if (tabelaFuncionarios == null) {  
            tabelaFuncionarios = new JTable(); 
            model = new FuncionarioTableModel(tabelaFuncionarios);
            funcionariosCadastrados = FuncionarioDao.selectTodosFuncionarios();
            model.adicionaFuncionarios(funcionariosCadastrados);
            tabelaFuncionarios.setModel(model);
            if (!isProprietario) {
                model.remove(TelaPrincipal.func.getNome());
            }
            tabelaFuncionarios.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    comportamentoSelecao();
//                        if (tabelaFuncionarios.getModel().getValueAt(i, 4).toString()
//                                .equalsIgnoreCase("proprietário")) {
//                            tabelaFuncionarios.removeRowSelectionInterval(i, i);
//                        }
                }
                
                @Override
                public void mouseReleased(MouseEvent evt) {
                    comportamentoSelecao();
                }
            });
            
            tabelaFuncionarios.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent evt) {
                    if (evt.getKeyCode() != 17) {
                        comportamentoSelecao();
                    }
                }
                
                @Override
                public void keyReleased(KeyEvent evt) {
                    if (evt.getKeyCode() != 17) {
                        comportamentoSelecao();
                    }
                }
            });
            
            model.addTableModelListener(new TableModelListener() {

                @Override
                public void tableChanged(TableModelEvent e) {
                    tabelaFuncionarios.clearSelection();
                    btnEditar.setEnabled(false);
                    btnExcluir.setEnabled(false);                    
                }
            });
            
            //tabelaFuncionarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            return tabelaFuncionarios;
        } else {
            return tabelaFuncionarios;
        }
    }
    
    private void comportamentoSelecao() {
        
            int[] linhasSelecionadas = tabelaFuncionarios.getSelectedRows();
            for (int i : linhasSelecionadas) {
                if (tabelaFuncionarios.getModel().getValueAt(i, 4).toString().equalsIgnoreCase("proprietário")) {
                    tabelaFuncionarios.removeRowSelectionInterval(i, i);
                }
                if (!isProprietario) {
                    if (tabelaFuncionarios.getModel().getValueAt(i, 4).toString().equalsIgnoreCase("administrador")) {
                        tabelaFuncionarios.removeRowSelectionInterval(i, i);
                    }
            }
        }
        if (tabelaFuncionarios.getSelectedRowCount() == 0) {
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);
        } else if (tabelaFuncionarios.getSelectedRowCount() == 1) {
            btnEditar.setEnabled(true);
            btnExcluir.setEnabled(true);
        } else {
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(true);
        }
    }
    
    private void atualizaTabelaFuncionarios(String nome) {
        List<Funcionario> f = FuncionarioDao.selectLike(nome);
        model.removeAll();
        
        for (Funcionario fun : f) {
            if (!fun.getNome().toLowerCase().contains(nome)) {
                f.remove(fun);
            }
        }
        model.adicionaFuncionarios(f);
    }
}
