import com.kazurayam.ks.URLResolver
import com.kazurayam.ks.URLDownloader
import com.kazurayam.materialstore.filesystem.FileType
import com.kazurayam.materialstore.metadata.Metadata
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path

assert pageUrl != null
assert store != null
assert jobName != null
assert jobTimestamp != null

List<Target> targets = new ArrayList<>()
targets.add(new Target("(//a[text()='EXCEL'])[1]", FileType.XLSX))
targets.add(new Target("(//a[text()='EXCEL'])[2]", FileType.XLSX))
targets.add(new Target("(//a[text()='EXCEL'])[3]", FileType.XLSX))
targets.add(new Target("(//a[text()='EXCEL'])[4]", FileType.XLSX))
targets.add(new Target("(//a[text()='PDF'])[1]", FileType.PDF))
targets.add(new Target("(//a[text()='PDF'])[2]", FileType.PDF))
targets.add(new Target("(//a[text()='PDF'])[3]", FileType.PDF))
targets.add(new Target("(//a[text()='PDF'])[4]", FileType.PDF))

WebUI.openBrowser("")
WebUI.navigateToUrl(pageUrl.toExternalForm())

for (int i = 0; i < targets.size(); i++) {
	// identify the target
	Target target = targets.get(i);
	String relativeHref = WebUI.getAttribute(target.getLocator(), "href")
	assert relativeHref != null
	URL materialUrl = URLResolver.resolve(pageUrl, relativeHref)
	// download the web resource
	Path tempFile = Files.createTempFile(null, null)
	long size = URLDownloader.download(materialUrl, tempFile)
	// store it into the "store" directory
	Metadata metadata =
		Metadata.builder(materialUrl)
			.put("seq", Integer.toString(i + 1))
			.build();
	store.write(jobName, jobTimestamp, target.getFileType(),
		metadata, tempFile)		
}

WebUI.closeBrowser()


//---------------------------------------------------------------------
/*
 * Helper classes, helper functions
 */

class Target {
	private TestObject locator
	private FileType fileType
	Target(String expr, FileType fileType) {
		this.locator = byXPath(expr)
		this.fileType = fileType
	}
	TestObject getLocator() {
		return locator
	}
	FileType getFileType() {
		return fileType
	}
	static TestObject byXPath(String expr) {
		TestObject tObj = new TestObject(expr)
		tObj.addProperty("xpath", ConditionType.EQUALS, expr)
		return tObj
	}
}




