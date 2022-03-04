import java.util.function.BiFunction

import com.kazurayam.materialstore.filesystem.MaterialList
import com.kazurayam.materialstore.reduce.MProductGroup
import com.kazurayam.materialstore.reduce.MProductGroupBuilder

assert store != null
assert currentMaterialList != null

BiFunction<MaterialList, MaterialList, MProductGroup> func = {
    MaterialList left, MaterialList right ->
	    MProductGroup.builder(left, right)
		    .build()
}

MProductGroup reduced = MProductGroupBuilder.chronos(store, currentMaterialList, func)
assert reduced != null

return reduced    