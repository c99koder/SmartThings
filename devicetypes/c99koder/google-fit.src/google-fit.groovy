/**
 *  Google Fit
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
metadata {
	definition (name: "Google Fit", namespace: "c99koder", author: "Sam Steele") {
		capability "Polling"
		capability "Refresh"
		capability "Step Sensor"
        attribute "stepsgoal", "number"
        attribute "calories", "string"
        attribute "caloriesgoal", "number"
        //attribute "activity", "string"
        //attribute "distance", "string"
        attribute "heartrate", "string"
        attribute "weight", "string"
        attribute "weight_string", "string"
        attribute "weightgoal", "number"
        //attribute "height", "string"
	}

	tiles(scale: 2) {
		standardTile("steps", "device.steps", width: 3, height: 3, canChangeIcon: false, canChangeBackground: false) {
            state("steps", label: '${currentValue} Steps', icon:"st.Health & Wellness.health11", backgroundColor: "#ffffff")                     
         }
        standardTile("calories", "device.calories", width: 3, height: 3, canChangeIcon: false, canChangeBackground: false) {
            state("calories", label: '${currentValue} Calories', icon:"st.Health & Wellness.health12", backgroundColor: "#ffffff")
       }
       //standardTile("activity", "device.activity", width: 3, height: 3, canChangeIcon: false, canChangeBackground: false) {
            //state("activity", label: '${currentValue} minutes', icon:"st.Health & Wellness.health3", backgroundColor: "#ffffff")     
      //}       
       //standardTile("distance", "device.distance", width: 3, height: 3, canChangeIcon: false, canChangeBackground: false) {
            //state("distance", label: '${currentValue} Miles', icon:"st.Health & Wellness.health9", backgroundColor: "#ffffff")
       //}       
        standardTile("heartrate", "device.heartrate", width: 3, height: 3, canChangeIcon: false, canChangeBackground: false) {
            state("heartrate", label: 'Last Reading: ${currentValue} BPM', icon:"st.Health & Wellness.health9", backgroundColor: "#ffffff")     
       }
        standardTile("weight", "device.weight_string", width: 3, height: 3, canChangeIcon: false, canChangeBackground: false) {
            state("weight_string", label: '${currentValue}', icon:"st.Bath.bath2", backgroundColor: "#ffffff")
       }
       //standardTile("height", "device.height", width: 3, height: 3, canChangeIcon: false, canChangeBackground: false) {
            //state("height", label: '${currentValue}', icon:"st.Health & Wellness.health9", backgroundColor: "#ffffff")     
       //}
		standardTile("refresh", "device.switch", width: 3, height: 3, decoration: "flat") {
			state "icon", action:"refresh.refresh", icon:"st.secondary.refresh", defaultState: true
	   }
        main "steps"
        details(["steps", "calories", "activity", "distance", "heartrate", "weight", "height", "refresh"])
	}
}

def parse(String description) {}

def poll() {
	def steps = parent.getSteps()
    
    if(steps) {
    	sendEvent("name":"steps", "value":steps)
        if(parent.getStepsGoal())
	        sendEvent("name":"stepsgoal", "value":parent.getStepsGoal())
        else
    	    sendEvent("name":"stepsgoal", "value":0)
    } else {
    	log.debug "No Google Fit steps data available"
    }

	def calories = parent.getCalories()
    
     if(calories) {
    	sendEvent("name":"calories", "value":String.format("%.0f",calories))
        if(parent.getCaloriesGoal())
	        sendEvent("name":"caloriesgoal", "value":parent.getCaloriesGoal())
        else
    	    sendEvent("name":"caloriesgoal", "value":0)
    } else {
    	log.debug "No Google Fit Calories data available"
    }
     
   // def distance = parent.getDistance()
    
   // if(distance) {
   // 	sendEvent("name":"distance", "value":String.format("%.1f",distance))
   // } else {
   // 	log.debug "No Google Fit Distance data available"
   // }
      
   // def activity = parent.getActivity()
    
   // if(activity) {
    //	sendEvent("name":"activity", "value":String.format("%.0f",activity))
    //} else {
   // 	log.debug "No Google Fit Activity Time data available"
   // }
    
    def heartrate = parent.getHeartRate()
    
    if(heartrate) {
    	sendEvent("name":"heartrate", "value":String.format("%.0f",heartrate))
    } else {
    	log.debug "No Google Fit Heart Rate data available"
    }
    
    def weight = parent.getWeight()
    
    if(weight) {
    	sendEvent("name":"weight", "value":String.format("%.0f",weight))
         if(parent.getWeightGoal())
	        sendEvent("name":"weightgoal", "value":parent.getWeightGoal())
        else
    	    sendEvent("name":"weightgoal", "value":0)        
        if(parent.isMetric()) {
    		sendEvent("name":"weight_string", "value":String.format("%.2f kg",weight))
        } else {
        	weight *= 2.20462;
	    	sendEvent("name":"weight_string", "value":String.format("%.2f lbs",weight))
        }
    } else {
    	log.debug "No Google Fit Weight data available"
    }
    
   // def height = parent.getHeight()
    
   // if(height) {
   // 	sendEvent("name":"height", "value":String.format("%.0f",height))
   // } else {
   // 	log.debug "No Google Fit Height data available"
   // }
}

def refresh() {
    poll()
}