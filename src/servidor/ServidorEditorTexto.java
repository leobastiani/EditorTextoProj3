/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import servidor.objetosPorRede.*;
import comunicador.*;
import documentos.Documentos;
import java.io.*;
import static java.lang.System.exit;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe do servidor com main que deve ser rodada
 * @author Leonardo
 */
public class ServidorEditorTexto extends ComunicadorServidor {
    
    public static String pastaPadrao = "documents/";

    public ServidorEditorTexto(String name, int port) {
        super(name, port);
    }

    public ServidorEditorTexto(String name) {
        super(name);
    }

    @Override
    public void onRedirecionar(Object obj, ComunicadorCliente quemEnviou) throws IOException {
        super.onRedirecionar(obj, quemEnviou); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean onRecive(Object obj, ComunicadorCliente quemEnviou) {
        System.out.println("Servidor.onRecive("+obj+")");
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
        if (obj instanceof SalvarArquivo) {
            SalvarArquivo objConvertido = (SalvarArquivo) obj;
            salvarArquivo(objConvertido);
            
            // agora que um cliente salvou um arquivo, todos os clientes que
            // estão editando esse mesmo arquivo devem carregar as novas
            // informações
            String nomeArquivo = objConvertido.getNomeArquivo();
            ArquivoConteudo enviarConteudo =
                    new ArquivoConteudo(objConvertido.getNomeArquivo(), objConvertido.getConteudoArquivo());
            for (ClienteRedirecionador cliente : clientes) {
                if (cliente == quemEnviou) {
                    // eh o msmo cara que mandou salvar
                    continue;
                }
                
                MaisInformacoesCliente infoCliente = getMaisInformacoes(cliente);
                if (!infoCliente.getNomeArquivo().equals(nomeArquivo)) {
                    // se o cliente para redirecionar
                    // não está editando o mesmo arquivo
                    // não envia nada
                    continue;
                }
                
                // se está editando o mesmo arquivo
                cliente.sendMsg(enviarConteudo);
            }
            
            // não se propaga
            // na verdade, propaga outro objeto
            return false;
        }
        if (obj instanceof Login) {
            // apenas muda o nome do cliente
            quemEnviou.setName(((Login) obj).name);
            return false;
        }
        if (obj instanceof CriarArquivo) {
            CriarArquivo objConvertido = (CriarArquivo) obj;
            
            // o servidor deve criar o arquivo caso ele não exista
            File arquivo = new File(Documentos.getFilePath(objConvertido.nomeArquivo));
            if (arquivo.exists()) {
                // o arquivo já existe
                return false;
            }
            // devo cria-lo
            FileWriter arquivoEscrever;
            try {
                arquivoEscrever = new FileWriter(arquivo);
                arquivoEscrever.write("");
                arquivoEscrever.close();
            } catch (IOException ex) {
            }
            return false;
        }
        return true;
    }
    
    private static void teste() throws InterruptedException {
        ServidorEditorTexto server = new ServidorEditorTexto("Servidor");
        if(server.start() == false) {
            System.out.println("Não foi possível iniciar o servidor!");
            System.exit(0);
        }
        
        ComunicadorCliente cliente1 = new ComunicadorCliente("cliente 1");
        String localhost = "127.0.0.1";
        cliente1.connectToIp(localhost);
        cliente1.sendMsg(new Login("cliente 1"));
        cliente1.sendMsg(new CriarArquivo("md5.htm"));
        
        ComunicadorCliente cliente2 = new ComunicadorCliente("cliente 2");
        cliente2.connectToIp(localhost);
        
        cliente2.sendMsg(new AbrirArquivoRequest("md5.htm"));
        cliente1.sendMsg(new AbrirArquivoRequest("md5.htm"));
        
        
        Thread.sleep(500);
        Random randGen = new Random();
        String randStr = Integer.toString(randGen.nextInt());
        cliente2.sendMsg(new SalvarArquivo("md5.htm", randStr));
        
            
        Thread.sleep(500);
        exit(0);
    }
    
    public static void main(String[] args) throws InterruptedException {
        // use a linha debaixo para testar
        // teste();
        
        ServidorEditorTexto server = new ServidorEditorTexto("Servidor", Comunicador.defaultPort);
        if(server.start() == false) {
            System.out.println("Não foi possível iniciar o servidor!");
            System.exit(0);
        }
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
    
    
    public void salvarArquivo(SalvarArquivo salvarArquivo) {
        System.out.println("Salvando o arquivo fisicamente");
        try {
            FileWriter arquivo = new FileWriter(Documentos.pastaPadrao+"/"+salvarArquivo.getNomeArquivo());
            arquivo.write(salvarArquivo.getConteudoArquivo());
            arquivo.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorEditorTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
