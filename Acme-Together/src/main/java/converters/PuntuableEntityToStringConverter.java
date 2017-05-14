
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.PuntuableEntity;

@Component
@Transactional
public class PuntuableEntityToStringConverter implements Converter<PuntuableEntity, String> {

	@Override
	public String convert(final PuntuableEntity source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
