
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.WarehouseRepository;
import domain.OrderDomain;
import domain.Warehouse;

@Service
@Transactional
public class WarehouseService {

	// Managed Repository -------------------------------------------

	@Autowired
	private WarehouseRepository	warehouseRepository;


	// Supporting services ----------------------------------------------------

	@Autowired
	private Validator				validator;

	@Autowired
	private DistributorService				distributorService;

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

	public Warehouse reconstruct(final Warehouse warehouse, final BindingResult binding) {
		Warehouse result;
		if (warehouse.getId() == 0){
			result = warehouse;
		List<OrderDomain> orders = new ArrayList<OrderDomain>();
		result.setOrders(orders);
		}
		else {
			
			result = this.warehouseRepository.findOne(warehouse.getId());
			result.setName(warehouse.getName());
			result.setWarehouseAddress(warehouse.getWarehouseAddress());
			this.validator.validate(result, binding);
		}
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

	public Warehouse saveAndFlush(final Warehouse warehouse) {
		Assert.notNull(warehouse);
		return this.warehouseRepository.saveAndFlush(warehouse);
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
