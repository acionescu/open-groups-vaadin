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
package ro.zg.opengroups.vo;

import java.io.Serializable;

public class FilterOption implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 2830376597397263354L;
    private String caption;
    private Object value;
    private String paramName;
    
    
    public FilterOption(String caption, String paramName, Object value) {
	this.caption = caption;
	this.paramName = paramName;
	this.value = value;
    }
    
    
    /**
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }
    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }
    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }
    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }
    
    /**
     * @return the paramName
     */
    public String getParamName() {
        return paramName;
    }
    /**
     * @param paramName the paramName to set
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    public String toString() {
	return caption;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((caption == null) ? 0 : caption.hashCode());
	result = prime * result + ((paramName == null) ? 0 : paramName.hashCode());
	result = prime * result + ((value == null) ? 0 : value.hashCode());
	return result;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	FilterOption other = (FilterOption) obj;
	if (caption == null) {
	    if (other.caption != null)
		return false;
	} else if (!caption.equals(other.caption))
	    return false;
	if (paramName == null) {
	    if (other.paramName != null)
		return false;
	} else if (!paramName.equals(other.paramName))
	    return false;
	if (value == null) {
	    if (other.value != null)
		return false;
	} else if (!value.equals(other.value))
	    return false;
	return true;
    }
    
    
}
