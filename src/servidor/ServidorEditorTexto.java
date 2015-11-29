/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import servidor.objetosPorRede.AbrirArquivoRequest;
import comunicador.*;
import java.io.IOException;
import java.util.Vector;
import servidor.objetosPorRede.ArquivoConteudo;

/**
 * Classe do servidor com main que deve ser rodada
 * @author Leonardo
 */
public class ServidorEditorTexto extends ComunicadorServidor {
    
    public static String pastaPadrao = "documents/";

    public ServidorEditorTexto(String name, int port) {
        super(name, port);
    }

    @Override
    public void onRedirecionar(Object obj, ComunicadorCliente quemEnviou) throws IOException {
        super.onRedirecionar(obj, quemEnviou); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean onRecive(Object obj, ComunicadorCliente quemEnviou) {
        if (obj instanceof AbrirArquivoRequest) {
            // Requisição de abrir um arquivo
            // devolve o conteúdo do arquivo pelo nome
            String nomeArquivo = ((AbrirArquivoRequest) obj).getNomeArquivo();
            // diz que esse cliente está editando esse arquivo agora
            getMaisInformacoes(quemEnviou).setNomeArquivo(nomeArquivo);
            
            // devolve a ele o conteúdo do arquivo
            quemEnviou.sendMsg(new ArquivoConteudo(nomeArquivo));
            
            return false;
        }
        return true;
    }
    
    private static void teste() {
        ServidorEditorTexto server = new ServidorEditorTexto("Servidor", 1234);
        if(server.start() == false) {
            System.out.println("Não foi possível iniciar o servidor!");
            System.exit(0);
        }
        
        ComunicadorCliente cliente1 = new ComunicadorCliente("cliente 1", 1234);
        String localhost = "127.0.0.1";
        cliente1.connectToIp(localhost);
        
        ComunicadorCliente cliente2 = new ComunicadorCliente("cliente 2", 1234);
        cliente2.connectToIp(localhost);
        
        cliente2.sendMsg(new AbrirArquivoRequest("leo.htm"));
    }
    
    public static void main(String[] args) {
        teste();
    }

    /**
     * garante que maisInformacoes do cliente não seja null
     */
    private void setMaisInformacoes(ComunicadorCliente cliente) {
        ComunicadorServidor.ClienteRedirecionador clienteRedirecionador = 
                (ComunicadorServidor.ClienteRedirecionador) cliente;
        if (clienteRedirecionador.maisInformacoes != null) {
            // se já está instanciada
            return ; // não preciso instanciar
        }
        clienteRedirecionador.maisInformacoes = new MaisInformacoesCliente();
    }
    
    /**
     * Garante que não vai retornar null
     * e retorna as maisInformacoes que o cliente possui
     */
    private MaisInformacoesCliente getMaisInformacoes(ComunicadorCliente cliente) {
        setMaisInformacoes(cliente);
        
        ComunicadorServidor.ClienteRedirecionador clienteRedirecionador = 
                (ComunicadorServidor.ClienteRedirecionador) cliente;
        return (MaisInformacoesCliente) clienteRedirecionador.maisInformacoes;
    }
    
}
