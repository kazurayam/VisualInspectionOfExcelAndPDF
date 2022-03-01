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
import com.kazurayam.materialstore.filesystem.FileType
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.metadata.Metadata
import com.kazurayam.materialstore.retadata.QueryOnMetadata
import com.kazurayam.materialstore.map.MappedResultSerializer
import com.kazurayam.materialstore.mapper.ExcelToCsvMapperPOI3

assert pageUrl != null
assert store != null
assert jobName != null
assert jobTimestamp != null

Metadata metadata =
	Metadata.builder().put("URL.host", pageUrl.getHost()).build()

JobTimestamp workingTimestamp = JobTimestamp.laterThan(jobTimestamp)
	
/*
 *  convert Excel *.xlsx files into *.csv
 */
// lookup xlsx files
MaterialList excelMaterials = store.select(jobName, jobTimestamp,
	QueryOnMetadata.builder(metadata).build(),
	FileType.XLSX)
assert excelMaterials.size()> 0

// setup the mapper
ExcelToCsvMapper mapper = new ExcelToCsvMapperPOI3();
mapper.setStore(store)
MappedResultSerializer serializer =
	new MappedResultSerializer(store, jobName, workingTimestamp)
mapper.setMappingListener(serializer)

// execute mapping Excel -> CSV
excelMaterials.forEach { xlsxMaterial ->
	mapper.map(xlsxMaterial)
}

// ensure CSV files are created
MaterialList csvMaterials = store.select(jobName, workingTimestamp,
	QueryOnMetadata.ANY, FileType.CSV)
assertTrue csvMaterials.size() > 0


