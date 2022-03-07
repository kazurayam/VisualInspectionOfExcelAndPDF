import java.util.function.BiFunction

import com.kazurayam.materialstore.Inspector
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.MProductGroupBuilder
import groovy.json.JsonOutput

/**
 * Test Cases/Patrol/AmznPress/reduce
 */
assert store != null
assert currentMaterialList != null
assert currentMaterialList.size() == 1

BiFunction<MaterialList, MaterialList, MProductGroup> func = {
	MaterialList left, MaterialList right ->
		MProductGroup.builder(left, right)
			.build()
}

MProductGroup prepared = MProductGroupBuilder.chronos(store, currentMaterialList, func)
assert prepared != null

println JsonOutput.prettyPrint(prepared.toString())

Inspector inspector = Inspector.newInstance(store)
MProductGroup reduced = inspector.reduce(prepared)

return reduced