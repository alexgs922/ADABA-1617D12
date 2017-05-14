
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Warehouse;

@Component
@Transactional
public class WarehouseToStringConverter implements Converter<Warehouse, String> {

	@Override
	public String convert(final Warehouse source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
