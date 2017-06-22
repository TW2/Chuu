/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.renderer;

import chuu.lib.ListEntry;
import chuu.lib.MangaChapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

/**
 *
 * @author Naruto
 */
public class MangaChapterListRenderer extends JPanel implements ListCellRenderer {

    JLabel lblImage = new JLabel();
    JLabel lblTitle = new JLabel();
    JLabel lblText = new JLabel();
    
    public MangaChapterListRenderer() {
        init();
    }

    private void init(){
        setOpaque(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 300));
        this.add(lblTitle, BorderLayout.NORTH);
        this.add(lblImage, BorderLayout.CENTER);
        this.add(lblText, BorderLayout.SOUTH);
        lblImage.setDoubleBuffered(true);
        setBackground(Color.white);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        if(value instanceof ListEntry){
            ListEntry le = (ListEntry)value;
            MangaChapter mc = le.getMangaChapter();
            
            if(cellHasFocus){
                setBorder(new LineBorder(Color.orange, 2));
                setBackground(Color.yellow.brighter());
            }else{
                setBorder(null);
                setBackground(Color.white);
            }
            
            if(le.getLevel() == ListEntry.ControlLevel.Chapitre){
                setToolTipText(
                        "<html>Fait par :"
                        + "<br>Traducteurs : " + mc.getTrads()
                        + "<br>Correcteurs : " + mc.getChecks()
                        + "<br>Editeurs : " + mc.getEdits()
                        + "<br>Nettoyeurs : " + mc.getCleans()
                        + "<br>QC : " + mc.getQcs()
                        + "<br>Description : " + mc.getDescription()
                        + "<br>Résumé : " + mc.getResume()
                );
            }else{
                setToolTipText(null);
            }            
            
            Font font = lblTitle.getFont().deriveFont(18f);
            
            lblTitle.setFont(font);
            lblTitle.setForeground(Color.red);
            lblTitle.setText(le.getTitle());
            lblTitle.setHorizontalAlignment(JLabel.CENTER);
            
            lblText.setFont(font);
            lblText.setText(le.getText());
            lblText.setHorizontalAlignment(JLabel.CENTER);
            
            lblImage.setIcon(new ImageIcon(le.getImage()));
            lblImage.setHorizontalAlignment(JLabel.CENTER);
        }
        
        return this;
    }
    
}
