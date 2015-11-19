/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Leonardo
 */
public abstract class Comunicador {

    /**
     * ****************************************************
     * Variáveis
     *****************************************************
     */
    // porta de comunicação
    int port;

    // nome do comunicador
    String name;

    // diz se está conectado ao não
    boolean isConectado = false;

    /**
     * ****************************************************
     * Construtores
     *****************************************************
     */
    public Comunicador(String name, int port) {
        this.name = name;
        this.port = port;
    }
    

    /**
     * ****************************************************
     * Métodos
     ******************************************************/
    
    /**
     * função que fecha a conexão
     */
    public abstract boolean close();
    
    
    /**
     * função que é chamada assim que a conexão é fechada
     */
    public void onClose() {
        // por padrão, não faz nd
    }
    
    
    /**
     * ****************************************************
     * Main
     *****************************************************
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 1234;
        ComunicadorServidor servidor = new ComunicadorServidor("Servidor", port);
        servidor.start();

        // o servidor já está rodando
        int maxClientes = 3;
        ComunicadorCliente[] clientes = new ComunicadorCliente[maxClientes];
        for (int i = 0; i < maxClientes; i++) {
            clientes[i] = new ComunicadorCliente("Cliente "+i, port) {

                @Override
                public void onRecive(Object obj) {
                    System.out.println("cliente "+this.name+" recebido: "+obj);
                }

                @Override
                public void onClose() {
                    System.out.println("A conexão foi perdida: "+this.name);
                }

            };
            clientes[i].connectToIp("127.0.0.1");
        }
        
        clientes[0].sendMsg("oi");
        
        Thread.sleep(500);
        clientes[1].close();
        
        servidor.close();
    }

}
