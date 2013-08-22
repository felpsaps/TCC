package harware;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class SensorController {

	public SensorController() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
		Enumeration<CommPortIdentifier> listaDePortas; 
		listaDePortas = CommPortIdentifier.getPortIdentifiers(); 
		CommPortIdentifier cp = null; 

		while (listaDePortas.hasMoreElements()) { 
			CommPortIdentifier ips = listaDePortas.nextElement(); 

			cp = CommPortIdentifier.getPortIdentifier(ips.getName());
		}
		
		
		CommPort commPort = cp.open("sensor",2000);

		if ( commPort instanceof SerialPort )
		{
			SerialPort serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

			InputStream in = serialPort.getInputStream();
			OutputStream out = serialPort.getOutputStream();

			out.write("INICIAR".getBytes());
			(new Thread(new SerialReader(in))).start();
			out.write("INICIAR".getBytes());

		}
	}
	
	public static class SerialReader implements Runnable {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1];
            int len = -1;
            int leds = 0;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                	String ret = new String(buffer,0,len);
                	if (ret.length() > 0 && ret.codePointAt(0) != 49) {
                		if (leds == 8) {
                    		System.out.println();
                    		leds = 0;
                		}
                		// TODO GRAVAR NO BD AS MUDANÇAS DE STATUS
                		leds++;
                		System.out.print(new String(buffer,0,len).codePointAt(0) + " - ");
                	}
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
	}

}
