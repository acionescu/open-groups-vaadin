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

import java.sql.Timestamp;

import ro.zg.util.data.GenericNameValueContext;

public class EntityUserData {
    private String vote;
    private Long priority;
    private String status;
    private Timestamp lastVoteUpdate;
    private Timestamp lastPriorityUpdate;
    private Timestamp lastStatusUpdate;
    
    public EntityUserData() {
	
    }
    
    public EntityUserData(GenericNameValueContext dataMap) {
	vote = (String)dataMap.getValue("user_vote");
	priority = (Long)dataMap.getValue("priority");
	status = (String)dataMap.getValue("status");
	lastVoteUpdate = (Timestamp)dataMap.getValue("last_vote_update");
	lastPriorityUpdate = (Timestamp)dataMap.getValue("last_priority_update");
	lastStatusUpdate = (Timestamp)dataMap.getValue("last_status_update");
    }
    
    /**
     * @return the vote
     */
    public String getVote() {
        return vote;
    }


    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the lastVoteUpdate
     */
    public Timestamp getLastVoteUpdate() {
        return lastVoteUpdate;
    }

    /**
     * @return the lastPriorityUpdate
     */
    public Timestamp getLastPriorityUpdate() {
        return lastPriorityUpdate;
    }

    /**
     * @return the lastStatusUpdate
     */
    public Timestamp getLastStatusUpdate() {
        return lastStatusUpdate;
    }

    /**
     * @param vote the vote to set
     */
    public void setVote(String vote) {
        this.vote = vote;
    }

   
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @param lastVoteUpdate the lastVoteUpdate to set
     */
    public void setLastVoteUpdate(Timestamp lastVoteUpdate) {
        this.lastVoteUpdate = lastVoteUpdate;
    }

    /**
     * @param lastPriorityUpdate the lastPriorityUpdate to set
     */
    public void setLastPriorityUpdate(Timestamp lastPriorityUpdate) {
        this.lastPriorityUpdate = lastPriorityUpdate;
    }

    /**
     * @param lastStatusUpdate the lastStatusUpdate to set
     */
    public void setLastStatusUpdate(Timestamp lastStatusUpdate) {
        this.lastStatusUpdate = lastStatusUpdate;
    }

    /**
     * @return the recordCreated
     */
    public boolean isEntityUserRecordCreated() {
        return (lastPriorityUpdate != null) || (lastStatusUpdate !=null);
    }
    
    public boolean isEntityLinkUserRecordCreated() {
	return (lastVoteUpdate != null); 
    }

    /**
     * @return the priority
     */
    public Long getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(Long priority) {
        this.priority = priority;
    }
    
}
