package com.pay2all.aeps.PaytmAEPS;

import android.util.Log;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DeviceScanFormate {
    public static String createPidOptXML(String str) throws ParserConfigurationException, TransformerException {
        String str2 = "0";
        String str3 = "1";
        String str4 = "";
        if (str != null) {
            try {
                if (str.length() > 0) {
                    DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
                    newInstance.setNamespaceAware(true);
                    Document newDocument = newInstance.newDocumentBuilder().newDocument();
                    newDocument.setXmlStandalone(true);
                    Element createElement = newDocument.createElement("PidOptions");
                    newDocument.appendChild(createElement);
                    Attr createAttribute = newDocument.createAttribute("ver");
                    createAttribute.setValue("1.0");
                    createElement.setAttributeNode(createAttribute);
                    Element createElement2 = newDocument.createElement("Opts");
                    createElement.appendChild(createElement2);
                    Attr createAttribute2 = newDocument.createAttribute("fCount");
                    createAttribute2.setValue(str3);
                    createElement2.setAttributeNode(createAttribute2);
                    Attr createAttribute3 = newDocument.createAttribute("fType");
                    createAttribute3.setValue("2");
                    createElement2.setAttributeNode(createAttribute3);
                    Attr createAttribute4 = newDocument.createAttribute("iCount");
                    createAttribute4.setValue(str2);
                    createElement2.setAttributeNode(createAttribute4);
                    Attr createAttribute5 = newDocument.createAttribute("iType");
                    createAttribute5.setValue(str2);
                    createElement2.setAttributeNode(createAttribute5);
                    Attr createAttribute6 = newDocument.createAttribute("pCount");
                    createAttribute6.setValue(str2);
                    createElement2.setAttributeNode(createAttribute6);
                    Attr createAttribute7 = newDocument.createAttribute("pType");
                    createAttribute7.setValue(str2);
                    createElement2.setAttributeNode(createAttribute7);
                    Attr createAttribute8 = newDocument.createAttribute("format");
                    createAttribute8.setValue(str);
                    createElement2.setAttributeNode(createAttribute8);
                    Attr createAttribute9 = newDocument.createAttribute("pidVer");
                    createAttribute9.setValue("2.0");
                    createElement2.setAttributeNode(createAttribute9);
                    Attr createAttribute10 = newDocument.createAttribute("timeout");
                    createAttribute10.setValue("20000");
                    createElement2.setAttributeNode(createAttribute10);
                    Attr createAttribute11 = newDocument.createAttribute("otp");
                    createAttribute11.setValue(str4);
                    createElement2.setAttributeNode(createAttribute11);
                    Attr createAttribute12 = newDocument.createAttribute("env");
                    createAttribute12.setValue("P");
                    createElement2.setAttributeNode(createAttribute12);
                    Attr createAttribute13 = newDocument.createAttribute("wadh");
                    createAttribute13.setValue(str4);
                    createElement2.setAttributeNode(createAttribute13);
                    Attr createAttribute14 = newDocument.createAttribute("posh");
                    createAttribute14.setValue("UNKNOWN");
                    createElement2.setAttributeNode(createAttribute14);
                    Element createElement3 = newDocument.createElement("Demo");
                    createElement3.setTextContent(str4);
                    createElement.appendChild(createElement3);
                    Element createElement4 = newDocument.createElement("CustOpts");
                    createElement.appendChild(createElement4);
                    Element createElement5 = newDocument.createElement("Param");
                    createElement4.appendChild(createElement5);
                    Attr createAttribute15 = newDocument.createAttribute("name");
                    createAttribute15.setValue("ValidationKey");
                    createElement5.setAttributeNode(createAttribute15);
                    Attr createAttribute16 = newDocument.createAttribute("value");
                    createAttribute16.setValue("ONLY USE FOR LOCKED DEVICES.");
                    createElement5.setAttributeNode(createAttribute16);
                    Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
                    DOMSource dOMSource = new DOMSource(newDocument);
                    StringWriter stringWriter = new StringWriter();
                    newTransformer.transform(dOMSource, new StreamResult(stringWriter));
                    String replaceAll = stringWriter.getBuffer().toString().replaceAll("[\n\r]", str4).replaceAll("&lt;", "<").replaceAll("&gt;", ">");
                    Log.e("Pid Options", replaceAll);
                    return replaceAll;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return str4;
            }
        }
        str = str3;
        DocumentBuilderFactory newInstance2 = DocumentBuilderFactory.newInstance();
        newInstance2.setNamespaceAware(true);
        Document newDocument2 = newInstance2.newDocumentBuilder().newDocument();
        newDocument2.setXmlStandalone(true);
        Element createElement6 = newDocument2.createElement("PidOptions");
        newDocument2.appendChild(createElement6);
        Attr createAttribute17 = newDocument2.createAttribute("ver");
        createAttribute17.setValue("1.0");
        createElement6.setAttributeNode(createAttribute17);
        Element createElement22 = newDocument2.createElement("Opts");
        createElement6.appendChild(createElement22);
        Attr createAttribute22 = newDocument2.createAttribute("fCount");
        createAttribute22.setValue(str3);
        createElement22.setAttributeNode(createAttribute22);
        Attr createAttribute32 = newDocument2.createAttribute("fType");
        createAttribute32.setValue("2");
        createElement22.setAttributeNode(createAttribute32);
        Attr createAttribute42 = newDocument2.createAttribute("iCount");
        createAttribute42.setValue(str2);
        createElement22.setAttributeNode(createAttribute42);
        Attr createAttribute52 = newDocument2.createAttribute("iType");
        createAttribute52.setValue(str2);
        createElement22.setAttributeNode(createAttribute52);
        Attr createAttribute62 = newDocument2.createAttribute("pCount");
        createAttribute62.setValue(str2);
        createElement22.setAttributeNode(createAttribute62);
        Attr createAttribute72 = newDocument2.createAttribute("pType");
        createAttribute72.setValue(str2);
        createElement22.setAttributeNode(createAttribute72);
        Attr createAttribute82 = newDocument2.createAttribute("format");
        createAttribute82.setValue(str);
        createElement22.setAttributeNode(createAttribute82);
        Attr createAttribute92 = newDocument2.createAttribute("pidVer");
        createAttribute92.setValue("2.0");
        createElement22.setAttributeNode(createAttribute92);
        Attr createAttribute102 = newDocument2.createAttribute("timeout");
        createAttribute102.setValue("20000");
        createElement22.setAttributeNode(createAttribute102);
        Attr createAttribute112 = newDocument2.createAttribute("otp");
        createAttribute112.setValue(str4);
        createElement22.setAttributeNode(createAttribute112);
        Attr createAttribute122 = newDocument2.createAttribute("env");
        createAttribute122.setValue("P");
        createElement22.setAttributeNode(createAttribute122);
        Attr createAttribute132 = newDocument2.createAttribute("wadh");
        createAttribute132.setValue(str4);
        createElement22.setAttributeNode(createAttribute132);
        Attr createAttribute142 = newDocument2.createAttribute("posh");
        createAttribute142.setValue("UNKNOWN");
        createElement22.setAttributeNode(createAttribute142);
        Element createElement32 = newDocument2.createElement("Demo");
        createElement32.setTextContent(str4);
        createElement6.appendChild(createElement32);
        Element createElement42 = newDocument2.createElement("CustOpts");
        createElement6.appendChild(createElement42);
        Element createElement52 = newDocument2.createElement("Param");
        createElement42.appendChild(createElement52);
        Attr createAttribute152 = newDocument2.createAttribute("name");
        createAttribute152.setValue("ValidationKey");
        createElement52.setAttributeNode(createAttribute152);
        Attr createAttribute162 = newDocument2.createAttribute("value");
        createAttribute162.setValue("ONLY USE FOR LOCKED DEVICES.");
        createElement52.setAttributeNode(createAttribute162);
        Transformer newTransformer2 = TransformerFactory.newInstance().newTransformer();

        DOMSource dOMSource2 = new DOMSource(newDocument2);
        StringWriter stringWriter2 = new StringWriter();
        newTransformer2.transform(dOMSource2, new StreamResult(stringWriter2));
        String replaceAll2 = stringWriter2.getBuffer().toString().replaceAll("[\n\r]", str4).replaceAll("&lt;", "<").replaceAll("&gt;", ">");
        Log.e("Pid Options", replaceAll2);
        return replaceAll2;
    }

    public static String createPrecisionPidOptXML(final String biometricFormat) {
        try {
//            final String fTypeStr = "0";
            String formatStr;
            if (biometricFormat != null && biometricFormat.length() > 0) {
                formatStr = biometricFormat;
            }
            else {
                formatStr = "1";
            }
            final String timeOutStr = "20000";
            final String envStr = "P";
            final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            final Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            final Element rootElement = doc.createElement("PidOptions");
            doc.appendChild(rootElement);
            final Attr attrs = doc.createAttribute("ver");
            attrs.setValue("1.0");
            rootElement.setAttributeNode(attrs);
            final Element opts = doc.createElement("Opts");
            rootElement.appendChild(opts);
            Attr attr = doc.createAttribute("fCount");
            attr.setValue("1");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("fType");
            attr.setValue("2");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("iCount");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("iType");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pCount");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pType");
            attr.setValue("");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("format");
            attr.setValue(formatStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pidVer");
            attr.setValue("2.0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("timeout");
            attr.setValue(timeOutStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("otp");
            attr.setValue("");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("env");
            attr.setValue(envStr);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("wadh");
            attr.setValue("");
//            attr.setValue("E0jzJ/P8UopUHAieZn8CKqS4WPMi5ZSYXgfnlfkWjrc=");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("posh");
            attr.setValue("UNKNOWN");
            opts.setAttributeNode(attr);
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            final DOMSource source = new DOMSource(doc);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String tmpOptXml = writer.getBuffer().toString().replaceAll("[\n\r]", "");
            tmpOptXml = tmpOptXml.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            Log.e("Precision Pid Options", tmpOptXml);
            return tmpOptXml;
        }

        catch (Exception ex) {
//            this.reportData.append("Excep message: ");
//            this.reportData.append(ex.getMessage());
//            this.reportData.append(", Cause: ");
//            this.reportData.append(ex.getMessage());
//            Utility.sendReportEmail((Context)this, "createPrecitionOptXml()", this.reportData.toString());
//            this.reportData.setLength(0);
//            Utility.showMessageDialogue((Context)this, "EXCEPTION- " + ex.getMessage(), "EXCEPTION");
            return ex.getMessage();
        }
    }
}