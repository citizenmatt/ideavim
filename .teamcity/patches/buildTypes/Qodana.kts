package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.Qodana
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.qodana
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.ScheduleTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'Qodana'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("Qodana")) {
    expectSteps {
        qodana {
            name = "Qodana"
            reportAsTests = true
            inspectionProfile = customProfile {
                path = ".idea/inspectionProfiles/Qodana.xml"
            }
            param("clonefinder-enable", "true")
            param("clonefinder-languages", "Java")
            param("clonefinder-languages-container", "Java Kotlin")
            param("clonefinder-mode", "")
            param("clonefinder-queried-project", "src")
            param("clonefinder-reference-projects", "src")
            param("fail-build-on-errors", "")
            param("licenseaudit-enable", "true")
            param("namesAndTagsCustom", "repo.labs.intellij.net/static-analyser/qodana")
            param("report-version", "")
            param("yaml-configuration", "")
        }
    }
    steps {
        update<Qodana>(0) {
            clearConditions()
            linter = jvm {
                version = Qodana.JVMVersion.LATEST
            }
        }
    }

    triggers {
        val trigger1 = find<ScheduleTrigger> {
            schedule {
                schedulingPolicy = weekly {
                    dayOfWeek = ScheduleTrigger.DAY.Tuesday
                }
                branchFilter = ""
                triggerBuild = always()
            }
        }
        trigger1.apply {
            enabled = false

        }
    }
}