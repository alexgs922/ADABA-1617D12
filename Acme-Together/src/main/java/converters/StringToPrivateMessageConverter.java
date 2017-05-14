
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.PrivateMessageRepository;
import domain.PrivateMessage;

@Component
@Transactional
public class StringToPrivateMessageConverter implements Converter<String, PrivateMessage> {

	@Autowired
	PrivateMessageRepository	privateMessageRepository;


	@Override
	public PrivateMessage convert(final String text) {
		PrivateMessage result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.privateMessageRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
