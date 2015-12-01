/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editortexto;

import comunicador.ComunicadorCliente;
import servidor.objetosPorRede.CriarArquivo;

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
                // onRecive eh oq acontece quando o cliente recebe uma msg
                System.out.println(obj.toString());
            }

            @Override
            public void onClose() {
                System.out.println("Cliente fechou!");
            }
            
            
            
        };
        cliente.connectToIp("127.0.0.1");
        
        cliente.sendMsg(new CriarArquivo("leo.htm"));
        
        
        Thread.sleep(10000);
        cliente.close();
    }
    
}
