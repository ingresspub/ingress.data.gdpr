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

import static ingress.data.gdpr.models.utils.Preconditions.isEmptyString;
import static ingress.data.gdpr.models.utils.Preconditions.notNull;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NOT_REGULAR_FILE;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NO_DATA;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.UNREADABLE_FILE;

import ingress.data.gdpr.models.records.profile.AgentProfile;
import ingress.data.gdpr.models.records.profile.Badge;
import ingress.data.gdpr.models.records.profile.BadgeLevel;
import ingress.data.gdpr.models.records.profile.PushNotificationFor;
import ingress.data.gdpr.models.records.profile.SmsVerification;
import ingress.data.gdpr.models.records.profile.TutorialState;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.exceptions.MalformattedRecordException;
import ingress.data.gdpr.parsers.utils.DefaultPacificDateTimeParser;
import io.sgr.geometry.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
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
public class AgentProfileParser implements DataFileParser<AgentProfile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentProfileParser.class);

    private static final AgentProfileParser INSTANCE = new AgentProfileParser();
    private static final DefaultPacificDateTimeParser TIME_PARSER = new DefaultPacificDateTimeParser("yyyy-MM-dd HH:mm:ss");

    private AgentProfileParser() {
    }

    public static AgentProfileParser getDefault() {
        return INSTANCE;
    }

    @Override public ReportDetails<AgentProfile> parse(final Path dataFile) {
        notNull(dataFile, "Data file needs to be specified");
        if (!Files.isRegularFile(dataFile)) {
            LOGGER.warn("{} is not a regular file", dataFile.getFileName());
            return ReportDetails.error(NOT_REGULAR_FILE);
        }
        if (!Files.isReadable(dataFile)) {
            LOGGER.warn("{} is not a readable file", dataFile.getFileName());
            return ReportDetails.error(UNREADABLE_FILE);
        }

        final List<String> lines;
        try {
            lines = Files.readAllLines(dataFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return ReportDetails.error(e.getMessage());
        }
        if (lines.isEmpty()) {
            return ReportDetails.error(NO_DATA);
        }

        Map<String, Object> data = new HashMap<>();
        Map<String, Map<String, String>> subDataSet = new HashMap<>();
        String line;
        Stack<String> keys = new Stack<>();
        LineType lastLineType = null;
        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);
            if (isEmptyString(line)) {
                continue;
            }
            if (line.startsWith("\t")) {
                if (LineType.DATA == lastLineType) {
                    String[] tmp = line.split(": ", 2);
                    data.put(tmp[0].replaceFirst("\t", ""), tmp[1]);
                    lastLineType = LineType.DATA;
                } else if (LineType.KEY == lastLineType || LineType.SUB_DATA == lastLineType) {
                    String lastKey = keys.peek();
                    if (isEmptyString(lastKey)) {
                        return ReportDetails.error(String.format("Unable to parse agent profile date at line %d because unable to locate parent node: %s", i, line));
                    }
                    Map<String, String> subData = subDataSet.get(lastKey);
                    String[] tmp = line.split("Badges".equals(lastKey) ? " at " : ": ", 2);
                    subData.put(tmp[0].replaceFirst("\t", ""), tmp[1]);
                    lastLineType = LineType.SUB_DATA;
                } else {
                    return ReportDetails.error(String.format("Unable to parse agent profile date at line %d because unable to locate parent node: %s", i, line));
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
                    String[] tmp = line.split(": ", 2);
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

    private static AgentProfile transform(final Map<String, Object> data, final Map<String, Map<String, String>> subDataSet) throws MalformattedRecordException {
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
                Integer.parseInt(data.get("Additional XM available from BOOSTED_POWER_CUBE").toString()),
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
                    String tmp[] = entry.getKey().split(" ");
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
                    String[] tmp = entry.getValue().split(" at ");
                    return new TutorialState(entry.getKey(), tmp[0], TIME_PARSER.parse(tmp[1]));
                })
                .collect(Collectors.toList());
    }

    private static List<String> parseBlockedAgents(final String blockedAgents) {
        return Arrays.asList(blockedAgents.replaceAll(" ", "").split(","));
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
        String[] tmp = smsVerificationState.split(" at ");
        if (tmp.length != 2) {
            throw new MalformattedRecordException(String.format("Unable to parse SMS verification state from '%s'", smsVerificationState));
        }
        return new SmsVerification("VERIFIED".equals(tmp[0]), TIME_PARSER.parse(tmp[1]));
    }

    private enum LineType {
        DATA,
        KEY,
        SUB_DATA,
        ;
    }

}
