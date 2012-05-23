package configurador;

/**
 *
 * @author Felps
 */
public class Funcionario {
    public static final int TIPO_PROPRIETARIO = 0;
    public static final int TIPO_ADMINISTRADOR = 1;
    public static final int TIPO_NORMAL = 2;
    
    private String codigo;
    private String nome;
    private String celular;
    private String email;
    private String login;
    private String senha;
    private int tipo;
    
    public Funcionario(String cog, String name, String mail, String cel, int type, String log, String pass) {
        codigo = cog;
        nome = name;
        email = mail;
        celular = cel;
        tipo = type;
        login = log;
        senha = pass;
    }
    
    public Funcionario(String cog, String name, String mail, String cel, int type) {
        this (cog, name, mail, cel, type, null, null);
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String cel) {
        celular = cel;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String cod) {
        codigo = cod;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        email = mail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String log) {
        login = log;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String name) {
        nome = name;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String pass) {
        senha = pass;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int type) {
        tipo = type;
    }
}
