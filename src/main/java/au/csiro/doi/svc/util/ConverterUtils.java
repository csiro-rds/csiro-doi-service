/**
 * Copyright 2010, CSIRO Australia.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.csiro.doi.svc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import au.csiro.doi.svc.dto.Creator;
import au.csiro.doi.svc.dto.Description;
import au.csiro.doi.svc.dto.GeoLocation;
import au.csiro.doi.svc.dto.RelatedIdentifier;
import au.csiro.doi.svc.dto.Subject;



/**
 * Generic XML conversion utility. This can be used to take a supplied XML document, and make modifications to the
 * textual data.
 * 
 * Copyright 2012, CSIRO Australia All rights reserved.
 * 
 * @author Martin Pienaar on 22/07/2012
 * @version $Revision$ $Date$
 */
public class ConverterUtils
{
    private static final String VALUE_URI = "valueURI";
    private static final String SCHEME_URI = "schemeURI";
    private static final String SUBJECT_SCHEME = "subjectScheme";

    /**
     * Generate an XML document from a file location string.
     * 
     * @param inputStream
     *            to the file.
     * @param xmlFile
     *            the name of the XML file
     * @return an XML document
     * @throws IOException
     *             when the xmlFile is not accessible
     * @throws JDOMException
     *             when the xmlFile is not valid
     */
    public static Document getXmlDocument(InputStream inputStream, String xmlFile) throws IOException, JDOMException
    {

        SAXBuilder builder = new SAXBuilder();
        return builder.build(inputStream, xmlFile);
    }

    /**
     * Convert the string of XML to a JDOM document.
     * 
     * @param xml
     *            The XML string to be converted.
     * @return The document
     * @throws JDOMException
     *             If the XML is not valid or the conversion otherwise fails
     * @throws IOException
     *             If the string cannot be read.
     */
    public static Document xmlToDoc(String xml) throws JDOMException, IOException
    {
        SAXBuilder builder = new SAXBuilder();
        StringReader reader = new StringReader(xml);
        Document rifCsDoc = builder.build(reader);
        return rifCsDoc;
    }

    /**
     * Update an existing XML element within an XML document using XPath.
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            Namespace associated with the element
     * @param element
     *            the element XPath that needs to be updated
     * @param value
     *            the new value of the element
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static void updateElementValue(Document document, String nameSpace, String element,
            String value) throws JDOMException
    {
        XPath xPath = XPath.newInstance(element);
        xPath.addNamespace(nameSpace, document.getRootElement().getNamespaceURI());
        Element theElement = (Element) xPath.selectSingleNode(document);
        theElement.setText(stripNonValidXMLCharacters(value));
    }

    /**
     * Update an existing XML element within an XML document using XPath.
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            Namespace associated with the element
     * @param element
     *            the element XPath that needs to be updated
     * @param relatedIdentifiers
     *            List of new values of the element
     * @param relatedIdentifierType
     *            The relatedIdentifierType attribute
     * @param relationType
     *            The relationType attribute
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     * @throws IOException 
     */
    public static void updateElementValuesForRelatedIdentifiers(Document document, String nameSpace, String element,
            String relatedIdentifierType, String relationType, List<RelatedIdentifier> relatedIdentifiers)
            throws JDOMException, IOException
    {
        if (CollectionUtils.isNotEmpty(relatedIdentifiers))
        {
            Element baseElement = getElement(document, nameSpace, element);

            if (baseElement != null)
            {
                Element relatedIdentifiersElement = baseElement.getParentElement();
                relatedIdentifiersElement.getContent().clear();
                relatedIdentifiersElement.addContent(baseElement);
                final RelatedIdentifier firstNode = relatedIdentifiers.get(0);

                baseElement.setText(stripNonValidXMLCharacters(firstNode.getValue()));

                if (StringUtils.isNotEmpty(firstNode.getRelatedIdentifierType()))
                {
                    baseElement.setAttribute(DoiMetaDataGenerator.RELATED_IDENTIFIER_TYPE,
                            firstNode.getRelatedIdentifierType());
                }

                if (StringUtils.isNotEmpty(firstNode.getRelationType()))
                {
                    baseElement.setAttribute(DoiMetaDataGenerator.RELATION_TYPE, firstNode.getRelationType());
                }

                for (int counter = 1; counter < relatedIdentifiers.size(); counter++)
                {
                    Element newNode = (Element) baseElement.clone();

                    final RelatedIdentifier ri = relatedIdentifiers.get(counter);
                    newNode.setText(stripNonValidXMLCharacters(ri.getValue()));

                    if (StringUtils.isNotEmpty(ri.getRelatedIdentifierType()))
                    {
                        newNode.setAttribute(DoiMetaDataGenerator.RELATED_IDENTIFIER_TYPE,
                                ri.getRelatedIdentifierType());
                    }
                    if (StringUtils.isNotEmpty(ri.getRelationType()))
                    {
                        newNode.setAttribute(DoiMetaDataGenerator.RELATION_TYPE, ri.getRelationType());
                    }

                    baseElement.getParentElement().addContent(1, newNode);
                }
            }
        }
        else
        {
            // clear the element as there is no data.
            ConverterUtils.removeElement(document, nameSpace, element);
        }
    }

