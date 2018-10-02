/*
 * Copyright (C) 2014-2018 SgrAlpha
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ingress.data.gdpr.parsers;

import ingress.data.gdpr.models.records.TimestampedRecord;
import ingress.data.gdpr.parsers.exceptions.MalformattedRecordException;

import java.time.ZonedDateTime;
import java.util.Arrays;

/**
 * @author SgrAlpha
 */
public class TimestampedRecordParser<T> implements SingleLineRecordParser<TimestampedRecord<T>> {

    private final ZonedDateTimeParser timeParser;
    private final SingleLineValueParser<T> valueParser;

    private TimestampedRecordParser(final ZonedDateTimeParser timeParser, final SingleLineValueParser<T> valueParser) {
        this.timeParser = timeParser;
        this.valueParser = valueParser;
    }

    public static <T> TimestampedRecordParser<T> using(final ZonedDateTimeParser timeParser, final SingleLineValueParser<T> valueParser) {
        return new TimestampedRecordParser<>(timeParser, valueParser);
    }

    @Override
    public TimestampedRecord<T> parse(final String... columns) throws MalformattedRecordException {
        if (columns.length < 2) {
            throw new MalformattedRecordException(String.format("Expecting more than %d columns but found %d", 2, columns.length));
        }
        final ZonedDateTime time;
        try {
            time = timeParser.parse(columns[0]);
        } catch (Exception e) {
            throw new MalformattedRecordException(String.format("Expecting a valid timestamp, but got %s", columns[0]), e);
        }
        final String[] valueColumns = Arrays.copyOfRange(columns, 1, columns.length);
        final T value;
        try {
            value = valueParser.parse(valueColumns);
        } catch (Exception e) {
            throw new MalformattedRecordException(String.format("Unable to parse value from %s", Arrays.asList(valueColumns)), e);
        }
        return new TimestampedRecord<>(time, value);
    }

}
