package url_parser;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.filters.Purifier;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Андрей on 10.12.2014.
 */
public class TestParser {
   public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
       AbstractSAXParser parser = new HTMLSAXParser();
       URL hp = new URL("http://www.ex.ua/view/81456602");
       URLConnection newconnection = hp.openConnection();
       InputStream inputStream = newconnection.getInputStream();
       InputSource inputSource = new InputSource(inputStream);
       XMLDocumentFilter purifier = new Purifier();
       XMLDocumentFilter[] filters = {purifier};
       LoadURLparserHandler parserHandler = new LoadURLparserHandler();
       parser.setContentHandler(parserHandler);
       parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
       parser.parse(inputSource);
   }
}
