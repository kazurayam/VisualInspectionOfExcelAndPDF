import java.util.function.BiFunction

import com.kazurayam.materialstore.Inspector
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.Reducer

/**
 * Test Cases/Patrol/NISA/reduce
 */

assert store != null
assert currentMaterialList != null

// want to look up the list of previous materials stored sometime in the last month
JobTimestamp beginningOfTheMonth = currentMaterialList.getJobTimestamp().beginningOfTheMonth()

MProductGroup reduced = 
	Reducer.chronos(store, currentMaterialList, beginningOfTheMonth)
assert reduced != null

//println prepared.toJson(true)

Inspector inspector = Inspector.newInstance(store)
MProductGroup processed = inspector.process(reduced)

return processed