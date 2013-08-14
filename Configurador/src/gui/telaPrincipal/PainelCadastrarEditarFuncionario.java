package gui.telaPrincipal;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import configurador.Funcionario;
import configurador.Vaga;
import dao.FuncionarioDao;
import dao.VagasDao;
import gui.componentes.BotaoPadrao;
import gui.componentes.LabelPadrao;
import gui.componentes.TxtFieldGrande;
import gui.componentes.TxtFieldMedio;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;
import utils.MedidasPadroes;

/**
 *
 * @author Felps
 */
public class PainelCadastrarEditarFuncionario extends JPanel{
    
    private boolean isProprietario;
    
    private Funcionario editarFunc;
    
    private TelaPrincipal telaPrincipal;
    
    private TxtFieldGrande txtCodigo;
    private TxtFieldGrande txtNome;
    private TxtFieldGrande txtEmail;
    private TxtFieldMedio txtLogin;
    private JPasswordField txtSenha;;
    private JPasswordField txtSenha2;
    private JFormattedTextField txtCelular;
    
    private JComboBox comboVagas;
    private JComboBox comboTipo;
    
    private BotaoPadrao btnSalvar;
    private BotaoPadrao btnCancelar;
    
    private JPanel painelUsuarioSenha;
    
    private int tipoFuncionario = Funcionario.TIPO_NORMAL;
    
    public static final int MODO_CADASTRAR = 0;
    public static final int MODO_EDITAR = 1;
    
    private int modo = 0;
    
    public PainelCadastrarEditarFuncionario(TelaPrincipal pai) {
        telaPrincipal = pai;
        modo = MODO_CADASTRAR;
        isProprietario = TelaPrincipal.func.getTipo() == Funcionario.TIPO_PROPRIETARIO ? true : false;
        init();
    }
    
    public PainelCadastrarEditarFuncionario(TelaPrincipal pai, Funcionario func) {
        telaPrincipal = pai;
        editarFunc = func;
        modo = MODO_EDITAR;
        isProprietario = TelaPrincipal.func.getTipo() == Funcionario.TIPO_PROPRIETARIO ? true : false;
        init();
    }
    
    public void init() {
        FormLayout layout = new FormLayout("30dlu, pref",
                                           "80dlu, pref, 7dlu, pref, 30dlu, pref");
        CellConstraints cc = new CellConstraints();
        setLayout(layout);
        setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        add(getPainelGeral(), cc.xy(2, 2));
        add(getLinha4(), cc.xy(2, 4));
        add(getBtns(), cc.xy(2, 6));
    }
    
