/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.xml;

import chuu.lib.MangaChapter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Naruto
 */
public class ChaptersXML {
    
    public ChaptersXML() {
        
    }
    
    public static void writeStream(List<MangaChapter> chs, OutputStream os){
        try {
            MangaChaptersWriter chswr = new MangaChaptersWriter();
            chswr.setMangaChapters(chs);
            chswr.getXML(os);
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static List<MangaChapter> readStream(InputStream is){
        List<MangaChapter> chs = new ArrayList<>();
        try {
            MangaChaptersHandler chsrd = new MangaChaptersHandler(is);
            chs = chsrd.getMangaChapters();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return chs;
    }
    
    public static void writeFile(List<MangaChapter> chs, File file){
        try {
            MangaChaptersWriter chswr = new MangaChaptersWriter();
            chswr.setMangaChapters(chs);
            chswr.getXML(file);
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static List<MangaChapter> readFile(File file){
        List<MangaChapter> chs = new ArrayList<>();
        try {
            MangaChaptersHandler chsrd = new MangaChaptersHandler(file);
            chs = chsrd.getMangaChapters();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return chs;
    }
    
    public static List<MangaChapter> readString(String str){
        List<MangaChapter> chs = new ArrayList<>();
        try {
            MangaChaptersHandler chsrd = new MangaChaptersHandler(str);
            chs = chsrd.getMangaChapters();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return chs;
    }
    
    public static class MangaChaptersWriter {
        /*   Structure à écrire :
        <nodes>
            <node fl="" ch="">encnode</node>
        </nodes>
        */

        List<MangaChapter> chapters = new ArrayList<>();

        public MangaChaptersWriter(){

        }

        public class MangaChapterSource extends InputSource{

            List<MangaChapter> chapters = new ArrayList<>();

            public MangaChapterSource(List<MangaChapter> chapters){
                super();
                this.chapters = chapters;
            }

            public List<MangaChapter> getMangaChapters(){
                return chapters;
            }

        }

        public class MangaChapterReader implements XMLReader{

            private ContentHandler chandler;
            private final AttributesImpl attributes = new AttributesImpl();
            private final Map<String,Boolean> features = new HashMap<>();
            private final Map<String,Object> properties = new HashMap<>();
            private EntityResolver resolver;
            private DTDHandler dhandler;
            private ErrorHandler ehandler;

            @Override
            public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
                return features.get(name);
            }

            @Override
            public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
                try{
                    features.put(name, value);
                }catch(Exception ex){
                }
            }

            @Override
            public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
                return properties.get(name);
            }

            @Override
            public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
                try{
                    properties.put(name, value);
                }catch(Exception ex){
                }
            }

            @Override
            public void setEntityResolver(EntityResolver resolver) {
                this.resolver = resolver;
            }

            @Override
            public EntityResolver getEntityResolver() {
                return resolver;
            }

            @Override
            public void setDTDHandler(DTDHandler handler) {
                this.dhandler = handler;
            }

            @Override
            public DTDHandler getDTDHandler() {
                return dhandler;
            }

            @Override
            public void setContentHandler(ContentHandler handler) {
                this.chandler = handler;
            }

            @Override
            public ContentHandler getContentHandler() {
                return chandler;
            }

            @Override
            public void setErrorHandler(ErrorHandler handler) {
                this.ehandler = handler;
            }

            @Override
            public ErrorHandler getErrorHandler() {
                return ehandler;
            }

            @Override
            public void parse(InputSource input) throws IOException, SAXException {

                if(!(input instanceof MangaChapterSource)){
                    throw new SAXException("The object isn't a ChannelSource");
                }
                if(chandler == null){
                    throw new SAXException("ContentHandler not defined");
                }

                MangaChapterSource source = (MangaChapterSource)input;
                List<MangaChapter> chapters = source.getMangaChapters();

                // Main element - beginning
                chandler.startDocument();
                chandler.startElement("", "manga", "manga", attributes);
                    
                for(MangaChapter ch : chapters){
                    // beginning
                    attributes.addAttribute("", "", "zip", "string", ch.getZip().getName());
                    attributes.addAttribute("", "", "image", "string", ch.getImageFile().getName());
                    chandler.startElement("", "chapitre", "chapitre", attributes);
                    attributes.clear();
                    
                    chandler.startElement("", "md5", "md5", attributes);
                    char[] md5 = ch.getMD5().toCharArray();
                    chandler.characters(md5,0,md5.length);
                    chandler.endElement("", "md5", "md5");
                    
                    chandler.startElement("", "nom", "nom", attributes);
                    char[] nom = ch.getNom().toCharArray();
                    chandler.characters(nom,0,nom.length);
                    chandler.endElement("", "nom", "nom");
                    
                    chandler.startElement("", "tome", "tome", attributes);
                    char[] tome = ch.getTome().toCharArray();
                    chandler.characters(tome,0,tome.length);
                    chandler.endElement("", "tome", "tome");
                    
                    chandler.startElement("", "arc", "arc", attributes);
                    char[] arc = ch.getArc().toCharArray();
                    chandler.characters(arc,0,arc.length);
                    chandler.endElement("", "arc", "arc");
                    
                    chandler.startElement("", "chapitretitre", "chapitretitre", attributes);
                    char[] chapitretitre = ch.getChapitre().toCharArray();
                    chandler.characters(chapitretitre,0,chapitretitre.length);
                    chandler.endElement("", "chapitretitre", "chapitretitre");
                    
                    chandler.startElement("", "numero", "numero", attributes);
                    char[] numero = ch.getNumero().toCharArray();
                    chandler.characters(numero,0,numero.length);
                    chandler.endElement("", "numero", "numero");
                    
                    chandler.startElement("", "version", "version", attributes);
                    char[] version = ch.getVersion().toCharArray();
                    chandler.characters(version,0,version.length);
                    chandler.endElement("", "version", "version");
                    
                    chandler.startElement("", "clean", "clean", attributes);
                    char[] clean = ch.getCleans().toCharArray();
                    chandler.characters(clean,0,clean.length);
                    chandler.endElement("", "clean", "clean");
                    
                    chandler.startElement("", "trad", "trad", attributes);
                    char[] trad = ch.getTrads().toCharArray();
                    chandler.characters(trad,0,trad.length);
                    chandler.endElement("", "trad", "trad");
                    
                    chandler.startElement("", "check", "check", attributes);
                    char[] check = ch.getChecks().toCharArray();
                    chandler.characters(check,0,check.length);
                    chandler.endElement("", "check", "check");
                    
                    chandler.startElement("", "edit", "edit", attributes);
                    char[] edit = ch.getEdits().toCharArray();
                    chandler.characters(edit,0,edit.length);
                    chandler.endElement("", "edit", "edit");
                    
                    chandler.startElement("", "qc", "qc", attributes);
                    char[] qc = ch.getQcs().toCharArray();
                    chandler.characters(qc,0,qc.length);
                    chandler.endElement("", "qc", "qc");
                    
                    chandler.startElement("", "team", "team", attributes);
                    char[] team = ch.getTeam().toCharArray();
                    chandler.characters(team,0,team.length);
                    chandler.endElement("", "team", "team");
                    
                    chandler.startElement("", "description", "description", attributes);
                    char[] description = ch.getDescription().toCharArray();
                    chandler.characters(description,0,description.length);
                    chandler.endElement("", "description", "description");
                    
                    chandler.startElement("", "resume", "resume", attributes);
                    char[] resume = ch.getResume().toCharArray();
                    chandler.characters(resume,0,resume.length);
                    chandler.endElement("", "resume", "resume");

                    // ending
                    chandler.endElement("", "chapitre", "chapitre");
                }
                
                // Main element - ending
                chandler.endElement("", "manga", "manga");
                chandler.endDocument();
            }

            @Override
            public void parse(String systemId) throws IOException, SAXException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        }

        public void getXML(OutputStream outstream) throws IOException, TransformerConfigurationException, TransformerException{
            XMLReader pread = new MangaChapterReader();
            InputSource psource = new MangaChapterSource(chapters);
            Source source = new SAXSource(pread, psource);

            Result resultat = new StreamResult(outstream);

            TransformerFactory fabrique = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = fabrique.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, resultat);
            
            outstream.flush();
        }
        
        public void getXML(File file) throws IOException, TransformerConfigurationException, TransformerException{
            XMLReader pread = new MangaChapterReader();
            InputSource psource = new MangaChapterSource(chapters);
            Source source = new SAXSource(pread, psource);

            Result resultat = new StreamResult(file);

            TransformerFactory fabrique = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = fabrique.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, resultat);
        }

        public void setMangaChapters(List<MangaChapter> nodes){
            this.chapters = nodes;
        }
    }
    
    public static class MangaChaptersHandler {
        
        /*   Structure à lire :
        <nodes>
            <node fl="" ch="">encnode</node>
        </nodes>
        */
        
        NdHandler cch;
    
        public MangaChaptersHandler(InputStream is) throws ParserConfigurationException, SAXException, IOException{
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur = fabrique.newSAXParser();

            cch = new NdHandler();
            parseur.parse(is, cch);
        }
        
        public MangaChaptersHandler(File file) throws ParserConfigurationException, SAXException, IOException{
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur = fabrique.newSAXParser();

            cch = new NdHandler();
            parseur.parse(file, cch);
        }
        
        public MangaChaptersHandler(String str) throws ParserConfigurationException, SAXException, IOException{
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur = fabrique.newSAXParser();

            InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
            cch = new NdHandler();
            parseur.parse(stream, cch);
        }

        public List<MangaChapter> getMangaChapters(){
            return cch.getMangaChapters();
        }

        public class NdHandler extends DefaultHandler{

            //résultats de notre parsing
            private List<MangaChapter> chapters;
            private MangaChapter ch;
            private String currentZip, currentImageFile;
            //flags nous indiquant la position du parseur
            private boolean inManga, inChapter, inName, inTome, inNumber, 
                    inVersion, inClean, inTrad, inCheck, inEdit, inQC, 
                    inTeam, inDescription, inResume, inMD5, inArc, inCht;
            //buffer nous permettant de récupérer les données 
            private StringBuffer buffer;

            public NdHandler(){
                super();
            }

            public List<MangaChapter> getMangaChapters(){
                return chapters;
            }

            //détection d'ouverture de balise
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{

                buffer = new StringBuffer();

                switch(qName){
                    case "manga":
                        chapters = new ArrayList<>();
                        inManga = true;
                        break;
                    case "chapitre":
                        ch = new MangaChapter();
                        currentZip = attributes.getValue(0);
                        currentImageFile = attributes.getValue(1);
                        inChapter = true;
                        break;
                    case "md5":
                        inMD5 = true;
                        break;
                    case "nom":
                        inName = true;
                        break;
                    case "tome":
                        inTome = true;
                        break;
                    case "arc":
                        inArc = true;
                        break;
                    case "chapitretitre":
                        inCht = true;
                        break;
                    case "numero":
                        inNumber = true;
                        break;
                    case "version":
                        inVersion = true;
                        break;
                    case "clean":
                        inClean = true;
                        break;
                    case "trad":
                        inTrad = true;
                        break;
                    case "check":
                        inCheck = true;
                        break;
                    case "edit":
                        inEdit = true;
                        break;
                    case "qc":
                        inQC = true;
                        break;
                    case "team":
                        inTeam = true;
                        break;
                    case "description":
                        inDescription = true;
                        break;
                    case "resume":
                        inResume = true;
                        break;
                    default:
                        break;
                }
            }

            //détection fin de balise
            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException{

                switch(qName){
                    case "manga":
                        inManga = false;
                        break;
                    case "chapitre":
                        ch.setZip(new File(currentZip));
                        ch.setImageFile(new File(currentImageFile));
                        chapters.add(ch);
                        buffer = null;
                        inChapter = false;
                        break;
                    case "md5":
                        ch.setMD5(buffer.toString());
                        buffer = null;
                        inMD5 = false;
                        break;
                    case "nom":
                        ch.setNom(buffer.toString());
                        buffer = null;
                        inName = false;
                        break;
                    case "tome":
                        ch.setTome(buffer.toString());
                        buffer = null;
                        inTome = false;
                        break;
                    case "arc":
                        ch.setArc(buffer.toString());
                        buffer = null;
                        inArc = false;
                        break;
                    case "chapitretitre":
                        ch.setChapitre(buffer.toString());
                        buffer = null;
                        inCht = false;
                        break;
                    case "numero":
                        ch.setNumero(buffer.toString());
                        buffer = null;
                        inNumber = false;
                        break;
                    case "version":
                        ch.setVersion(buffer.toString());
                        buffer = null;
                        inVersion = false;
                        break;
                    case "clean":
                        ch.setCleans(buffer.toString());
                        buffer = null;
                        inClean = false;
                        break;
                    case "trad":
                        ch.setTrads(buffer.toString());
                        buffer = null;
                        inTrad = false;
                        break;
                    case "check":
                        ch.setChecks(buffer.toString());
                        buffer = null;
                        inCheck = false;
                        break;
                    case "edit":
                        ch.setEdits(buffer.toString());
                        buffer = null;
                        inEdit = false;
                        break;
                    case "qc":
                        ch.setQcs(buffer.toString());
                        buffer = null;
                        inQC = false;
                        break;
                    case "team":
                        ch.setTeam(buffer.toString());
                        buffer = null;
                        inTeam = false;
                        break;
                    case "description":
                        ch.setDescription(buffer.toString());
                        buffer = null;
                        inDescription = false;
                        break;
                    case "resume":
                        ch.setResume(buffer.toString());
                        buffer = null;
                        inResume = false;
                        break;
                    default:
                        break;
                }
            }

            //détection de caractères
            @Override
            public void characters(char[] ch,int start, int length)
                            throws SAXException{
                String lecture = new String(ch,start,length);
                if(buffer != null) {
                    buffer.append(lecture);
                }       
            }

            //début du parsing
            @Override
            public void startDocument() throws SAXException {
    //            System.out.println("Début du parsing");
            }

            //fin du parsing
            @Override
            public void endDocument() throws SAXException {
    //            System.out.println("Fin du parsing");
    //            System.out.println("Resultats du parsing");
    //            for(ParticleObject p : lpo){
    //                    System.out.println(p);
    //            }
            }

        }
    }
    
}
