
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.WarehouseRepository;
import domain.Warehouse;

@Service
@Transactional
public class WarehouseService {

	// Managed Repository -------------------------------------------

	@Autowired
	private WarehouseRepository	warehouseRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public WarehouseService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Warehouse create() {
		Warehouse result;

		result = new Warehouse();

		return result;

	}

	public Collection<Warehouse> findAll() {
		Collection<Warehouse> result;
		result = this.warehouseRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Warehouse findOne(final int warehouseId) {
		Warehouse result;
		result = this.warehouseRepository.findOne(warehouseId);
		Assert.notNull(result);
		return result;
	}

	public Warehouse save(final Warehouse warehouse) {
		return this.warehouseRepository.save(warehouse);
	}

	public void delete(final Warehouse warehouse) {
		Assert.notNull(warehouse);
		this.warehouseRepository.delete(warehouse);

	}

	// Other business methods ----------------------------------------------

	public void flush() {
		this.warehouseRepository.flush();
	}

}
