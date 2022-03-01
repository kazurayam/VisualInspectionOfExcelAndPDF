import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.filesystem.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/unittiest/NISA/materializeRunner
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
	["pageUrl": pageUrl, "store": store, "jobName": jobName, "jobTimestamp": materializingTimestamp])
