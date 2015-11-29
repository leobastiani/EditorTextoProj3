/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documentos;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * Usar somente a função getDocumentosDisponiveis que retorna
 * um array com todos os documentos na pasta ./documents/
 * @author Leonardo
 */
public class Documentos {
    
    public static final String pastaPadrao = "documents";

    public static void main(String args[]) throws IOException {
        ArrayList<String> documentos = getDocumetosDisponiveis();
        for (String documento : documentos) {
            System.out.println(documento);
        }
    }
    
    /**
     * Função que devolve todos os documentos disponíveis
     * no caso, são todos os documentos dentro de ./documents/
     */
    public static ArrayList<String> getDocumetosDisponiveis() {
        String glob = "glob:**.*";
        String location = "./"+pastaPadrao+"/";
        ArrayList<String> result = new ArrayList<>();

        try {
            final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(
                    glob);

            Files.walkFileTree(Paths.get(location), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path path,
                        BasicFileAttributes attrs) throws IOException {
                    if (pathMatcher.matches(path)) {
                        result.add(path.getFileName().toString());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                        throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
        }
        return result;
    }

    /**
     * Devolve o direitório do arquivo na pasta padrão
     */
    public static String getFilePath(String arquivo) {
        return "./"+pastaPadrao+"/"+arquivo;
    }
}
