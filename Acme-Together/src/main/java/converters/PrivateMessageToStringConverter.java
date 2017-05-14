
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.PrivateMessage;

@Component
@Transactional
public class PrivateMessageToStringConverter implements Converter<PrivateMessage, String> {

	@Override
	public String convert(final PrivateMessage source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
