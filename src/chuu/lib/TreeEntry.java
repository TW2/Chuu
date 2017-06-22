/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.lib;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Naruto
 */
public class TreeEntry extends DefaultMutableTreeNode {

    public enum EntryType{
        Team, Serie, Tome, Chapitre;
    }
    
    MangaChapter mangaChapter = null;
    EntryType entryType = EntryType.Chapitre;
    
    public TreeEntry() {
    }
    
    public static TreeEntry createTeam(MangaChapter mangaChapter){
        TreeEntry te = new TreeEntry();
        te.setUserObject(mangaChapter.getTeam());
        te.setEntryType(EntryType.Team);
        te.setMangaChapter(mangaChapter);
        return te;
    }
    
    public static TreeEntry createSerie(MangaChapter mangaChapter){
        TreeEntry te = new TreeEntry();
        te.setUserObject(mangaChapter.getNom());
        te.setEntryType(EntryType.Serie);
        te.setMangaChapter(mangaChapter);
        return te;
    }
    
    public static TreeEntry createTome(MangaChapter mangaChapter){
        TreeEntry te = new TreeEntry();
        te.setUserObject(mangaChapter.getTome());
        te.setEntryType(EntryType.Tome);
        te.setMangaChapter(mangaChapter);
        return te;
    }
    
    public static TreeEntry createChapitre(MangaChapter mangaChapter){
        TreeEntry te = new TreeEntry();
        te.setUserObject(mangaChapter.getNumero());
        te.setEntryType(EntryType.Chapitre);
        te.setMangaChapter(mangaChapter);
        return te;
    }

    public void setMangaChapter(MangaChapter mangaChapter) {
        this.mangaChapter = mangaChapter;
    }

    public MangaChapter getMangaChapter() {
        return mangaChapter;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public EntryType getEntryType() {
        return entryType;
    }
    
    
    
}
