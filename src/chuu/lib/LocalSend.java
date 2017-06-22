/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.lib;

/**
 *
 * @author Naruto
 */
public class LocalSend {
    
    private MangaChapter mangaChapter = new MangaChapter();
    private String server = "", port = "", login = "", password = "";

    public LocalSend() {
    }

    public void setMangaChapter(MangaChapter mangaChapter) {
        this.mangaChapter = mangaChapter;
    }

    public MangaChapter getMangaChapter() {
        return mangaChapter;
    }

    public void setServer(String server) {
        this.server = server;
    }
    
    public String getServer() {
        return server;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    
    
}
