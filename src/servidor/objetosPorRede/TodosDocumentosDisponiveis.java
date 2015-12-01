/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.objetosPorRede;

import documentos.Documentos;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Mande um Request e receba este objeto
 * this.documentos tem todos os documentos disponíveis
 * @author Leonardo
 */
public class TodosDocumentosDisponiveis implements Serializable {
    public class Request implements Serializable {
        
    }
    
    public String[] documentos;

    public TodosDocumentosDisponiveis() {
        ArrayList<String> documentosArray = Documentos.getDocumetosDisponiveis();
        // converte para String[]
        documentos = (String[]) documentosArray.toArray();
    }
    
}
