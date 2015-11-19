/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leonardo
 *
 * Devo apenas adicionar as funções de servidor padrões
 */
public class ComunicadorServidor extends Comunicador {

    /**
     * ****************************************************
     * Variáveis
     *****************************************************
     */
    // socket de servidor que armazena a informação
    private ServerSocket serverSocket;

    // número máximo de clients
    private int maxClients = 10;

    // objeto com todos os clientes que o servidor possui
    private Vector<ClienteRedirecionador> clientes = new Vector(maxClients);

    /**
     * ****************************************************
     * Construtores
     *****************************************************
     */
    public ComunicadorServidor(String name, int port) {
        super(name, port);
    }

    /**
     * ****************************************************
     * Métodos
     *****************************************************
     */
    /**
     * Inicializa o servidor
     */
    private boolean initServer() {
        // o servidor já está aberto
        if (isConectado) {
            return false;
        }

        try {
            serverSocket = new ServerSocket(port, maxClients);
            System.out.println("Servidor aberto pela porta: " + port);
        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    /**
     * Coloca o servidor para ouvir
     */
    private void serverListen() {
        // cria uma thread para ficar ouvindo as chamadas
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("Aguardando conexoes...");
                        Socket socket = serverSocket.accept();
                        clientes.add(new ClienteRedirecionador(socket, port, ComunicadorServidor.this));
                        System.out.println("Conectado!");
                    } catch (IOException ex) {
                        // fim da thread
                        return;
                    }
                }
            }
        });
        t.start();
        System.out.println("Servidor escutando");
    }

    private void redirecionar(Object obj, ClienteRedirecionador quemEnviou) throws IOException {
        // para todos os clientes presentes
        for (ClienteRedirecionador cliente : clientes) {
            if (cliente != quemEnviou) {
                cliente.sendMsg(obj);
            }
        }
    }

    private void onCloseCliente(ClienteRedirecionador cliente) {
        System.out.println("Cliente encerrado: "+cliente);
    }

    /**
     * Classe Cliente que redireciona
     */
    private class ClienteRedirecionador extends ComunicadorCliente {

        // servidor que me possui
        private ComunicadorServidor servidor;

        private ClienteRedirecionador(Socket socket, int port, ComunicadorServidor servidor) throws IOException {
            super(socket, port);
            this.servidor = servidor;
        }

        @Override
        public void onRecive(Object obj) {
            try {
                // avisa o servidor para redirecionar
                servidor.redirecionar(obj, this);
            } catch (IOException ex) {
                onClose();
            }
        }

        @Override
        public void onClose() {
            ComunicadorServidor.this.onCloseCliente(this);
        }
    }

    /**
     * Método start padrão para inicializar o servidor
     */
    public boolean start() {
        if (!initServer()) {
            return false;
        }
        serverListen();
        return true;
    }

    @Override
    public void onClose() {
        System.out.println("Encerrando o Servidor");
    }

    @Override
    public boolean close() {
        onClose();
        
        try {
            // fecha a conexão com todos os clientes
            for (ClienteRedirecionador cliente : clientes) {
                if (!cliente.close()) {
                    // se não foi possível encerrar o cliente
                    return false;
                }
            }

            // fecha a si msmo
            serverSocket.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
