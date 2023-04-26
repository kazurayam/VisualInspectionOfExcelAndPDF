import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.inspectus.core.Inspectus
import com.kazurayam.inspectus.core.Intermediates
import com.kazurayam.inspectus.core.Parameters
import com.kazurayam.inspectus.katalon.KatalonChronosDiff
import com.kazurayam.materialstore.core.JobName
import com.kazurayam.materialstore.core.JobTimestamp
import com.kazurayam.materialstore.core.SortKeys
import com.kazurayam.materialstore.core.Store
import com.kazurayam.materialstore.core.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil

/**
 * Test Cases/NISA/main
 * 
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path local = projectDir.resolve("store")
Path remote = projectDir.resolve("store-backup")
Store store = Stores.newInstance(local)
Store backup = Stores.newInstance(remote)
JobName jobName = new JobName("NISA")
JobTimestamp jobTimestamp = JobTimestamp.now()
SortKeys sortKeys = new SortKeys("seq", "sheet_index", "URL.host", "URL.path", "URL.fragment")

Parameters p =
    new Parameters.Builder()
	    .store(store)
		.backup(backup)
		.jobName(jobName)
		.jobTimestamp(jobTimestamp)
		.baselinePriorTo(jobTimestamp) // compare against the last run's result
		.sortKeys(sortKeys)
		.threshold(1.0)  // ignore differences les than 1.0%
		.build();

Inspectus inspectus = new KatalonChronosDiff("Test Cases/NISA/materialize")
Intermediates result = inspectus.execute(p)

if (result.getWarnings() > 0) {
	KeywordUtil.markWarning("There found ${result.getWarnings()} warning(s)")
}
