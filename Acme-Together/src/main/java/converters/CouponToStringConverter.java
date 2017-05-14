
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Coupon;

@Component
@Transactional
public class CouponToStringConverter implements Converter<Coupon, String> {

	@Override
	public String convert(final Coupon source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
