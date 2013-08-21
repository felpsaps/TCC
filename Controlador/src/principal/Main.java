package principal;


import GUI.TelaPrincipal;
import dao.Banco;
import dao.FuncionarioDao;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import harware.SensorController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Felps
 */
public class Main {
     public static void main(String[] args) throws SQLException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        new TelaPrincipal();
        new SensorController();
    }
}
