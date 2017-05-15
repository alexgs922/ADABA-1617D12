
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.OrderDomainRepository;
import domain.OrderDomain;

@Component
@Transactional
public class StringToOrderConverter implements Converter<String, OrderDomain> {

	@Autowired
	OrderDomainRepository	orderRepository;


	@Override
	public OrderDomain convert(final String text) {
		OrderDomain result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.orderRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
