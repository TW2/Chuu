/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.ui;

import chuu.MainFrame;
import chuu.filefilter.ImageFilter;
import chuu.lib.LocalSend;
import chuu.lib.MangaChapter;
import chuu.xml.ChaptersXML;
import chuu.xml.LocalSendXML;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Naruto
 */
public class PrepareReleaseDialog extends javax.swing.JDialog {

    public enum ButtonManager{
        OK, Cancel, None;
    }
    
    private ButtonManager bm = ButtonManager.None;
    private final DefaultListModel dlmFiles = new DefaultListModel();
    
    /**
     * Creates new form UploadDialog
     * @param parent
     * @param modal
     */
    public PrepareReleaseDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }
    
    private void init(){
        listFiles.setModel(dlmFiles);
    }
    
    public ButtonManager getButton(){
        return bm;
    }
    
    public void showDialog(){
        setLocationRelativeTo(null);
        fromXML();
        setVisible(true);
    }
    
    private void fromXML(){
        File file = new File(getApplicationDirectory(), "localsend.save");
        if(file.exists() == true){
            LocalSend snd = LocalSendXML.readFile(file);
            String zip = snd.getMangaChapter().getZip().getName();
            tfZipName.setText(zip.substring(0, zip.lastIndexOf(".")));
            tfTomeImage.setText(snd.getMangaChapter().getImageFile().getAbsolutePath());
            tfName.setText(snd.getMangaChapter().getNom());
            spinnerChapterNumber.setValue(Integer.parseInt(snd.getMangaChapter().getNumero()));
            spinnerVersion.setValue(Integer.parseInt(snd.getMangaChapter().getVersion()));
            tfClean.setText(snd.getMangaChapter().getCleans());
            tfTrad.setText(snd.getMangaChapter().getTrads());
            tfCheck.setText(snd.getMangaChapter().getChecks());
            tfEdit.setText(snd.getMangaChapter().getEdits());
            tfQC.setText(snd.getMangaChapter().getQcs());
            spinnerTome.setValue(Integer.parseInt(snd.getMangaChapter().getTome()));
            tfTeam.setText(snd.getMangaChapter().getTeam());
            tfDescription.setText(snd.getMangaChapter().getDescription());
            tfResume.setText(snd.getMangaChapter().getResume());
            tfArc.setText(snd.getMangaChapter().getArc());
            tfChapitreTitre.setText(snd.getMangaChapter().getChapitre());
        }
    }
    
    private void storeXML(){
        File file = new File(getApplicationDirectory(), "localsend.save");
        
        //On récupère les valeurs de connexion
        LocalSend snd = new LocalSend();
//        snd.setServer(tfAddress.getText());
//        snd.setPort(tfPort.getText());
//        snd.setLogin(tfLogin.getText());
//        snd.setPassword(tfPassword.getText());
        
        //Création d'un MangaChapter
        File zipFile = new File(MainFrame.getDocumentsFolder() + File.separator + tfZipName.getText() + ".zip");
        File imageFile = new File(tfTomeImage.getText());
        MangaChapter mc = new MangaChapter(
                zipFile, 
                imageFile,
                tfName.getText(),
                spinnerChapterNumber.getValue().toString(),
                spinnerVersion.getValue().toString(),
                tfClean.getText(),
                tfTrad.getText(),
                tfCheck.getText(),
                tfEdit.getText(),
                tfQC.getText(),
                spinnerTome.getValue().toString(),
                tfTeam.getText(),
                tfDescription.getText(),
                tfResume.getText(),
                tfArc.getText(),
                tfChapitreTitre.getText());
        
        //On récupère le MangaChapter
        snd.setMangaChapter(mc);
        
        //On enregistre
        LocalSendXML.writeFile(snd, file);
    }
    
    private String getApplicationDirectory(){
        if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
            java.io.File file = new java.io.File("");
            return file.getAbsolutePath();
        }
        String path = System.getProperty("user.dir");
        if(path.toLowerCase().contains("jre")){
            File f = new File(getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString()
                    .substring(6));
            path = f.getParent();
        }
        return path;
    }
    
    private String replaceSpace(String s){
        return s.replaceAll(" ", "_");
    }
    
    private void createZip(List<File> files, String path){
        
        File toWrite = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(toWrite.getParent() + File.separator + replaceSpace(toWrite.getName()));
            try (ZipOutputStream zos = new ZipOutputStream(fos)) {
                for(File f : files){
                    addToZipFile(f, zos);
                }
            }
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrepareReleaseDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrepareReleaseDialog.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(fos != null){
                    fos.close();
                }                
            } catch (IOException ex) {
                Logger.getLogger(PrepareReleaseDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void addToZipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException {

	System.out.println("Ecriture de '" + file.getName() + "' dans le zip en cours...");

        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);
            
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            
            zos.closeEntry();
        }
        
        System.out.println("Ecriture de '" + file.getName() + "' dans le zip : OK");
    }
    