    /**
     * create / update subjects from DoiDto
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            Namespace associated with the element
     * @param element
     *            the element XPath that needs to be updated
     * @param subjects
     *            List of subjects to add
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static void updateElementValuesForSubjects(Document document, String nameSpace, String element,
            List<Subject> subjects) throws JDOMException
    {
        if (CollectionUtils.isNotEmpty(subjects))
        {
            Element baseElement = getElement(document, nameSpace, element);
            
            Element relatedIdentifiersElement = baseElement.getParentElement();
            relatedIdentifiersElement.getContent().clear();
            relatedIdentifiersElement.addContent(baseElement);
            final Subject firstNode = subjects.get(0);

            updateSubjectElement(baseElement, firstNode);

            for (int counter = 1; counter < subjects.size(); counter++)
            {
                Element newNode = (Element) baseElement.clone();

                final Subject s = subjects.get(counter);

                updateSubjectElement(newNode, s);

                baseElement.getParentElement().addContent(1, newNode);
            }
        }
        else
        {
            // clear the element as there is no data.
            ConverterUtils.removeElement(document, nameSpace, element);
        }
    }

    /**
     * Gets an element by name.
     * 
     * @param document
     *            The xml document
     * @param nameSpace
     *            The namespace the element belongs to
     * @param element
     *            The name of the element to retrieve.
     * @return The element if found.
     * @throws JDOMException
     *             On error.
     */
    private static Element getElement(Document document, String nameSpace, String element) throws JDOMException
    {
        XPath xPath = XPath.newInstance(element);
        xPath.addNamespace(nameSpace, document.getRootElement().getNamespaceURI());
        return (Element) xPath.selectSingleNode(document);
    }

    private static void updateSubjectElement(Element baseElement, final Subject firstNode)
    {
        baseElement.setText(stripNonValidXMLCharacters(firstNode.getSubject()));

        if (StringUtils.isNotEmpty(firstNode.getSubjectScheme()))
        {
            baseElement.setAttribute(SUBJECT_SCHEME, firstNode.getSubjectScheme());
        }
        
        if (StringUtils.isNotEmpty(firstNode.getSchemeURI()))
        {
            baseElement.setAttribute(SCHEME_URI, firstNode.getSchemeURI());
        }
        
        if (StringUtils.isNotEmpty(firstNode.getValueURI()))
        {
            baseElement.setAttribute(VALUE_URI, firstNode.getValueURI());
        }
    }

