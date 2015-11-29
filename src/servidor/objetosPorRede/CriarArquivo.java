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
public class CriarArquivo implements Serializable {
    public String nomeArquivo;

    public CriarArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    
}
