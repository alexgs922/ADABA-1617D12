
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Distributor;

@Component
@Transactional
public class DistributorToStringConverter implements Converter<Distributor, String> {

	@Override
	public String convert(final Distributor source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