    /**
     * Update an existing XML element within an XML document using XPath.
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            Namespace associated with the element
     * @param element
     *            the element XPath that needs to be updated
     * @param geoLocations
     *            List of new values of the element
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     * @throws IOException
     *             IOException
     */
    public static void updateElementValuesForGeoLocations(Document document, String nameSpace, String element,
            List<GeoLocation> geoLocations) throws JDOMException, IOException
    {
        if (CollectionUtils.isNotEmpty(geoLocations))
        {
            XPath xPath = XPath.newInstance(element);
            xPath.addNamespace(nameSpace, document.getRootElement().getNamespaceURI());
            Element baseElement = (Element) xPath.selectSingleNode(document);
            
            if (baseElement != null)
            {
                final GeoLocation firstNode = geoLocations.get(0);
                Element geoLocationsElement = baseElement.getParentElement();
                geoLocationsElement.getContent().clear();
                baseElement.getContent().clear();
                geoLocationsElement.addContent(baseElement);
                final Element immutableclonedGeoLocationElement = (Element) baseElement.clone();

                Element newElement = createGeoLocationData(document, baseElement.getNamespace(), firstNode);
                if(newElement != null) 
                {
                    baseElement.addContent(newElement);
                }

                detachExistingNodes(xPath, document);

                for (int counter = 1; counter < geoLocations.size(); counter++)
                {
                    Element newGeoLocationNode = (Element) immutableclonedGeoLocationElement.clone();
                    newElement = createGeoLocationData(document, newGeoLocationNode.getNamespace(),
                            geoLocations.get(counter));

                    if (newElement != null)
                    {
                        newGeoLocationNode.addContent(newElement);
                    }

                    baseElement.getParentElement().addContent(1, newGeoLocationNode);
                }  
            }
        }
        else
        {
            // clear the element as there is no data.
            ConverterUtils.removeElement(document, nameSpace, element);
        }
    }

    private static Element createGeoLocationData(Document document, Namespace nameSpace, final GeoLocation first)
            throws JDOMException
    {
        List<Element> elements = new ArrayList<>();

        if (first.isArea())
        {
            Element geoLocationBox = new Element("geoLocationBox", nameSpace);

            Element n = new Element("northBoundLatitude", nameSpace);
            n.setText(first.getNorth());
            elements.add(n);
            Element s = new Element("southBoundLatitude", nameSpace);
            s.setText(first.getSouth());
            elements.add(s);
            Element e = new Element("eastBoundLongitude", nameSpace);
            e.setText(first.getEast());
            elements.add(e);
            Element w = new Element("westBoundLongitude", nameSpace);
            w.setText(first.getWest());
            elements.add(w);

            geoLocationBox.addContent(elements);

            return geoLocationBox;

        }
        else if (first.isPoint())
        {
            Element geoLocationPoint = new Element("geoLocationPoint", nameSpace);

            Element lat = new Element("pointLatitude", nameSpace);
            lat.setText(first.getLatitude());
            elements.add(lat);
            Element lon = new Element("pointLongitude", nameSpace);
            lon.setText(first.getLongitude());
            elements.add(lon);

            geoLocationPoint.addContent(elements);

            return geoLocationPoint;

        }

        return null;
    }

