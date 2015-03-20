/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rnggenerator;

import com.thaiopensource.relaxng.IncorrectSchemaException;
import com.thaiopensource.relaxng.util.ErrorHandlerImpl;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.kohsuke.rng2srng.Translator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author maths22
 */
public class RNGGenerator {
    LinkedHashMap<String, Cstruct> structs;
    Map<String, Node> defs;
    Set<String> usedIds;
    StringBuilder out;

    public RNGGenerator() {
        out = new StringBuilder();
        structs = new LinkedHashMap<>();
        defs = new HashMap<>();
        usedIds = new HashSet<>();
    }

    public void createStructure(String path) {
        StringBuilder code = new StringBuilder();
        try {
            DocumentBuilderFactory builderFactory
                    = DocumentBuilderFactory.newInstance();
            builderFactory.setIgnoringElementContentWhitespace(true);

            URL grammar = new File(path).toURI().toURL();
            DOMResult documentret = new DOMResult();
            Translator.translateXmlToResult(
                    new InputSource(grammar.toExternalForm()),
                    new ErrorHandlerImpl(),
                    documentret);
            Document document = (Document) documentret.getNode();

            NodeList defines = document.getElementsByTagName("define");
            for (int i = 0; i < defines.getLength(); i++) {
                defs.put(defines.item(i).getAttributes().getNamedItem("name").getNodeValue(), defines.item(i).getFirstChild());
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();

            Node start = document.getElementsByTagName("start").item(0);
            String elem1 = start.getFirstChild().getAttributes().getNamedItem("name").getNodeValue();
            Ctype ret = this.processDefineNode(elem1);
            code.append("#include <stdio.h>\n"
                    + "#include <stdlib.h>\n"
                    + "#include <stdbool.h>\n"
                    + "#include <libxml/parser.h>\n"
                    + "#include <libxml/xpath.h>\n").append("\n");
            code.append(((Cstruct)(ret)).getHeaderDefs());
            
            code.append(((Cstruct)(ret)).getDefinition());
            code.append("void main(int argc, char** argv){\n");
            code.append("char *xmlfile = argv[1];\n");
            code.append("xmlDocPtr doc = xmlParseFile(xmlfile);\n");
            code.append("xmlNodePtr ptr = xmlDocGetRootElement(doc);\n");
            code.append("xmlNodePtr parent = ptr;\n");
            code.append("xmlXPathContextPtr xpathCtx = xmlXPathNewContext(doc);\n");
            code.append("int size = 0;\n");
            code.append(((Cstruct)(ret)).getRootType()).append("* root = malloc(sizeof(").append(((Cstruct)(ret)).getRootType().replaceFirst("\\*", "")).append("));\n");
            code.append(((Cstruct)(ret)).getParseCode("root"));
            code.append("}\n");
        } catch (IOException | SAXException | IncorrectSchemaException ex) {
            Logger.getLogger(RNGGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(RNGGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(RNGGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(code.toString());
    }

    public Ctype processDefineNode(String ref) {
        Node n = defs.get(ref);
        Ctype ret = processElementNode(n);
        return ret;
    }

    private boolean base; 
    
    public Ctype processElementNode(Node n) {
        String name = "";
        base = true;
        Cstruct outer = new Cstruct();
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) n;
            name = e.getElementsByTagName("name").item(0).getTextContent();
            outer.setName(name);
            //Not currently handled correctly:
                /*
             nameClass ::= <anyName> [exceptNameClass] </anyName>
             | <nsName ns="string"> [exceptNameClass] </nsName>
             | <name ns="string"> NCName </name>
             | <choice> nameClass nameClass </choice>
             */
            String rootname = name;
            int j = 0;
            NodeList children = e.getChildNodes();
            if (e.getTagName().equals("element")) {
                j++;
            }
            for (; j < children.getLength(); j++) {
                Element child = (Element) children.item(j);
                outer.addAll(processPattern(child, true));
            }
        }

        return outer;
    }
    
    public List<Ctype> processPattern(Element pattern){
        return processPattern(pattern, false);
    }

    public List<Ctype> processPattern(Element pattern, boolean fullcode) {
        String tag = pattern.getTagName();
        String ret = "";
        ArrayList<Ctype> cret = new ArrayList<>();
        switch (tag) {
            case "empty":
                return null;
            case "text":
                cret.add(new Cprimitive("char*"));
                break;
            case "data":
                cret.add(new Cprimitive("char*"));
                break;
            case "value":
                cret.add(new Cprimitive("char*"));
                break;
            case "list":
                throw new UnsupportedOperationException("Tag " + tag + "not supported");
            case "attribute":
                cret.add(processElementNode(pattern));
                break;
            case "ref":
                cret.add(processDefineNode(pattern.getAttribute("name")));
                if(fullcode){
                    if(base){
                        ret = "char* "+ret+";\n";
                    } else {
                        
                    }
                }
                break;
            case "oneOrMore":
                cret.addAll(processPattern((Element) pattern.getFirstChild(), false));
                cret.get(0).setPlural(true);
                break;
            case "choice":
                if ("empty".equals(((Element) pattern.getFirstChild()).getTagName())) {
                    cret.addAll(processPattern((Element) pattern.getFirstChild().getNextSibling(), false));
                } else {
                    cret.addAll(processPattern((Element) pattern.getChildNodes().item(0), true));
                    cret.addAll(processPattern((Element) pattern.getChildNodes().item(1), true));
                }

                break;
            case "group":
                cret.addAll(processPattern((Element) pattern.getChildNodes().item(0), true));
                cret.addAll(processPattern((Element) pattern.getChildNodes().item(1), true));
                break;
            case "interleave":
                throw new UnsupportedOperationException("Tag " + tag + "not supported");
            default:
                //throw new UnsupportedOperationException("Tag " + tag + "not supported");
        }
        return cret;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RNGGenerator gen = new RNGGenerator();
        gen.createStructure(args[0]);

    }

}
