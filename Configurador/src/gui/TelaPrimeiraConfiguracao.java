package gui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import configurador.Funcionario;
import configurador.ServidorSMTP;
import dao.FuncionarioDao;
import dao.ServidorSMTPDao;
import excessoes.ServidorSMTPDaoException;
import gui.componentes.BotaoPadrao;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import utils.Criptografia;
import utils.Email;
import utils.LeitorArquivoConfiguracoes;
import utils.LeitorArquivoServidoresSMTP;
import utils.MedidasPadroes;

/**
 *
 * @author Felps
 */
public class TelaPrimeiraConfiguracao extends JFrame{
    
    // TextFields
    private JTextField txtLogin;
    private JTextField txtNome;
    private JTextField txtEmail; 
    private JTextField txtCodigo;   
    private JTextField txtEmailEnvioAutomatico;
    private JPasswordField txtSenhaLogin;
    private JPasswordField txtSenhaLogin2;
    private JPasswordField txtSenhaEmailEnvioAutomatico;
    private JPasswordField txtSenhaEmailEnvioAutomatico2;
    private JFormattedTextField txtCelular;
    
    // Labels
    private JLabel lblNome = new JLabel("Nome:");
    private JLabel lblCelular = new JLabel("Celular:");
    private JLabel lblEmail = new JLabel("Email:");
    private JLabel lblSenhaEmail = new JLabel("Senha:");
    private JLabel lblConfirmarSenhaEmail = new JLabel("Confirmar Senha:");
    private JLabel lblEmailAutomatico = new JLabel("Email Para Envio Automático:");
    private JLabel lblServidorEmail = new JLabel("Servidor de Email Automático:");
    private JLabel lblLogin = new JLabel("Login:");
    private JLabel lblSenhaLogin = new JLabel("Senha:");
    private JLabel lblConfirmarSenhaLogin = new JLabel("Confirmar Senha:");
    private JLabel lblCodigo = new JLabel("Código:");
    
    // ComboBox para o servidor de email automático
    private JComboBox comboServidoresEmailAutomatico;
    
    // Botões
    private BotaoPadrao btnOK;
    private BotaoPadrao btnCancelar;
    private BotaoPadrao btnTestarServidorSMTP;
    
    private Properties servidoresSMTP;
    