    private JPanel getPainelGeral() {
        FormLayout layout = new FormLayout("0dlu, pref",
                                           "0dlu, pref, 7dlu, pref, 7dlu, pref, 3dlu");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        
        builder.add(getLinha1(), cc.xy(2, 2));
        builder.add(getLinha2(), cc.xy(2, 4));
        builder.add(getLinha3(), cc.xy(2, 6));
        builder.setBorder(BorderFactory.createTitledBorder("Informações Gerais"));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getLinha1() {
        FormLayout layout = new FormLayout("pref, 100dlu, pref",
                                           "pref, 2dlu, pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        
        builder.add(new LabelPadrao("Código:"), cc.xy(1, 1));
        builder.add(getTxtCodigo(), cc.xy(1, 3));
        builder.add(new LabelPadrao("Nome:"), cc.xy(3, 1));
        builder.add(getTxtNome(), cc.xy(3, 3));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    private JPanel getLinha2() {
        FormLayout layout = new FormLayout("pref, 100dlu, pref",
                                           "pref, 2dlu, pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        
        builder.add(new LabelPadrao("Email:"), cc.xy(1, 1));
        builder.add(getTxtEmail(), cc.xy(1, 3));
        builder.add(new LabelPadrao("Celular"), cc.xy(3, 1));
        builder.add(getTxtCelular(), cc.xy(3, 3));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getLinha3() {
        FormLayout layout = new FormLayout("pref, 150dlu, pref",
                                           "pref, 2dlu, pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        
        builder.add(new LabelPadrao("Reservar Vaga:"), cc.xy(1, 1));
        builder.add(getComboVagas(), cc.xy(1, 3));
        builder.add(getLblTipo(), cc.xy(3, 1));
        builder.add(getComboTipo(), cc.xy(3, 3));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getLinha4() {
        FormLayout layout = new FormLayout("pref, 5dlu, pref",
                                           "0, pref, 2dlu, pref, 3dlu, pref, 2dlu, pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        
        builder.add(new LabelPadrao("Login:"), cc.xy(1, 2));
        builder.add(getTxtLogin(), cc.xy(1, 4));
        builder.add(new LabelPadrao("Senha:"), cc.xy(1, 6));
        builder.add(getTxtSenha(), cc.xy(1, 8));
        builder.add(new LabelPadrao("Confirmar Senha:"), cc.xy(3, 6));
        builder.add(getTxtSenha2(), cc.xy(3, 8));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        builder.setBorder(BorderFactory.createTitledBorder("Login e Senha do Administrador"));
        painelUsuarioSenha = builder.getPanel();
        painelUsuarioSenha.setVisible(isProprietario);
        
        if (modo == MODO_EDITAR) {
            if (!isProprietario) {
                painelUsuarioSenha.setVisible(false);
            } else {
                if (editarFunc.getTipo() == Funcionario.TIPO_ADMINISTRADOR) {
                    painelUsuarioSenha.setVisible(true);
                } else {
                    painelUsuarioSenha.setVisible(false);
                }
            }
        }
        
        return painelUsuarioSenha;
    }
    
    private JPasswordField getTxtSenha2() {
        if (txtSenha2 == null) {
            txtSenha2 = new JPasswordField();
            txtSenha2.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
            if (modo == MODO_EDITAR) {
                txtSenha2.setText(editarFunc.getSenha());
            }
            return txtSenha2;
        } else {
            return txtSenha2;
        }
    }
    
    private JPasswordField getTxtSenha() {
        if (txtSenha == null) {
            txtSenha = new JPasswordField();
            txtSenha.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
            if (modo == MODO_EDITAR) {
                txtSenha.setText(editarFunc.getSenha());
            }
            return txtSenha;
        } else {
            return txtSenha;
        }
    }
    
    private TxtFieldMedio getTxtLogin() {
        if (txtLogin == null) {
            txtLogin = new TxtFieldMedio();
            if (modo == MODO_EDITAR) {
                txtLogin.setText(editarFunc.getLogin());
            }
            return txtLogin;
        } else {
            return txtLogin;
        }
    }
    
    private LabelPadrao getLblTipo() {
        LabelPadrao lbl = new LabelPadrao("Tipo:");
        lbl.setVisible(isProprietario);
        return lbl;
    }
    
    private JComboBox getComboTipo() {
        if (comboTipo == null) {
            comboTipo = new JComboBox();
            comboTipo.addItem("Administrador");
            comboTipo.addItem("Normal");
            comboTipo.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (comboTipo.getSelectedIndex() == 0) {
                        painelUsuarioSenha.setVisible(true);
                        tipoFuncionario = Funcionario.TIPO_ADMINISTRADOR;
                    } else {
                        painelUsuarioSenha.setVisible(false);
                        tipoFuncionario = Funcionario.TIPO_NORMAL;
                    }
                }
            });
            comboTipo.setVisible(isProprietario);
            tipoFuncionario = isProprietario == true ? Funcionario.TIPO_ADMINISTRADOR : tipoFuncionario;
            if ( modo == MODO_EDITAR) {
                comboTipo.setEnabled(false);
            }
            return comboTipo;
        } else {
            return comboTipo;
        }
    }
    
    private JComboBox getComboVagas() {
        if (comboVagas == null) {
            comboVagas = new JComboBox();
            comboVagas.addItem("Não Reservar");
            
            for (Vaga v : VagasDao.selectVagas()) {
                if (v.getReservadaPara() == null || "".equals(v.getReservadaPara())) {
                    comboVagas.addItem(v.getNumero());
                } 
                
                if (modo == MODO_EDITAR) {
	                if (v.getReservadaPara() != null && v.getReservadaPara().equals(editarFunc.getCodigo())) {
	                	comboVagas.addItem(v.getNumero());
	                	comboVagas.setSelectedItem(v.getNumero());
	                }
                }
            }
            
            return comboVagas;
        } else {
            return comboVagas;
        }
    }
    
    private JFormattedTextField getTxtCelular() {
        if (txtCelular == null) {
            try {
                MaskFormatter mask = new MaskFormatter("(##)####-####");
                mask.setValueContainsLiteralCharacters(false);
                txtCelular = new JFormattedTextField(mask);
                txtCelular.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
                if (modo == MODO_EDITAR) {
                    txtCelular.setValue(editarFunc.getCelular());
                }
                return txtCelular;
            } catch (ParseException ex) {
                return null;
            }
        } else {
            return txtCelular;
        }
    }
    
    private TxtFieldGrande getTxtEmail() {
        if (txtEmail == null) {
            txtEmail = new TxtFieldGrande();
            if (modo == MODO_EDITAR) {
                txtEmail.setText(editarFunc.getEmail());
            }
            return txtEmail;
        } else {
            return txtEmail;
        }
    }
    
    
    private JPanel getBtns() {
        FormLayout layout = new FormLayout("250dlu, pref, 3dlu, pref",
                                           "pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);        
        
        builder.add(getBtnSalvar(), cc.xy(2, 1));
        builder.add(getBtnCancelar(), cc.xy(4, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private TxtFieldGrande getTxtNome() {
        if (txtNome == null) {
            txtNome = new TxtFieldGrande();
            if (modo == MODO_EDITAR) {
                txtNome.setText(editarFunc.getNome());
            }
            return txtNome;
        } else {
            return txtNome;
        }
    }
    
    private TxtFieldGrande getTxtCodigo() {
        if (txtCodigo == null) {
            txtCodigo = new TxtFieldGrande();
            if (modo == MODO_EDITAR) {
                txtCodigo.setText(editarFunc.getCodigo());
                txtCodigo.setEnabled(false);
            }
            return txtCodigo;
        } else {
            return txtCodigo;
        }
    }
    
    private BotaoPadrao getBtnCancelar() {
        if (btnCancelar == null) {
            btnCancelar = new BotaoPadrao("Cancelar");
            btnCancelar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    telaPrincipal.setPainel(PainelCadastrarEditarFuncionario.this,
                            new ConsultarFuncionario(telaPrincipal), 
                            "consultar");
                }
            });
            return btnCancelar;
        } else {
            return btnCancelar;
        }
    }
    
    private BotaoPadrao getBtnSalvar() {
        if (btnSalvar == null) {
            btnSalvar = new BotaoPadrao("Salvar");
            btnSalvar.addActionListener(new AcaoSalvar());
            return btnSalvar;
        } else {
            return btnSalvar;
        }
    }
    
    private class AcaoSalvar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!verificaCampos()) {
                return;
            }
            if (modo == MODO_CADASTRAR) {
                Funcionario f = new Funcionario(txtCodigo.getText(), 
                        txtNome.getText(), txtEmail.getText(), txtCelular.getValue().toString(), tipoFuncionario);

                if (isProprietario) {
                    if (comboTipo.getSelectedIndex() == 0) {
                        f.setLogin(txtLogin.getText());
                        f.setSenha(new String(txtSenha.getPassword()));
                    }
                }

                FuncionarioDao.insere(f);
                
                if (comboVagas.getSelectedIndex() != 0) {
                	VagasDao.reservaVaga((Integer)comboVagas.getSelectedItem(), f.getCodigo());
                }
                
                JOptionPane.showMessageDialog(PainelCadastrarEditarFuncionario.this, 
                        "Funcionário Adicionado com Sucesso!!", 
                        "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
                
                telaPrincipal.setPainel(PainelCadastrarEditarFuncionario.this, 
                        new ConsultarFuncionario(telaPrincipal), 
                        "consultar");
            } else if (modo == MODO_EDITAR) {
                editarFunc.setCelular(txtCelular.getValue().toString());
                editarFunc.setEmail(txtEmail.getText());
                editarFunc.setNome(txtNome.getText());
                editarFunc.setCodigo(txtCodigo.getText());
                if (editarFunc.getTipo() == Funcionario.TIPO_ADMINISTRADOR
                    || editarFunc.getTipo() == Funcionario.TIPO_PROPRIETARIO) {
                    editarFunc.setLogin(txtLogin.getText());
                    editarFunc.setSenha(new String(txtSenha.getPassword()));
                }
                
                FuncionarioDao.atualiza(editarFunc);
                
                Integer vg = comboVagas.getSelectedIndex() == 0 ? 0 : (Integer)comboVagas.getSelectedItem();
                VagasDao.atualizarReservaVaga(vg, editarFunc.getCodigo());
                
                JOptionPane.showMessageDialog(PainelCadastrarEditarFuncionario.this, 
                        "Funcionário Atualizado com Sucesso!!", 
                        "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
                
                telaPrincipal.setPainel(PainelCadastrarEditarFuncionario.this, 
                        new ConsultarFuncionario(telaPrincipal), 
                        "consultar");
            }
        }
        
    }
    public boolean verificaCampos() {
        
        return true;
    }
}
