import java.util.function.BiFunction

import com.kazurayam.materialstore.Inspector
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.MProductGroupBuilder

/**
 * Test Cases/Patrol/NISA/reduce
 */

assert store != null
assert currentMaterialList != null

// want to look up the list of previous materials stored sometime in the last month
JobTimestamp beginningOfTheMonth = currentMaterialList.getJobTimestamp().beginningOfTheMonth()

MProductGroup prepared = 
	MProductGroupBuilder.chronos(store, currentMaterialList, beginningOfTheMonth)
	 
assert prepared != null

//println prepared.toJson(true)

Inspector inspector = Inspector.newInstance(store)
MProductGroup reduced = inspector.reduce(prepared)

return reduced    