    public TelaPrimeiraConfiguracao() {
        super("Configurações Iniciais");
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("arquivos/configIcone.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(MedidasPadroes.TAMANHO_TELA_PRIMEIRA_CONFIGURACAO);
        setResizable(false);
        setLocationRelativeTo(null);
        init();
        
        
        servidoresSMTP = LeitorArquivoServidoresSMTP.getInstance();
        teste();
        setVisible(true);
    }
    private void teste() {
        txtNome.setText("Felipe");
        txtCelular.setValue("1998419344");
        txtEmail.setText("felipinlineaps@gmail.com");
        txtLogin.setText("felipeaps");
        txtSenhaEmailEnvioAutomatico.setText("89082323");
        txtSenhaEmailEnvioAutomatico2.setText("89082323");
        txtSenhaLogin.setText("230898");
        txtSenhaLogin2.setText("230898");
        comboServidoresEmailAutomatico.setSelectedItem("gmail");
        txtEmailEnvioAutomatico.setEnabled(true);
        txtEmailEnvioAutomatico.setText("felipeaps89@gmail.com");
        btnTestarServidorSMTP.setEnabled(false);
    }
    
    private void montarComboServidoresSMTP() {
        Properties servidoresSMTProperties = LeitorArquivoServidoresSMTP.getInstance();
                
        comboServidoresEmailAutomatico.addItem("Selecione");
        for (String s: servidoresSMTProperties.stringPropertyNames()) {
            comboServidoresEmailAutomatico.addItem(s);
        }
    }
    
    private void init() { 
        JPanel painelFundo = new JPanel();
        painelFundo.setBackground(MedidasPadroes.COR_DE_FUNDO);
        FormLayout layout = new FormLayout("10dlu, pref, 3dlu", // Colunas
                                           "10dlu, pref, 10dlu, "
                                           + "pref, 10dlu, "
                                           + "pref, 2dlu, "
                                           + "pref, 10dlu, "
                                           + "pref, 10dlu, "
                                           + "pref, 2dlu, "
                                           + "pref, 20dlu,"
                                           + "pref, 10dlu"); // Linhas
        painelFundo.setLayout(layout);
        CellConstraints cc = new CellConstraints();
        
        painelFundo.add(getPainelLinha1(), cc.xy(2, 2));
        painelFundo.add(getPainelLinha2(), cc.xy(2, 4));        
        painelFundo.add(getPainelLinha3(), cc.xy(2, 6));       
        painelFundo.add(getPainelLinha4(), cc.xy(2, 8));       
        painelFundo.add(getPainelLinha5(), cc.xy(2, 10));       
        painelFundo.add(getPainelLinha6(), cc.xy(2, 12));       
        painelFundo.add(getPainelLinha7(), cc.xy(2, 14));       
        painelFundo.add(getPainelLinha8(), cc.xy(2, 16));
        txtCodigo.requestFocus();
        setCorLabels(Color.BLACK);
        
        add(painelFundo);
    }
    
    private JPanel getPainelLinha1() {
        FormLayout layout = new FormLayout("pref, 3dlu, pref, 20dlu, pref, 6dlu, pref, 3dlu", // Colunas
                                           "pref"); // Linhas
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        
        builder.add(lblCodigo, cc.xy(1, 1));
        builder.add(getTxtCodigo(), cc.xy(3, 1));
        builder.add(lblNome, cc.xy(5, 1));
        builder.add(getTxtNome(), cc.xy(7, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getPainelLinha2() {
        FormLayout layout = new FormLayout("pref, 6dlu, pref, 21dlu, pref, 3dlu, pref, 3dlu", // Colunas
                                           "pref"); // Linhas
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        
        builder.add(lblEmail, cc.xy(1, 1));
        builder.add(getTxtEmail(), cc.xy(3, 1));
        builder.add(lblCelular, cc.xy(5, 1));
        builder.add(getTxtCelular(), cc.xy(7, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    private JPanel getPainelLinha3() {
        FormLayout layout = new FormLayout("pref, 7dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu", // Colunas
                                           "pref"); // Linhas
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        
        builder.add(lblLogin, cc.xy(1, 1));
        builder.add(getTxtLogin(), cc.xy(3, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getPainelLinha4() {
        FormLayout layout = new FormLayout("pref, 4dlu, pref, 10dlu, pref, 3dlu, pref, 3dlu", // Colunas
                                           "pref"); // Linhas
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();  
        
        builder.add(lblSenhaLogin, cc.xy(1, 1));
        builder.add(getTxtSenhaLogin(), cc.xy(3, 1));
        builder.add(lblConfirmarSenhaLogin, cc.xy(5, 1));
        builder.add(getTxtSenhaLogin2(), cc.xy(7, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }   
    
    
    private JPanel getPainelLinha5() {
        FormLayout layout = new FormLayout("pref, 4dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu", // Colunas
                                           "pref"); // Linhas
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        
        builder.add(lblServidorEmail, cc.xy(1, 1));
        builder.add(getComboServidores(), cc.xy(3, 1));
        builder.add(getLblAdicionarServidor(), cc.xy(5, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getPainelLinha6() {
        FormLayout layout = new FormLayout("pref, 4dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu", // Colunas
                                           "pref"); // Linhas
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        
        builder.add(lblEmailAutomatico, cc.xy(1, 1));
        builder.add(getTxtEmailEnvioAutomatico(), cc.xy(3, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getPainelLinha7() {
        FormLayout layout = new FormLayout("pref, 4dlu, pref, 10dlu, pref, 3dlu, pref, 3dlu, pref", // Colunas
                                           "pref"); // Linhas
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();  
        
        builder.add(lblSenhaEmail, cc.xy(1, 1));
        builder.add(getTxtSenhaEmail(), cc.xy(3, 1));
        builder.add(lblConfirmarSenhaEmail, cc.xy(5, 1));
        builder.add(getTxtSenhaEmail2(), cc.xy(7, 1));
        builder.add(getBtnTestarServidorSMTP(), cc.xy(9, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getPainelLinha8() {
        FormLayout layout = new FormLayout("pref, 4dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu", // Colunas
                                           "pref"); // Linhas
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        
        builder.add(getBtnOK(), cc.xy(1, 1));
        builder.add(getBtnCancelar(), cc.xy(3, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JLabel getLblAdicionarServidor() {
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("arquivos/adicionarIcone.gif"));
        final JLabel lbl = new JLabel(img);
        lbl.setToolTipText("Adicionar outros servidores SMTP para o envio de email automático");
        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                lbl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
            
            }
        });
        return lbl;
    }
    
    private JPasswordField getTxtSenhaLogin() {
        if (txtSenhaLogin == null) {
            txtSenhaLogin = new JPasswordField();
            txtSenhaLogin.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
            return txtSenhaLogin;
        } else {
            return txtSenhaLogin;
        }
    }
    
    private JTextField getTxtCodigo() {
        if (txtCodigo == null) {
            txtCodigo = new JTextField();
            txtCodigo.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_GRANDE);
            txtCodigo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == 10) {
                        txtNome.requestFocus();
                    }
                }
            });
            return txtCodigo;
        } else {
            return txtCodigo;
        }
    }
    
    private JTextField getTxtSenhaLogin2() {
        if (txtSenhaLogin2 == null) {
            txtSenhaLogin2 = new JPasswordField();
            txtSenhaLogin2.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
            return txtSenhaLogin2;
        } else {
            return txtSenhaLogin2;
        }
    }
    
    private JTextField getTxtLogin() {
        if (txtLogin == null) {
            txtLogin = new JTextField();
            txtLogin.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
            return txtLogin;
        } else {
            return txtLogin;
        }
    }
    
    private BotaoPadrao getBtnTestarServidorSMTP() {
        if (btnTestarServidorSMTP == null) {
            btnTestarServidorSMTP = new BotaoPadrao("Testar Envio Automático");
            btnTestarServidorSMTP.setPreferredSize(new Dimension(170, 28));
            btnTestarServidorSMTP.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String nome =  comboServidoresEmailAutomatico.getSelectedItem().toString();
                    String enderecoServidor = servidoresSMTP.getProperty(nome);

                    String portaServidor = enderecoServidor.substring(enderecoServidor.indexOf("/")+1, 
                            enderecoServidor.length());
                    enderecoServidor = enderecoServidor.substring(0, enderecoServidor.indexOf("/"));

                    ServidorSMTP servidorSMTP = new ServidorSMTP(nome, enderecoServidor,                        
                            new String(txtSenhaEmailEnvioAutomatico.getPassword()), portaServidor,
                            txtEmailEnvioAutomatico.getText());

                    Email mail = new Email(servidorSMTP.getEnderecoServidor(), servidorSMTP.getPorta(),
                            servidorSMTP.getEmail(), servidorSMTP.getSenha());
                    mail.sendMail(txtEmail.getText(), "Teste Email Automático",
                            "Teste de envio automático", TelaPrimeiraConfiguracao.this);
                    if (mail.getSucesso()) {
                        setCorLabels(Color.BLACK);
                        JOptionPane.showMessageDialog(TelaPrimeiraConfiguracao.this, "Email Enviado com Sucesso!\n"
                                + "Verifique sua caixa de email.\n"
                                + "Email enviado para: " + txtEmail.getText(), "Sucesso!", 
                                JOptionPane.INFORMATION_MESSAGE);
                        btnTestarServidorSMTP.setEnabled(false);
                    }
                }                
            });
            return btnTestarServidorSMTP;
        } else {
            return btnTestarServidorSMTP;
        }
    }
    
    private BotaoPadrao getBtnOK() {
        if (btnOK == null) {
            btnOK = new BotaoPadrao("OK");
            btnOK.addActionListener(new AcaoOK());
            return btnOK;
        }
        return btnOK;
    }
    
    private BotaoPadrao getBtnCancelar() {
        if (btnCancelar == null) {
            btnCancelar = new BotaoPadrao("Cancelar");
            btnCancelar.setSize(MedidasPadroes.MEDIDA_BTN_PADRAO);
            btnCancelar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    TelaPrimeiraConfiguracao.this.dispose();
                }
            });
            return btnCancelar;
        }
        return btnCancelar;
    }
    
    private JComboBox getComboServidores() {
        if (comboServidoresEmailAutomatico == null) {
            comboServidoresEmailAutomatico = new JComboBox();
            montarComboServidoresSMTP();
            comboServidoresEmailAutomatico.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
            comboServidoresEmailAutomatico.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    btnTestarServidorSMTP.setEnabled(true);
                    if (comboServidoresEmailAutomatico.getSelectedItem().equals("gmail")) {
                        txtEmailEnvioAutomatico.setEnabled(true);
                        txtEmailEnvioAutomatico.setText("@gmail.com");
                        txtEmailEnvioAutomatico.requestFocus();
                        txtEmailEnvioAutomatico.setCaretPosition(0);
                    } else if (comboServidoresEmailAutomatico.getSelectedItem().equals("yahoo")) {
                        txtEmailEnvioAutomatico.setEnabled(true);
                        txtEmailEnvioAutomatico.setText("@yahoo.com.br");
                        txtEmailEnvioAutomatico.requestFocus();
                        txtEmailEnvioAutomatico.setCaretPosition(0);
                    } else if (comboServidoresEmailAutomatico.getSelectedItem().equals("hotmail")) {
                        txtEmailEnvioAutomatico.setEnabled(true);
                        txtEmailEnvioAutomatico.setText("@hotmail.com");
                        txtEmailEnvioAutomatico.requestFocus();
                        txtEmailEnvioAutomatico.setCaretPosition(0);
                    } else if (comboServidoresEmailAutomatico.getSelectedItem().equals("Selecione")) {
                        txtEmailEnvioAutomatico.setEnabled(false);
                        txtEmailEnvioAutomatico.setText("");
                    } else {
                        txtEmailEnvioAutomatico.setEnabled(true);
                        txtEmailEnvioAutomatico.setText("Email Para Envio Automático");
                        txtEmailEnvioAutomatico.requestFocus();
                    }                    
                }
            });
            return comboServidoresEmailAutomatico;
        } else {
            return comboServidoresEmailAutomatico;
        }        
    }
    
    private JFormattedTextField getTxtCelular() {
        if (txtCelular == null) {
            try {
                MaskFormatter mask = new MaskFormatter("(##)####-####");
                mask.setValueContainsLiteralCharacters(false);
                txtCelular = new JFormattedTextField(mask);
                txtCelular.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
                return txtCelular;
            } catch (ParseException ex) {
                return null;
            }
        } else {
            return txtCelular;
        }
    }
    
    private JTextField getTxtEmail() {
        if (txtEmail == null) {
            txtEmail = new JTextField();
            txtEmail.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_GRANDE);
            return txtEmail;
        } else {
            return txtEmail;
        }
    }
    
    private JTextField getTxtEmailEnvioAutomatico() {
        if (txtEmailEnvioAutomatico == null) {
            txtEmailEnvioAutomatico = new JTextField();
            txtEmailEnvioAutomatico.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_GRANDE);
            txtEmailEnvioAutomatico.setToolTipText("Deve ser um email do mesmo servidor de emails automático");
            txtEmailEnvioAutomatico.setEnabled(false);
            txtEmailEnvioAutomatico.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    btnTestarServidorSMTP.setEnabled(true);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }
            });
            return txtEmailEnvioAutomatico;
        } else {
            return txtEmailEnvioAutomatico;
        }
    }
    
    private JPasswordField getTxtSenhaEmail() {
        if (txtSenhaEmailEnvioAutomatico == null) {
            txtSenhaEmailEnvioAutomatico = new JPasswordField();
            txtSenhaEmailEnvioAutomatico.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
            txtSenhaEmailEnvioAutomatico.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    btnTestarServidorSMTP.setEnabled(true);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }
            });
            return txtSenhaEmailEnvioAutomatico;
        } else {
            return txtSenhaEmailEnvioAutomatico;
        }
    }
    
    private JPasswordField getTxtSenhaEmail2() {
        if (txtSenhaEmailEnvioAutomatico2 == null) {
            txtSenhaEmailEnvioAutomatico2 = new JPasswordField();
            txtSenhaEmailEnvioAutomatico2.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
            txtSenhaEmailEnvioAutomatico2.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    btnTestarServidorSMTP.setEnabled(true);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }
            });
            
            return txtSenhaEmailEnvioAutomatico2;
        } else {
            return txtSenhaEmailEnvioAutomatico2;
        }
    }
    
    private JTextField getTxtNome() {
        if (txtNome == null) {
            txtNome = new JTextField();
            txtNome.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_GRANDE);
            return txtNome;
        } else {
            return txtNome;
        }
    }
    
    private void setCorLabels(Color c) {
        lblCodigo.setForeground(c);
        lblCelular.setForeground(c);
        lblConfirmarSenhaEmail.setForeground(c);
        lblConfirmarSenhaLogin.setForeground(c);
        lblEmail.setForeground(c);
        lblEmailAutomatico.setForeground(c);
        lblLogin.setForeground(c);
        lblNome.setForeground(c);
        lblSenhaEmail.setForeground(c);
        lblSenhaLogin.setForeground(c);
        lblServidorEmail.setForeground(c);
    }
    
    private class AcaoOK extends AbstractAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {            
            setCorLabels(Color.BLACK);
            if (validarCampos() != 0) {
                return;
            }
            FileOutputStream out;
            try {               
                Properties configuracoesIniciais = LeitorArquivoConfiguracoes.getInstance();
                
                File prop = new File("configuracoes.properties");
                
                String usuarioProprietario = txtLogin.getText();
                String senhaProprietario = new String(txtSenhaLogin.getPassword());
                
                configuracoesIniciais.setProperty("login.proprietario", usuarioProprietario);
                configuracoesIniciais.setProperty("senha.proprietario", senhaProprietario);
                
                out = new FileOutputStream(prop);
                configuracoesIniciais.store(out, "Configurações Iniciais");
                out.close();
                
                Funcionario func = new Funcionario(txtCodigo.getText(), txtNome.getText(), txtEmail.getText(), 
                        txtCelular.getValue().toString(), Funcionario.TIPO_PROPRIETARIO, 
                        usuarioProprietario, senhaProprietario);
                FuncionarioDao.insere(func);
                                
                String servidorSMTP =  comboServidoresEmailAutomatico.getSelectedItem().toString();
                String enderecoServidor = servidoresSMTP.getProperty(servidorSMTP);
                
                String portaServidor = enderecoServidor.substring(enderecoServidor.indexOf("/")+1, 
                        enderecoServidor.length());
                enderecoServidor = enderecoServidor.substring(0, enderecoServidor.indexOf("/"));
                
                ServidorSMTP serverSMTP = new ServidorSMTP(servidorSMTP, enderecoServidor,                        
                        new String(txtSenhaEmailEnvioAutomatico.getPassword()), portaServidor,
                        txtEmailEnvioAutomatico.getText());
                ServidorSMTPDao.insere(serverSMTP);
                
                JOptionPane.showMessageDialog(TelaPrimeiraConfiguracao.this, "Configurações Realizadas com Sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                TelaPrimeiraConfiguracao.this.dispose();
                new TelaLogin();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {                
                ex.printStackTrace();
            }           
        }        
    }
    
    private int validarCampos() {        
        if (txtCodigo.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Código Inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            lblCodigo.setForeground(Color.RED);
            txtCodigo.requestFocus();
            return -1;
        }
        if (txtNome.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Nome Inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            lblNome.setForeground(Color.RED);
            txtNome.requestFocus();
            return -1;
        }
        if (txtCelular.getValue() == null || txtCelular.getValue().toString().equals("")
            || txtCelular.getValue().toString().length() != 10) {
            JOptionPane.showMessageDialog(this, "Número Celular Invalido!", "Erro", JOptionPane.ERROR_MESSAGE);
            lblCelular.setForeground(Color.RED);
            txtCelular.requestFocus();
            return -1;
        }
        String emailRegex = ".+@+.+\\.+.+";
        if (!txtEmail.getText().matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Email Inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            lblEmail.setForeground(Color.RED);
            txtEmail.requestFocus();
            return -1;
        }
        if (txtLogin.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Login Inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            lblLogin.setForeground(Color.RED);
            txtLogin.requestFocus();
            return -1;
        }   
        if (new String(txtSenhaLogin.getPassword()).equals("")
            || new String(txtSenhaLogin2.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(this, "Senha Invalida!", "Erro", JOptionPane.ERROR_MESSAGE);
            lblSenhaLogin.setForeground(Color.RED);
            lblConfirmarSenhaLogin.setForeground(Color.RED);
            txtSenhaLogin.requestFocus();
                return -1;
            }
        if (!new String(txtSenhaLogin.getPassword())
                .equals(new String(txtSenhaLogin2.getPassword()))) {
            JOptionPane.showMessageDialog(this, "As senhas não são iguais!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtSenhaLogin.setText("");
            txtSenhaLogin2.setText("");
            lblSenhaLogin.setForeground(Color.RED);
            lblConfirmarSenhaLogin.setForeground(Color.RED);
            txtSenhaLogin.requestFocus();
            return -1;
        }        
        if (!txtEmailEnvioAutomatico.getText().matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Email Automático Inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            if (comboServidoresEmailAutomatico.getSelectedItem().equals("Selecione")) {
                lblServidorEmail.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this, "Selecione um servidor"
                        + " para envio de email automático antes!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            lblEmailAutomatico.setForeground(Color.RED);
            txtEmailEnvioAutomatico.requestFocus();
            return -1;
        }
        if (new String(txtSenhaEmailEnvioAutomatico.getPassword()).equals("")
            || new String(txtSenhaEmailEnvioAutomatico2.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(this, "Senha Email Automático Invalida!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtSenhaEmailEnvioAutomatico.setText("");
            txtSenhaEmailEnvioAutomatico2.setText("");
            lblSenhaEmail.setForeground(Color.RED);
            lblConfirmarSenhaEmail.setForeground(Color.RED);
            txtSenhaEmailEnvioAutomatico.requestFocus();
                return -1;
            }
        if (!new String(txtSenhaEmailEnvioAutomatico.getPassword())
                .equals(new String(txtSenhaEmailEnvioAutomatico2.getPassword()))) {
            JOptionPane.showMessageDialog(this, "As Senhas de Email Automático não "
                    + "são iguais!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtSenhaEmailEnvioAutomatico.setText("");
            txtSenhaEmailEnvioAutomatico2.setText("");
            lblSenhaEmail.setForeground(Color.RED);
            lblConfirmarSenhaEmail.setForeground(Color.RED);
            txtSenhaEmailEnvioAutomatico.requestFocus();
            return -1;
        } if (btnTestarServidorSMTP.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Por favor, teste o envio de email"
                    + " automático antes de continuar!", "Erro", JOptionPane.ERROR_MESSAGE);
            btnTestarServidorSMTP.requestFocus();
            return -1;
        }        
        return 0;
    }
    
    public void atualizarComboServidores() {
        montarComboServidoresSMTP();
    }
}
