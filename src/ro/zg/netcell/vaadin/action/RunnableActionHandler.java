/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
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
package ro.zg.netcell.vaadin.action;

import ro.zg.opengroups.vo.Entity;

import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;

public class RunnableActionHandler implements Runnable{

    private ActionHandler nestedHandler;
    private ActionContext actionContext;
    private ActionErrorHandler errorHandler;
    
    public RunnableActionHandler(ActionHandler h, ActionContext ac, ActionErrorHandler errorHandler) {
	nestedHandler=h;
	this.actionContext = ac;
	this.errorHandler = errorHandler;
    }
    
    @Override
    public void run() {
	try {
	    nestedHandler.handle(actionContext);
	} catch (Exception e) {
	    errorHandler.handleActionError(e,actionContext);
	}
	
    }

}
