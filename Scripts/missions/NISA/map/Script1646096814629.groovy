import com.kazurayam.materialstore.filesystem.FileType
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Material
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.map.Mapper
import com.kazurayam.materialstore.map.MappedResultSerializer
import com.kazurayam.materialstore.mapper.ExcelToCsvMapperPOI3
import com.kazurayam.materialstore.metadata.Metadata
import com.kazurayam.materialstore.metadata.QueryOnMetadata

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
Mapper mapper = new ExcelToCsvMapperPOI3();
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
return workingTimestamp
