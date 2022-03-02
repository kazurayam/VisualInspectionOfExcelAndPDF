import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kazurayam.materialstore.metadata.QueryOnMetadata
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

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
WebUI.callTestCase(findTestCase("missions/NISA/materialize"), 
		["pageUrl": pageUrl, "store": store, "jobName": jobName, 
			"jobTimestamp": materializingTimestamp])

//---------------------------------------------------------------------
/*
 * Map stage
 */
// convert Excel files into CSV files
// convert PDF files into HTML files
// will output all derivatives in the currentTimestamp directory
JobTimestamp currentMappingJobTimestamp =
	WebUI.callTestCase(findTestCase("missions/NISA/map"),
		["pageUrl": pageUrl, "store": store, "jobName": jobName,
			"jobTimestamp": materializingTimestamp])


//---------------------------------------------------------------------
/*
 * Reduce stage
 */
// lookup a previous jobTimesamp directory.
// compare the current materials with the previos one
// in order to find differences between the 2 versions. --- Chronos mode

JobTimestamp previousMappingTimestamp = 
	store.queryJobTimestampWithSimilarContentPriorTo(jobName, currentMappingJobTimestamp)

WebUI.comment("previousMappingTimestamp=${previousMappingTimestamp.toString()}")	
WebUI.comment("materializingTimestamp=${materializingTimestamp.toString()}")	
WebUI.comment("currentMappingTimestamp=${currentMappingJobTimestamp.toString()}")


MProductGroup reduced =
	WebUI.callTestCase(findTestCase("missions/NISA/reduce"),
		["pageUrl": pageUrl, "store": store, "jobName": jobName,
			"previousJobTimestamp": previousMappingTimestamp,
			"currentJobTimestamp": currentMappingJobTimestamp
			])


//---------------------------------------------------------------------
/*
 * Report stage
 */
// compile a human-readable report
int warnings =
	WebUI.callTestCase(findTestCase("missions/NISA/report"),
		["store": store, "jobName": jobName, "mProductGroup": reduced, "criteria": 0.0d])


//---------------------------------------------------------------------
/*
 * Epilogue
 */
if (warnings > 0) {
	KeywordUtil.markWarning("found ${warnings} differences.")
}