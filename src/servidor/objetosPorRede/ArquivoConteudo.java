/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.objetosPorRede;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.ServidorEditorTexto;

/**
 *
 * @author Leonardo
 */
public class ArquivoConteudo implements Serializable {

    public String nomeArquivo;
    public String conteudoArquivo;

    /**
     * Define o nome do arquivo
     * e seu conteúdo é obtido abrindo-se o arquivo
     */
    public ArquivoConteudo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        try {
            // abre o arquivo e obtem seu conteúdo
            String fullPath = ServidorEditorTexto.pastaPadrao + nomeArquivo;
            conteudoArquivo = new String(Files.readAllBytes(Paths.get(fullPath)), "UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(ArquivoConteudo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArquivoConteudo(String nomeArquivo, String conteudoArquivo) {
        this.nomeArquivo = nomeArquivo;
        this.conteudoArquivo = conteudoArquivo;
    }

    @Override
    public String toString() {
        return "ArquivoConteudo("+nomeArquivo+", "+conteudoArquivo+")";
    }
}
