import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.io.FileUtils

import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/Patrol/AmznPress/setup
 * 
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("store")
Path docsStoreDir = projectDir.resolve("docs/store")

String jobName = "AmznPress"

// remove the directory and files at [projectDir]/store/AmznPress/*
Path targetDir = root.resolve(jobName)
FileUtils.deleteDirectory(targetDir.toFile())
FileUtils.deleteQuietly(root.resolve(jobName + "-index.html").toFile())

// copy the directory and files from [projectDir]/docs/store/AmznPress/* 
// which is the "previous" materials, the current materials will be compared against them
Path sourceDir = docsStoreDir.resolve(jobName)
FileUtils.copyDirectory(sourceDir.resolve("20220307_100304").toFile(), targetDir.resolve("20220307_100304").toFile())
FileUtils.copyDirectory(sourceDir.resolve("20220307_100311").toFile(), targetDir.resolve("20220307_100311").toFile())

