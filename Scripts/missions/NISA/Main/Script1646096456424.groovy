import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

//---------------------------------------------------------------------
/*
 * setup
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("store")

Store store = Stores.newInstance(root)
JobName jobName = new JobName("NISA")
JobTimestamp startingTimestamp = JobTimestamp.now()

URL pageUrl = new URL("https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html")

//---------------------------------------------------------------------
/*
 * Materialize stage
 */
WebUI.callTestCase(findTestCase("missions/NISA/materialize"), 
	["pageUrl": pageUrl, "store": store, "jobName": jobName, "jobTimestamp": startingTimestamp])

//---------------------------------------------------------------------
/*
 * Map stage
 */
// convert Excel files into CSV files
// convert PDF files into HTML files
// will output all derivatives in the currentTimestamp directory
WebUI.callTestCase(findTestCase("missions/NISA/map"),
	["pageUrl": pageUrl, "store": store, "jobName": jobName, "jobTimestamp": startingTimestamp])


//---------------------------------------------------------------------
/*
 * Reduce stage
 */
// lookup a previous jobTimesamp directory.
// compare the current materials with the previos one
// in order to find differences between the 2 versions. --- Chronos mode


//---------------------------------------------------------------------
/*
 * Report stage
 */
// compile a human-readable report
