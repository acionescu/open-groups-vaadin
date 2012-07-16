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

public abstract class BaseView<T,E extends UserEvent> {
    private UserEventHandler<E> userEventHandler;
    private ViewsManager viewsManager;
    
    public BaseView() {
	super();
    }
    
    /**
     * Override when needed
     */
    protected void init(){
	
    }

    public abstract void update(T updateData);

    public void setUserEventHandler(UserEventHandler<E> userEventHandler) {
        this.userEventHandler = userEventHandler;
    }

    public void setViewsManager(ViewsManager viewsManager) {
        this.viewsManager = viewsManager;
    }
    
    protected <V extends BaseView<?, E>, H extends UserEventHandler<E>> V createView(Class<V> clazz, H handler){
	return viewsManager.createView(clazz, handler);
    }
    
    protected <V extends BaseView<?, E>> V createView(Class<V> clazz){
	return viewsManager.createView(clazz, userEventHandler);
    }
    
    protected void handleEvent(E event){
	userEventHandler.handleEvent(event);
    }
}
