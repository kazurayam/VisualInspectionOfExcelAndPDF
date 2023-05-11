import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.core.JobName
import com.kazurayam.materialstore.core.JobTimestamp
import com.kazurayam.materialstore.core.Store
import com.kazurayam.materialstore.core.Stores
import com.kazurayam.materialstore.util.CopyDir
import com.kazurayam.materialstore.util.DeleteDir
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * prepare the `<projectDir>/store/NISA_Chronos/20220307_100608` with
 * .xlsx and .pdf files, then convert them to .csv and .png files
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path sourceDir = projectDir.resolve("src/test/fixtures/store")
Path storeDir = projectDir.resolve("store")
Path backupDir = projectDir.resolve("store-backup")

// initialize the output storeDir
if (Files.exists(storeDir)) {
	DeleteDir.deleteDirectoryRecursively(storeDir)
}
Files.createDirectories(storeDir)

// remove the store-backup dir if exists
if (Files.exists(backupDir)) {
	DeleteDir.deleteDirectoryRecursively(backupDir)
}

// copy the previous Materials (.xlsx and .pdf)
Files.walkFileTree(sourceDir, new CopyDir(sourceDir, storeDir))

Store store = Stores.newInstance(storeDir)
JobName jobName = new JobName("NISA_Chronos")
JobTimestamp jobTimestamp = new JobTimestamp("20220307_100608")
// call the mapping stage
WebUI.callTestCase(findTestCase("Test Cases/NISA/mapping"),
	["store": store, "jobName": jobName, "jobTimestamp": jobTimestamp])

