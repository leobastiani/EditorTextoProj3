/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temporizador;

/**
 * Classe que você instancia um objeto
 * dá um start e um interrupt nela msma
 * não precisa criar um objeto de Thread
 * @author Leonardo
 */
public abstract class SelfRunnable implements Runnable {
    Thread minhaThread = null;

    @Override
    public abstract void run();
    
    /**
     * Se inicia com o intervalo dado
     */
    public boolean start() {
        if (minhaThread != null) {
            // a thread já está rodando
            return false;
        }
        
        minhaThread = new Thread(this);
        return true;
    }
    
    /**
     * Método que para a thread
     */
    public boolean interrupt() {
        if (minhaThread == null) {
            // a thread nem estava rodando
            return false;
        }
        
        minhaThread.interrupt();
        minhaThread = null;
        return true;
    }
}
