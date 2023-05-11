import com.kazurayam.materialstore.core.FileType
import com.kazurayam.materialstore.core.Material
import com.kazurayam.materialstore.core.MaterialList
import com.kazurayam.materialstore.core.QueryOnMetadata
import com.kazurayam.materialstore.map.MappedResultSerializer
import com.kazurayam.materialstore.map.Mapper
import com.kazurayam.materialstore.mapper.Excel2CSVMapperPOI3
import com.kazurayam.materialstore.mapper.PDF2ImageJPEGMapper
import com.kazurayam.materialstore.mapper.PDF2ImagePNGMapper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// the following parameters should have been given by the caller
assert store != null
assert jobName != null
assert jobTimestamp != null

/*
 *  mapping stage:
 */
// we will save generated CSV and PNG files into the same directory as the source Excel and PDF
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
	WebUI.comment("mapped ${xlsx.toPath().getFileName().toString()} to CSV")
}
// ensure CSV files have been created
MaterialList csvMaterials =
	store.select(jobName, jobTimestamp, FileType.CSV, QueryOnMetadata.ANY)
assert csvMaterials.size() > 0



// lookup pdf files
MaterialList pdfMaterials =
	store.select(jobName, jobTimestamp, FileType.PDF, QueryOnMetadata.ANY)
assert pdfMaterials.size() > 0



// setup the mapper which converts pdf to PNG image
Mapper pdf2png = new PDF2ImagePNGMapper()
pdf2png.setStore(store)
pdf2png.setMappingListener(serializer)
// execute mapping PDF -> PNG
for (Material pdf : pdfMaterials) {
	pdf2png.map(pdf)
	WebUI.comment("mapped ${pdf.toPath().getFileName().toString()} to PNG")
}
// ensure PNG images have been created
MaterialList pngMaterials =
	store.select(jobName, jobTimestamp, FileType.PNG, QueryOnMetadata.ANY)
assert pngMaterials.size() > 0


/* I have found out that JPEG is not suitable for text-only pages

// setup the mapper with conversts pdf to JPEG image
PDF2ImageJPEGMapper pdf2jpeg = new PDF2ImageJPEGMapper()
pdf2jpeg.setStore(store)
pdf2jpeg.setMappingListener(serializer)
pdf2jpeg.setCompressionQuality(0.7f)
// execute mapping PDF -> JPEG
for (Material pdf : pdfMaterials) {
	pdf2jpeg.map(pdf)
	WebUI.comment("mapped ${pdf.toPath().getFileName().toString()} to JPEG")
}
// ensure JPEG images have been created
MaterialList jpegMaterials =
	store.select(jobName, jobTimestamp, FileType.JPEG, QueryOnMetadata.ANY)
assert jpegMaterials.size() > 0
*/