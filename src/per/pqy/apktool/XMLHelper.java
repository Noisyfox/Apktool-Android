package per.pqy.apktool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class XMLHelper {

	public class XMLFile {
		public File XML = null;
	}

	public class XMLTag {
		private int pointer = 0;
		private List<String> tags = null;

		public XMLTag() {
			tags = new LinkedList<String>();
			pointer = 0;
		}

		public XMLTag(String url) {
			tags = new LinkedList<String>();
			pointer = 0;
		}

		public final boolean hasNext() {
			return (pointer < tags.size() - 1) && tags.size() > 0;
		}

		public final boolean hasForward() {
			return pointer > 0 && tags.size() > 0;
		}

		public final String getRoot() {
			pointer = 0;
			return tags.get(0);
		}

		public final String getNext() {
			pointer += 1;
			return tags.get(pointer);
		}

		public final String getForward() {
			pointer -= 1;
			return tags.get(pointer);
		}

		public final int getLocation() {
			return pointer;
		}

		public final void add(String tag) {
			tags.add(tag);
		}

		public final void add(String tag, int loc) {
			tags.add(loc, tag);
		}

		public final void set(String tag, int loc) {
			tags.set(loc, tag);
		}

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

	public boolean readXML(XMLFile xml) {
		return true;
	}

	public List<Integer> readAttributeInt(XMLFile xml, XMLTag tag,
			String attribute) {
		List<Integer> value = null;
		for (String v : readAttributeString(xml, tag, attribute)) {
			if (value == null) {
				value = new ArrayList<Integer>();
			}
			value.add(new Integer(v));
		}
		return value;
	}

	public List<String> readAttributeString(XMLFile xml, XMLTag tag,
			String attribute) {
		InputStream inStream = null;
		List<String> value = null;
		try {
			inStream = new BufferedInputStream(new FileInputStream(xml.XML));
			XmlPullParser parser = Xml.newPullParser();
			// auto-detect the encoding from the stream
			parser.setInput(inStream, null);
			int eventType = parser.getEventType();
			String currentTag = "";
			boolean finish = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !finish) {
				String name = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文件开始，START_DOCUMENT文件开始开始常量
					currentTag = tag.getRoot();
					break;

				case XmlPullParser.START_TAG:// 元素标签开始，START_TAG标签开始常量
					if (name.equals(currentTag)) {
						if (tag.hasNext()) {
							currentTag = tag.getNext();
						} else {
							if (value == null) {
								value = new ArrayList<String>();
							}
							value.add(parser.getAttributeValue(null, attribute));
							finish = true;
						}
					}
					break;

				case XmlPullParser.END_TAG:// 元素标签结束，END_TAG结束常量
					if (name.equals(currentTag)) {

					}
					break;
				}
				eventType = parser.getEventType();
			}
		} catch (Exception e) {

		}
		return value;
	}

	public boolean writeXML(XMLFile xml) {
		return true;
	}

}
