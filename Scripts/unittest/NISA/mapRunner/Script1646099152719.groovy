import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.io.FileUtils

import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/unittiest/NISA/mapRunner
 */
// prepare the working directory with fixture data
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path fixtureDir = projectDir.resolve("Include/fixture")
Path outputDir = projectDir.resolve("build/tmp/testOutput").resolve("mapRunner")
if (Files.exists(outputDir)) {
	FileUtils.deleteDirectory(outputDir.toFile())
}
FileUtils.copyDirectory(fixtureDir.toFile(), outputDir.toFile())


// setup env to call the Map stage
Path root = outputDir.resolve("store")
Store store = Stores.newInstance(root)
JobName jobName = new JobName("NISA")
JobTimestamp materializingTimestamp = new JobTimestamp("20220301_105226")

URL pageUrl = new URL("https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html")

// call the Map stage
JobTimestamp mappedTimestamp = WebUI.callTestCase(findTestCase("main/NISA/map"),
	["pageUrl": pageUrl, "store": store, "jobName": jobName, "jobTimestamp": materializingTimestamp])
