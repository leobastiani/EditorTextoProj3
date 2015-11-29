/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temporizador;

import InterfaceRede.TextoSincronizado;
import comunicador.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;

/**
 * Use esta classe para enviar que, de tempos em tempos, você envie as altera
 * ções provocadas no Arquivo
 * @author Leonardo
 */
public class SincronizarArquivo extends SelfRunnable {
    private JEditorPane editorTexto;
    private static int intervaloMiliS = 5000;
    private ComunicadorCliente comunicador;
    private String nomeArquivo;
    
    /**
     * Função chamada assim que ocorrer uma sincronização
     */
    public void onDone() {
        // por padrão, não faz nada
    }
    
    /**
     * Função que é executada assim que você tenta enviar um arquivo
     * para o servidor, mas uma falha acontece
     */
    public void onFail() {
        
    }

    public SincronizarArquivo(String nomeArquivo, JEditorPane editorTexto, ComunicadorCliente comunicador) {
        this.editorTexto = editorTexto;
        this.comunicador = comunicador;
        this.nomeArquivo = nomeArquivo;
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                // o comunicador deve enviar o objeto que sincroniza
                boolean enviadoComSucesso = comunicador.sendMsg(new TextoSincronizado(nomeArquivo, editorTexto.getText()));
                if (enviadoComSucesso) {
                    onDone();
                } else {
                    onFail();
                }

                Thread.sleep(intervaloMiliS);
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
}
