package url_parser;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Андрей on 10.12.2014.
 */
public class LoadURLparserHandler implements ContentHandler {
    private boolean isParsed=false;
    private String nextName = null;

    public Map<String,String> mapOfUrl = new TreeMap<String, String>(new Comparator<String>(){
        @Override
        public int compare(String s, String s2) {
            return s.compareTo(s2);
        }
    });

    //Add URl from attribute "href" to mapOfUrl
    private void addUrl(Attributes attributes){
        String attr = attributes.getValue("href");
        String title = attributes.getValue("title");
        if(title!=null) nextName = title;
        Pattern pattern = Pattern.compile("^(/load/){1}(\\d+)$");
        Matcher matcher = pattern.matcher(attr);
        if(matcher.find())
            mapOfUrl.put(nextName,attr);
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        if(isParsed)
            return;
    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {
        isParsed=true;
    }

    @Override
    public void startPrefixMapping(String s, String s2) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String s) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("A") || qName.equals("a"))
            addUrl(attributes);

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void ignorableWhitespace(char[] chars, int i, int i2) throws SAXException {

    }

    @Override
    public void processingInstruction(String s, String s2) throws SAXException {

    }

    @Override
    public void skippedEntity(String s) throws SAXException {

    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    }

    public Map<String,String> getMapOfUrl() {
        return mapOfUrl;
    }
}
