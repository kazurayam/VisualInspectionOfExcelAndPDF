import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.ks.URLDownloader
import com.kazurayam.ks.URLResolver
import com.kazurayam.materialstore.core.FileType
import com.kazurayam.materialstore.core.Material
import com.kazurayam.materialstore.core.MaterialList
import com.kazurayam.materialstore.core.Metadata
import com.kazurayam.materialstore.core.QueryOnMetadata
import com.kazurayam.materialstore.map.MappedResultSerializer
import com.kazurayam.materialstore.map.Mapper
import com.kazurayam.materialstore.mapper.Excel2CSVMapperPOI3
import com.kazurayam.materialstore.mapper.PDF2ImagePNGMapper
import com.kazurayam.materialstore.mapper.PDF2ImageJPEGMapper
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


/**
 * 
 */
class Subject {
	private TestObject locator
	private FileType fileType
	Subject(String expr, FileType fileType) {
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

// the following parameters should have been given by the caller
assert store != null
assert jobName != null
assert jobTimestamp != null

URL pageUrl = new URL("https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html")

List<Subject> subjects = new ArrayList<>()
subjects.add(new Subject("(//a[text()='EXCEL'])[1]", FileType.XLSX))
subjects.add(new Subject("(//a[text()='EXCEL'])[2]", FileType.XLSX))
subjects.add(new Subject("(//a[text()='EXCEL'])[3]", FileType.XLSX))
subjects.add(new Subject("(//a[text()='EXCEL'])[4]", FileType.XLSX))
subjects.add(new Subject("(//a[text()='PDF'])[1]", FileType.PDF))
subjects.add(new Subject("(//a[text()='PDF'])[2]", FileType.PDF))
subjects.add(new Subject("(//a[text()='PDF'])[3]", FileType.PDF))
subjects.add(new Subject("(//a[text()='PDF'])[4]", FileType.PDF))

/*
 * materializing stage: visit the URL and download files
 */
WebUI.openBrowser("")
WebUI.navigateToUrl(pageUrl.toExternalForm())
for (int i = 0; i < subjects.size(); i++) {
	// identify the subjet to download
	Subject subject = subjects.get(i);
	String relativeHref = WebUI.getAttribute(subject.getLocator(), "href")
	assert relativeHref != null
	URL materialUrl = URLResolver.resolve(pageUrl, relativeHref)
	// download the web resource
	Path tempFile = Files.createTempFile(null, null)
	long size = URLDownloader.download(materialUrl, tempFile)
	assert size > 0
	// store it into the "store" directory
	Metadata metadata =
		Metadata.builder(materialUrl)
			.put("seq", Integer.toString(i + 1))
			.build();
	store.write(jobName, jobTimestamp, subject.getFileType(),
		metadata, tempFile)
}
WebUI.closeBrowser()

// call the mapping stage
WebUI.callTestCase(findTestCase("Test Cases/NISA/mapping"),
	["store": store, "jobName": jobName, "jobTimestamp": jobTimestamp])