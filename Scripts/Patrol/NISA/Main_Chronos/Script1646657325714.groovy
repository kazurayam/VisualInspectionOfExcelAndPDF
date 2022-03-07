import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kazurayam.materialstore.metadata.Metadata
import com.kazurayam.materialstore.metadata.QueryOnMetadata
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import groovy.json.JsonOutput

/**
 * Test Cases/Patrol/NISA/Main_Chronos
 * 
 */

//---------------------------------------------------------------------
/*
 * setup
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("store")

Store store = Stores.newInstance(root)
JobName jobName = new JobName("NISA")
JobTimestamp materializingTimestamp = JobTimestamp.now()

URL pageUrl = new URL("https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html")

//---------------------------------------------------------------------
/*
 * Materialize stage
 */
WebUI.callTestCase(findTestCase("Patrol/NISA/materialize"),
		["pageUrl": pageUrl, "store": store, "jobName": jobName,
			"jobTimestamp": materializingTimestamp])

//---------------------------------------------------------------------
/*
 * Map stage
 */
// convert Excel files into CSV files
// convert PDF files into HTML files
// will output all derivatives in the currentTimestamp directory
Metadata metadata =
	Metadata.builder()
		.put("URL.host", pageUrl.getHost())
		.build()
MaterialList currentMaterialList =
	WebUI.callTestCase(findTestCase("Patrol/NISA/map"),
		["store": store, "jobName": jobName, "jobTimestamp": materializingTimestamp,
			"metadata": metadata])


//---------------------------------------------------------------------
/*
 * Reduce stage
 */
// lookup a previous jobTimesamp directory.
// compare the current materials with the previos one
// in order to find differences between the 2 versions. --- Chronos mode
MProductGroup reduced =
	WebUI.callTestCase(findTestCase("Patrol/NISA/reduce"),
		["store": store, "currentMaterialList": currentMaterialList ])

println JsonOutput.prettyPrint(reduced.toString())

//---------------------------------------------------------------------
/*
 * Report stage
 */
// compile a human-readable report
int warnings =
	WebUI.callTestCase(findTestCase("Patrol/NISA/report"),
		["store": store, "mProductGroup": reduced, "criteria": 0.0d])


//---------------------------------------------------------------------
/*
 * Epilogue
 */
if (warnings > 0) {
	KeywordUtil.markWarning("found ${warnings} differences.")
}