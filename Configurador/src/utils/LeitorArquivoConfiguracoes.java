/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import configurador.Main;
import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felps
 */
public class LeitorArquivoConfiguracoes {
    
    private static Properties properties;
    
    public static Properties getInstance() {
        if (properties == null) {
            FileInputStream input = null;
            try {
                File prop = new File("configuracoes.properties");
                properties = new Properties();

                if (!prop.exists()) {
                    FileOutputStream out = new FileOutputStream(prop);
                    properties.setProperty("login.proprietario", "");
                    properties.setProperty("senha.proprietario", "");
                    properties.store(out, "Configurações Iniciais");
                    out.close();
                }

                input = new FileInputStream(prop);
                //Lê os dados que estão no arquivo  
                properties.load(input);
                return properties;
            } catch(FileNotFoundException ex) {
                return null;
            } catch (IOException ex) {
                return null;
            } finally {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            return properties;
        }
    }
    
}
