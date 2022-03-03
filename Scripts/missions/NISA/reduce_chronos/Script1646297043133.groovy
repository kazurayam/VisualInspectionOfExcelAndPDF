import com.kazurayam.materialstore.MaterialstoreFacade
import com.kazurayam.materialstore.filesystem.FileType
import com.kazurayam.materialstore.filesystem.JobName
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.Material
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.filesystem.Store
import com.kazurayam.materialstore.map.Mapper
import com.kazurayam.materialstore.map.MappedResultSerializer
import com.kazurayam.materialstore.mapper.ExcelToCsvMapperPOI3
import com.kazurayam.materialstore.metadata.Metadata
import com.kazurayam.materialstore.metadata.QueryOnMetadata
import com.kazurayam.materialstore.reduce.MProduct
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import groovy.json.JsonOutput

assert store != null
assert currentMaterialList != null

JobName jobName = currentMaterialList.getJobName()
JobTimestamp currentTimestamp = currentMaterialList.getJobTimestamp()
JobTimestamp previousTimestamp =
	store.queryJobTimestampWithSimilarContentPriorTo(jobName, currentTimestamp)

WebUI.comment("reduce started; jobName=${jobName}, store=${store}")
WebUI.comment("reduce started; previousTimestamp=${previousTimestamp}")
WebUI.comment("reduce started; currentTimestamp=${currentTimestamp}")

// Look up the materials stored in the previous time of run
MaterialList left = store.select(jobName, previousTimestamp, QueryOnMetadata.ANY)
assert left.size() > 0

// Look up the materials stored in the current time of run
MaterialList right = store.select(jobName, currentTimestamp, QueryOnMetadata.ANY)
assert right.size() > 0

// zip 2 Materilas to form a single Artifact
MProductGroup prepared =
	MProductGroup.builder(left, right)
		.build()
assert prepared.size() > 0

//println JsonOutput.prettyPrint(prepared.toString())
		
// make diff with 2 Materials and record it in a single Artifact
MaterialstoreFacade facade = MaterialstoreFacade.newInstance(store)

MProductGroup reduced = facade.reduce(prepared)

return reduced


