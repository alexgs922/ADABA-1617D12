
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Commercial;

@Component
@Transactional
public class CommercialToStringConverter implements Converter<Commercial, String> {

	@Override
	public String convert(final Commercial source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
