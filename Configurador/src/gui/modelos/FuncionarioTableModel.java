package gui.modelos;

import configurador.Funcionario;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Felps
 */
public class FuncionarioTableModel extends AbstractTableModel {

    private String[] colunas;
    private List<Funcionario> linhas;
    private JTableHeader header;
    private boolean crescente = true;
    private JTable tab;

    public FuncionarioTableModel(JTable tabela) {
        colunas = new String[]{"Código", "Nome", "Email", "Celular", "Tipo"};
        linhas = new ArrayList<Funcionario>(10);
        header = tabela.getTableHeader();
        tab = tabela;
        adicionaOrdenacaoPorColuna();
    }
    
    private void adicionaOrdenacaoPorColuna() {
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
                    
                    List<Funcionario> func = getFuncionarios();
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
                        adicionaFuncionarios(func);
                        crescente = false;
                        return;
                    }
                    adicionaFuncionarios(func);
                    crescente = true;
                    fireTableChanged(null);
                    
                }
            });
    }
    
    @Override  
    public Class<?> getColumnClass(int coluna) {   
        return String.class;  
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
        Funcionario f = linhas.get(linhaIndex);

        switch (colunaIndex) {
            case 0:
                return f.getCodigo();
            case 1:
                return f.getNome();
            case 2:
                return f.getEmail();
            case 3:
                return f.getCelular();
            case 4:
                int tipo = f.getTipo();
                switch (tipo) {
                    case Funcionario.TIPO_PROPRIETARIO:
                        return "Proprietário";
                    case Funcionario.TIPO_ADMINISTRADOR:
                        return "Administrador";
                    default:
                        return "Funcionário";
                }
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object valor, int linhaIndex, int colunaIndex) {
        Funcionario f = linhas.get(linhaIndex);

        switch (colunaIndex) {
            case 1:
                f.setNome(valor.toString());
                break;
            case 2:
                f.setEmail(valor.toString());
                break;
            case 3:
                f.setCelular(valor.toString());
                break;
            case 4:
                if (valor.toString().equals("Proprietário")) {
                    f.setTipo(Funcionario.TIPO_PROPRIETARIO);
                } else if (valor.toString().equals("Administrador")) {
                    f.setTipo(Funcionario.TIPO_ADMINISTRADOR);                    
                } else {
                    f.setTipo(Funcionario.TIPO_NORMAL);                    
                }
                break;
        }
        fireTableDataChanged();
    }
    
    public void adicionaFuncionarios(List<Funcionario> f) {  
        // Pega o tamanho antigo da tabela.  
        int tamanhoAntigo = getRowCount();  
  
        // Adiciona os registros.  
        linhas.addAll(f);  
  
        fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);  
    }

    public void adicionaFuncionario(Funcionario f) {
        linhas.add(f);
        // informamos os listeners que a linha (size - 1) foi adicionada  
        fireTableRowsInserted(linhas.size() - 1, linhas.size() - 1);
    }
    
    public List<Funcionario> getFuncionarios() {
        List<Funcionario> func = new ArrayList<Funcionario>();
        func.addAll(linhas);
        return func;
    }
    
    public void removeAll() {
        linhas.clear();
    }
    
    public void remove (List<Integer> lista) {
        System.out.println(linhas.size());
        Collections.reverse(lista);
        for (int i : lista) {
            linhas.remove(i);
        }
        System.out.println(linhas.size());
        fireTableRowsDeleted(lista.get(0), lista.get(lista.size() - 1));
    }

    public void remove(int indice) {
        linhas.remove(indice);
        fireTableRowsDeleted(indice, indice);
    }
    
    public void remove(String nome) {
        int indice = 0;
        for (Funcionario f : linhas) {
            if (f.getNome().equalsIgnoreCase(nome)) {
                indice = linhas.indexOf(f);
            }
        }
        linhas.remove(indice);
        fireTableRowsDeleted(indice, indice);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    private class ComparaNumeros implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
        
    }
    
    private class ComparaStrings implements Comparator<Funcionario> {
        private int index;
        
        public ComparaStrings(int ind) {
            index = ind;
        }
        @Override
        public int compare(Funcionario o1, Funcionario o2) {
            switch (index) {
                case 0:
                    return new ComparaNumeros().compare(Integer.parseInt(o1.getCodigo()), 
                            Integer.parseInt(o2.getCodigo()));
                case 1:
                    return o1.getNome().compareToIgnoreCase(o2.getNome());
                case 2:
                    return o1.getEmail().compareToIgnoreCase(o2.getEmail());
                case 3:
                    return o1.getCelular().compareToIgnoreCase(o2.getCelular());
                case 4:
                    return String.valueOf(o1.getTipo()).compareToIgnoreCase(String.valueOf(o2.getTipo()));
                default:
                    return -1;
            }
        }
        
    }
}
