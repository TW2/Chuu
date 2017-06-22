/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.renderer;

import chuu.MainFrame;
import chuu.lib.MangaChapter;
import chuu.lib.TreeEntry;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Naruto
 */
public class MangaChapterTreeRenderer extends DefaultTreeCellRenderer {

    private final JPanel mainPanel;
    private final JPanel westPanel;
    private final JPanel centerPanel;
    
    private final JLabel lblIcon = new JLabel();
    private final JLabel lblNorth = new JLabel();
    private final JLabel lblCenter = new JLabel();
    
    public MangaChapterTreeRenderer() {
        mainPanel = new JPanel(new BorderLayout(), true);
        westPanel = new JPanel(new BorderLayout(), true);
        centerPanel = new JPanel(new BorderLayout(), true);
        
        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        westPanel.add(lblIcon, BorderLayout.CENTER);
        centerPanel.add(lblNorth, BorderLayout.NORTH);
        centerPanel.add(lblCenter, BorderLayout.CENTER);
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
 
            super.getTreeCellRendererComponent(tree, value, sel,
                    expanded, leaf, row, hasFocus);
            
            if(value instanceof TreeEntry && leaf){                
                TreeEntry te = (TreeEntry)value;
                MangaChapter mc = te.getMangaChapter();
                
                ImageIcon ii = new ImageIcon(MainFrame.getDocumentsFolder() 
                        + File.separator + mc.getImageFile().getName());
                lblIcon.setText(null);
                lblIcon.setIcon(ii);
                
                lblNorth.setFont(lblNorth.getFont().deriveFont(Font.BOLD, 18f));
                lblNorth.setText("  " + mc.getNom() + " T" + mc.getTome() + " ch" 
                        + mc.getNumero() + " v" + mc.getVersion() + " par " + mc.getTeam() + "  ");
                
                lblCenter.setText("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" 
                        + "Description : " + mc.getDescription() 
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>" 
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" 
                        + "Résumé : " + mc.getResume() 
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                
                setToolTipText(
                        "<html>Fait par :"
                        + "<br>Traducteurs : " + mc.getTrads()
                        + "<br>Correcteurs : " + mc.getChecks()
                        + "<br>Editeurs : " + mc.getEdits()
                        + "<br>Nettoyeurs : " + mc.getCleans()
                        + "<br>QC : " + mc.getQcs()
                );
                
                mainPanel.setBackground(Color.white);
                westPanel.setBackground(Color.white);
                centerPanel.setBackground(Color.white);
                
                if(sel){
                    mainPanel.setBackground(Color.yellow);
                    westPanel.setBackground(Color.yellow);
                    centerPanel.setBackground(Color.yellow);
                }
                
                return mainPanel;
            }
 
            return this;
        }
    
}
