package com.cn.smart.baselib.logcat.zlog;

import android.util.Log;

import com.cn.smart.baselib.logcat.ZLog;
import com.cn.smart.baselib.logcat.ZLogUtil;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * author：leo on 2016/11/23 10:14
 * email： leocheung4ever@gmail.com
 * description: Xml日志打印类
 * what & why is modified:
 */

public class XmlLog {

    public static void printXml(String tag, String xml, String headString) {

        if (xml != null) {
            xml = XmlLog.formatXML(xml);
            xml = headString + "\n" + xml;
        } else {
            xml = headString + ZLog.NULL_TIPS;
        }

        ZLogUtil.printLine(tag, true);
        String[] lines = xml.split(ZLog.LINE_SEPARATOR);
        for (String line : lines) {
            if (!ZLogUtil.isEmpty(line)) {
                Log.d(tag, "║ " + line);
            }
        }
        ZLogUtil.printLine(tag, false);
    }

    private static String formatXML(String inputXML) {
        try {
            Source xmlInput = new StreamSource(new StringReader(inputXML));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception e) {
            e.printStackTrace();
            return inputXML;
        }
    }
}
