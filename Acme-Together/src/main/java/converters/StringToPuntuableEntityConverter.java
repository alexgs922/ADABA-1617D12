
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import domain.PuntuableEntity;

@Component
@Transactional
public class StringToPuntuableEntityConverter implements Converter<String, PuntuableEntity> {

	@Autowired
	PuntuableEntityRepository	puntuableEntityRepository;


	@Override
	public PuntuableEntity convert(final String text) {
		PuntuableEntity result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.puntuableEntityRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
