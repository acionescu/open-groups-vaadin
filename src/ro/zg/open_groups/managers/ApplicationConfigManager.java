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
package ro.zg.open_groups.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;
import ro.zg.util.data.ListMap;

public class ApplicationConfigManager {
    private static ApplicationConfigManager _instance = new ApplicationConfigManager();

    private static final String GET_APPLICATION_CONFIG_PARAMS_FLOW = "ro.problems.flows.get-application-config-params";
    private static final String GET_COMPLEX_ENTITY_TYPES_FLOW = "ro.problems.flows.get-complex-entity-types";
    private static final String GET_ENTITIES_TYPES_RELATIONS = "ro.problems.flows.get-all-entities-types-relations";

    private Map<String, Object> applicationConfigParams = new HashMap<String, Object>();
    private Map<String, GenericNameValueContext> complexEntityTypes = new HashMap<String, GenericNameValueContext>();
    private ListMap<String, String> subtypesRelations = new ListMap<String, String>();
    private boolean initialized;

    public static ApplicationConfigManager getInstance() {
	return _instance;
    }

    private ApplicationConfigManager() {
	init();
    }

    private void init() {
	loadConfig();
    }

    private void loadConfig() {
	if (!initialized) {
	    initialized = loadApplicationConfigParams() && loadComplexEntityTypes() && loadEntitiesTypesRelations();
	}
    }

    private boolean loadApplicationConfigParams() {
	CommandResponse response = ActionsManager.getInstance().execute(GET_APPLICATION_CONFIG_PARAMS_FLOW,
		new HashMap());
	if (response == null) {
	    return false;
	}
	GenericNameValueList result = (GenericNameValueList) response.getValue("result");
	for (int i = 0; i < result.size(); i++) {
	    GenericNameValueContext row = (GenericNameValueContext) result.getValueForIndex(i);
	    String paramName = row.getValue("param_name").toString();
	    Long intValue = (Long) row.getValue("int_value");
	    Float floatValue = (Float) row.getValue("number_value");
	    String stringValue = (String) row.getValue("string_value");
	    if (intValue != null) {
		setApplicationConfigParam(paramName, intValue);
	    } else if (stringValue != null) {
		setApplicationConfigParam(paramName, stringValue);
	    } else if (floatValue != null) {
		setApplicationConfigParam(paramName, floatValue);
	    }
	}

	// String logableActionsString = (String)getApplicationConfigParam(ApplicationConfigParam.LOGABLE_ACTIONS);
	// logableActions = Arrays.asList(logableActionsString.split(","));
	// System.out.println("Logable actions: "+logableActions);

	return true;
    }

    private boolean loadComplexEntityTypes() {
	CommandResponse response = ActionsManager.getInstance().execute(GET_COMPLEX_ENTITY_TYPES_FLOW, new HashMap());
	if (response == null) {
	    return false;
	}
	GenericNameValueList result = (GenericNameValueList) response.getValue("result");
	for (int i = 0; i < result.size(); i++) {
	    GenericNameValueContext row = (GenericNameValueContext) result.getValueForIndex(i);
	    addComplexEntityType(row.getValue("complex_type").toString(), row);
	}
	return true;
    }

    private boolean loadEntitiesTypesRelations() {
	CommandResponse response = ActionsManager.getInstance().execute(GET_ENTITIES_TYPES_RELATIONS, new HashMap());
	if (response == null) {
	    return false;
	}
	GenericNameValueList result = (GenericNameValueList) response.getValue("result");
	for (int i = 0; i < result.size(); i++) {
	    GenericNameValueContext row = (GenericNameValueContext) result.getValueForIndex(i);
	    subtypesRelations.add(row.getValue("source_complex_type").toString(), row.getValue("complex_subtype")
		    .toString());
	}
	return true;
    }

    public Object getApplicationConfigParam(String paramName) {
	init();
	return applicationConfigParams.get(paramName);
    }

    public void setApplicationConfigParam(String paramName, Object value) {
	applicationConfigParams.put(paramName, value);
    }

    public void addComplexEntityType(String type, GenericNameValueContext entity) {
	complexEntityTypes.put(type, entity);
    }

    public Object getComplexEntityParam(String complexType, String paramName) {
	init();
	GenericNameValueContext c = complexEntityTypes.get(complexType);
	if (c == null) {
	    throw new RuntimeException("No complex entity with type '" + complexType + "'");
	}
	return c.getValue(paramName);
    }

    public Boolean getComplexEntityBooleanParam(String complexType, String paramName) {
	Object param = getComplexEntityParam(complexType, paramName);
	String value = param.toString();
	if ("y".equals(value)) {
	    return true;
	} else if ("n".equals(value)) {
	    return false;
	}
	return (Boolean) param;
    }

    public List<String> getSubtypesForComplexType(String complexType) {
	init();
	return subtypesRelations.get(complexType);
    }

    public List<Long> getIdsForComplexTypes(String paramnName, String value) {
	init();
	List<Long> ids = new ArrayList<Long>();
	for (GenericNameValueContext c : complexEntityTypes.values()) {
	    if (value.equals(c.getValue(paramnName))) {
		ids.add((Long) c.getValue("id"));
	    }
	}
	return ids;
    }

}
