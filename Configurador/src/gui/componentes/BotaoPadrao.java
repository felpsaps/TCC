package gui.componentes;

import javax.swing.JButton;
import utils.MedidasPadroes;

/**
 *
 * @author Felps
 */
public class BotaoPadrao extends JButton{
    
    public BotaoPadrao(String nome) {
        super(nome);
        setPreferredSize(MedidasPadroes.MEDIDA_BTN_PADRAO);
    }    
}
