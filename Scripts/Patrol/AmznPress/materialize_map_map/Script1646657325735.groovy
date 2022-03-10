import com.kazurayam.materialstore.filesystem.FileType
import com.kazurayam.materialstore.filesystem.Material
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.filesystem.QueryOnMetadata
import com.kazurayam.materialstore.map.MappedResultSerializer
import com.kazurayam.materialstore.map.Mapper
import com.kazurayam.materialstore.map.MappingListener
import com.kazurayam.materialstore.mapper.Excel2CSVMapperPOI3
import com.kazurayam.materialstore.mapper.RSSAmznPress2ExcelMapper
import com.kazurayam.materialstore.materialize.URLMaterializer

/**
 * Test Cases/Patrol/AmznPress/materialize_map_map
 */

assert rssUrl != null

assert store != null
assert jobName != null
assert jobTimestamp != null

// 1. download the XML file of Amazon Press RSS, save it into the store directory
URLMaterializer urlMaterializer = new URLMaterializer(store)
Material rssMaterial = urlMaterializer.materialize(rssUrl, jobName, jobTimestamp, FileType.XML)


// 2. convert the XML file into an Excel file
Mapper rss2excel = new RSSAmznPress2ExcelMapper()
MappingListener serializer = new MappedResultSerializer(store, jobName, jobTimestamp)
rss2excel.setStore(store)
rss2excel.setMappingListener(serializer)
rss2excel.map(rssMaterial)


// 3. convert the Excel file into a CSV file
Mapper excel2csv = new Excel2CSVMapperPOI3()
excel2csv.setStore(store)
excel2csv.setMappingListener(serializer)
Material excelMaterial = store.selectSingle(jobName, jobTimestamp, QueryOnMetadata.ANY, FileType.XLSX)
excel2csv.map(excelMaterial)


// 4. construct a MaterialList which contains only CSV file, and return nit
MaterialList csvList = store.select(jobName, jobTimestamp, QueryOnMetadata.ANY, FileType.CSV)
assert csvList.size() == 1

return csvList