    /**
     * Add / update creators.
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            Namespace associated with the element
     * @param element
     *            the element XPath that needs to be updated
     * @param creators
     *            Creators list
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     * @throws IOException
     *             IOException
     */
    public static void updateElementValuesForCreators(Document document, String nameSpace, String element,
            List<Creator> creators) throws JDOMException, IOException
    {
        if (CollectionUtils.isNotEmpty(creators))
        {
            XPath xPath = XPath.newInstance(element);
            xPath.addNamespace(nameSpace, document.getRootElement().getNamespaceURI());
            Element baseElement = (Element) xPath.selectSingleNode(document);
            
            final Creator firstNode = creators.get(0);
            final Element immutableclonedParentElement = (Element) baseElement.getParentElement().clone();

            baseElement.setText(stripNonValidXMLCharacters(firstNode.getCreatorName()));

            if (StringUtils.isNotEmpty(firstNode.getNameType()))
            {
            	baseElement.setAttribute(DoiMetaDataGenerator.NAME_TYPE, firstNode.getNameType());
            }

            setElementValueForParent(document, baseElement.getParentElement(), DoiMetaDataGenerator.GIVEN_NAME,
                    firstNode.getFirstName());
            setElementValueForParent(document, baseElement.getParentElement(), DoiMetaDataGenerator.FAMILY_NAME,
                    firstNode.getLastName());
            setElementValueForParent(document, baseElement.getParentElement(), DoiMetaDataGenerator.NAME_IDENTIFIER,
                    firstNode.getNameIdentifier());
            setElementValueForParent(document, baseElement.getParentElement(), DoiMetaDataGenerator.AFFILIATION,
                    firstNode.getAffiliation());

            detachExistingNodes(xPath, document);
            
            for (int counter = 1; counter < creators.size(); counter++)
            {
                Element newCreatorNode = (Element) immutableclonedParentElement.clone();
                Element childElement = (Element) newCreatorNode.getChildren().get(0);

                Creator creator = creators.get(counter);
                childElement.setText(stripNonValidXMLCharacters(creator.getCreatorName()));
                if (StringUtils.isNotEmpty(creator.getNameType()))
                {
                	childElement.setAttribute(DoiMetaDataGenerator.NAME_TYPE, creator.getNameType());
                }
                setElementValueForParent(document, newCreatorNode, DoiMetaDataGenerator.GIVEN_NAME,
                        creator.getFirstName());
                setElementValueForParent(document, newCreatorNode, DoiMetaDataGenerator.FAMILY_NAME,
                        creator.getLastName());
                setElementValueForParent(document, newCreatorNode, DoiMetaDataGenerator.NAME_IDENTIFIER,
                        creator.getNameIdentifier());
                setElementValueForParent(document, newCreatorNode, DoiMetaDataGenerator.AFFILIATION,
                        creator.getAffiliation());
                
                Element grandParent = baseElement.getParentElement();
                grandParent.getParentElement().addContent(counter+1, newCreatorNode);
            }
        }
        else
        {
            // clear the element as there is no data.
            ConverterUtils.removeElement(document, nameSpace, element);
        }
    }
    
    /**
     * Add
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            Namespace associated with the element
     * @param element
     *            the element XPath that needs to be updated
     * @param descriptions
     *            The descriptions list
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static void updateElementValuesForDescriptions(Document document, String nameSpace, String element,
            List<Description> descriptions) throws JDOMException
    {
        if (CollectionUtils.isNotEmpty(descriptions))
        {
            Element baseElement = getElement(document, nameSpace, element);

            if (baseElement != null)
            {
                Element descriptionNode = baseElement.getParentElement();
                descriptionNode.getContent().clear();
                descriptionNode.addContent(baseElement);
                final Description firstNode = descriptions.get(0);

                baseElement.setText(stripNonValidXMLCharacters(firstNode.getDescription()));

                if (StringUtils.isNotEmpty(firstNode.getDescriptionType()))
                {
                    baseElement.setAttribute(DoiMetaDataGenerator.DESCRIPTION_TYPE, firstNode.getDescriptionType());
                }

                for (int counter = 1; counter < descriptions.size(); counter++)
                {
                    Element newNode = (Element) baseElement.clone();

                    final Description d = descriptions.get(counter);
                    newNode.setText(stripNonValidXMLCharacters(d.getDescription()));

                    if (StringUtils.isNotEmpty(d.getDescriptionType()))
                    {
                        baseElement.setAttribute(DoiMetaDataGenerator.DESCRIPTION_TYPE, firstNode.getDescriptionType());
                    }

                    baseElement.getParentElement().addContent(1, newNode);
                }
            }
        }
        else
        {
            // clear the element as there is no data.
            ConverterUtils.removeElement(document, nameSpace, element);
        }
    }

    private static void setElementValueForParent(Document document, Element parentElement, String elementName,
            String value) throws JDOMException
    {
        final Element child = parentElement.getChild(elementName, parentElement.getNamespace());

        if (child == null)
        {
            return;
        }
        
        if (StringUtils.isNotEmpty(value))
        {
            child.setText(value);
        }
        else
        {
            child.removeContent();
            child.detach();
        }
    }

    /**
     * Remove element from a document.
     * 
     * @param document
     *            The document.
     * @param nameSpace
     *            The namespace the element belongs to.
     * @param element
     *            The name of the element to remove.
     * @throws JDOMException
     *             JDOMException
     */
    public static void removeElement(Document document, String nameSpace, String element) throws JDOMException
    {
        XPath xPath = XPath.newInstance(element);
        xPath.addNamespace(nameSpace, document.getRootElement().getNamespaceURI());
        Element baseElement = (Element) xPath.selectSingleNode(document);
        if (baseElement != null)
        {
            baseElement.removeContent();
            baseElement.detach();
        }
    }

