/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configurador;

/**
 *
 * @author Felps
 */
public class ServidorSMTP {  
    private String enderecoServidor;
    private String email;
    private String senha;
    private String porta;
    
    public ServidorSMTP(String nome, String endereco, String pass, String pt, String mail) {
       nomeServidor = nome;
       enderecoServidor = endereco;
       email = mail;
       senha = pass;
       porta = pt;
    }
    
    private String nomeServidor;

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        email = mail;
    }

    public String getEnderecoServidor() {
        return enderecoServidor;
    }

    public void setEnderecoServidor(String endereco) {
        enderecoServidor = endereco;
    }

    public String getNomeServidor() {
        return nomeServidor;
    }

    public void setNomeServidor(String nome) {
        nomeServidor = nome;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String pt) {
        porta = pt;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String pass) {
        senha = pass;
    }
}