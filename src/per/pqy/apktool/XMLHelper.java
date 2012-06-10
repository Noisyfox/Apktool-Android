package per.pqy.apktool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;
import android.os.Bundle;

public abstract class XMLHelper {

	public class XMLFile extends File {
		private static final long serialVersionUID = 461786746277636145L;

		public XMLFile(String path) {
			super(path);
		}
	}

	public class XMLTags {
		private String name = "";
		private String value = "";
		private List<XMLTags> childTags = new ArrayList<XMLTags>();
		private XMLTags fatherTag = null;
		private Bundle attributes = new Bundle();

		public XMLTags(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setValue(String value) {
			this.value = value == null ? "" : value;
		}

		public String getValue() {
			return value;
		}

		public void setAttribute(String name, String value) {
			if (attributes.keySet().contains(name)) {
				attributes.remove(name);
			}
			attributes.putString(name, value);
		}

		public String getAttribute(String name) {
			return attributes.getString(name);
		}
		
		public String getAttribute(int index) {
			return getAttributeKey(index);
		}
		
		public int getAttributeCount(){
			return attributes.size();
		}
		
		public String getAttributeKey(int index) {
			return (String) attributes.keySet().toArray()[index];
		}
		

		public void addChildTag(XMLTags tag) {
			if (tag.fatherTag != null) {
				tag.fatherTag.removeChildTag(tag);
			}
			if (!childTags.contains(tag)) {
				childTags.add(tag);
			}
			tag.setFatherTag(this);
		}

		public void removeChildTag(XMLTags tag) {
			if (childTags.remove(tag)) {
				tag.setFatherTag(null);
			}
		}

		public void setFatherTag(XMLTags fathertag) {
			fatherTag = fathertag;
		}
		
		public boolean hasChildTag(){
			return !childTags.isEmpty();
		}
		
		public int getChildTagCount(){
			return childTags.size();
		}
		
		public XMLTags getChildTag(int index){
			return childTags.get(index);
		}

	}

	public XMLFile openXML(String path) {
		XMLFile xml = new XMLFile(path);
		boolean success = true;
		if (!xml.isFile())
			success = false;
		return success ? xml : null;
	}

	public XMLFile createXML(String path) {
		XMLFile xml = new XMLFile(path);
		OutputStream outStream = null;
		boolean success = true;
		try {
			xml.createNewFile();
			// xml.setWritable(true);
			outStream = new BufferedOutputStream(new FileOutputStream(xml,
					false));
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(outStream, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.endDocument();
			outStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
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
		return success ? xml : null;
	}

	private XMLTags readTag(XmlPullParser parser) throws Exception {
		while (parser.getEventType() != XmlPullParser.START_TAG) {
			if (parser.getEventType() == XmlPullParser.END_DOCUMENT)
				return null;
			parser.next();
		}
		String name = parser.getName();
		XMLTags rootTag = new XMLTags(name);
		for (int i = 0; i < parser.getAttributeCount(); i++) {
			rootTag.setAttribute(parser.getAttributeName(i),
					parser.getAttributeValue(i));
		}
		parser.next();
		int eventType = parser.getEventType();
		boolean finish = false;
		while (!finish && eventType != XmlPullParser.END_DOCUMENT) {
			eventType = parser.getEventType();
			switch (eventType) {
			case XmlPullParser.START_TAG:// 元素标签开始，START_TAG标签开始常量
				XMLTags childTag = readTag(parser);
				rootTag.addChildTag(childTag);
				parser.next();
				break;

			case XmlPullParser.TEXT://
				rootTag.setValue(parser.getText());
				parser.next();
				break;

			case XmlPullParser.END_TAG:// 元素标签结束，END_TAG结束常量
				finish = true;
				break;

			default:
				parser.next();
			}
			eventType = parser.getEventType();
		}
		if(rootTag.hasChildTag())rootTag.setValue("");
		return rootTag;
	}

	public XMLTags readXML(XMLFile xml) {
		InputStream inStream = null;
		XMLTags rootTag = null;
		try {
			inStream = new BufferedInputStream(new FileInputStream(xml));
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(inStream, null);
			rootTag = readTag(parser);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return rootTag;
	}

	private void writeTag(XmlSerializer serializer, XMLTags tag, int level)
			throws Exception {
		for (int i = 0; i < level; i++) {
			serializer.text("    ");
		}
		serializer.startTag(null, tag.getName());
		for (int i = 0; i < tag.getAttributeCount(); i++) {
			String key = tag.getAttributeKey(i);
			serializer.attribute(null, key, tag.getAttribute(key));
		}
		serializer.text(tag.getValue());
		for(int i = 0; i< tag.getChildTagCount(); i++){
			serializer.text("\n");
			writeTag(serializer, tag.getChildTag(i), level + 1);
		}
		if (tag.hasChildTag()) {
			serializer.text("\n");
			for (int i = 0; i < level; i++) {
				serializer.text("    ");
			}
		}
		serializer.endTag(null, tag.getName());
	}

	public boolean writeXML(XMLFile xml) {
		OutputStream outStream = null;
		boolean success = true;
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(xml));
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(outStream, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.text("\n");
			writeTag(serializer, generateXML(), 0);
			serializer.endDocument();
			outStream.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		} finally {
			try {
				outStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		}
		return success;
	}

	public boolean writeXML(XMLFile xml, XMLTags tag) {
		OutputStream outStream = null;
		boolean success = true;
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(xml));
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(outStream, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.text("\n");
			writeTag(serializer, tag, 0);
			serializer.endDocument();
			outStream.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		} finally {
			try {
				outStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		}
		return success;
	}

	public abstract XMLTags generateXML();

}
