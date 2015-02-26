package url_parser;




import org.apache.xerces.parsers.AbstractSAXParser;
import org.cyberneko.html.HTMLConfiguration;

public class HTMLSAXParser extends AbstractSAXParser {

    public HTMLSAXParser() {
       super(new HTMLConfiguration());
    }
}