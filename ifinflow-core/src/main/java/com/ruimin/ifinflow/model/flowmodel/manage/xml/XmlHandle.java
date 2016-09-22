 package com.ruimin.ifinflow.model.flowmodel.manage.xml;
 
 import com.ruimin.ifinflow.util.exception.IFinFlowException;
 import java.io.ByteArrayInputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.StringWriter;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;
 import java.util.Random;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import javax.xml.parsers.DocumentBuilder;
 import javax.xml.parsers.DocumentBuilderFactory;
 import javax.xml.parsers.ParserConfigurationException;
 import javax.xml.transform.Result;
 import javax.xml.transform.Transformer;
 import javax.xml.transform.TransformerConfigurationException;
 import javax.xml.transform.TransformerException;
 import javax.xml.transform.TransformerFactory;
 import javax.xml.transform.TransformerFactoryConfigurationError;
 import javax.xml.transform.dom.DOMSource;
 import javax.xml.transform.stream.StreamResult;
 import org.apache.commons.lang.StringUtils;
 import org.jbpm.pvm.internal.util.XmlUtil;
 import org.w3c.dom.Document;
 import org.w3c.dom.Element;
 import org.w3c.dom.Node;
 import org.w3c.dom.NodeList;
 import org.xml.sax.SAXException;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class XmlHandle
 {
   public static Element getRootElement(String jpdlStr)
     throws IFinFlowException
   {
     return getDocument(jpdlStr).getDocumentElement();
   }
   
 
 
 
 
 
 
 
   public static Document getDocument(String jpdlStr)
     throws IFinFlowException
   {
     if (StringUtils.isBlank(jpdlStr)) {
       throw new IFinFlowException(110023, new Object[0]);
     }
     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
     dbf.setNamespaceAware(true);
     
     try
     {
       DocumentBuilder builder = dbf.newDocumentBuilder();
       
       InputStream is = new ByteArrayInputStream(jpdlStr.getBytes("UTF-8"));
       if (null != is) {
         return builder.parse(is);
       }
     } catch (ParserConfigurationException e) {
       e.printStackTrace();
     } catch (SAXException e) {
       e.printStackTrace();
     } catch (IOException e) {
       e.printStackTrace();
     }
     return null;
   }
   
 
 
 
 
 
   public static String dealString(String toDeal)
   {
     if (("".equals(toDeal.trim())) || ("null".equalsIgnoreCase(toDeal))) {
       return "";
     }
     return null != toDeal ? toDeal.trim() : toDeal;
   }
   
 
 
 
 
 
 
 
   public static boolean isNotNull(String toDeal)
   {
     if ((null != toDeal) && (!"".equals(toDeal.trim())) && (!"null".equals(toDeal)))
     {
       return true;
     }
     return false;
   }
   
   public static void output(Node node) {
     TransformerFactory transFactory = TransformerFactory.newInstance();
     try {
       Transformer transformer = transFactory.newTransformer();
       transformer.setOutputProperty("encoding", "utf-8");
       transformer.setOutputProperty("indent", "yes");
       
       DOMSource source = new DOMSource();
       source.setNode(node);
       StreamResult result = new StreamResult();
       result.setOutputStream(System.out);
       
       transformer.transform(source, result);
     } catch (TransformerConfigurationException e) {
       e.printStackTrace();
     } catch (TransformerException e) {
       e.printStackTrace();
     }
   }
   
   public static String toFormatedXML(Document doc) throws TransformerFactoryConfigurationError, TransformerException
   {
     DOMSource source = new DOMSource(doc);
     StringWriter writer = new StringWriter();
     Result result = new StreamResult(writer);
     Transformer transformer = TransformerFactory.newInstance().newTransformer();
     
     transformer.setOutputProperty("encoding", "utf-8");
     transformer.setOutputProperty("indent", "yes");
     transformer.setOutputProperty("cdata-section-elements", "yes");
     transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
     
     transformer.transform(source, result);
     return writer.getBuffer().toString();
   }
   
   public static Element createElement(Element element, String name, String content, Element parent)
   {
     parent.appendChild(element);
     if (StringUtils.isNotBlank(content)) {
       element.setTextContent(content);
     }
     return element;
   }
   
 
   public static Element createElement(Element element, String name, String content, Element parent, String[] propNames, String[] propValues)
   {
     Element newElement = createElement(element, name, content, parent);
     int length = propNames.length;
     for (int i = 0; i < length; i++) {
       newElement.setAttribute(propNames[i], propValues[i]);
     }
     return newElement;
   }
   
   public static Element createElement(Element element, String name, String content, Element parent, String propName, String propValue)
   {
     Element newElement = createElement(element, name, content, parent);
     newElement.setAttribute(propName, propValue);
     return newElement;
   }
   
   public static void setAttribute(Element element, String name, String nodeValue)
   {
     element.setAttribute(name, nodeValue);
   }
   
 
 
 
 
 
 
 
 
 
 
 
   public static Element element(Element element, String proName, String proValue)
   {
     if ((element != null) && (element.hasChildNodes())) {
       for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
       {
         if (child.getNodeType() == 1) {
           Element chileEle = (Element)child;
           if (proValue.equals(XmlUtil.attribute(chileEle, proName))) {
             return (Element)child;
           }
         }
       }
     }
     return null;
   }
   
 
 
 
 
 
 
 
 
 
   public static Element getUniqueTransitionElement(Element element, String nodeName)
   {
     Element result = null;
     if ((element != null) && (element.hasChildNodes())) {
       int count = 0;
       for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
       {
         if (nodeName.equals(child.getNodeName())) {
           if (count > 0) {
             element.removeChild(child);
           } else if (count == 0) {
             result = (Element)child;
           }
           count++;
         }
       }
     }
     return result;
   }
   
 
 
 
 
 
 
 
 
   public static List<Element> elements(Element element, String eleName)
   {
     if ((element == null) || (!element.hasChildNodes())) {
       return Collections.emptyList();
     }
     
     List<Element> elements = new ArrayList();
     for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
     {
       if (child.getNodeType() == 1) {
         Element childElement = (Element)child;
         String childNodeName = childElement.getNodeName();
         
         if (eleName.equals(childNodeName)) {
           elements.add(childElement);
         }
       }
     }
     return elements;
   }
   
   public static void removeElements(Element element, String eleName) {
     if ((element == null) || (!element.hasChildNodes())) {
       return;
     }
     
     for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
     {
       if (child.getNodeType() == 1) {
         Element childElement = (Element)child;
         String childNodeName = childElement.getNodeName();
         
         if (eleName.equals(childNodeName)) {
           element.getParentNode().removeChild(childElement);
         }
       }
     }
   }
   
 
 
 
 
 
 
 
 
 
 
   public static void removeElement(Element parent, String tagName)
   {
     NodeList nl = parent.getChildNodes();
     for (int i = 0; i < nl.getLength(); i++) {
       Node nd = nl.item(i);
       if (nd.getNodeName().equals(tagName)) {
         parent.removeChild(nd);
       }
     }
   }
   
 
 
 
 
 
 
   public static final String randomString(int length)
   {
     Random randGen = null;
     char[] numbersAndLetters = null;
     
     if (length < 1) {
       return null;
     }
     if (randGen == null) {
       randGen = new Random();
       numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
     }
     
     char[] randBuffer = new char[length];
     for (int i = 0; i < randBuffer.length; i++) {
       randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
     }
     return new String(randBuffer);
   }
   
 
 
 
 
 
   public static String replace(String sourceStr, String replaceStr)
   {
     Pattern pattern = Pattern.compile(replaceStr);
     Matcher matcher = pattern.matcher(sourceStr);
     List<String> strs = new ArrayList();
     while (matcher.find()) {
       strs.add(matcher.group(1));
     }
     for (String s : strs)
     {
       String tmpS = s.substring(0, s.indexOf("_") + 1) + randomString(5);
       sourceStr = sourceStr.replace(s, tmpS);
     }
     return sourceStr;
   }
 }

