package gui.componentes;

import javax.swing.JLabel;
import utils.MedidasPadroes;

/**
 *
 * @author Felps
 */
public class LabelPadrao extends JLabel{
    
    /**
     * Cria um label padrao
     * @param nome
     *          Rótulo do label
     */
    public LabelPadrao(String nome) {
        super(nome);
        setFont(MedidasPadroes.FONTE_LABELS);
        setForeground(MedidasPadroes.COR_LABEL);
    }
    
}
