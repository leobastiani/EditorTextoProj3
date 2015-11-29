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
 * Classe base para a edição do Comunicador
 * Implementar os métodos começado com "on"
 * 
 * @author Leonardo
 */
public abstract class Comunicador {
    // porta padrão para este comunicador
    public static int defaultPort = 1234;

    /**
     * Variáveis
     */
    // porta de comunicação
    int port;

    // nome do comunicador
    String name;

    // diz se está conectado ao não
    boolean isConectado = false;

    /**
     * Construtores
     */
    public Comunicador(String name, int port) {
        this.name = name;
        this.port = port;
    }

    /**
     * Métodos
     */
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
     * função chamada assim que acontece uma conexão
     */
    public void onConnect() {
        // por padrão, não faz nada
    }

    /**
     * função chamada se não foi possível efetuar a conexão
     */
    public void onFail() {
        // por padrão, não faz nada
    }

    /**
     * Main
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 1234;
        ComunicadorServidor servidor = new ComunicadorServidor("Servidor", port) {

            @Override
            public void onClose() {
                System.out.println("Encerrando o Servidor");
            }

            @Override
            public void onClienteClose(ComunicadorCliente cliente) {
                System.out.println("Cliente " + cliente + " desconectado");
            }

        };
        servidor.start();

        // o servidor já está rodando
        int maxClientes = 3;
        ComunicadorCliente[] clientes = new ComunicadorCliente[maxClientes];
        for (int i = 0; i < maxClientes; i++) {
            clientes[i] = new ComunicadorCliente("Cliente " + i, port) {

                @Override
                public void onRecive(Object obj) {
                    System.out.println(this.name + ": recebido: " + obj);
                }

                @Override
                public void onClose() {
                    System.out.println(this.name + ": A conexão foi perdida");
                }

            };
            clientes[i].connectToIp("127.0.0.1");
        }

        clientes[0].sendMsg("oi");

        Thread.sleep(500);
        clientes[1].close();

        Thread.sleep(500);

        servidor.close();
    }
    
    
    @Override
    public String toString() {
        if (name.equals("")) {
            return super.toString();
        }
        return name;
    }
}
