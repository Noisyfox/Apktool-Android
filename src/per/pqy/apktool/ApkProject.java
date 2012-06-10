package per.pqy.apktool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import per.pqy.apktool.GlobalValues.GCore;
import per.pqy.apktool.GlobalValues.GMark;
import per.pqy.apktool.GlobalValues.GString;
import android.content.Context;
import android.util.Log;

public class ApkProject {
	public class Framework {
		private File _Res = null;
		private String tag = "";
		private boolean local = false;

		public Framework(String p, String t, boolean l) {
			_Res = new File(p);
			tag = t;
			local = l;
		}

		public File getFile() {
			return _Res;
		}

		public String getPath() {
			return _Res.getAbsolutePath();
		}

		public String getTag() {
			return tag;
		}

		public boolean isLocal() {
			return local;
		}
	}

	private boolean opened = false;
	private String projectName = "";
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

	private Context mContext = null;

	private XMLHelper XMLOperator = new XMLHelper() {
		public XMLTags generateXML() {
			XMLTags rootTag = new XMLTags("apktool_project");
			rootTag.setAttribute("name", projectName);
			rootTag.setAttribute("projectVersion", GCore.MAINAPP_VERSION);
			rootTag.setAttribute("externalVersion", GCore.EXTERNAL_VERSION);

			{// workingDirectoryTag
				XMLTags workingDirectoryTag = new XMLTags("workingDirectory");
				rootTag.addChildTag(workingDirectoryTag);
				// inputApk
				XMLTags inputApkTag = new XMLTags("inputApk");
				inputApkTag.setAttribute("name", inputApk == null ? ""
						: inputApk.getAbsolutePath());
				inputApkTag.setAttribute("isLocal", iApkLocal ? "true"
						: "false");
				workingDirectoryTag.addChildTag(inputApkTag);
				// outputApk
				XMLTags outputApkTag = new XMLTags("outputApk");
				outputApkTag.setAttribute("name", outputApk == null ? ""
						: outputApk.getAbsolutePath());
				outputApkTag.setAttribute("isLocal", oApkLocal ? "true"
						: "false");
				workingDirectoryTag.addChildTag(outputApkTag);
				// inputDir
				XMLTags inputDirTag = new XMLTags("inputDirectory");
				inputDirTag.setAttribute("name", inputDir == null ? ""
						: inputDir.getAbsolutePath());
				inputDirTag.setAttribute("isLocal", iDirLocal ? "true"
						: "false");
				workingDirectoryTag.addChildTag(inputDirTag);
				// outputDir
				XMLTags outputDirTag = new XMLTags("outputDirectory");
				outputDirTag.setAttribute("name", outputDir == null ? ""
						: outputDir.getAbsolutePath());
				outputDirTag.setAttribute("isLocal", oDirLocal ? "true"
						: "false");
				workingDirectoryTag.addChildTag(outputDirTag);
			}
			{// framework-res tag
				XMLTags uses_frameworkTag = new XMLTags("uses-framework");
				rootTag.addChildTag(uses_frameworkTag);
				if (frameworks != null) {
					for (Framework fm : frameworks) {
						XMLTags frameworkTag = new XMLTags("framework");
						uses_frameworkTag.addChildTag(frameworkTag);
						frameworkTag.setAttribute("name",
								fm.getFile() == null ? "" : fm.getFile()
										.getAbsolutePath());
						frameworkTag.setAttribute("tag", fm.tag);
						frameworkTag.setAttribute("isLocal",
								fm.isLocal() ? "true" : "false");
					}
				}
			}
			return rootTag;
		}
	};

	public ApkProject(Context context) {
		frameworks = new ArrayList<Framework>();
		mContext = context;
	}

	public final void addFramework(String frameworkRes, String tag,
			boolean local) {
		Framework _framework = new Framework(frameworkRes, tag, local);
		frameworks.add(_framework);
	}

	public final File getFile(int filemark) {
		switch (filemark) {
		case GMark.MARK_FILE_APK_INPUT:
			return this.inputApk;
		case GMark.MARK_FILE_APK_OUTPUT:
			return this.outputApk;
		case GMark.MARK_FILE_DIR_INPUT:
			return this.inputDir;
		case GMark.MARK_FILE_DIR_OUTPUT:
			return this.outputDir;
		case GMark.MARK_FILE_DIR_PROJECT:
			return this.project;
		default:
		}
		return null;
	}

	public final List<Framework> getFramework() {
		return frameworks;
	}

	public final String getProjectName() {
		return projectName;
	}

	public final void loadInputApk(String path) {

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
		a = XMLOperator.createXML("/sdcard/aa.xml");
		XMLOperator.writeXML(a);
		XMLHelper.XMLTags tag = XMLOperator.readXML(a);
		XMLHelper.XMLFile b;
		b = XMLOperator.createXML("/sdcard/ab.xml");
		XMLOperator.writeXML(b, tag);
	}
}
