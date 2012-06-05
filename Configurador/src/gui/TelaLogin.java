package gui;

import gui.telaPrincipal.TelaPrincipal;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import configurador.Funcionario;
import dao.FuncionarioDao;
import excessoes.FuncionarioDaoException;
import gui.componentes.BotaoPadrao;
import gui.componentes.TxtFieldMedio;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.*;
import utils.MedidasPadroes;
import utils.TextFieldComoTab;

/**
 *
 * @author felipeaps
 */
public class TelaLogin extends JFrame {

    private TxtFieldMedio txtLogin;
    private JPasswordField txtSenha;
    private JLabel lblEsqueceuSenha;
    private JLabel lblUsuario;
    private JLabel lblSenha;
    private BotaoPadrao btnLogin;
    private BotaoPadrao btnCancelar;

    public TelaLogin() {
        super("  Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("arquivos/loginIcone.png")).getImage());
        setResizable(false);
        setSize(new Dimension(500, 370));
        setLocationRelativeTo(null);
        init();
        setVisible(true);
    }

    private void init() {

        JPanel painelFundo = new ImagemFundo();

        FormLayout layout = new FormLayout("pref", //Colunas
                                           "pref, 5dlu,"
                                         + "pref, 1dlu,"
                                         + "pref, 20dlu,"
                                         + "pref, 5dlu" //Linhas
                                            );
        painelFundo.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        painelFundo.add(getLinha1(), cc.xy(1, 1));
        painelFundo.add(getLinha2(), cc.xy(1, 3));
        painelFundo.add(getLinha3(), cc.xy(1, 5));
        painelFundo.add(getLinha4(), cc.xy(1, 7));
        
        add(painelFundo);
    }

    private JPanel getLinha1() {
        FormLayout layout = new FormLayout("175dlu, pref, 3dlu, pref, 3dlu", //Colunas
                "80dlu, pref" //Linhas
                );
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.add(getLblUsuario(), cc.xy(2, 2));
        builder.add(getTxtLogin(), cc.xy(4, 2));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);

        return builder.getPanel();
    }

    private JPanel getLinha2() {
        FormLayout layout = new FormLayout("175dlu, pref, 8dlu, pref, 3dlu", //Colunas
                "0dlu, pref" //Linhas
                );
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.add(getLblSenha(), cc.xy(2, 2));
        builder.add(getTxtSenha(), cc.xy(4, 2));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);

        return builder.getPanel();
    }

    private JPanel getLinha3() {
        FormLayout layout = new FormLayout("215dlu, pref", //Colunas
                                           "pref" //Linhas
                );
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.add(getLblEsqueceuSenha(), cc.xy(2, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);

        return builder.getPanel();
    }

    private JPanel getLinha4() {
        FormLayout layout = new FormLayout("165dlu, pref, 10dlu, pref", //Colunas
                                           "pref" //Linhas
                );
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.add(getBtnLogin(), cc.xy(2, 1));
        builder.add(getBtnCancelar(), cc.xy(4, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);

        return builder.getPanel();
    }

    private BotaoPadrao getBtnLogin() {
        if (btnLogin == null) {
            btnLogin = new BotaoPadrao("Login");
            btnLogin.addActionListener(new AcaoLogin());
            return btnLogin;
        } else {
            return btnLogin;
        }
    }

    private BotaoPadrao getBtnCancelar() {
        if (btnCancelar == null) {
            btnCancelar = new BotaoPadrao("Cancelar");
            btnCancelar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            return btnCancelar;
        } else {
            return btnCancelar;
        }
    }

    private JLabel getLblUsuario() {
        if (lblUsuario == null) {
            lblUsuario = new JLabel("Usuário:");
            lblUsuario.setForeground(Color.LIGHT_GRAY);
            lblUsuario.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
            return lblUsuario;
        } else {
            return lblUsuario;
        }
    }

    private JLabel getLblSenha() {
        if (lblSenha == null) {
            lblSenha = new JLabel("Senha:");
            lblSenha.setForeground(Color.LIGHT_GRAY);
            lblSenha.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
            return lblSenha;
        } else {
            return lblSenha;
        }
    }

    private JLabel getLblEsqueceuSenha() {
        if (lblEsqueceuSenha == null) {
            lblEsqueceuSenha = new JLabel("Recuperar Senha");
            lblEsqueceuSenha.setFont(new Font("Arial", Font.BOLD, 9));
            lblEsqueceuSenha.setForeground(Color.ORANGE);
            lblEsqueceuSenha.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    lblEsqueceuSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    lblEsqueceuSenha.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    new RecuperarSenhaLogin();
                }
            });
            return lblEsqueceuSenha;
        } else {
            return lblEsqueceuSenha;
        }
    }

    private TxtFieldMedio getTxtLogin() {
        if (txtLogin == null) {
            txtLogin = new TxtFieldMedio();
            TextFieldComoTab.considerarEnterComoTab(txtLogin);
            return txtLogin;
        } else {
            return txtLogin;
        }
    }

    private JPasswordField getTxtSenha() {
        if (txtSenha == null) {
            txtSenha = new JPasswordField();
            txtSenha.setPreferredSize(MedidasPadroes.MEDIDA_TEXTFIELD_MEDIO);
            txtSenha.addActionListener(new AcaoLogin());
            return txtSenha;
        } else {
            return txtSenha;
        }
    }    

    private class AcaoLogin extends AbstractAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Funcionario func = FuncionarioDao.selectLoginESenha(txtLogin.getText(), 
                        new String(txtSenha.getPassword()));
                TelaLogin.this.dispose();
                new TelaPrincipal(func);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (FuncionarioDaoException ex) {
                txtLogin.setText("");
                txtSenha.setText("");
                txtLogin.requestFocus();
                JOptionPane.showMessageDialog(TelaLogin.this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ImagemFundo extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Image imagemLogin = new ImageIcon(getClass().getClassLoader().getResource("arquivos/login.jpg")).getImage();

            g.drawImage(imagemLogin, 0, 0, this);
            getLblUsuario().repaint();
            getLblEsqueceuSenha().repaint();
            getLblSenha().repaint();
            getBtnCancelar().repaint();
            getBtnLogin().repaint();
            getTxtLogin().repaint();
            getTxtSenha().repaint();
        }
    }
}