    /**
     * Update an existing XML element within an XML document using XPath.
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            Namespace associated with the element
     * @param element
     *            the element XPath that needs to be updated
     * @param name
     *            the attribute name
     * @param value
     *            the new value of the element
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static void updateAttribute (Document document, String nameSpace, String element,
            String name, String value) throws JDOMException
    {
        XPath xPath = XPath.newInstance(element);
        xPath.addNamespace(nameSpace, document.getRootElement().getNamespaceURI());
        Element theElement = (Element) xPath.selectSingleNode(document);
        Attribute attr = theElement.getAttribute(name);
        if(attr == null) 
        {
            theElement.setAttribute(new Attribute(name, value));
        }
        else
        {
            attr.setValue(stripNonValidXMLCharacters(value));
        }
    }

    /**
     * Update an existing XML element within an XML document using XPath.
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            Namespace associated with the element
     * @param element
     *            the element XPath that needs to be updated
     * @param values
     *            List of new values of the element
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static void updateElementValues(Document document, String nameSpace, String element, List<String> values)
            throws JDOMException
    {
        XPath xPath = XPath.newInstance(element);
        xPath.addNamespace(nameSpace, document.getRootElement().getNamespaceURI());
        Element baseElement = (Element) xPath.selectSingleNode(document);
        
        baseElement.setText(stripNonValidXMLCharacters(values.get(0)));

        detachExistingNodes(xPath, document);

        for (int counter = 1; counter < values.size(); counter++)
        {

            // Clone the parent of the base element
            Element parent = baseElement.getParentElement();
            Element clonedParentElement = (Element) parent.clone();
            Element childElement = (Element) clonedParentElement.getChildren().get(0);
            childElement.setText(stripNonValidXMLCharacters(values.get(counter)));
            childElement.setAttribute(DoiMetaDataGenerator.NAME_TYPE, "Organizational");

            clonedParentElement.detach();
            Element grandParent = parent.getParentElement();
            grandParent.addContent(1, clonedParentElement);
        }

    }

    /**
     * Removes all elements following the first element
     * 
     * @param xPath
     * @param document
     * @throws JDOMException
     */
    private static void detachExistingNodes(XPath xPath, Document document) throws JDOMException
    {
        @SuppressWarnings("unchecked")
        List<Element> selectNodes = xPath.selectNodes(document);
        for (int counter = 1; counter < selectNodes.size(); counter++)
        {
            Element element = (Element) selectNodes.get(counter);
            element.getParentElement().detach();
        }
    }

