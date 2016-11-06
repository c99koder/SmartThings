/**
 *  Steps Reminder
 *
 *  Copyright 2016 Sam Steele
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Steps Reminder",
    namespace: "c99koder",
    author: "Sam Steele",
    description: "Reminder to take a walk when steps count is low",
    category: "Health & Wellness",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/HealthAndWellness/Cat-HealthAndWellness.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/HealthAndWellness/Cat-HealthAndWellness@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/HealthAndWellness/Cat-HealthAndWellness@2x.png")


preferences {
	section("Choose Your Steps Sensor") {
        input "theSteps", "capability.stepSensor", required: true
    }
	section("Minimum Steps") {
        input "theMinimumSteps", "number", required: true, defaultValue: 1000
    }
	section("Scheduled Time") {
        input "theTime", "time", required: true
    }
}

def installed() {
	initialize()
}

def updated() {
	unsubscribe()
	initialize()
}

def initialize() {
    schedule(theTime, handler)
}

def handler() {
    if(theSteps.currentSteps < theMinimumSteps) {
    	sendPush("You've only taken ${theSteps.currentSteps} steps today.  Go for a walk to help you reach your ${theSteps.currentGoal} steps goal!")
    }
}