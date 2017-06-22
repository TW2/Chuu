/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.filefilter;

import java.io.File;

/**
 *
 * @author Naruto
 */
public class ImageFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File f) {
        //Pour voir tous les dossiers :
        if (f.isDirectory()) {
            return true;
        }

        //Montre tous les fichiers au format ADF : Ass Drawing File
        if(f.getName().endsWith(".jpg") | f.getName().endsWith(".jpeg") |
                f.getName().endsWith(".png") | f.getName().endsWith(".gif") |
                f.getName().endsWith(".bmp")){
            return true;
        }

        //C'est tout, on n'a pas besoin d'en voir plus.
        return false;
    }

    @Override
    public String getDescription() {
        //Montre ceci dans le sélecteur :
        return "Fichiers Image (*.jpg,*.jpeg,*.png,*.gif,*.bmp)";
    }
}
