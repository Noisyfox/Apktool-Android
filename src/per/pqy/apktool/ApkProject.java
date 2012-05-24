package per.pqy.apktool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import per.pqy.apktool.GlobalValues.GString;
import per.pqy.apktool.XMLHelper.XMLTags;

public class ApkProject {
	public boolean opened = false;
	private File inputApk = null;
	private File outputApk = null;
	private File inputDir = null;
	private File outputDir = null;
	public File project = null;
	private List<Framework> frameworks = null;
	private boolean iApkLocal = false;
	private boolean oApkLocal = false;
	private boolean iDirLocal = false;
	private boolean oDirLocal = false;
	private Context mContext;
	
	private XMLHelper XMLOperator =new XMLHelper(){
		@Override
		public XMLTags generateXML() {
			super.generateXML();
			XMLTags rotTag=new XMLTags("APKTOOL");
			
			return rotTag;
		}
	};

	public class Framework {
		private File _Res = null;
		private String tag = null;
		private boolean local = false;

		public Framework(String p, String t, boolean l) {
			_Res = new File(p);
			tag = t;
			local = l;
		}

		public String getTag() {
			return tag;
		}

		public String getPath() {
			return _Res.getAbsolutePath();
		}

		public File getFile() {
			return _Res;
		}

		public boolean isLocal() {
			return local;
		}
	}

	public ApkProject(Context context) {
		frameworks = new ArrayList<Framework>();
		mContext = context;
	}

	public final boolean openProject(String projectpath) {
		if (opened) {
			Log.d(GString.LOG_TAG, "Project " + projectpath
					+ "has already been opened as " + project.getAbsolutePath());
			return false;
		}
		project = new File(projectpath);
		opened = true;
		return true;
	}

	public final void saveProject() {
		XMLHelper.XMLFile a;
		a=XMLOperator.createXML("/aaaava.xml");
		XMLOperator.writeXML(a);
	}

	public final void addFramework(String frameworkRes, String tag,
			boolean local) {
		Framework _framework = new Framework(frameworkRes, tag, local);
		frameworks.add(_framework);
	}

	public final void loadInputApk(String path) {

	}

	public final File getInputApk() {
		return inputApk;
	}
}
