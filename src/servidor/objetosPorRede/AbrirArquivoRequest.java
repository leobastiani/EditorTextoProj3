/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.objetosPorRede;

import java.io.Serializable;

/**
 * Classe que o cliente manda pedindo para o servidor abrir o arquivo
 * @author Leonardo
 */
public class AbrirArquivoRequest implements Serializable {
    private String nomeArquivo;

    public AbrirArquivoRequest(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    @Override
    public String toString() {
        return this.nomeArquivo;
    }
}
