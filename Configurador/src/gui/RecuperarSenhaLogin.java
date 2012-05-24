package gui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import configurador.Funcionario;
import configurador.ServidorSMTP;
import dao.FuncionarioDao;
import dao.ServidorSMTPDao;
import email.SendMail;
import excessoes.FuncionarioDaoException;
import excessoes.ServidorSMTPDaoException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.*;
import utils.MedidasPadroes;

/**
 *
 * @author felipeaps
 */
public class RecuperarSenhaLogin extends JDialog{

    private static final char[] ALL_CHARS = new char[62];
    private JButton btnEnviar;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JButton btnCancelar;

    static {
        for (int i = 48, j = 0; i < 123; i++) {
            if (Character.isLetterOrDigit(i)) {
                ALL_CHARS[j] = (char) i;
                j++;
            }
        }
    }

    public RecuperarSenhaLogin() {
        setTitle("Recuperar Senha!");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("arquivos/recuperarSenhaIcone.gif"))
                .getImage());
        setSize(new Dimension(320, 150));
        setLocationRelativeTo(null);
        setModal(true);
        init();
        txtEmail.setText("felipinlineaps@gmail.com");
        setVisible(true);
    }

    private void init() {
        JPanel painelFundo = new ImagemFundo();
        FormLayout layout = new FormLayout("35dlu, pref, 3dlu",
                                           "15dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu");
        CellConstraints cc = new CellConstraints();

        painelFundo.setLayout(layout);

        painelFundo.add(getPanelLbl(), cc.xy(2, 2));
        painelFundo.add(getPanelTxtEmail(), cc.xy(2, 4));
        painelFundo.add(getLinhaBotoes(), cc.xy(2, 6));
        
        add(painelFundo);
    }
    
    private JPanel getPanelTxtEmail() {
        FormLayout layout = new FormLayout("pref",
                                           "pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        builder.add(getTxtEmail(), cc.xy(1, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }
    
    private JPanel getPanelLbl() {
        FormLayout layout = new FormLayout("pref",
                                           "pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        builder.add(getLblEmail(), cc.xy(1, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);
        
        return builder.getPanel();
    }

    private JPanel getLinhaBotoes() {
        FormLayout layout = new FormLayout("pref, 35dlu, pref",
                                           "pref");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);

        builder.add(getBtnEnviar(), cc.xy(1, 1));
        builder.add(getBtnCancelar(), cc.xy(3, 1));
        builder.setBackground(MedidasPadroes.COR_DE_FUNDO);

        return builder.getPanel();
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
    
    private JLabel getLblEmail() {
        if (lblEmail == null) {
            lblEmail = new JLabel("Digite seu email cadastrado:");
            lblEmail.setForeground(Color.BLACK);
            lblEmail.setFont(new Font(Font.SERIF, Font.BOLD, 12));
            return lblEmail;
        } else {
            return lblEmail;
        }
    }

    private JButton getBtnEnviar() {
        if (btnEnviar == null) {
            btnEnviar = new JButton("Enviar");
            btnEnviar.addActionListener(new AcaoEnviar());
            return btnEnviar;
        } else {
            return btnEnviar;
        }
    }

    private JButton getBtnCancelar() {
        if (btnCancelar == null) {
            btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    RecuperarSenhaLogin.this.dispose();
                }
            });
            return btnCancelar;
        } else {
            return btnCancelar;
        }
    }

    private String geraNovaSenha() {
        Random r = new Random();
        char[] novaSenhaAux = new char[8];

        for (int i = 0; i < 8; i++) {
            novaSenhaAux[i] = ALL_CHARS[r.nextInt(ALL_CHARS.length)];
        }

        return String.valueOf(novaSenhaAux);
    }

    private String getMensagemEmail(String senha, String nomeUsuario) {
        return new String().format("Prezado %s,\n\n\t"
                                 + "Foi requerida a mudança de senha para sua conta.\n\n\t"
                                 + "Sua nova senha é %s \n\n\t"
                                 + "Você pode alterá-la acessando sua conta e indo no menu Configurações.\n\n\n\t"
                                 + "Obrigado.", nomeUsuario, senha);
    }

    private class AcaoEnviar extends AbstractAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                final ServidorSMTP servidorSMTP = ServidorSMTPDao.getServidor();
                final Funcionario func = FuncionarioDao.selectPorEmail(txtEmail.getText());
                final String novaSenha = geraNovaSenha();
                func.setSenha(novaSenha);
                FuncionarioDao.atualiza(func);
                Thread enviarEmail = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        SendMail mail = new SendMail(servidorSMTP.getEnderecoServidor(), servidorSMTP.getPorta(),
                                servidorSMTP.getEmail(), servidorSMTP.getSenha());
                        mail.sendMail(func.getEmail(), "Recuperação de Senha",
                                getMensagemEmail(novaSenha, func.getNome()), RecuperarSenhaLogin.this);
                    }
                });
                JOptionPane.showMessageDialog(RecuperarSenhaLogin.this, "Sua nova senha foi enviada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                RecuperarSenhaLogin.this.dispose();
                enviarEmail.start();

            } catch(FuncionarioDaoException ex) {
                JOptionPane.showMessageDialog(RecuperarSenhaLogin.this, ex.getMessage(), "Email Não Cadastrado!", 
                        JOptionPane.ERROR_MESSAGE);
            } catch (ServidorSMTPDaoException ex) {
                JOptionPane.showMessageDialog(RecuperarSenhaLogin.this, ex.getMessage(), "Erro!", 
                        JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private class ImagemFundo extends JPanel {
        @Override
        public void paint(Graphics g) {
           super.paint(g);

            Image ImagemFundo = new ImageIcon(getClass().getClassLoader()
                                        .getResource("arquivos/backGround.jpg")).getImage();

            g.drawImage(ImagemFundo, 0, 0, this);
            getBtnCancelar().repaint();
            getBtnEnviar().repaint();
            getLblEmail().repaint();
            getTxtEmail().repaint();
        }
    }
}