/*******************************************************************************
 * Copyright 2012 AdrianIonescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.presentation.utils;

public class UserEvent {
    private String eventType;
    private Object source;
    private Object target;
    
    
    public UserEvent(String eventType, Object source, Object target) {
	super();
	this.eventType = eventType;
	this.source = source;
	this.target = target;
    }


    public String getEventType() {
        return eventType;
    }


    public Object getSource() {
        return source;
    }


    public Object getTarget() {
        return target;
    }
    
    
}
