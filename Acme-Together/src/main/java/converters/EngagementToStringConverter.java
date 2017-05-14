
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Engagement;

@Component
@Transactional
public class EngagementToStringConverter implements Converter<Engagement, String> {

	@Override
	public String convert(final Engagement source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
