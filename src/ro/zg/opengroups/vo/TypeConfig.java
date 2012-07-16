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
package ro.zg.opengroups.vo;

import ro.zg.util.data.GenericNameValueContext;

public class TypeConfig {
    private GenericNameValueContext configContext;
    
    public Object getConfigParam(String paramName) {
	return configContext.getValue(paramName);
    }
    
    public Boolean getConfigBooleanParam(String paramName) {
	Object param = getConfigParam(paramName);
	String value = param.toString();
	if ("y".equals(value)) {
	    return true;
	} else if ("n".equals(value)) {
	    return false;
	}
	return (Boolean) param;
    }

    /**
     * @return the configContext
     */
    public GenericNameValueContext getConfigContext() {
        return configContext;
    }

    /**
     * @param configContext the configContext to set
     */
    public void setConfigContext(GenericNameValueContext configContext) {
        this.configContext = configContext;
    }
    
}
