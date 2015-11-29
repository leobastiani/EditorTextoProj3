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


    // socket de servidor que armazena a informação
    private ServerSocket serverSocket;

    // número máximo de clients
    int maxClients = 10;

    // objeto com todos os clientes que o servidor possui
    public Vector<ClienteRedirecionador> clientes = new Vector(maxClients);

    
    public ComunicadorServidor(String name, int port) {
        super(name, port);
    }

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
                        ClienteRedirecionador cliente = new ClienteRedirecionador(socket, port, ComunicadorServidor.this);
                        clientes.add(cliente);
                        System.out.println("Conectado!");
                        onClienteConectado(cliente);
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

    public void onRedirecionar(Object obj, ComunicadorCliente quemEnviou) throws IOException {
        // para todos os clientes presentes
        for (ClienteRedirecionador cliente : clientes) {
            if (cliente != quemEnviou) {
                cliente.sendMsg(obj);
            }
        }
    }

    /**
     * Função chamada assim que um cliente é terminado
     */
    public void onClienteClose(ComunicadorCliente cliente) {
        System.out.println("onClienteClose: " + cliente.toString());
    }

    /**
     * Função chamada assim que o cliente é conectado
     */
    private void onClienteConectado(ClienteRedirecionador cliente) {
        System.out.println("onClienteConectado: " + cliente.toString());
    }

    /**
     * Função chamada assim que o servidor recebe um objeto
     */
    public boolean onRecive(Object obj, ComunicadorCliente quemEnviou) {
        System.out.println("Servidor.onRecive("+quemEnviou+"): "+ obj.toString());
        return true; // sempre redireciona
    }
    
    /**
     * Classe Cliente que redireciona
     */
    public class ClienteRedirecionador extends ComunicadorCliente {

        // servidor que me possui
        private ComunicadorServidor servidor;
        
        // neste objeto, vc pode ter mais informações a respeito do cliente dentro
        // do servidor
        public Object maisInformacoes = null;

        public ClienteRedirecionador(Socket socket, int port, ComunicadorServidor servidor) throws IOException {
            super(socket, port);
            this.servidor = servidor;
        }

        @Override
        public void onRecive(Object obj) {
            try {
                // se o servidor decidir que deve redirecionar
                if (servidor.onRecive(obj, this)) {
                    servidor.onRedirecionar(obj, this);
                }
            } catch (IOException ex) {
                onClose();
            }
        }

        @Override
        public void onClose() {
            servidor.onClienteClose(this);
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
