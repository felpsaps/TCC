package gui.telaPrincipal;

import configurador.Funcionario;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Felps
 */
public class PainelCadastroFuncionario extends JPanel{
    
    private Funcionario func;
    private boolean isProprietario;
    
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtLogin;
    private JTextField txtSenha;
    
    public PainelCadastroFuncionario(Funcionario f) {
        func = f;
        isProprietario = func.getTipo() == Funcionario.TIPO_PROPRIETARIO ? true : false;
    }
}
