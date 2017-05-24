
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Punctuation;

@Component
@Transactional
public class PunctuationToStringConverter implements Converter<Punctuation, String> {

	@Override
	public String convert(final Punctuation source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
