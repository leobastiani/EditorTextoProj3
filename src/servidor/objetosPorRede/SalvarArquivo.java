/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.objetosPorRede;

import java.io.Serializable;

/**
 *
 * @author Leonardo
 */
public class SalvarArquivo implements Serializable {
    private String nomeArquivo;
    private String conteudoArquivo;

    public SalvarArquivo(String nomeArquivo, String conteudoArquivo) {
        this.nomeArquivo = nomeArquivo;
        this.conteudoArquivo = conteudoArquivo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getConteudoArquivo() {
        return conteudoArquivo;
    }

    public void setConteudoArquivo(String conteudoArquivo) {
        this.conteudoArquivo = conteudoArquivo;
    }

    @Override
    public String toString() {
        return "SalvarArquivo("+nomeArquivo+", "+conteudoArquivo+")";
    }
    
}
