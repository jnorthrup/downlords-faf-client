package com.faforever.client.fxml.model;

import com.faforever.client.fxml.infrastructure.JavaNode;
import com.faforever.client.fxml.utils.OsUtils;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Set;

public class XmlDocToJavaNode {

    public Pair<JavaNode, GeneratorConfiguration> buildNodeInfo(Document document, Set<String> importsList) {
        Node firstChild = document.getFirstChild();
        processImports(firstChild, importsList);

        GeneratorConfiguration configuration = new GeneratorConfiguration();
        processFlags(firstChild, configuration);

        Element element = document.getDocumentElement();
        JavaNode tinyNode = populateElement(element, null);
        return new Pair<>(tinyNode, configuration);
    }

    private void processFlags(Node firstChild, GeneratorConfiguration configuration) {
        Node startNode = firstChild;
        while (startNode != null) {
            if ("#comment".equals(startNode.getNodeName())) {
                String commentValue = startNode.getNodeValue().trim();
                configuration.isKotlinController = true;
            }

            startNode = startNode.getNextSibling();
        }
    }

    JavaNode populateElement(Element element, JavaNode parent) {
        JavaNode result = new JavaNode(
            element.getTagName(), parent
        );

        if (element.hasChildNodes()) {
            String text = element.getFirstChild().getTextContent();
            if (!OsUtils.isNullOrEmpty(text.trim())) {
                result.setInnerText(text);
            }
        }
        int attrLength = element.getAttributes().getLength();
        for (int i = 0; i < attrLength; i++) {
            Node attrNode = element.getAttributes().item(i);
            result.Attributes.put(attrNode.getNodeName(), attrNode.getNodeValue());
        }

        int childrenLength = element.getChildNodes().getLength();
        for (int i = 0; i < childrenLength; i++) {
            Node nodeUntyped = element.getChildNodes().item(i);
            if (!(nodeUntyped instanceof Element)) {
                continue;
            }
            result.getChildren().add(populateElement((Element) nodeUntyped, result));
        }

        return result;
    }

    private void processImports(Node firstChild, Set<String> importsList) {
        Node startNode = firstChild;
        while (startNode != null && "import".equals(startNode.getNodeName())) {
            importsList.add(startNode.getNodeValue());
            startNode = startNode.getNextSibling();
        }
    }
}