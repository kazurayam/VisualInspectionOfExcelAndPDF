import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.io.FileUtils

import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/unittest/NISA/reportRunner
 */
// prepare the working directory with fixture data
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path fixtureDir = projectDir.resolve("Include/fixture")
Path outputDir = projectDir.resolve("build/tmp/testOutput").resolve("reportRunner")
if (Files.exists(outputDir)) {
	//FileUtils.deleteDirectory(outputDir.toFile())
	// NO I SHOULD PRESERVE THE RESULT OF PREVIOUS RUNS
}
FileUtils.copyDirectory(fixtureDir.toFile(), outputDir.toFile())
Path root = outputDir.resolve("store")


// this test case skips the "Materialize stage" to be gentle to the external web site.


// setup env to call the Map stage
System.setProperty("org.slf4j.simpleLogger.log.com.kazurayam.materialstore.filesystem.StoreImpl", "DEBUG")  // verbose logging
Store store = Stores.newInstance(root)
JobName jobName = new JobName("NISA")
JobTimestamp materializingTimestamp = new JobTimestamp("20220301_105226")

URL pageUrl = new URL("https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html")

// call the Map stage: Excel -> CSV conversion
JobTimestamp addedTimestamp = WebUI.callTestCase(findTestCase("Patrol/NISA/map"),
	["pageUrl": pageUrl, "store": store, "jobName": jobName, "jobTimestamp": materializingTimestamp])

// call the Reduce stage: CSV + CSV -> diff
MProductGroup reduced =
	WebUI.callTestCase(findTestCase("Patrol/NISA/reduce"),
		["pageUrl": pageUrl, "store": store, "jobName": jobName, "jobTimestamp": addedTimestamp])
	
// call the Report stage: generate <JobName>-index.html
int warnings = 
    WebUI.callTestCase(findTestCase("Patrol/NISA/report"),
		["store": store, "jobName": jobName, "mProductGroup": reduced, "criteria": 0.0d])

if (warnings > 0) {
	KeywordUtil.markWarning("found ${warnings} differences.")
}