import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kazurayam.materialstore.filesystem.Metadata
import com.kazurayam.materialstore.filesystem.QueryOnMetadata
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import groovy.json.JsonOutput

/**
 * Test Cases/Patrol/AmznPress/Main_Chronos 
 */

//---------------------------------------------------------------------
/*
 * setup
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("store")

Store store = Stores.newInstance(root)
JobName jobName = new JobName("AmznPress")
JobTimestamp startingTimestamp = JobTimestamp.now()

URL rssUrl = new URL("https://press.aboutamazon.com/rss/news-releases.xml")

//---------------------------------------------------------------------
/*
 * Materialize stage and Map stage in one Test Case
 * 
 * 1. download RSS XML document to store
 * 2. convert RSS XML document into Excel .xlsx file
 * 3. convert Excel file into CSV file 
 */
MaterialList currentMaterialList =
	WebUI.callTestCase(findTestCase("Patrol/AmznPress/materialize_map_map"),
		["rssUrl": rssUrl, "store": store, "jobName": jobName,
			"jobTimestamp": startingTimestamp])


//---------------------------------------------------------------------
/*
 * Reduce stage
 */
// lookup a previous jobTimesamp directory.
// compare the current materials with the previos one
// in order to find differences between the 2 versions. --- Chronos mode
MProductGroup reduced =
	WebUI.callTestCase(findTestCase("Patrol/AmznPress/reduce"),
		["store": store, "currentMaterialList": currentMaterialList ])

println JsonOutput.prettyPrint(reduced.toString())

//---------------------------------------------------------------------
/*
 * Report stage
 */
// compile a human-readable report
int warnings =
	WebUI.callTestCase(findTestCase("Patrol/AmznPress/report"),
		["store": store, "mProductGroup": reduced, "criteria": 0.0d])


//---------------------------------------------------------------------
/*
 * Epilogue
 */
if (warnings > 0) {
	KeywordUtil.markWarning("found ${warnings} differences.")
}