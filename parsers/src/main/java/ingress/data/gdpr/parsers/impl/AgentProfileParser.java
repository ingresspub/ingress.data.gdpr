/*
 * Copyright (C) 2014-2021 SgrAlpha
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
 *
 */

package ingress.data.gdpr.parsers.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NO_DATA;

import ingress.data.gdpr.models.records.profile.AgentProfile;
import ingress.data.gdpr.models.records.profile.Badge;
import ingress.data.gdpr.models.records.profile.BadgeLevel;
import ingress.data.gdpr.models.records.profile.PushNotificationFor;
import ingress.data.gdpr.models.records.profile.SmsVerification;
import ingress.data.gdpr.models.records.profile.TutorialState;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.PlainTextDataFileParser;
import ingress.data.gdpr.parsers.exceptions.MalformattedRecordException;
import ingress.data.gdpr.parsers.utils.DefaultPacificDateTimeParser;

import io.sgr.geometry.Coordinate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @author SgrAlpha
 */
public class AgentProfileParser extends PlainTextDataFileParser<AgentProfile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentProfileParser.class);

    private static final AgentProfileParser INSTANCE = new AgentProfileParser();
    private static final DefaultPacificDateTimeParser TIME_PARSER = new DefaultPacificDateTimeParser("yyyy-MM-dd HH:mm:ss");

    private AgentProfileParser() {
    }

    public static AgentProfileParser getDefault() {
        return INSTANCE;
    }

    @Override protected ReportDetails<AgentProfile> readLines(final List<String> lines, final Path dataFile) {
        checkNotNull(lines, "No line to read from");
        if (lines.isEmpty()) {
            return ReportDetails.error(NO_DATA);
        }
        checkNotNull(dataFile, "Data file needs to be specified");

        Map<String, Object> data = new HashMap<>();
        Map<String, Map<String, String>> subDataSet = new HashMap<>();
        String line;
        Stack<String> keys = new Stack<>();
        LineType lastLineType = null;
        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);
            if (isNullOrEmpty(line)) {
                continue;
            }
            if (line.startsWith(SEPARATOR_TAB)) {
                if (LineType.DATA == lastLineType) {
                    String[] tmp = line.split(SEPARATOR_COMMA_AND_SPACE, 2);
                    data.put(tmp[0].replaceFirst(SEPARATOR_TAB, ""), tmp[1]);
                    lastLineType = LineType.DATA;
                } else if (LineType.KEY == lastLineType || LineType.SUB_DATA == lastLineType) {
                    String lastKey = keys.peek();
                    if (isNullOrEmpty(lastKey)) {
                        return ReportDetails.error(String.format("Unable to parse agent profile data at line %d because unable to locate parent node: %s", i, line));
                    }
                    Map<String, String> subData = subDataSet.get(lastKey);
                    String[] tmp = line.split("Badges".equals(lastKey) ? SEPARATOR_WORD_AT : SEPARATOR_COMMA_AND_SPACE, 2);
                    subData.put(tmp[0].replaceFirst(SEPARATOR_TAB, ""), tmp[1]);
                    lastLineType = LineType.SUB_DATA;
                } else {
                    return ReportDetails.error(String.format("Unable to parse agent profile data at line %d because unable to locate parent node: %s", i, line));
                }
            } else {
                if (line.endsWith(":")) {
                    if (LineType.SUB_DATA == lastLineType) {
                        keys.pop();
                    }
                    String key = line.substring(0, line.length() - 1);
                    keys.push(key);
                    subDataSet.put(key, new HashMap<>());
                    lastLineType = LineType.KEY;
                } else {
                    if (LineType.SUB_DATA == lastLineType) {
                        keys.pop();
                    }
                    String[] tmp = line.split(SEPARATOR_COMMA_AND_SPACE, 2);
                    data.put(tmp[0], tmp[1]);
                    lastLineType = LineType.DATA;
                }
            }
        }
        try {
            AgentProfile profile = transform(data, subDataSet);
            LOGGER.info("Parsed agent profile in {}", dataFile.getFileName());
            return ReportDetails.ok(profile);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
    }

    @Override protected Logger getLogger() {
        return LOGGER;
    }

    private static AgentProfile transform(final Map<String, Object> data, final Map<String, Map<String, String>> subDataSet) throws MalformattedRecordException {
        Object extraXm = data.get("Additional XM available from BOOSTED_POWER_CUBE");
        return new AgentProfile(
                data.get("Email").toString(),
                TIME_PARSER.parse(data.get("Creation Time").toString()),
                parseSmsVerification(data.get("SMS verification state").toString()),
                TIME_PARSER.parse(data.get("Terms of Service last accepted").toString()),
                data.get("Invites Total/Used/Accepted").toString(),
                data.get("Agent name").toString(),
                data.get("Faction").toString(),
                Integer.parseInt(data.get("Agent level").toString()),
                Integer.parseInt(data.get("Action Points (AP)").toString()),
                Integer.parseInt(data.get("Energy (XM)").toString()),
                extraXm == null ? null : Integer.parseInt(extraXm.toString()),
                Boolean.parseBoolean(data.get("Display stats to others").toString()),
                Coordinate.parseCommaSeparatedString(data.get("Last Location").toString()),
                TIME_PARSER.parse(data.get("Last Location Time").toString()),
                parseHighestMediaIdByCategory(subDataSet.get("Highest Media ID by category")),
                Boolean.parseBoolean(data.get("Email game notifications").toString()),
                Boolean.parseBoolean(data.get("Email news, events, promos, and offers").toString()),
                parsePushNotificationFor(subDataSet.get("Push notifications for")),
                Boolean.parseBoolean(data.get("Has captured Portal").toString()),
                Boolean.parseBoolean(data.get("Has created Link").toString()),
                Boolean.parseBoolean(data.get("Has created Field").toString()),
                parseBlockedAgents(data.get("Blocked agents").toString()),
                parseTutorialState(subDataSet.get("Tutorial State")),
                parseBadges(subDataSet.get("Badges")),
                parseInventory(subDataSet.get("Inventory"))
        );
    }

    private static Map<String, Integer> parseInventory(final Map<String, String> inventory) {
        return inventory
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Integer.parseInt(entry.getValue())
                ));
    }

    private static List<Badge> parseBadges(final Map<String, String> badges) {
        return badges
                .entrySet()
                .stream()
                .map(entry -> {
                    String tmp[] = entry.getKey().split(" ", 2);
                    ZonedDateTime time = TIME_PARSER.parse(entry.getValue());
                    return new Badge(tmp[1], BadgeLevel.valueOf(tmp[0].toUpperCase()), time);
                })
                .collect(Collectors.toList());
    }

    private static List<TutorialState> parseTutorialState(final Map<String, String> subData) {
        return subData
                .entrySet()
                .stream()
                .map(entry -> {
                    String[] tmp = entry.getValue().split(SEPARATOR_WORD_AT);
                    return new TutorialState(entry.getKey(), tmp[0], TIME_PARSER.parse(tmp[1]));
                })
                .collect(Collectors.toList());
    }

    private static List<String> parseBlockedAgents(final String blockedAgents) {
        return Arrays.asList(blockedAgents.split(SEPARATOR_COMMA_AND_SPACE));
    }

    private static PushNotificationFor parsePushNotificationFor(final Map<String, String> subData) {
        return new PushNotificationFor(
                Boolean.parseBoolean(subData.get("Player Comm mentions")),
                Boolean.parseBoolean(subData.get("Portal attacks")),
                Boolean.parseBoolean(subData.get("Recruiting and Faction activity")),
                Boolean.parseBoolean(subData.get("News stories")),
                Boolean.parseBoolean(subData.get("Events and opportunities"))
        );
    }

    private static Map<String, Integer> parseHighestMediaIdByCategory(final Map<String, String> subData) {
        return subData
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Integer.parseInt(entry.getValue())
                ));
    }

    private static SmsVerification parseSmsVerification(final String smsVerificationState) throws MalformattedRecordException {
        String[] tmp = smsVerificationState.split(SEPARATOR_WORD_AT);
        if (tmp.length == 2 && "VERIFIED".equals(tmp[0])) {
            return SmsVerification.verified(TIME_PARSER.parse(tmp[1]));
        } else if (tmp.length == 1 && "UNVERIFIED".equals(tmp[0])) {
            return SmsVerification.unverified();
        } else {
            throw new MalformattedRecordException(String.format("Unable to parse SMS verification state from %s", smsVerificationState));
        }
    }

    private enum LineType {
        DATA,
        KEY,
        SUB_DATA,
        ;
    }

}
