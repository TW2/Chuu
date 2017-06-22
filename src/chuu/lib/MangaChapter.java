/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.lib;

import chuu.MainFrame;
import chuu.crypto.Checksum;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Naruto
 */
public class MangaChapter {
    
    private File zip;
    private File imageFile;
    
    private String nom;
    private String numero;
    private String version;
    private String cleans;    
    private String trads;
    private String checks;
    private String edits;
    private String qcs;
    
    private String clientMangasPath;
        
    private String team;
    private String tome;
    private String arc;
    private String chapitre;
    
    private String description;
    private String resume;
    
    private String md5;

    public MangaChapter() {
        init();
    }

    public MangaChapter(File zip, File imageFile, String nom, String numero, 
            String version, String cleans, String trads, String checks, 
            String edits, String qcs, String tome, String team, 
            String description, String resume, String arc, String chapitre) {
        this.zip = zip;
        this.imageFile = imageFile;
        this.nom = nom;
        this.numero = numero;
        this.version = version;
        this.cleans = cleans;
        this.trads = trads;
        this.checks = checks;
        this.edits = edits;
        this.qcs = qcs;
        
        this.team = team;
        this.tome = tome;
        this.arc = arc;
        this.chapitre = chapitre;
        
        this.description = description;
        this.resume = resume;
        
        this.md5 = getMD5(imageFile);
        
        init();
    }
    
    private void init(){
        clientMangasPath = MainFrame.getDocumentsFolder();
    }
    
    public void setZip(File zip) {
        this.zip = zip;
    }

    public File getZip() {
        return zip;
    }

    public void setImageFile(File imageFile) {
        setImageFile(imageFile, false);
    }
    
    public void setImageFile(File imageFile, boolean createMD5) {
        this.imageFile = imageFile;
        if(createMD5 == true){
            this.md5 = getMD5(imageFile);
        }
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setCleans(String cleans) {
        this.cleans = cleans;
    }

    public String getCleans() {
        return cleans;
    }

    public void setTrads(String trads) {
        this.trads = trads;
    }

    public String getTrads() {
        return trads;
    }

    public void setChecks(String checks) {
        this.checks = checks;
    }

    public String getChecks() {
        return checks;
    }

    public void setEdits(String edits) {
        this.edits = edits;
    }

    public String getEdits() {
        return edits;
    }

    public void setQcs(String qcs) {
        this.qcs = qcs;
    }

    public String getQcs() {
        return qcs;
    }

    public void setClientMangasPath(String clientMangasPath) {
        this.clientMangasPath = clientMangasPath;
    }

    public String getClientMangasPath() {
        return clientMangasPath;
    }

    public void setTome(String tome) {
        this.tome = tome;
    }

    public String getTome() {
        return tome;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getResume() {
        return resume;
    }

    public void setArc(String arc) {
        this.arc = arc;
    }

    public String getArc() {
        return arc;
    }

    public void setChapitre(String chapitre) {
        this.chapitre = chapitre;
    }

    public String getChapitre() {
        return chapitre;
    }
    
    public void setMD5(String md5){
        this.md5 = md5;
    }
    
    public String getMD5(){
        return getMD5(false);
    }
    
    public String getMD5(boolean force){
        if(force == true){
            return getMD5(zip);
        }else{
            return md5;
        }
    }
    
    private String getMD5(File file){
        String md5String = "";
        
        try {
            md5String = Checksum.getMD5Checksum(MainFrame.getDocumentsFolder()+File.separator+file.getName());
        } catch (Exception ex) {
            Logger.getLogger(MangaChapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return md5String;
    }
    
    public BufferedImage getResize(int width, int height){
        BufferedImage img = null;
            
        try {    
            BufferedImage image;
            File f = new File(getClientMangasPath(), imageFile.getName());
            image = ImageIO.read(f);
            if(image.getWidth() > image.getHeight()){
                //img width => width
                //img height => h
                int h = width*image.getHeight()/image.getWidth();
                img = new BufferedImage(width, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = img.createGraphics();
                g2d.drawImage(image, 0, 0, width, h, 0, 0, image.getWidth(), image.getHeight(), null);
                g2d.dispose();
            }else if(image.getWidth() < image.getHeight()){
                //img width => w
                //img height => height
                int w = height*image.getWidth()/image.getHeight();
                img = new BufferedImage(w, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = img.createGraphics();
                g2d.drawImage(image, 0, 0, w, height, 0, 0, image.getWidth(), image.getHeight(), null);
                g2d.dispose();
            }else{
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = img.createGraphics();
                g2d.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
                g2d.dispose();
            }
        } catch (IOException ex) {
            Logger.getLogger(MangaChapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return img;
    }
    
}
