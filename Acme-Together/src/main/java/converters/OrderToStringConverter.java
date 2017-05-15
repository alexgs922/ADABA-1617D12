
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.OrderDomain;

@Component
@Transactional
public class OrderToStringConverter implements Converter<OrderDomain, String> {

	@Override
	public String convert(final OrderDomain source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
