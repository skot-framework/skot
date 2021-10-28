# Setup Project environment

## Add Environment (Prod, Preprod, Rec)

In the skot module, , add a skSwitchTask for each Env you use in your project : 

* in the skot block of the build.gradle.kts
  `skSwitchTask("TaskEnvName", "dev")`

* launch gradle sync

* All the task added are visible in the gradle task group skot_switch_variant__

## Change current Env

* launch the switch task you want to use 

* launch gradle sync (or gradle clean) task 

## Bash script to update Env on ci/cv client

```bash
#!/usr/bin/env bash
./gradlew yourChangeEnvTask
./gradlew clean
```
