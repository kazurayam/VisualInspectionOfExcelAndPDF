import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.core.FileType
import com.kazurayam.materialstore.core.JobName
import com.kazurayam.materialstore.core.JobTimestamp
import com.kazurayam.materialstore.core.Material
import com.kazurayam.materialstore.core.MaterialList
import com.kazurayam.materialstore.core.QueryOnMetadata
import com.kazurayam.materialstore.core.Store
import com.kazurayam.materialstore.core.Stores
import com.kazurayam.materialstore.map.MappedResultSerializer
import com.kazurayam.materialstore.map.Mapper
import com.kazurayam.materialstore.mapper.Excel2CSVMapperPOI3
import com.kazurayam.materialstore.mapper.PDF2ImageMapper
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




/*
 *  convert .xlsx files to .csv files, .pdf files to .png files
 */
Store store = Stores.newInstance(storeDir)
JobName jobName = new JobName("NISA_Chronos")
JobTimestamp jobTimestamp = new JobTimestamp("20220307_100608")
MappedResultSerializer serializer = new MappedResultSerializer(store, jobName, jobTimestamp)

// lookup xlsx files
MaterialList excelMaterials =
	store.select(jobName, jobTimestamp, FileType.XLSX, QueryOnMetadata.ANY)
assert excelMaterials.size() > 0
// setup the mapper which converts xlsx to csv
Mapper xlsx2csv = new Excel2CSVMapperPOI3()
xlsx2csv.setStore(store)
xlsx2csv.setMappingListener(serializer)
// execute mapping Excel -> CSV
for (Material xlsx : excelMaterials) {
	xlsx2csv.map(xlsx)
	WebUI.comment("processed ${xlsx.toPath().getFileName().toString()}")
}
// ensure CSV files have been created
MaterialList csvMaterials =
	store.select(jobName, jobTimestamp, FileType.CSV, QueryOnMetadata.ANY)
assert csvMaterials.size() > 0



// lookup pdf files
MaterialList pdfMaterials =
	store.select(jobName, jobTimestamp, FileType.PDF, QueryOnMetadata.ANY)
assert pdfMaterials.size() > 0
// setup the mapper which converts PDF to PNG image
Mapper pdf2image = new PDF2ImageMapper()
pdf2image.setStore(store)
pdf2image.setMappingListener(serializer)
// execute mapping PDF -> PNG
for (Material pdf : pdfMaterials) {
	pdf2image.map(pdf)
	WebUI.comment("processed ${pdf.toPath().getFileName().toString()}")
}
// ensure PNG images have been created
MaterialList pngMaterials =
	store.select(jobName, jobTimestamp, FileType.PNG, QueryOnMetadata.ANY)
assert pngMaterials.size() > 0

