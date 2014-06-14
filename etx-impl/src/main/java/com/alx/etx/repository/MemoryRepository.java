package com.alx.etx.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alx.etx.model.CoordinationEntity;

/**
 * Implementation of the repository for tests purposes.
 * 
 * @author alemser
 */
public class MemoryRepository implements Repository {

	private static Map<String, CoordinationEntity> coordinations = new HashMap<String, CoordinationEntity>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String initCoordination() {
		String id = UUID.randomUUID().toString();
		coordinations.put(id, new CoordinationEntity(id));
		return id;
	}
	
}
