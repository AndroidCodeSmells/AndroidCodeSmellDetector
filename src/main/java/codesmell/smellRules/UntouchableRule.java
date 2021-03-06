package codesmell.smellRules;
//xml
import codesmell.AbstractSmell;
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UntouchableRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;

    public UntouchableRule() {
        smellyElementList = new ArrayList<>();
    }

    @Override
    public String getSmellName() {
        return "UntouchableRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {

       XmlParser.ElementsCollection layout_widthAttribute = xmlParser.FindAttribute();

       for (Element element : layout_widthAttribute.getElementsWithAttribute()){



           executeChecking(element);





       }

    }
    private void executeChecking(Element element) {


     //   System.out.println(element.getName());

        for (Element elm : element.elements()){

                checkElementWidth(elm);
                executeChecking(elm);

            }



    }
    private void checkElementWidth(Element element) {
        String attributeValue =   element.attributeValue("layout_width");

        if (attributeValue != null)
        {
            attributeValue =  attributeValue.toString();

            if (attributeValue.indexOf("dp") !=-1){

                attributeValue = attributeValue.replace("dp","");
                if (attributeValue.length()<15){
                    float number = Float.valueOf(attributeValue).floatValue();

                    if (number <= 48){
                        Method xmlelement = new Method(element.getName());
                        xmlelement.setHasSmell(true);
                        smellyElementList.add(xmlelement);
                    }
                }

                }



        }

    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }
}