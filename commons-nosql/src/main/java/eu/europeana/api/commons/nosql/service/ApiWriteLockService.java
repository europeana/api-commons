package eu.europeana.api.commons.nosql.service;

import eu.europeana.api.commons.definitions.exception.ApiWriteLockException;
import eu.europeana.api.commons.nosql.entity.ApiWriteLock;

public interface ApiWriteLockService extends AbstractNoSqlService<ApiWriteLock, String> {

	ApiWriteLock lock(String lockType) throws ApiWriteLockException;
	
	void unlock(ApiWriteLock writeLock) throws ApiWriteLockException;
	
	ApiWriteLock getLastActiveLock(String lockType) throws ApiWriteLockException;
	
	ApiWriteLock getLockById(String id) throws ApiWriteLockException;

	void deleteAllLocks() throws ApiWriteLockException;
	
}
