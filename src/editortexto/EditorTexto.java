/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editortexto;

import comunicador.ComunicadorCliente;
import servidor.objetosPorRede.*;

/**
 * Sumida, edite aqui!
 * @author Leonardo
 */
public class EditorTexto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        ComunicadorCliente cliente = new ComunicadorCliente("Nome do cliente") {

            @Override
            public void onRecive(Object obj) {
                super.onRecive(obj);
                if (obj instanceof TodosDocumentosDisponiveis) {
                    // faz alguma coisa com o obj
                    TodosDocumentosDisponiveis objConvertido = (TodosDocumentosDisponiveis) obj;
                    System.out.println("Recebi um TodosDoc...: "+objConvertido);
                }
                else if (obj instanceof ArquivoConteudo) {
                    ArquivoConteudo objConvertido = (ArquivoConteudo) obj;
                    System.out.println("Recebi o conteudo de um arquivo: "+objConvertido.nomeArquivo+":"+objConvertido.conteudoArquivo);
                }
            }
            
        };
        cliente.connectToIp("127.0.0.1");
        
        cliente.sendMsg(new CriarArquivo("leo.htm"));
        cliente.sendMsg(new TodosDocumentosDisponiveis.Request());
        cliente.sendMsg(new AbrirArquivoRequest("leo.htm"));
        
        
        Thread.sleep(10000);
        cliente.close();
    }
    
}
