/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rnggenerator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maths22
 */
public class Cstruct extends Ctype {

    ArrayList<Ctype> elements;

    public Cstruct() {
        elements = new ArrayList();
    }

    public void addAll(List<Ctype> items) {
        if (items != null) {
            elements.addAll(items);
        }
    }

    @Override
    public String getParseCode(String parent) {
        StringBuilder ret = new StringBuilder();
        if (elements.size() > 1) {
            if (!isPlural()) {
                ret.append(getType()).append(" ").append(getName()).append("localstruct = malloc(sizeof(").append(getType()).append("));\n");
                for (Ctype t : elements) {
                    ret.append(t.getParseCode(getName() + "localstruct")).append("\n");
                }
                if (parent != null) {
                    ret.append(parent).append("->").append(getName()).append("=").append(getName()).append("localstruct;\n");
                }
            } else {
                ret.append("size = xmlChildElementCount(ptr);\n");
                ret.append("parent = ptr;\n");
                ret.append("ptr = ptr->children;\n");
                ret.append(getType()).append("* ").append(getName()).append("localstructs = calloc(size*3,sizeof(").append(getType()).append("));\n");
                ret.append("int i").append(getName()).append(" = 0;\n");
                ret.append("while(ptr != NULL){\n");
                ret.append("if(ptr->type == XML_ELEMENT_NODE){\n");
                ret.append("xmlXPathSetContextNode(ptr,xpathCtx);\n");
                ret.append(getType()).append(" ").append(getName()).append("localstruct = malloc(sizeof(").append(getType().replaceFirst("\\*", "")).append("));\n");
                for (Ctype t : elements) {
                    ret.append(t.getParseCode(getName() + "localstruct")).append("\n");
                }
                ret.append(getName()).append("localstructs[i").append(getName()).append("] = ").append(getName()).append("localstruct;\n");

                ret.append("i").append(getName()).append("++;\n");
                
                ret.append("}\n");
                ret.append("ptr = ptr->next;\n");
                ret.append("}\n");
                ret.append("ptr = parent;\n");
                if (parent != null) {
                    ret.append(parent).append("->").append(getName()).append("=").append(getName()).append("localstructs;\n");
                }
            }
        } else if (elements.size() == 1) {
            if (elements.get(0) instanceof Cprimitive) {
                return parent+"->"+getName()+"= xmlXPathEval(\"./"+getName()+"\",xpathCtx)->nodesetval->nodeTab[0]->children->content;\n";
                //return "//parseprimhere: " + getName();
            } else {
                return elements.get(0).getParseCode(parent);
            }
        } else {
            return parent+"->"+getName()+"= xmlXPathEval(\"./"+getName()+"\",xpathCtx)->nodesetval->nodeTab[0]->children->content;\n";
        }

        /*for (Ctype t : elements) {
         if (t instanceof Cstruct && !((Cstruct) t).isMinimal()) {
         ret.append("\n").append(((Cstruct) t).getInnerDefinition());
         }
         }*/
        return ret.toString();
    }

    public String getType() {
        if (elements.size() > 1) {
            if (!isPlural()) {
                return getName() + "_struct";
            } else {
                return getName() + "_struct*";
            }
        } else if (elements.size() == 1) {
            if (elements.get(0) instanceof Cprimitive) {
                return "char*";
            } else {
                return "char*";
                //return elements.get(0).getType();
            }
        } else {
            return "bool";
        }
    }

    public String getRootType() {
        if (!isPlural()) {
            return getName() + "_struct";
        } else {
            return getName() + "_struct*";
        }
    }

    @Override
    public String getDeclaration() {
        if (elements.size() > 1) {
            if (!isPlural()) {
                return getName() + "_struct* " + getName() + ";";
            } else {
                return getName() + "_struct** " + getName() + ";";
            }
        } else if (elements.size() == 1) {
            if (elements.get(0) instanceof Cprimitive) {
                return "char* " + getName() + ";";
            } else {
                return elements.get(0).getDeclaration();//+ " " + getName() + ";";
            }
        } else {
            return "bool " + getName() + ";";
        }
    }

    public boolean isMinimal() {
        boolean ret = elements.size() <= 1;
        if (elements.size() == 1) {
            ret = elements.get(0) instanceof Cprimitive;
        }
        return ret;
    }

    public String getHeaderDefs() {
        StringBuilder ret = new StringBuilder();
        ret.append("typedef struct ").append(getName()).append("_struct ").append(getName()).append("_struct;\n");
        for (Ctype t : elements) {
            if (t instanceof Cstruct && !((Cstruct) t).isMinimal()) {
                ret.append("\n").append(((Cstruct) t).getHeaderDefs());
            }
        }
        return ret.toString();
    }

    public String getDefinition() {
        StringBuilder ret = new StringBuilder();

        ret.append("struct ").append(getName()).append("_struct {\n");
        for (Ctype t : elements) {
            ret.append("    ").append(t.getDeclaration()).append("\n");
        }
        ret.append("};\n");
        for (Ctype t : elements) {
            if (t instanceof Cstruct && !((Cstruct) t).isMinimal()) {
                ret.append("\n").append(((Cstruct) t).getInnerDefinition());
            }
        }
        return ret.toString();
    }

    public String getInnerDefinition() {
        StringBuilder ret = new StringBuilder();
        if (elements.size() > 1) {
            ret.append("typedef struct ").append(getName()).append("_struct {\n");
            for (Ctype t : elements) {
                ret.append("    ").append(t.getDeclaration()).append("\n");
            }
            ret.append("} ").append(getName()).append("_struct;\n");
        }
        for (Ctype t : elements) {
            if (t instanceof Cstruct && !((Cstruct) t).isMinimal()) {
                ret.append("\n").append(((Cstruct) t).getDefinition());
            }
        }
        return ret.toString();
    }

}
