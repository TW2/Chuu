/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.lib;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Naruto
 */
public class ListEntry {
    
    public enum ControlLevel{
        Team, Serie, Tome, Arc, Chapitre;
    }
    
    private MangaChapter mangaChapter;
    private ControlLevel level;

    public ListEntry() {
    }

    public ListEntry(MangaChapter mangaChapter, ControlLevel level) {
        this.mangaChapter = mangaChapter;
        this.level = level;
    }

    public void setMangaChapter(MangaChapter mangaChapter) {
        this.mangaChapter = mangaChapter;
    }

    public MangaChapter getMangaChapter() {
        return mangaChapter;
    }

    public void setLevel(ControlLevel level) {
        this.level = level;
    }

    public ControlLevel getLevel() {
        return level;
    }
    
    public String getTitle(){
        String title = "";
        switch(level){
            case Team: title = mangaChapter.getTeam(); break;
            case Serie: title = mangaChapter.getNom(); break;
            case Tome: title = "<html>"+mangaChapter.getNom()+"<small><br>T"+mangaChapter.getTome()+"</small>"; break;
            case Arc: title = "<html>"+mangaChapter.getNom()+"<small><br>T"+mangaChapter.getTome()+"<br>"+mangaChapter.getArc()+"</small>"; break;
            case Chapitre: title = "<html>"+mangaChapter.getNom()+"<small><br>T"+mangaChapter.getTome()+"<br>"+mangaChapter.getArc()+"<br>"+mangaChapter.getChapitre()+"</small>"; break;
        }
        return title;
    }
    
    public BufferedImage getImage(){
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        Color teamColor = fromHexa(mangaChapter.getMD5() == null ? mangaChapter.getMD5(true) : mangaChapter.getMD5());
        String content;
        BufferedImage icon;
        int w, h;
        switch(level){
            case Team:
                content = mangaChapter.getTeam().substring(0, 1);
                g2d.setColor(teamColor);
                g2d.fillOval(0, 0, 200, 200);
                g2d.setColor(invert(teamColor));
                g2d.setFont(g2d.getFont().deriveFont(100f));
                content = content.toUpperCase();
                w = (200-g2d.getFontMetrics().stringWidth(content))/2;
                h = (200-g2d.getFontMetrics().getHeight())/2+100;
                g2d.drawString(content, w, h);
                break;
            case Serie:
                content = mangaChapter.getNom().substring(0, 1);
                g2d.setColor(teamColor);
                g2d.fillOval(0, 0, 200, 200);
                g2d.setColor(invert(teamColor));
                g2d.setFont(g2d.getFont().deriveFont(100f));
                content = content.toUpperCase();
                w = (200-g2d.getFontMetrics().stringWidth(content))/2;
                h = (200-g2d.getFontMetrics().getHeight())/2+100;
                g2d.drawString(content, w, h);
                break;
            case Tome:
                icon = mangaChapter.getResize(200, 200);
                g2d.drawImage(icon, (200-icon.getWidth())/2, (200-icon.getHeight())/2, null);
                break;
            case Arc:
                icon = mangaChapter.getResize(200, 200);
                g2d.drawImage(icon, (200-icon.getWidth())/2, (200-icon.getHeight())/2, null);
                break;
            case Chapitre:
                icon = mangaChapter.getResize(200, 200);
                g2d.drawImage(icon, (200-icon.getWidth())/2, (200-icon.getHeight())/2, null);
                break;
        }
        g2d.dispose();
        return image;
    }
    
    public String getText(){
        String text = "";
        switch(level){
            case Chapitre: text = "ch" + mangaChapter.getNumero() + " v" + mangaChapter.getVersion(); break;
        }
        return text;
    }
    
    private Color fromHexa(String hexa){
        int red = Integer.parseInt(hexa.substring(0, 2), 16);
        int green = Integer.parseInt(hexa.substring(2, 4), 16);
        int blue = Integer.parseInt(hexa.substring(4, 6), 16);
        return new Color(red, green, blue);
    }
    
    private Color invert(Color c){
        int red = 255 - c.getRed();
        int green = 255 - c.getGreen();
        int blue = 255 - c.getBlue();
        return new Color(red, green, blue);
    }
    
}
