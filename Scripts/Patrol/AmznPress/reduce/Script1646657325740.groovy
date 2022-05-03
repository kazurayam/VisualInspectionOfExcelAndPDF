import java.util.function.BiFunction

import com.kazurayam.materialstore.Inspector
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.Reducer
import groovy.json.JsonOutput

/**
 * Test Cases/Patrol/AmznPress/reduce
 */
assert store != null
assert currentMaterialList != null
assert currentMaterialList.size() == 1

MProductGroup reduced = Reducer.chronos(store, currentMaterialList)
assert reduced != null
println JsonOutput.prettyPrint(reduced.toString())

Inspector inspector = Inspector.newInstance(store)
MProductGroup processed = inspector.process(reduced)

return processed