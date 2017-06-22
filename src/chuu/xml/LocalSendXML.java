/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.xml;

import chuu.lib.LocalSend;
import chuu.lib.MangaChapter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
public class LocalSendXML {
    
    public LocalSendXML() {
        
    }
    
    public static void writeStream(LocalSend snd, OutputStream os){
        try {
            LocalSendWriter chswr = new LocalSendWriter();
            chswr.setLocalSend(snd);
            chswr.getXML(os);
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static LocalSend readStream(InputStream is){
        LocalSend snd = new LocalSend();
        try {
            LocalSendHandler chsrd = new LocalSendHandler(is);
            snd = chsrd.getLocalSend();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return snd;
    }
    
    public static void writeFile(LocalSend snd, File file){
        try {
            LocalSendWriter chswr = new LocalSendWriter();
            chswr.setLocalSend(snd);
            chswr.getXML(file);
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static LocalSend readFile(File file){
        LocalSend snd = new LocalSend();
        try {
            LocalSendHandler chsrd = new LocalSendHandler(file);
            snd = chsrd.getLocalSend();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return snd;
    }
    
    public static LocalSend readString(String str){
        LocalSend snd = new LocalSend();
        try {
            LocalSendHandler chsrd = new LocalSendHandler(str);
            snd = chsrd.getLocalSend();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ChaptersXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return snd;
    }
    
    public static class LocalSendWriter {
        /*   Structure à écrire :
        <nodes>
            <node fl="" ch="">encnode</node>
        </nodes>
        */

        LocalSend snd = new LocalSend();

        public LocalSendWriter(){

        }

        public class LocalSendSource extends InputSource{

            LocalSend snd = new LocalSend();

            public LocalSendSource(LocalSend snd){
                super();
                this.snd = snd;
            }

            public LocalSend getLocalSend(){
                return snd;
            }

        }

        public class LocalSendReader implements XMLReader{

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

                if(!(input instanceof LocalSendSource)){
                    throw new SAXException("The object isn't a ChannelSource");
                }
                if(chandler == null){
                    throw new SAXException("ContentHandler not defined");
                }

                LocalSendSource source = (LocalSendSource)input;
                LocalSend snd = source.getLocalSend();
                MangaChapter ch = snd.getMangaChapter();

                // Main element - beginning
                chandler.startDocument();
                chandler.startElement("", "root", "root", attributes);
                
                chandler.startElement("", "nom", "nom", attributes);
                char[] nom = ch.getNom().toCharArray();
                chandler.characters(nom,0,nom.length);
                chandler.endElement("", "nom", "nom");

                chandler.startElement("", "tome", "tome", attributes);
                char[] tome = ch.getTome().toCharArray();
                chandler.characters(tome,0,tome.length);
                chandler.endElement("", "tome", "tome");

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
                
                chandler.startElement("", "fzip", "fzip", attributes);
                char[] fzip = ch.getZip().getAbsolutePath().toCharArray();
                chandler.characters(fzip,0,fzip.length);
                chandler.endElement("", "fzip", "fzip");
                
                chandler.startElement("", "fimage", "fimage", attributes);
                char[] fimage = ch.getImageFile().getAbsolutePath().toCharArray();
                chandler.characters(fimage,0,fimage.length);
                chandler.endElement("", "fimage", "fimage");
                
                chandler.startElement("", "serveur", "serveur", attributes);
                char[] serveur = snd.getServer().toCharArray();
                chandler.characters(serveur,0,serveur.length);
                chandler.endElement("", "serveur", "serveur");

                chandler.startElement("", "port", "port", attributes);
                char[] port = snd.getPort().toCharArray();
                chandler.characters(port,0,port.length);
                chandler.endElement("", "port", "port");
                
                chandler.startElement("", "utilisateur", "utilisateur", attributes);
                char[] utilisateur = snd.getLogin().toCharArray();
                chandler.characters(utilisateur,0,utilisateur.length);
                chandler.endElement("", "utilisateur", "utilisateur");
                
                chandler.startElement("", "motdepasse", "motdepasse", attributes);
                char[] motdepasse = snd.getPassword().toCharArray();
                chandler.characters(motdepasse,0,motdepasse.length);
                chandler.endElement("", "motdepasse", "motdepasse");
                
                // Main element - ending
                chandler.endElement("", "root", "root");
                chandler.endDocument();
            }

            @Override
            public void parse(String systemId) throws IOException, SAXException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        }

        public void getXML(OutputStream outstream) throws IOException, TransformerConfigurationException, TransformerException{
            XMLReader pread = new LocalSendReader();
            InputSource psource = new LocalSendSource(snd);
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
            XMLReader pread = new LocalSendReader();
            InputSource psource = new LocalSendSource(snd);
            Source source = new SAXSource(pread, psource);

            Result resultat = new StreamResult(file);

            TransformerFactory fabrique = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = fabrique.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, resultat);
        }

        public void setLocalSend(LocalSend snd){
            this.snd = snd;
        }
    }
    
    public static class LocalSendHandler {
        
        /*   Structure à lire :
        <nodes>
            <node fl="" ch="">encnode</node>
        </nodes>
        */
        
        NdHandler cch;
    
        public LocalSendHandler(InputStream is) throws ParserConfigurationException, SAXException, IOException{
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur = fabrique.newSAXParser();

            cch = new NdHandler();
            parseur.parse(is, cch);
        }
        
        public LocalSendHandler(File file) throws ParserConfigurationException, SAXException, IOException{
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur = fabrique.newSAXParser();

            cch = new NdHandler();
            parseur.parse(file, cch);
        }
        
        public LocalSendHandler(String str) throws ParserConfigurationException, SAXException, IOException{
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur = fabrique.newSAXParser();

            InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
            cch = new NdHandler();
            parseur.parse(stream, cch);
        }

        public LocalSend getLocalSend(){
            return cch.getLocalSend();
        }

        public class NdHandler extends DefaultHandler{

            //résultats de notre parsing
            private LocalSend snd;
            private MangaChapter ch;
            //flags nous indiquant la position du parseur
            private boolean inSND, inName, inTome, inNumber, inVersion, inClean,
                    inTrad, inCheck, inEdit, inQC, inTeam, inDescription,
                    inResume, inServer, inPort, inUser, inPassword, inZip, inImage;
            //buffer nous permettant de récupérer les données 
            private StringBuffer buffer;

            public NdHandler(){
                super();
            }

            public LocalSend getLocalSend(){
                return snd;
            }

            //détection d'ouverture de balise
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{

                buffer = new StringBuffer();

                switch(qName){
                    case "root":
                        snd = new LocalSend();
                        ch = new MangaChapter();
                        inSND = true;
                        break;
                    case "nom":
                        inName = true;
                        break;
                    case "tome":
                        inTome = true;
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
                    case "fzip":
                        inZip = true;
                        break;
                    case "fimage":
                        inImage = true;
                        break;
                    case "serveur":
                        inServer = true;
                        break;
                    case "port":
                        inPort = true;
                        break;
                    case "utilisateur":
                        inUser = true;
                        break;
                    case "motdepasse":
                        inPassword = true;
                        break;
                    default:
                        break;
                }
            }

            //détection fin de balise
            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException{

                switch(qName){
                    case "root":
                        inSND = false;
                        snd.setMangaChapter(ch);
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
                    case "fzip":
                        ch.setZip(new File(buffer.toString()));
                        buffer = null;
                        inZip = false;
                        break;
                    case "fimage":
                        ch.setImageFile(new File(buffer.toString()));
                        buffer = null;
                        inImage = false;
                        break;
                    case "serveur":
                        snd.setServer(buffer.toString());
                        buffer = null;
                        inServer = false;
                        break;
                    case "port":
                        snd.setPort(buffer.toString());
                        buffer = null;
                        inPort = false;
                        break;
                    case "utilisateur":
                        snd.setLogin(buffer.toString());
                        buffer = null;
                        inUser = false;
                        break;
                    case "motdepasse":
                        snd.setPassword(buffer.toString());
                        buffer = null;
                        inPassword = false;
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
