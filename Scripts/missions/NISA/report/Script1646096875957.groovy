import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.materialstore.MaterialstoreFacade
import com.kazurayam.materialstore.filesystem.JobName
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

assert store != null
assert mProductGroup != null
assert criteria != null

WebUI.comment("report started; criteria=${criteria}, mProductGroup=${mProductGroup.getDescription()}, store=${store}")

JobName jobName = mProductGroup.getJobName()

// the file name of HTML report
String fileName = jobName.toString()+ "-index.html"

MaterialstoreFacade facade = MaterialstoreFacade.newInstance(store)
Path report = facade.report(mProductGroup, criteria, fileName)

assert Files.exists(report)
WebUI.comment("The report can be found at ${report.toString()}")

int warnings = mProductGroup.countWarnings(criteria)
return warnings

