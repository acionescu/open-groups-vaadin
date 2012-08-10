package ro.zg.opengroups.vo;

import java.io.Serializable;

import ro.zg.util.data.GenericNameValueContext;

public class AccessRule implements Serializable,Comparable<AccessRule>{
    /**
     * 
     */
    private static final long serialVersionUID = 8015769038200907227L;
    private long id;
    private int accessLevel;
    private String name;
    
    
    public AccessRule(GenericNameValueContext data){
	
	id = Long.parseLong(data.getValue("id").toString());
	accessLevel= Integer.parseInt(data.getValue("access_level").toString());
	name = data.getValue("name").toString();
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public int getAccessLevel() {
        return accessLevel;
    }


    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int compareTo(AccessRule o) {
	if(accessLevel < o.accessLevel) return -1;
	else if(accessLevel == o.accessLevel) return 0;
	return 1;
    }


    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + accessLevel;
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }


    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	AccessRule other = (AccessRule) obj;
	if (accessLevel != other.accessLevel)
	    return false;
	if (id != other.id)
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }
    
}
