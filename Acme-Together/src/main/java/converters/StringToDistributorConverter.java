
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.DistributorRepository;
import domain.Distributor;

@Component
@Transactional
public class StringToDistributorConverter implements Converter<String, Distributor> {

	@Autowired
	DistributorRepository	distributorRepository;


	@Override
	public Distributor convert(final String text) {
		Distributor result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.distributorRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