    /**
     * Add list of children nodes to a parent node
     * 
     * @param document
     *            the existing XML document to update
     * @param parent
     *            the parent XPath
     * @param childrenElements
     *            children elements to be added
     * @param nameSpaces
     *            one or more XML Namespaces associated with the XML document
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    @SuppressWarnings("unchecked")
    public static void addChildrenToNode(Document document, String parent, List<Element> childrenElements,
            Namespace... nameSpaces) throws JDOMException
    {
        Element parentNode = getElement(document, parent, nameSpaces);
        List<Element> existingChildrenList = parentNode.getChildren();
        existingChildrenList.addAll(childrenElements);
    }

    /**
     * Add list of children nodes to the start of the child list for a parent node
     * 
     * @param document
     *            the existing XML document to update
     * @param parent
     *            the parent XPath
     * @param childrenElements
     *            children elements to be added
     * @param nameSpaces
     *            one or more XML Namespaces associated with the XML document
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    @SuppressWarnings("unchecked")
    public static void insertChildrenToNode(Document document, String parent, List<Element> childrenElements,
            Namespace... nameSpaces) throws JDOMException
    {
        Element parentNode = getElement(document, parent, nameSpaces);
        List<Element> existingChildrenList = parentNode.getChildren();
        existingChildrenList.addAll(0, childrenElements);
    }

    /**
     * Get element from an XML document.
     * 
     * @param document
     *            the existing XML document to update
     * @param element
     *            the element XPath that needs to be added
     * @param nameSpaces
     *            one or more XML Namespaces associated with the XML document
     * @return the element specified by the XPath element, null otherwise.
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static Element getElement(Document document, String element, Namespace... nameSpaces) throws JDOMException
    {
        XPath xPath = XPath.newInstance(element);
        for (int i = 0; i < nameSpaces.length; i++)
        {
            xPath.addNamespace(nameSpaces[i]);
        }
        return (Element) xPath.selectSingleNode(document);
    }

    /**
     * Add a new element containing text to the parent node.
     * 
     * @param parent
     *            The node to which the element is being added.
     * @param elementName
     *            The name of the new element.
     * @param nameSpaceUri
     *            The UIR of the new node's namespace.
     * @param text
     *            The text content of the new node
     */
    public static void addTextElement(Element parent, String elementName, String nameSpaceUri, String text)
    {
        Element description = new Element(elementName, nameSpaceUri);
        description.setText(text);
        parent.addContent(description);
    }

    /**
     * Add a new element containing text to the parent node, allows namespace prefix.
     * 
     * @param parent
     *            The node to which the element is being added.
     * @param elementName
     *            The name of the new element.
     * @param nameSpacePrefix
     *            The prefix of the new node's namespace.
     * @param nameSpaceUri
     *            The UIR of the new node's namespace.
     * @param text
     *            The text content of the new node
     */
    public static void addTextElement(Element parent, String elementName, String nameSpacePrefix, String nameSpaceUri,
            String text)
    {
        Element description = new Element(elementName, nameSpacePrefix, nameSpaceUri);
        description.setText(text);
        parent.addContent(description);
    }

    /**
     * Add a new element containing text to the parent node.
     * 
     * @param parent
     *            The node to which the element is being added.
     * @param elementName
     *            The name of the new element.
     * @param elementType
     *            The type attribute of the new element.
     * @param nameSpaceUri
     *            The UIR of the new node's namespace.
     * @param text
     *            The text content of the new node
     */
    public static void addTextElementWithAttribute(Element parent, String elementName, String elementType,
            String nameSpaceUri, String text)
    {
        Element description = new Element(elementName, nameSpaceUri);
        description.setAttribute("type", elementType);
        description.setText(text);
        parent.addContent(description);
    }

