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
package ro.zg.opengroups.constants;

import java.util.HashMap;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.control.CommandResponse;

public class OpenGroupsExceptions {
    public static final String NO_SUCH_ENTITY = "no.such.entity.error";
    public static final String SYSTEM_ERROR = "system.error";
    public static final String ACTION_FORBIDDEN = "action.forbidden";

    private static Map<String, String> responseCodesErrorsMap;

    static {
	responseCodesErrorsMap = new HashMap<String, String>();
	responseCodesErrorsMap.put("rc9999", SYSTEM_ERROR);
	responseCodesErrorsMap.put("rc10", ACTION_FORBIDDEN);

    }

    public static ContextAwareException getNoSuchEntityException(long entityId) {
	ExceptionContext ec = new ExceptionContext();
	ec.put("ENTITY_ID", entityId);
	return new ContextAwareException(OpenGroupsExceptions.NO_SUCH_ENTITY,
		ec);
    }

    public static ContextAwareException getSystemError() {
	return new ContextAwareException(SYSTEM_ERROR);
    }

    public static ContextAwareException getErrorFromResponse(CommandResponse res) {
	String responseCode = res.getResponseCode();
	System.out.println("buid error for response: "+res);
	String error = responseCodesErrorsMap.get(responseCode);

	if (error == null) {
	    error = SYSTEM_ERROR;
	}
	return new ContextAwareException(error);
    }

}
