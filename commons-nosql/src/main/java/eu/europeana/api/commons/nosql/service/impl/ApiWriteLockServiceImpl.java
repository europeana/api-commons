package eu.europeana.api.commons.nosql.service.impl;

import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.cache.annotation.EnableCaching;
import eu.europeana.api.commons.definitions.exception.ApiWriteLockException;
import eu.europeana.api.commons.nosql.entity.ApiWriteLock;
import eu.europeana.api.commons.nosql.entity.ApiWriteLockImpl;
import eu.europeana.api.commons.nosql.service.ApiWriteLockService;

@EnableCaching
public class ApiWriteLockServiceImpl extends
		AbstractNoSqlServiceImpl<ApiWriteLock, String> implements	
		ApiWriteLockService {

	@Override
	public ApiWriteLock lock(String lockType) throws ApiWriteLockException {
		
		try {
			ApiWriteLock writeLock = new ApiWriteLockImpl(lockType);
			return super.store(writeLock);
		} catch(Exception e) {
			throw new ApiWriteLockException("Unable to create api write lock.", e);
		}
	}
	
	@Override 
	public ApiWriteLock getLastActiveLock(String lockType) throws ApiWriteLockException {
		
		try {
			Query<ApiWriteLock> query = getDao().createQuery();
			
			if(lockType != null) {
			  query.criteria(ApiWriteLock.FIELD_NAME).contains(lockType);
			}
			query.criteria(ApiWriteLock.FIELD_ENDED).doesNotExist();
			query.order("-"+ ApiWriteLock.FIELD_STARTED);
			return getDao().findOne(query);
		} catch(Exception e) {
			throw new ApiWriteLockException("Unable to get last api write lock.", e);
		}
	}
	
	@Override
	public ApiWriteLock getLockById(String id) throws ApiWriteLockException {
		try {
			Query<ApiWriteLock> query = getDao().createQuery();
			query.criteria(ApiWriteLock.FIELD_OBJECT_ID).equal(new ObjectId(id));
			return getDao().findOne(query);
		} catch(Exception e) {
			throw new ApiWriteLockException("Unable to get api write lock by id.", e);
		}
	}

	@Override
	public void unlock(ApiWriteLock writeLock) throws ApiWriteLockException {
		try {
			writeLock.setEnded(new Date());
			super.store(writeLock);
		} catch(Exception e) {
			throw new ApiWriteLockException("Unable to unlock write operations for the api.", e);
		}
	}
	
	@Override
	public void deleteAllLocks() throws ApiWriteLockException {
		try {
			getDao().deleteAll();
		} catch(Exception e) {
			throw new ApiWriteLockException("Unable to delete api write locks.", e);
		}
	}

}
