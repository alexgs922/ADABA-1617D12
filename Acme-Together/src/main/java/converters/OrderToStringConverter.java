
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Order;

@Component
@Transactional
public class OrderToStringConverter implements Converter<Order, String> {

	@Override
	public String convert(final Order source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
