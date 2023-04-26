import com.kazurayam.materialstore.base.inspector.Inspector
import com.kazurayam.materialstore.base.reduce.MaterialProductGroup
import com.kazurayam.materialstore.base.reduce.Reducer
import com.kazurayam.materialstore.core.JobTimestamp

/**
 * Test Cases/Patrol/NISA/reduce
 */

assert store != null
assert currentMaterialList != null

// want to look up the list of previous materials stored sometime in the last month
JobTimestamp beginningOfTheMonth = currentMaterialList.getJobTimestamp().beginningOfTheMonth()

MaterialProductGroup mpg = 
	Reducer.chronos(store, currentMaterialList, beginningOfTheMonth)
assert mpg != null

//println prepared.toJson(true)

Inspector inspector = Inspector.newInstance(store)
MaterialProductGroup reduced = inspector.reduceAndSort(mpg)

return reduced