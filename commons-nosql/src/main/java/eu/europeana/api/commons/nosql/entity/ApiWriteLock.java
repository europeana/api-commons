package eu.europeana.api.commons.nosql.entity;

import java.util.Date;
import org.bson.types.ObjectId;

public interface ApiWriteLock extends NoSqlEntity {	
	
    //lock object name
    public static final String LOCK_WRITE_TYPE="lockWriteOperations"; 
    public static final String FIELD_NAME ="name";
    public static final String FIELD_OBJECT_ID ="_id";
    public static final String FIELD_STARTED ="started";
    public static final String FIELD_ENDED ="ended";
    
	
    ObjectId getId();

	String getName();

	void setName(String name);
	
	void setId(ObjectId id);

	void setStarted(Date started);

	Date getStarted();

	Date getEnded();

	void setEnded(Date ended);

}
