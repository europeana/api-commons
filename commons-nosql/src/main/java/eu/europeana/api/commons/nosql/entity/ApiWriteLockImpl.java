package eu.europeana.api.commons.nosql.entity;

import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

@Entity("apiwritelock")
public class ApiWriteLockImpl implements PersistentObject, ApiWriteLock {

	@Id
	private ObjectId id;
	/**
	 * holding the type of the lock, we may have different locks for writing to mongo, writing to solr, etc.
	 */
	private String name;
	/**
	 * The date when the lock was created / enabled
	 */
	@Indexed
	private Date started;
	/**
	 * The date when the locking of the API was ended / disabled
	 */
	private Date ended;
	
	public ApiWriteLockImpl() {}
	
	public ApiWriteLockImpl(String name) {
		this.name = name;
		this.started = new Date();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Date getStarted() {
		return started;
	}

	@Override
	public void setStarted(Date started) {
		this.started = started;
	}

	@Override
	public Date getEnded() {
		return ended;
	}

	@Override
	public void setEnded(Date ended) {
		this.ended = ended;
	}

	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "PersistentApiWriteLockImplImpl [" 
				+ "id:" + getId() + ", " 
				+ "name:" + getName() + ","
				+ "started:" + getStarted().toString() + ", " 
				+ "ended:" + getEnded().toString() + "] " ;
	}

  @Override
  public Date getCreated() {
    return null;
  }

  @Override
  public void setCreated(Date creationDate) {
    //emtpy but it must be overriden
  }

  @Override
  public Date getLastUpdate() {
    return null;
  }

  @Override
  public void setLastUpdate(Date lastUpdate) {
    //emtpy but it must be overriden
  }

}