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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leonardo
 */
public class ComunicadorCliente extends Comunicador {

    // entrada e saída de dados
    ObjectOutputStream output;
    ObjectInputStream input;

    // Ponte que possui a conexão
    Socket socket;

    public ComunicadorCliente(String name, int port) {
        super(name, port);
    }

    /**
     * construtor usado pelo servidor já possui o socket
     */
    ComunicadorCliente(Socket socket, int port) throws IOException {
        this("", port);
        this.socket = socket;
        this.setInputAndOutput();
    }

    /**
     * Tenta se conectar a um IP
     */
    public boolean connectToIp(String ip) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Conectado ao IP: " + ip);

            // adiquire input e output
            this.setInputAndOutput();
        } catch (IOException ex) {
            // ocorreu um erro
            return false;
        }
        this.isConectado = true;
        // começa a ouvir objetos recebidos
        return true;
    }

    public boolean sendMsg(Object obj) {
        try {
            output.writeObject(obj);
        } catch (IOException ex) {
            Logger.getLogger(ComunicadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * função chamada assim que recebe um objeto
     */
    public void onRecive(Object obj) {
        System.out.println("Cliente.onRecive("+this+"): "+obj.toString());
    }

    private void startListen() {
        new Thread(new ListenerInput()).start();
    }

    private void setInputAndOutput() throws IOException {
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        // começa a ouvir
        this.startListen();
    }

    /**
     * Classe que ouve os objetos recebidos
     */
    private class ListenerInput implements Runnable {

        @Override
        public void run() {
            while (true) {
                Object objRecebido;
                try {
                    objRecebido = input.readObject();

                    // já recebi o obj
                    // só preciso chamar a função
                    onRecive(objRecebido);
                } catch (IOException | ClassNotFoundException ex) {
                    // a conexão caiu
                    onClose();
                    break;
                }
            }
        }

    }

    @Override
    public boolean close() {
        
        // agora eu posso fechar as conexões
        try {
            // fehca as conexões
            input.close();
            output.close();
            socket.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    

}
