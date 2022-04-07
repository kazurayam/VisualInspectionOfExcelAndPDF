import com.kazurayam.materialstore.filesystem.FileType
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Material
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.filesystem.QueryOnMetadata
import com.kazurayam.materialstore.map.MappedResultSerializer
import com.kazurayam.materialstore.map.Mapper
import com.kazurayam.materialstore.mapper.Excel2CSVMapperPOI3
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/Patrol/NISA/map
 */

assert store != null
assert jobName != null
assert jobTimestamp != null
assert metadata != null

WebUI.comment("map started; metadata=${metadata}, jobTimestamp=${jobTimestamp}, jobName=${jobName}, store=${store}")

JobTimestamp workingTimestamp = JobTimestamp.laterThan(jobTimestamp)
	
/*
 *  convert Excel *.xlsx files into *.csv
 */
// lookup xlsx files
QueryOnMetadata query = QueryOnMetadata.builder(metadata).build()
MaterialList excelMaterials = store.select(jobName, jobTimestamp, query, FileType.XLSX)
assert excelMaterials.size() > 0

// setup the mapper
Mapper mapper = new Excel2CSVMapperPOI3();
mapper.setStore(store)
MappedResultSerializer serializer =
	new MappedResultSerializer(store, jobName, workingTimestamp)
mapper.setMappingListener(serializer)

// execute mapping Excel -> CSV
for (Material xlsxMaterial : excelMaterials) {
	mapper.map(xlsxMaterial)
}

// ensure CSV files are created
MaterialList csvMaterials = store.select(jobName, workingTimestamp,
	QueryOnMetadata.ANY, FileType.CSV)
assert csvMaterials.size() > 0

// return the name of newly created directory
return csvMaterials
