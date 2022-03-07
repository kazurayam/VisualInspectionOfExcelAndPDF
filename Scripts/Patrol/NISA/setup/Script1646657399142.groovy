import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import org.apache.commons.io.FileUtils
import com.kms.katalon.core.configuration.RunConfiguration

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Test Cases/Patrol/NISA/setup
 *
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("store")
Path docsStoreDir = projectDir.resolve("docs/store")

String jobName = "NISA"

// remove the directory and files at [projectDir]/store/AmznPress/*
Path targetDir = root.resolve(jobName)
FileUtils.deleteDirectory(targetDir.toFile())
FileUtils.deleteQuietly(root.resolve(jobName + "-index.html").toFile())

// copy the directory and files from [projectDir]/docs/store/AmznPress/*
// which is the "previous" materials, the current materials will be compared against them
Path sourceDir = docsStoreDir.resolve(jobName)
FileUtils.copyDirectory(sourceDir.toFile(), targetDir.toFile())


