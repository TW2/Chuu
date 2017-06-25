/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu;

import chuu.lib.ListEntry;
import chuu.xml.ChaptersXML;
import chuu.ui.ViewDialog;
import chuu.lib.MangaChapter;
import chuu.renderer.MangaChapterListRenderer;
import chuu.ui.PrepareFTPDialog;
import chuu.ui.PrepareReleaseDialog;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Naruto
 */
public class MainFrame extends javax.swing.JFrame {

    private final List<MangaChapter> chapitres = new ArrayList<>();
    private final DefaultListModel mangaListModel = new DefaultListModel();
    private final String mangasOnlineFolder = "http://minna.red/testrss/";
    
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mangas");
    
    private List<String> controlList = new ArrayList<>();
    private ListEntry.ControlLevel level = ListEntry.ControlLevel.Team;
    private ListEntry lastListEntry = null;
    
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        init();
    }
    
    private void init(){
        try {
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
        
        setLocationRelativeTo(null);
        
        
        //On récupère le TXT et les XML et JPG depuis l'url
        ///downloadFromURL(mangasOnlineFolder, getDocumentsFolder());
        
        //connectAndDownload();
        
        System.out.println(getDocumentsFolder());
        File documentsFolder = new File(getDocumentsFolder());
        for(File file : documentsFolder.listFiles()){
            if(file.getName().endsWith(".xml")){
                List<MangaChapter> lmc = ChaptersXML.readFile(file);
                for(int i=0; i< lmc.size(); i++){
                    MangaChapter mc = lmc.get(i);
                    chapitres.add(mc);
                }
            }
        }
        
        jList1.setModel(mangaListModel);
        jList1.setCellRenderer(new MangaChapterListRenderer());        
        jList1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listMouseClicked(e);
            }
        });
        populateList(level, null);
    }
        
    public void listMouseClicked(MouseEvent e){
        ListEntry le = null;
        try{
            le = (ListEntry)mangaListModel.get(jList1.getSelectedIndex());
            lastListEntry = le;
        }catch(Exception exc){
            //Pas de sélection
        }
        
        if(e.getButton()==1 && e.getClickCount()==2 && jList1.getSelectedIndex() != -1){
            switch(level){
                case Team: level = ListEntry.ControlLevel.Serie; populateList(level, le); break;
                case Serie: level = ListEntry.ControlLevel.Tome; populateList(level, le); break;
                case Tome: level = ListEntry.ControlLevel.Arc; populateList(level, le); break;
                case Arc: level = ListEntry.ControlLevel.Chapitre; populateList(level, le); break;
                case Chapitre:
                    if(le != null){
                        MangaChapter mc = le.getMangaChapter();
                        ViewDialog vd = new ViewDialog(this, true);
                        vd.doEntries(mc);
                        vd.showDialog();
                    }
                    break;
            }
        }
        if(e.getButton()==3){
            switch(level){
                case Team: break;
                case Serie: level = ListEntry.ControlLevel.Team; populateList(level, null); break;
                case Tome: level = ListEntry.ControlLevel.Serie; populateList(level, lastListEntry != null ? lastListEntry : null); break;
                case Arc: level = ListEntry.ControlLevel.Tome; populateList(level, lastListEntry != null ? lastListEntry : null); break;
                case Chapitre: level = ListEntry.ControlLevel.Arc; populateList(level, lastListEntry != null ? lastListEntry : null); break;
            }
        }
    }
    
    public String getApplicationDirectory(){
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
    
    public static String getDocumentsFolder(){
        String path = null;
        if(System.getProperty("os.name").toLowerCase().contains("mac") |
                System.getProperty("os.name").toLowerCase().contains("linux")){
            path = System.getProperty("user.home") + File.separator + "Mes Mangas";
        }
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Mes Mangas";
        }
        return path;
    }
    
    private URI reCreate(URL url) throws URISyntaxException{
        String path = url.toString();
        path = path.replaceAll(" ", "%20");
        path = path.replaceAll("\\[", "%5B");
        path = path.replaceAll("\\]", "%5D");
        path = path.replaceAll("/", "%2F");
        path = path.replaceAll(":", "%3A");
        path = path.replaceAll("\\\\", "%5C");
        return new URI(path);
    }
    
    private void connectAndDownload(){        
        try {
            Map<String, URL> urls = new HashMap<>();
            urls.put("zip", new URL(mangasOnlineFolder + "rssfeed_zip.php"));
            urls.put("xml", new URL(mangasOnlineFolder + "rssfeed_xml.php"));
            urls.put("jpg", new URL(mangasOnlineFolder + "rssfeed_jpg.php"));
            urls.put("txt", new URL(mangasOnlineFolder + "rssfeed_txt.php"));
            
            for(Map.Entry<String, URL> entry : urls.entrySet()){
                String str = entry.getKey();
                URL url = entry.getValue();
                System.out.println("Key is " + str + " and value is " + url);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(url));
                System.out.println(feed);
                URL u2 = new URL(feed.getLink());
                
                ReadableByteChannel rbc = Channels.newChannel(u2.openStream());
                File xmlList = new File("https://drive.google.com/open?id=1aWYF5Xxi_I4k5o9Dgd-uOYebe75VtV2sSnROigCbeok");
                try (FileOutputStream fos = new FileOutputStream(xmlList)) {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }
            }
            
        } catch (IOException | IllegalArgumentException | FeedException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void connectToRSS(URL url) {
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));
            System.out.println(feed);
        } catch (IOException | IllegalArgumentException | FeedException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void downloadFromURL(String folderaddress, String folder){
        try {
            //Donwload TXT file
            URL url = new URL(folderaddress + "xml.txt");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            File xmlList = new File(folder + File.separator + "xml.txt");
            try (FileOutputStream fos = new FileOutputStream(xmlList)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
            
            //Scan TXT file and download artefacts
            try (BufferedReader br = new BufferedReader(new FileReader(xmlList))) {
                String line;
                while((line = br.readLine()) != null){
                    if(line.toLowerCase().startsWith("xml")){
                        String name = line.substring("xml=".length());
                        URL urlXML = new URL(folderaddress + name);                        
                        ReadableByteChannel one = Channels.newChannel(urlXML.openStream());
                        File xml = new File(folder + File.separator + name);
                        try (FileOutputStream fos = new FileOutputStream(xml)) {
                            fos.getChannel().transferFrom(one, 0, Long.MAX_VALUE);
                        }
                    }
                    if(line.toLowerCase().startsWith("jpg")){
                        String name = line.substring("jpg=".length());
                        URL urlJPG = new URL(folderaddress + name);
                        ReadableByteChannel two = Channels.newChannel(urlJPG.openStream());
                        File jpg = new File(folder + File.separator + name);
                        try (FileOutputStream fos = new FileOutputStream(jpg)) {
                            fos.getChannel().transferFrom(two, 0, Long.MAX_VALUE);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur !\nPas de données.\n"+ex.getMessage(), "Erreur(s)", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void execPHP(URL url){
        try {
            URLConnection yc = url.openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null){
                    System.out.println(inputLine);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void populateList(ListEntry.ControlLevel level, ListEntry le){
        mangaListModel.removeAllElements();
        controlList = new ArrayList<>();
        
        
        
        switch(level){
            case Team:
                for(MangaChapter mc : chapitres){
                    if(controlList.contains(mc.getTeam()) == false){
                        controlList.add(mc.getTeam());
                        mangaListModel.addElement(new ListEntry(mc, level));
                    }
                }
                break;
            case Serie:
                for(MangaChapter mc : chapitres){
                    if(controlList.contains(mc.getNom()) == false && le != null
                            && le.getMangaChapter().getTeam().equalsIgnoreCase(mc.getTeam())){
                        controlList.add(mc.getNom());
                        mangaListModel.addElement(new ListEntry(mc, level));
                    }
                }
                break;
            case Tome:
                for(MangaChapter mc : chapitres){
                    if(controlList.contains(mc.getTome()) == false && le != null
                            && le.getMangaChapter().getTeam().equalsIgnoreCase(mc.getTeam())
                            && le.getMangaChapter().getNom().equalsIgnoreCase(mc.getNom())){
                        controlList.add(mc.getTome());
                        mangaListModel.addElement(new ListEntry(mc, level));
                    }
                }
                break;
            case Arc:
                for(MangaChapter mc : chapitres){
                    if(controlList.contains(mc.getArc()) == false && le != null
                            && le.getMangaChapter().getTeam().equalsIgnoreCase(mc.getTeam())
                            && le.getMangaChapter().getNom().equalsIgnoreCase(mc.getNom())
                            && le.getMangaChapter().getTome().equalsIgnoreCase(mc.getTome())){
                        controlList.add(mc.getArc());
                        mangaListModel.addElement(new ListEntry(mc, level));
                    }
                }
                break;
            case Chapitre:
                for(MangaChapter mc : chapitres){
                    if(controlList.contains(mc.getChapitre()) == false && le != null
                            && le.getMangaChapter().getTeam().equalsIgnoreCase(mc.getTeam())
                            && le.getMangaChapter().getNom().equalsIgnoreCase(mc.getNom())
                            && le.getMangaChapter().getTome().equalsIgnoreCase(mc.getTome())
                            && le.getMangaChapter().getArc().equalsIgnoreCase(mc.getArc())){
                        controlList.add(mc.getChapitre());
                        mangaListModel.addElement(new ListEntry(mc, level));
                    }
                }
                break;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuFileAddRelease = new javax.swing.JMenuItem();
        mnuFileKeyGen = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(" Chuu :: Système de partage de scans");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel2.setText("Sorties (double clic = avancer ; clic droit = reculer) : ");

        jMenu1.setText("File");

        mnuFileAddRelease.setText("Préparer une release...");
        mnuFileAddRelease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFileAddReleaseActionPerformed(evt);
            }
        });
        jMenu1.add(mnuFileAddRelease);

        mnuFileKeyGen.setText("Générer la clé de téléchargement...");
        mnuFileKeyGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFileKeyGenActionPerformed(evt);
            }
        });
        jMenu1.add(mnuFileKeyGen);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuFileAddReleaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFileAddReleaseActionPerformed
        PrepareReleaseDialog up = new PrepareReleaseDialog(this, true);
        up.showDialog();
    }//GEN-LAST:event_mnuFileAddReleaseActionPerformed

    private void mnuFileKeyGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFileKeyGenActionPerformed
        PrepareFTPDialog ftp = new PrepareFTPDialog(this, true);
        ftp.showDialog();
    }//GEN-LAST:event_mnuFileKeyGenActionPerformed

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem mnuFileAddRelease;
    private javax.swing.JMenuItem mnuFileKeyGen;
    // End of variables declaration//GEN-END:variables
}
