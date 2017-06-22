/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.ui;

import chuu.MainFrame;
import chuu.lib.MangaChapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author Naruto
 */
public class ViewDialog extends javax.swing.JDialog {

    public enum ButtonManager{
        OK, Cancel, None;
    }
    
    private ButtonManager bm = ButtonManager.None;
    private MangaChapter ch = null;
    
    private Path outDir = null;
    private final List<ZipEntry> entries = new ArrayList<>();
    private int lastIndex = 0;
    
    private final ScreenView view = new ScreenView();
    
    /**
     * Creates new form ViewDialog
     * @param parent
     * @param modal
     */
    public ViewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
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
        
        //-- dim >> Obtient la taille de l'écran
        //-- gconf >> Obtient la configuration de l'écran
        //-- insets >> Obtient les 'marges' de l'écran
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension dim = toolkit.getScreenSize();
        java.awt.GraphicsConfiguration gconf = java.awt.GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        java.awt.Insets insets = toolkit.getScreenInsets(gconf);
        setSize(dim.width - insets.left - insets.right,
                dim.height - insets.top - insets.bottom);
        
        this.revalidate();
        contentPanel.add(view, BorderLayout.CENTER);
        view.setSize(new Dimension(contentPanel.getWidth(), contentPanel.getHeight()));
    }
    
    public ButtonManager getButton(){
        return bm;
    }
    
    public void showDialog(){
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void doEntries(MangaChapter ch){
        this.ch = ch;
        
        String zip = ch.getZip().getName().replaceAll(" ", "_");
        
        try {
            URL url = new File(MainFrame.getDocumentsFolder() + File.separator + zip).toURI().toURL();
            
            outDir = Paths.get(url.toURI());
            try (FileInputStream fos = new FileInputStream(outDir.toFile()); ZipInputStream zis = new ZipInputStream(fos)) {
                ZipEntry entry;
                while((entry = zis.getNextEntry())!=null){
                    if(entry.isDirectory() == false){
                        entries.add(entry);
                    }
                }
            }
            Collections.sort(entries, new Comparator<ZipEntry>() {
                @Override
                public int compare(ZipEntry e1, ZipEntry e2){
                    return e1.getName().compareTo(e2.getName());
                }
            });
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ViewDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ViewDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(entries.isEmpty() == false){
            readEntry(entries.get(lastIndex));
        }
    }
    
    private void previous(){
        if(entries.isEmpty() == false){
            lastIndex = lastIndex <= 0 ? entries.size() - 1 : lastIndex - 1;
            readEntry(entries.get(lastIndex));
        }
    }
    
    private void forward(){
        if(entries.isEmpty() == false){
            lastIndex = lastIndex >= entries.size() - 1 ? 0 : lastIndex + 1;
            readEntry(entries.get(lastIndex));
        }
    }
    
    private void readEntry(ZipEntry entry){
        InputStream is = null;
        try {
            ZipFile zf = new ZipFile(outDir.toFile());
            is = new BufferedInputStream(zf.getInputStream(entry));
            Image image = ImageIO.read(is);
            lblManga.setText(ch.getNom() + " ch" + ch.getNumero() + " v" + ch.getVersion());
            lblManga.setFont(lblManga.getFont().deriveFont(Font.BOLD));
            view.refresh((BufferedImage)image);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ViewDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViewDialog.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(is != null){
                    is.close();
                }                
            } catch (IOException ex) {
                Logger.getLogger(ViewDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    private String reCreate(String path){
        path = path.replaceAll(" ", "%20");
        path = path.replaceAll("\\[", "%5B");
        path = path.replaceAll("\\]", "%5D");
        return path.replaceAll("/", "\\\\");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnExit = new javax.swing.JButton();
        btnForward = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        lblManga = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnForward.setText(">>");
        btnForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForwardActionPerformed(evt);
            }
        });

        btnPrevious.setText("<<");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        lblManga.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblManga.setText("Manga");

        contentPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnPrevious)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblManga, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnForward, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit)
                    .addComponent(lblManga))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnForward, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                    .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(btnPrevious, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        bm = ButtonManager.OK;
        dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        previous();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForwardActionPerformed
        forward();
    }//GEN-LAST:event_btnForwardActionPerformed

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
            java.util.logging.Logger.getLogger(ViewDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ViewDialog dialog = new ViewDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnForward;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel lblManga;
    // End of variables declaration//GEN-END:variables
    
    public static class ScreenView extends JPanel {

        BufferedImage bi = null;
        
        public ScreenView() {
            init();
        }
        
        private void init(){
            setDoubleBuffered(true);
        }
        
        public void refresh(BufferedImage img){
            bi = getResize(getWidth(), getHeight(), img);
            repaint();
        }
        
        @Override
        public void paint(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.gray);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            if(bi != null){
                g2d.drawImage(bi, (getWidth()-bi.getWidth())/2, 0, null);
            }
        }
        
        private BufferedImage getResize(int width, int height, BufferedImage image){
            BufferedImage img;            

//            if(image.getWidth() > image.getHeight()){
//                //img width => width
//                //img height => h
//                int h = width*image.getHeight()/image.getWidth();
//                img = new BufferedImage(width, h, BufferedImage.TYPE_INT_ARGB);
//                Graphics2D g2d = img.createGraphics();
//                g2d.drawImage(image, 0, 0, width, h, 0, 0, image.getWidth(), image.getHeight(), null);
//                g2d.dispose();
//            }else if(image.getWidth() < image.getHeight()){
//                //img width => w
//                //img height => height
//                int w = height*image.getWidth()/image.getHeight();
//                img = new BufferedImage(w, height, BufferedImage.TYPE_INT_ARGB);
//                Graphics2D g2d = img.createGraphics();
//                g2d.drawImage(image, 0, 0, w, height, 0, 0, image.getWidth(), image.getHeight(), null);
//                g2d.dispose();
//            }else{
//                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//                Graphics2D g2d = img.createGraphics();
//                g2d.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
//                g2d.dispose();
//            }
            
            //img width => w
            //img height => height
            int w = height*image.getWidth()/image.getHeight();
            img = new BufferedImage(w, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(image, 0, 0, w, height, 0, 0, image.getWidth(), image.getHeight(), null);
            g2d.dispose();

            return img;
        }
    }
}
