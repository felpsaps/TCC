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
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;

import dao.FuncionarioDao;
import dao.VagaDAO;

import principal.Email;
import principal.FuncionarioBean;
import principal.ListaUsuarios;
import principal.ServidorSMTP;
import principal.VagaBean;
import smsSender.SMSSender;

public class SensorController {

	public static final int VAGA_OCUPADA = 0;
	public static final int VAGA_DISPONIVEL = 1;
	public SensorController() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
		Enumeration<CommPortIdentifier> listaDePortas; 
		listaDePortas = CommPortIdentifier.getPortIdentifiers(); 
		CommPortIdentifier cp = null; 

		while (listaDePortas.hasMoreElements()) { 
			CommPortIdentifier ips = listaDePortas.nextElement(); 
			cp = CommPortIdentifier.getPortIdentifier(ips.getName());
		}

		CommPort commPort = cp.open("teste",2000);	
		
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
    	List<VagaBean> vagas;
    	VagaDAO vgDAO;
        
        public SerialReader ( InputStream in )
        {
        	vgDAO = new VagaDAO();
            this.in = in;
    		vagas = vgDAO.selectVagas();
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1];
            int len = -1;
            int leds = 0;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 ) {
                	String ret = new String(buffer,0,len);
                	if (ret.length() > 0 && ret.codePointAt(0) != 49) {
                		if (leds == vagas.size()) {
                    		//System.out.println();
                    		leds = 0;
                		}
                		VagaBean vg = vagas.get(leds);
                		Integer valor = new String(buffer,0,len).codePointAt(0);
                		// GRAVA NO BD AS MUDANÇAS DE STATUS
                		//System.out.print(new String(buffer,0,len).codePointAt(0) + " - ");
                		if (!vg.getDisponibilidade().equals(valor)) {
                			System.out.println("Atualizando vaga nro " + vg.getNro() + " de: " + vg.getDisponibilidade() + " para: " + valor);
                			vg.setDisponibilidade(valor);

                			vgDAO.updateDisponibilidade(vg);
                			vgDAO.insertEstatistica(vagas);
                			// QUANDO O STATUS FOR 0, OU SEJA, ALGUEM ENTROU NA VAGA
                			if (valor.equals(VAGA_OCUPADA)) {
                				ListaUsuarios listaUsr = ListaUsuarios.getInstance();
                				// VERIFICA SE A VAGA ERA RESERVADA E SE TALVEZ ALGUEM NAO AUTORIZADO TENHA ESTACIONADO NELA
                				/* A LOGICA AQUI EH A SEGUINTE:
                				 * QUANDO ALGUEM ENTRA COM O CODIGO DE BARRA, A PESSOA ENTRA EM UMA FILA
                				 * QUANDO ALGUEM ESTACIONA O SISTEMA ASSUME QUE QUEM ESTA ESTACIONANDO EH A PROXIMA PESSOA
                				 * DA FILA. HA ENTAO UMA MARGEM DE ERRO, JA QUE 2 PESSOAS PODEM ENTRAR E A SEGUNDA PODE ESTACIONAR 
                				 * ANTES QUE A PRIMEIRA */
                				if(vg.getUsrReservadoId() != null) {
                					FuncionarioBean func = listaUsr.getPrimeiroFunc();
                					if (func != null && !func.getCodigo().equals(vg.getUsrReservadoId())) {
                						/* ENVIA EMAIL E SMS AO FUNCIONARIO
                						 * AVISANDO QUE ELE PODE TER PARADO EM VAGA NAO AUTORIZADA
                						 * TAMBEM EMVIA MENSAGENS PARA OS ADMINISTRADORES*/
                						FuncionarioBean funcAut = null;
                						try {
                							funcAut = new FuncionarioDao().buscarPorCodigo(vg.getUsrReservadoId());
                						} catch (SQLException ex) {
                							ex.printStackTrace();
                						}
                						System.out.println("Usuario " + func.getNome() + " entrou em vaga nao autorizada");
                						SMSSender sms =  new SMSSender();
                						String smsStr = "Atencao! Se voce estacionou na vaga "+vg.getNro()+" e voce nao e "+funcAut.getNome()+", por favor retire seu veiculo!";
                						sms.sendSms(func.getCelular(), smsStr);
                						
                						ServidorSMTP smtp = null;
										try {
											smtp = new FuncionarioDao().getServidor();
										} catch (SQLException e) {
											e.printStackTrace();
										}
                						Email mail = new Email(smtp.getEnderecoServidor(), smtp.getPorta(), smtp.getEmail(), smtp.getSenha());
                						String mailStr = "Caro " + func.getNome() +".\n\nA vaga " + vg.getNro() + " está reservada.\n" +
                										 "Se você estacionou nela e você não é "+funcAut.getNome()+" por favor retire seu veículo, caso contrário favor desconsiderar esta mensagem. \n\n Obrigado.";
                						mail.sendMail(func.getEmail(), "Vaga Não Autorizada!", mailStr);
                						
                						/* GRAVA REGISTRO DE VAGA NAO AUTORIZADA PARA MOSTRAR MENSAGEM AO ADM*/
                						vgDAO.vagaNaoAltorizada(func, vg);
                					}
                					
                				}
            					// APENAS REMOVE O FUNCIONARIO DA LISTA
            					listaUsr.removePrimeiroFunc();
                			}
                		}
                		leds++;
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
