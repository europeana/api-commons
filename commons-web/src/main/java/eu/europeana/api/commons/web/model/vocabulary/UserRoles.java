package eu.europeana.api.commons.web.model.vocabulary;

public enum UserRoles {

	USER(new String[]{Operations.RETRIEVE, Operations.CREATE, Operations.DELETE, Operations.UPDATE}), 
	ADMIN(new String[]{Operations.RETRIEVE, Operations.CREATE, Operations.DELETE, Operations.UPDATE, Operations.ADMIN_ALL, Operations.ADMIN_UNLOCK, Operations.ADMIN_REINDEX, Operations.USER_CREATE, Operations.USER_DELETE}); 
	
	String[] operations;
	
	UserRoles (String[] operations){
		this.operations = operations;
	}
	
	public String[] getOperations() {
		return operations;
	}
	
	public void setOperations(String[] operations) {
		this.operations = operations;
	}
	
}
