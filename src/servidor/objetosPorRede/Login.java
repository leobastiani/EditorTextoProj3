/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.objetosPorRede;

import java.io.Serializable;

/**
 * Classe que envia seu nome para login
 * @author Leonardo
 */
public class Login implements Serializable {
    public String name;

    public Login(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Login("+name+")";
    }
}
