package per.pqy.apktool;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class XMLHelper {

	public class XMLFile {
		public File XML = null;
	}

	public XMLFile openXML(String path) {
		XMLFile xmlF = new XMLFile();
		File xml = new File(path);
		xmlF.XML = xml;
		boolean success = true;
		if (!xml.isFile())
			success = false;
		return success ? xmlF : null;
	}

	public XMLFile createXML(String path) {
		XMLFile xmlF = new XMLFile();
		File xml = new File(path);
		xmlF.XML = xml;
		OutputStream outStream = null;
		boolean success = true;
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(xml));
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(outStream, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.endDocument();
			outStream.flush();
		} catch (Exception e) {
			success = false;
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Exception e) {
					success = false;
				}
			}
		}
		return success ? xmlF : null;
	}
	
	public boolean readXML(XMLFile xml){
		return true;
	}
	
	public boolean writeXML(XMLFile xml){
		return true;
	}

}
