
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.WarehouseRepository;
import domain.Warehouse;

@Component
@Transactional
public class StringToWarehouseConverter implements Converter<String, Warehouse> {

	@Autowired
	WarehouseRepository	warehouseRepository;


	@Override
	public Warehouse convert(final String text) {
		Warehouse res;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				res = null;
			else {
				id = Integer.valueOf(text);
				res = this.warehouseRepository.findOne(id);
			}
		} catch (final Throwable th) {
			throw new IllegalArgumentException(th);
		}

		return res;

	}

}