    /**
     * This method ensures that the output String has only valid XML unicode characters as specified by the XML 1.0
     * standard. For reference, please see <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
     * standard</a>. This method will return an empty String if the input is null or empty.
     * 
     * @param in
     *            The String whose non-valid characters we want to remove.
     * @return The in String, stripped of non-valid characters.
     */
    public static String stripNonValidXMLCharacters(String in)
    {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.
        final int charCodeTab = 0x9;
        final int charCodeLF = 0xA;
        final int charCodeCR = 0xD;
        final int charCodeSpace = 0x20;
        final int charCodeMaxBlock1 = 0xD7FF;
        final int charCodeMinBlock2 = 0xE000;
        final int charCodeMaxBlock2 = 0xE000;
        final int charCodeMinBlock3 = 0x10000;
        final int charCodeMaxBlock3 = 0x10FFFF;

        if (in == null || ("".equals(in)))
        {
            return ""; // vacancy test.
        }
        for (int i = 0; i < in.length(); i++)
        {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == charCodeTab) || (current == charCodeLF) || (current == charCodeCR)
                    || ((current >= charCodeSpace) && (current <= charCodeMaxBlock1))
                    || ((current >= charCodeMinBlock2) && (current <= charCodeMaxBlock2))
                    || ((current >= charCodeMinBlock3) && (current <= charCodeMaxBlock3)))
            {
                out.append(current);
            }
        }
        return out.toString();
    }

    /**
     * Add new XML element(s) within an XML document using XPath. Note that one instance of the element must be in the
     * XML document. This is required so that we know where to insert the new elements. This instance of the element
     * will be updated to reflect the first value of the values List. The other values will be added below this.
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            the namespace associated with the element
     * @param element
     *            the element XPath that needs to be added
     * @param values
     *            a List of new value(s) of the element
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static void addElementValues(Document document, String nameSpace, String element,
            List<String> values) throws JDOMException
    {
        XPath xPath = XPath.newInstance(element);
        xPath.addNamespace(nameSpace, document.getRootElement().getNamespaceURI());

        Element templateElement = (Element) xPath.selectSingleNode(document);
        Element parent = templateElement.getParentElement();
        int positionInParent = parent.indexOf(templateElement);

        for (int counter = 0; counter < values.size(); counter++, positionInParent++)
        {
            if (counter == 0)
            {
                updateElementValue(document, nameSpace, element, values.get(counter));
            }
            else
            {
                Element newElement = (Element) templateElement.clone();
                newElement.detach();
                newElement.setText(values.get(counter));
                parent.addContent(positionInParent, newElement);
            }
        }
    }

    /**
     * Add new XML element(s) within an XML document using XPath. Note that one instance of the element must be in the
     * XML document. This is required so that we know where to insert the new elements. The new values will be added
     * below this.
     * 
     * @param document
     *            the existing XML document to update
     * @param nameSpace
     *            an xml namespace associated with the element
     * @param element
     *            the element XPath that needs to be added
     * @param value
     *            a String of the new value of the element
     * @throws JDOMException
     *             when invalid XML parsing/access occurs
     */
    public static void addElementValue(Document document, String nameSpace, String element,
            String value) throws JDOMException
    {
        XPath xPath = XPath.newInstance(element);
        xPath.addNamespace(nameSpace, document.getRootElement().getNamespaceURI());

        Element templateElement = (Element) xPath.selectSingleNode(document);
        Element parent = templateElement.getParentElement();
        int positionInParent = parent.indexOf(templateElement) + 1;

        Element newElement = (Element) templateElement.clone();
        newElement.detach();
        newElement.setText(value);
        parent.addContent(positionInParent, newElement);
    }



    /**
     * Convert a map of namespaces into an array.
     * 
     * @param namespaceMap
     *            The map of namespace prefixes to uris.
     * @return The array of namespace objects
     */
    public static Namespace[] getNamespaceArray(Hashtable<String, String> namespaceMap)
    {
        Namespace namespaces[] = new Namespace[namespaceMap.size()];
        int i = 0;
        for (Map.Entry<String, String> prefixEntry : namespaceMap.entrySet())
        {
            Namespace ns = Namespace.getNamespace(prefixEntry.getKey(), prefixEntry.getValue());
            namespaces[i++] = ns;
        }
        return namespaces;
    }

    /**
     * A convenience method to output an XML document as a String
     * 
     * @param document
     *            the XML document to output as a string
     * @return the XML document output as a string
     * @throws IOException
     *             if the XML Document access fails
     */
    public static String outputTheXml(Document document) throws IOException
    {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        StringWriter writer = new StringWriter();
        outputter.output(document, writer);
        return writer.toString();
    }

}