//    private boolean send(String server, String port, String user, String pass, File toSend, File XML, File image) {
// 
//        boolean result = false;
//
//        FTPSClient ftpClient = new FTPSClient();
//        
//        ftpClient.setCopyStreamListener(new CopyStreamAdapter() {
//            @Override
//            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
//                System.out.println("Progression de l'upload : " + bytesTransferred + " de " + totalBytesTransferred);
//            }
//        });
//        
//        ftpClient.addProtocolCommandListener(new ProtocolCommandListener() {
//            @Override
//            public void protocolCommandSent(ProtocolCommandEvent pce) {
//                System.out.println(pce.getMessage());
//            }
//
//            @Override
//            public void protocolReplyReceived(ProtocolCommandEvent pce) {
//                System.out.println(pce.getMessage());
//            }
//        });
//        
//        try {
// 
//            ftpClient.connect(server, Integer.parseInt(port));
//            ftpClient.login(user, pass);
//            System.out.print(ftpClient.getReplyString());
//            
//            ftpClient.enterLocalPassiveMode();
//            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
//            System.out.print(ftpClient.getReplyString());
//            
//            ftpClient.execPBSZ(0L);
//            System.out.print(ftpClient.getReplyString());
//            
//            ftpClient.execPROT("P");
//            System.out.print(ftpClient.getReplyString());
//            
// 
//            List<File> files = new ArrayList<>();
//            files.add(toSend);
//            files.add(XML);
//            files.add(image);
//            
//            for(File f : files){
//                String remoteFolder = "/home/Mes Mangas/";//home/Mes Mangas/
//                
//                boolean done;
//                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
//                    System.out.println("Fichier '" + f.getName() + "' en cours d'upload");
//                    done = ftpClient.storeFile(remoteFolder + f.getName(), bis);
//                    System.out.print(ftpClient.getReplyString());
//                }
//                if (done) {
//                    System.out.println("Fichier '" + f.getName() + "' uppé avec succès.");
//                    result = true;
//                }
//            }
//            
// 
//        } catch (IOException ex) {
//            System.out.println("Error: " + ex.getMessage());
//        } finally {
//            try {
//                if (ftpClient.isConnected()) {
//                    ftpClient.logout();
//                    ftpClient.disconnect();
//                }
//            } catch (IOException ex) {
//                System.out.println("Error: " + ex.getMessage());
//            }
//        }
//        
//        return result;
//    }

    public File getResizedImage(int width, int height, File f){
        BufferedImage img;
        File result = new File(MainFrame.getDocumentsFolder(), f.getName());
        
        if(result.exists() == false){
            try {    
                BufferedImage image;
                image = ImageIO.read(f);
                if(image.getWidth() > image.getHeight()){
                    //img width => width
                    //img height => h
                    int h = width*image.getHeight()/image.getWidth();
                    img = new BufferedImage(width, h, BufferedImage.TYPE_3BYTE_BGR);
                    Graphics2D g2d = img.createGraphics();
                    g2d.drawImage(image, 0, 0, width, h, 0, 0, image.getWidth(), image.getHeight(), null);
                    g2d.dispose();
                }else if(image.getWidth() < image.getHeight()){
                    //img width => w
                    //img height => height
                    int w = height*image.getWidth()/image.getHeight();
                    img = new BufferedImage(w, height, BufferedImage.TYPE_3BYTE_BGR);
                    Graphics2D g2d = img.createGraphics();
                    g2d.drawImage(image, 0, 0, w, height, 0, 0, image.getWidth(), image.getHeight(), null);
                    g2d.dispose();
                }else{
                    img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                    Graphics2D g2d = img.createGraphics();
                    g2d.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
                    g2d.dispose();
                }
                ImageIO.write(img, f.getName().substring(f.getName().lastIndexOf(".")+1), result);
            } catch (IOException ex) {
                Logger.getLogger(MangaChapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
        return result;
    }
    
    private void doXmlTXT(String folder){
        //Commence avec XML= alors xml de mangas
        //Commence avec JPG= alors image de mangas
        
        File fold = new File(folder);
        
        //Commençons par scanner le dossier
        List<File> xmlFiles = new ArrayList<>();
        List<File> jpgFiles = new ArrayList<>();
        File[] files = fold.listFiles();
        for(File f : files){
            if(f.getName().endsWith(".xml") == true){
                xmlFiles.add(f);
            }
            if(f.getName().endsWith(".jpg") == true){
                jpgFiles.add(f);
            }
        }
        
        //Ecrivons le fichier TXT
        try {
            try (PrintWriter pw = new PrintWriter(folder + File.separator + "xml.txt")) {
                for(int i = 0; i<xmlFiles.size(); i++){
                    pw.println("XML=" + xmlFiles.get(i).getName());
                }
                for(int i = 0; i<jpgFiles.size(); i++){
                    pw.println("JPG=" + jpgFiles.get(i).getName());
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrepareReleaseDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listFiles = new javax.swing.JList<>();
        btnAddFile = new javax.swing.JButton();
        btnRemoveFile = new javax.swing.JButton();
        tfZipName = new javax.swing.JTextField();
        lblZipName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        tfTeam = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        spinnerTome = new javax.swing.JSpinner();
        spinnerChapterNumber = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        spinnerVersion = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tfClean = new javax.swing.JTextField();
        tfTrad = new javax.swing.JTextField();
        tfCheck = new javax.swing.JTextField();
        tfEdit = new javax.swing.JTextField();
        tfQC = new javax.swing.JTextField();
        tfDescription = new javax.swing.JTextField();
        tfResume = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btnTomeImage = new javax.swing.JButton();
        tfTomeImage = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        tfArc = new javax.swing.JTextField();
        tfChapitreTitre = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnCreateZip = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Envoi");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Informations sur les fichiers"));

        listFiles.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listFiles);

        btnAddFile.setText("Ajouter");
        btnAddFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFileActionPerformed(evt);
            }
        });

        btnRemoveFile.setText("Retirer");
        btnRemoveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveFileActionPerformed(evt);
            }
        });

        lblZipName.setText("Nom du zip :");

        jLabel1.setText("Team :");

        jLabel2.setText("Nom de la série :");

        jLabel3.setText("Numéro du chapitre :");

        jLabel4.setText("Inclus dans le tome :");

        jLabel5.setText("Version :");

        jLabel6.setText("Cleaners :");

        jLabel7.setText("Traducteurs :");

        jLabel8.setText("Correcteurs :");

        jLabel9.setText("Editeurs :");

        jLabel10.setText("Qualité :");

        jLabel11.setText("Description :");

        jLabel12.setText("Résumé :");

        jLabel13.setText("Image du tome :");

        btnTomeImage.setText("...");
        btnTomeImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTomeImageActionPerformed(evt);
            }
        });

        jLabel14.setText("Arc :");

        jLabel15.setText("Titre du chapitre :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfTrad, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfQC, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfResume)
                            .addComponent(tfDescription)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(tfTomeImage, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTomeImage))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spinnerTome, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spinnerVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(tfClean))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblZipName)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(67, 67, 67)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfName)
                            .addComponent(tfTeam)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(tfZipName, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRemoveFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddFile))
                            .addComponent(tfArc)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(spinnerChapterNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(tfChapitreTitre))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddFile)
                    .addComponent(btnRemoveFile)
                    .addComponent(tfZipName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblZipName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfTeam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(tfArc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(tfChapitreTitre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(spinnerTome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerChapterNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(spinnerVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfClean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tfTrad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tfCheck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(tfEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tfQC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(tfDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(tfResume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(btnTomeImage)
                    .addComponent(tfTomeImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnCancel.setText("Annuler");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnClose.setText("Fermer");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnCreateZip.setText("Créer zip, image, xml et txt");
        btnCreateZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateZipActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCreateZip)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose)
                    .addComponent(btnCreateZip)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFileActionPerformed
        JFileChooser fc = new JFileChooser();
        for(FileFilter ff : fc.getChoosableFileFilters()){
            fc.removeChoosableFileFilter(ff);
        }
        fc.addChoosableFileFilter(new ImageFilter());
        fc.setMultiSelectionEnabled(true);
        int z = fc.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION && fc.getSelectedFiles().length > 0){
            for(File file : fc.getSelectedFiles()){
                boolean add = true;
                for(int i=0; i<dlmFiles.size(); i++){
                    String entry = (String)dlmFiles.get(i);
                    if(entry.equalsIgnoreCase(file.getAbsolutePath())){
                        add = false;
                    }
                }
                if(add == true){
                    dlmFiles.addElement(file.getAbsolutePath());
                }
            }            
        }
    }//GEN-LAST:event_btnAddFileActionPerformed

    private void btnRemoveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveFileActionPerformed
        if(listFiles.getSelectedIndex() != -1){
            int[] selectedItems = listFiles.getSelectedIndices();
            for(int i=selectedItems.length-1; i>=0; i--){
                dlmFiles.remove(selectedItems[i]);
            }
        }
    }//GEN-LAST:event_btnRemoveFileActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        bm = ButtonManager.OK;
        try{
           storeXML(); 
        }catch(Exception exc){}        
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        bm = ButtonManager.Cancel;
        try{
           storeXML(); 
        }catch(Exception exc){}
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnTomeImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTomeImageActionPerformed
        JFileChooser fc = new JFileChooser();
        for(FileFilter ff : fc.getChoosableFileFilters()){
            fc.removeChoosableFileFilter(ff);
        }
        fc.addChoosableFileFilter(new ImageFilter());
        int z = fc.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            if(fc.getSelectedFile() != null){
                tfTomeImage.setText(fc.getSelectedFile().getAbsolutePath());
            }       
        }
    }//GEN-LAST:event_btnTomeImageActionPerformed

    private void btnCreateZipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateZipActionPerformed
        //Vérification de l'existence du dossier "Mes Mangas"
        File mesMangas = new File(MainFrame.getDocumentsFolder());
        if(mesMangas.exists() == false){
            mesMangas.mkdirs();
        }
        
        //Ecriture du zip
        List<File> files = new ArrayList<>();
        for(int i=0; i<dlmFiles.size(); i++){
            File file = new File((String)dlmFiles.get(i));
            files.add(file);
        }
        createZip(files, mesMangas.getAbsolutePath() + File.separator + tfZipName.getText() + ".zip");
        
        //Création d'un MangaChapter
        File zipFile = new File(mesMangas.getAbsolutePath() + File.separator + tfZipName.getText() + ".zip");
        File imageFile = new File(tfTomeImage.getText());
        MangaChapter mc = new MangaChapter(
                zipFile, 
                imageFile,
                tfName.getText(),
                spinnerChapterNumber.getValue().toString(),
                spinnerVersion.getValue().toString(),
                tfClean.getText(),
                tfTrad.getText(),
                tfCheck.getText(),
                tfEdit.getText(),
                tfQC.getText(),
                spinnerTome.getValue().toString(),
                tfTeam.getText(),
                tfDescription.getText(),
                tfResume.getText(),
                tfArc.getText(),
                tfChapitreTitre.getText());
        
        //Réunion de tous les MangaChapters dans le XML
        File chsFile = new File(mesMangas.getAbsolutePath() + File.separator + tfName.getText() + ".xml");
        List<MangaChapter> chs = new ArrayList<>();
        if(chsFile.exists() == true){
            chs = ChaptersXML.readFile(chsFile);
        }
        chs.add(mc);
        ChaptersXML.writeFile(chs, chsFile);
        
        //Redimensionnment de l'image
        File image = getResizedImage(70, 100, new File(tfTomeImage.getText()));
        
        //Création d'un fichier TXT
        doXmlTXT(mesMangas.getAbsolutePath());
        
        //Affiche une fenêtre
        JOptionPane.showMessageDialog(this, "Opération terminée !");
    }//GEN-LAST:event_btnCreateZipActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrepareReleaseDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                PrepareReleaseDialog dialog = new PrepareReleaseDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFile;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnCreateZip;
    private javax.swing.JButton btnRemoveFile;
    private javax.swing.JButton btnTomeImage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblZipName;
    private javax.swing.JList<String> listFiles;
    private javax.swing.JSpinner spinnerChapterNumber;
    private javax.swing.JSpinner spinnerTome;
    private javax.swing.JSpinner spinnerVersion;
    private javax.swing.JTextField tfArc;
    private javax.swing.JTextField tfChapitreTitre;
    private javax.swing.JTextField tfCheck;
    private javax.swing.JTextField tfClean;
    private javax.swing.JTextField tfDescription;
    private javax.swing.JTextField tfEdit;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfQC;
    private javax.swing.JTextField tfResume;
    private javax.swing.JTextField tfTeam;
    private javax.swing.JTextField tfTomeImage;
    private javax.swing.JTextField tfTrad;
    private javax.swing.JTextField tfZipName;
    // End of variables declaration//GEN-END:variables
}
