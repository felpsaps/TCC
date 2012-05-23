package configurador;

import utils.LeitorArquivoConfiguracoes;
import gui.TelaLogin;
import gui.TelaPrimeiraConfiguracao;
import java.io.IOException;
import java.util.Properties;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Felps
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, 
            InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        Properties props = LeitorArquivoConfiguracoes.getInstance();
        boolean primeiraVez = true;
        
        for (Object value : props.values()) {
            if(value.equals("")) {
                new TelaPrimeiraConfiguracao();
                break;
            }
            primeiraVez = false;
        }
        if (!primeiraVez) {
            new TelaLogin();
        }
    }
}
