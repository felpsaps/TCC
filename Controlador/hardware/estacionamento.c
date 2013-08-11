//------------------------------
//========== INCLUDES ==========
//------------------------------
#include "8051.H"                //Flags do 8051
#include <stdio.h>

//--------------------------------
//========== PROTITIPOS ==========
//--------------------------------
void TIMER(unsigned int);
//--------------------------------------------
//========== VARIAVEIS DE USO GERAL ==========
//--------------------------------------------
unsigned char DADO=0;

//----------------------------------------------------------------
//========================= TEMPORIZADOR =========================
//----------------------------------------------------------------
void TIMER(unsigned int TEMPO)
{
    unsigned char CONTA1=0;
    unsigned char CONTA2=0;
    unsigned char CONTA3=0;   
    while(CONTA1<=TEMPO)
    {
        CONTA1++;
        CONTA2=0;
        while(CONTA2<=2)
        {
            CONTA2++;
            CONTA3=0;
            while(CONTA3<=90)
            {
            CONTA3++;
            }
        }   
    }   
}

//#############################################################
//-------------------------------------------------------------
//=========================== MAIN ============================
//-------------------------------------------------------------
//#############################################################
void main (void)
{    
    IE=0x90;    //Habilita interrupcoes pela Serial
    TMOD=0x20;  //Timer 1 no modo 2 (8 bits com auto recarga) usado na geração do BaudRate
    //---------- USART ----------
    SCON=0x54;  //canal serial ajustado para modo assincrono
    PCON=0;     //Não Dobra BAUDRATE mantendo em 9600bps                     
    TL1=0xFD;   //carrega taxa de transmissao p/ 9600bps
    TH1=0xFD;   //carrega taxa de transmissao p/ 9600bps
    TR1=1;      //Start Timer p/ geração do Baud Rate da Serial         
    //----- Loop infinito -----
    while (1)        
    { 
        P1=P0;    
    }
}

//#############################################################
//-------------------------------------------------------------
//======================= SUB ROTINAS =========================
//-------------------------------------------------------------
//#############################################################
void int_serial (void) interrupt 4 using 1
{
    TI=0; //Zera flag de Status de envio  
    // ----- Verifica se Int é de RECEPÇÃO ----- RI = 1 quando esta recebendo dados
    if (RI == 1) {
        DADO = P1;
        RI = 0;
    }           
    
    if (DADO == P1) {
        SBUF= 49;
    } else {
        DADO = P1;
        //SBUF=DADO;
        SBUF = P1_0;
        while(TI==0);   //Espera fim da transmissão
        TI=0;
        SBUF = P1_1;
        while(TI==0);   //Espera fim da transmissão
        TI=0;
        SBUF = P1_2;
        while(TI==0);   //Espera fim da transmissão
        TI=0;
        SBUF = P1_3;
        while(TI==0);   //Espera fim da transmissão
        TI=0;
        SBUF = P1_4;
        while(TI==0);   //Espera fim da transmissão
        TI=0;
        SBUF = P1_5;
        while(TI==0);   //Espera fim da transmissão
        TI=0;
        SBUF = P1_6;
        while(TI==0);   //Espera fim da transmissão
        TI=0;
        SBUF = P1_7;
        while(TI==0);   //Espera fim da transmissão         
    }
    TIMER(252); 
}
