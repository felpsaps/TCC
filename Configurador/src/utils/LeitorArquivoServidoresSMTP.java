/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.*;
import java.util.Properties;

/**
 *
 * @author Felps
 */
public class LeitorArquivoServidoresSMTP {
    
    private static Properties properties;
    
    public static Properties getInstance() {
        if (properties == null) {
            FileInputStream input = null;
            try {
                File prop = new File("servidoresSMTP.properties");
                properties = new Properties();

                if (!prop.exists()) {
                    FileOutputStream out = new FileOutputStream(prop);
                    properties.setProperty("gmail", "smtp.gmail.com/465");
                    properties.setProperty("yahoo", "smtp.mail.yahoo.com.br/465");
                    properties.setProperty("hotmail", "smtp.live.com/587");
                    properties.store(out, "Servidores SMTP");
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
