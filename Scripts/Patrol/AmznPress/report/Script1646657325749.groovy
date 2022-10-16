import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.materialstore.inspector.Inspector
import com.kazurayam.materialstore.filesystem.JobName
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/Patrol/AmznPress/report
 */

assert store != null
assert mProductGroup != null
assert criteria != null

/**
 * Test Cases/missions/AmznPress/report
 */
WebUI.comment("report started; store=${store}")
println "mProductGroup.toSummary()=" + mProductGroup.toSummary()

JobName jobName = mProductGroup.getJobName()

// the file name of HTML report
String fileName = jobName.toString()+ "-index.html"

Inspector inspector = Inspector.newInstance(store)
Path report = inspector.report(mProductGroup, criteria, fileName)

assert Files.exists(report)
WebUI.comment("The report can be found at ${report.toString()}")

int warnings = mProductGroup.countWarnings(criteria)
return warnings


