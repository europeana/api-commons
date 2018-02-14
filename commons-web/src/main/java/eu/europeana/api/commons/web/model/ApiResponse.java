/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

/**
 * 
 */
package eu.europeana.api.commons.web.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Willem-Jan Boogerd www.eledge.net/contact
 */
@JsonSerialize()
@JsonInclude(content=Include.NON_EMPTY, value=Include.NON_EMPTY)
public abstract class ApiResponse {

	public String apikey;

	public String action;

	public boolean success = true;

	public String error;

	public Date statsStartTime;

	public Long statsDuration;

	public Long requestNumber;

	String status;

	String stackTrace;

	public ApiResponse(String apikey, String action) {
		this.apikey = apikey;
		this.action = action;
	}

	public ApiResponse() {
		// used by Jackson
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
}
