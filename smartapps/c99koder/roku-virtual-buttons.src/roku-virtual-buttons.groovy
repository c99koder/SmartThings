/**
 *  Roku Virtual Buttons
 *
 *  Copyright 2017 Sam Steele
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
    name: "Roku Virtual Buttons",
    namespace: "c99koder",
    author: "Sam Steele",
    description: "Creates virtual buttons to switch between apps / inputs on a Roku TV",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Title") {
		input "roku","capability.mediaController", title: "Roku Device",multiple: false, required: true
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
	subscribe(roku, "activityList", createButtons)
    createButtons()
}

def createButtons() {
	getAllChildDevices().each {
        deleteChildDevice(it.deviceNetworkId)
    }
    
    def activityList = roku.currentValue("activityList");
    def appsNode = new XmlSlurper().parseText(activityList);
    
    appsNode.children().each{
    	def appId = it.@id.toString()
        def deviceLabel = it.text()
        def device = addChildDevice("smartthings", "Momentary Button Tile", appId, null, [label: "Roku: $deviceLabel"])
        state["$device.id"] = appId
        subscribe(device, "switch", switchHandler)
    	log.debug "Created button tile $device.id for channel $deviceLabel ($appId)"
    }
}

def switchHandler(evt) {
    if (evt.value == "on") {
    	roku.launchAppId(state["$evt.device.id"])
    }
}