
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ConfigurationRepository;
import domain.Configuration;

@Service
@Transactional
public class ConfigurationService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ConfigurationRepository	configurationRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public ConfigurationService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Collection<Configuration> findAll() {
		Collection<Configuration> all;

		all = this.configurationRepository.findAll();

		return all;
	}

}
