package gui.modelos;


import gui.componentes.Link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import configurador.Funcionario;
import configurador.MensagemBean;

public class MensagemTableModel extends AbstractTableModel {

	private String[] colunas;
    private List<MensagemBean> linhas;
    private JTableHeader header;
    private boolean crescente = true;
    private JTable tab;

    public MensagemTableModel(JTable tabela) {

        colunas = new String[]{"", "Mensagem", "Data", "Lida"};
        linhas = new ArrayList<MensagemBean>(10);
        header = tabela.getTableHeader();
        tab = tabela;
        //adicionaOrdenacaoPorColuna();
    }
    
   /* private void adicionaOrdenacaoPorColuna() {
        header.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent evt) {
                    JTable table = ((JTableHeader) evt.getSource()).getTable();
                    TableColumnModel colModel = table.getColumnModel();

                    // índice da coluna cujo titulo foi clicado
                    int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                    int mColIndex = table.convertColumnIndexToModel(vColIndex);

                    if (vColIndex == -1) {
                        return;
                    }
                    
                    List<Funcionario> func = getMensagens();
                    removeAll();
                    
                    if (mColIndex == 0) {                        
                        Collections.sort(func, new ComparaStrings(0));                        
                    }
                    if (mColIndex == 1) {                        
                        Collections.sort(func, new ComparaStrings(1));                        
                    }
                    if (mColIndex == 2) {                        
                        Collections.sort(func, new ComparaStrings(2));                        
                    }
                    if (mColIndex == 3) {                        
                        Collections.sort(func, new ComparaStrings(3));                        
                    }
                    if (mColIndex == 4) {                        
                        Collections.sort(func, new ComparaStrings(4));                        
                    }
                    
                    int indexProprietario = 0;
                    for (Funcionario fu : func) {
                        if (fu.getTipo() == Funcionario.TIPO_PROPRIETARIO) {
                            indexProprietario = func.indexOf(fu);
                        }
                    }
                    
                    Funcionario fu = func.get(indexProprietario);
                    func.remove(indexProprietario);
                    func.add(0, fu);
                    
                    if (crescente) {
                        Collections.reverse(func);
                        fu = func.get(func.size()-1);
                        func.remove(func.size()-1);
                        func.add(0, fu);
                        adicionaMensagem(func);
                        crescente = false;
                        return;
                    }
                    adicionaMensagem(func);
                    crescente = true;
                    fireTableChanged(null);
                    
                }
            });
    }*/
    
    @Override  
    public Class<?> getColumnClass(int coluna) { 
    	switch (coluna) {
    		case 0:
    			return Boolean.class;
    		case 1:
    			return Link.class;
    		default:
    			return String.class;    			
    	}
    }
    
    @Override
    public String getColumnName(int coluna) {  
        switch (coluna) {
            case 0:
                return colunas[0];
            case 1:
                return colunas[1];
            case 2:
                return colunas[2];
            case 3:
                return colunas[3];
            case 4:
                return colunas[4];
            default:
                return null;   
        } 
    }

    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }
    
    

    @Override
    public Object getValueAt(int linhaIndex, int colunaIndex) {
        MensagemBean m = linhas.get(linhaIndex);

        switch (colunaIndex) {
            case 0:
                return m.getChecado();
            case 1:
                return "Vaga não autorizada - " + m.getUsrEstacionado().getNome();
            case 2:
                return m.getData();
            case 3:
            	if ("S".equals(m.getLida())) {
            		return "Sim";
            	} else {
            		return "Não";            		
            	}
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object valor, int linhaIndex, int colunaIndex) {
        MensagemBean m = linhas.get(linhaIndex);

        switch (colunaIndex) {
            case 0:
            	m.setChecado();
                break;
            case 2:
                break;
            case 3:
                if ("Sim".equals(valor)) {
                	System.out.println("pqp");
                	m.setLida("S");
                }
        }
        fireTableDataChanged();
    }
    
    public void adicionaMensagem(List<MensagemBean> f) {  
        // Pega o tamanho antigo da tabela.  
        int tamanhoAntigo = getRowCount();  
  
        // Adiciona os registros.  
        linhas.addAll(f);  
  
        fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);  
    }

    public void adicionaMensagem(MensagemBean f) {
        linhas.add(f);
        // informamos os listeners que a linha (size - 1) foi adicionada  
        fireTableRowsInserted(linhas.size() - 1, linhas.size() - 1);
    }
    
    public List<MensagemBean> getMensagens() {
        List<MensagemBean> func = new ArrayList<MensagemBean>();
        func.addAll(linhas);
        return func;
    }
    
    public MensagemBean getMensagen(Integer linha) {
    	System.out.println(linha);
        return linhas.get(linha);
    }
    
    public void removeAll() {
        linhas.clear();
    }
    
    public void remove (List<Integer> lista) {
        Collections.reverse(lista);
        for (int i : lista) {
            linhas.remove(i);
        }
        fireTableRowsDeleted(lista.get(0), lista.get(lista.size() - 1));
    }

    public void remove(int indice) {
        linhas.remove(indice);
        fireTableRowsDeleted(indice, indice);
    }
    
    public void remove(MensagemBean msg) {        
    	remove(linhas.indexOf(msg));
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if (columnIndex == 0) {
    		return true;
    	}
        return false;
    }
    
    private class ComparaNumeros implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
        
    }
    
}
