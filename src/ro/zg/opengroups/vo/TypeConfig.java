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
