
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import domain.ShoppingGroup;

@Component
@Transactional
public class StringToShoppingGroupConverter implements Converter<String, ShoppingGroup> {

	@Autowired
	ShoppingGroupRepository	shoppingGroupRepository;


	@Override
	public ShoppingGroup convert(final String text) {
		ShoppingGroup result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.shoppingGroupRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
