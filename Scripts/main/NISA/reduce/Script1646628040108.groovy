import java.util.function.BiFunction

import com.kazurayam.materialstore.Inspector
import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.MProductGroupBuilder

/**
 * Test Cases/main/NISA/reduce
 */

assert store != null
assert currentMaterialList != null

BiFunction<MaterialList, MaterialList, MProductGroup> func = {
    MaterialList left, MaterialList right ->
	    MProductGroup.builder(left, right)
		    .build()
}

MProductGroup prepared = MProductGroupBuilder.chronos(store, currentMaterialList, func)
assert prepared != null

Inspector inspector = Inspector.newInstance(store)
MProductGroup reduced = inspector.reduce(prepared)

return reduced    