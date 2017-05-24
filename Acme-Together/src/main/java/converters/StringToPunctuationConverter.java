
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.PunctuationRepository;
import domain.Punctuation;

@Component
@Transactional
public class StringToPunctuationConverter implements Converter<String, Punctuation> {

	@Autowired
	PunctuationRepository	punctuationRepository;


	@Override
	public Punctuation convert(final String text) {
		Punctuation result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.punctuationRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
