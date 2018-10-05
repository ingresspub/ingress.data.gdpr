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

package ingress.data.gdpr.parsers.impl;

import static ingress.data.gdpr.models.utils.Preconditions.isEmptyString;
import static ingress.data.gdpr.models.utils.Preconditions.notNull;
import static ingress.data.gdpr.parsers.utils.ErrorConstants.NO_DATA;

import ingress.data.gdpr.models.records.mission.Mission;
import ingress.data.gdpr.models.records.mission.MissionDetail;
import ingress.data.gdpr.models.reports.ReportDetails;
import ingress.data.gdpr.parsers.PlainTextDataFileParser;
import ingress.data.gdpr.parsers.utils.DefaultPacificDateTimeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @author SgrAlpha
 */
public class MissionsParser extends PlainTextDataFileParser<List<Mission>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionsParser.class);

    private static final MissionsParser INSTANCE = new MissionsParser();
    private static final DefaultPacificDateTimeParser TIME_PARSER = new DefaultPacificDateTimeParser("yyyy-MM-dd HH:mm:ss");

    private MissionsParser() {
    }

    public static MissionsParser getDefault() {
        return INSTANCE;
    }

    @Override protected ReportDetails<List<Mission>> readLines(final List<String> lines, final Path dataFile) {
        notNull(lines, "No line to read from");
        if (lines.size() < 2) {
            return ReportDetails.error(NO_DATA);
        }
        notNull(dataFile, "Data file needs to be specified");

        Stack<Mission> missionStack = new Stack<>();
        Stack<MissionDetail> versionStack = new Stack<>();

        String line;
        LineType lastLineType = null;
        for (int i = 1; i < lines.size(); i++) {
            line = lines.get(i);
            if (isEmptyString(line)) {
                continue;
            }
            if (line.startsWith(SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB)) {
                if (lastLineType == LineType.VERSION || lastLineType == LineType.WAYPOINT) {
                    String tmp[] = line.split(SEPARATOR_TAB);
                    if (tmp.length != 11) {
                        return ReportDetails.error(String.format("Expecting a row of mission way point at line %d with %d columns in it, but got only %d columns: %s", i, 11, tmp.length, line));
                    }
                    MissionDetail version = versionStack.peek();
                    if (version == null) {
                        return ReportDetails.error(String.format("Unable to parse way point at line %d because unable to locate parent node: %s", i, line));
                    }
                    version.addWayPoints(tmp[10]);
                    lastLineType = LineType.WAYPOINT;
                } else {
                    return ReportDetails.error(String.format("Unable to parse way point at line %d because unable to locate parent node: %s", i, line));
                }
            } else if (line.startsWith(SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB + SEPARATOR_TAB)) {
                String[] tmp = line.split(SEPARATOR_TAB);
                if (tmp.length < 10) {
                    return ReportDetails.error(String.format("Expecting a row of mission version at line %d with at least %d columns in it, but got only %d columns: %s", i, 10, tmp.length, line));
                }
                MissionDetail version = parseMissionVersion(tmp);
                versionStack.push(version);
                lastLineType = LineType.VERSION;
            } else {
                if (!versionStack.empty()) {
                    List<MissionDetail> versions = new LinkedList<>(versionStack);
                    final Mission lastMission = missionStack.peek();
                    if (lastMission == null) {
                        return ReportDetails.error(String.format("Unable to close last mission because unable to locate parent node for %d version(s)", versions.size()));
                    }
                    versionStack.forEach(lastMission::addVersions);
                    versionStack.clear();
                }
                String[] tmp = line.split(SEPARATOR_TAB);
                if (tmp.length < 7) {
                    return ReportDetails.error(String.format("Expecting a row of mission at line %d with at least %d columns in it, but got only %d columns: %s", i, 7, tmp.length, line));
                }
                Mission mission = parseMission(tmp);
                missionStack.push(mission);
                lastLineType = LineType.MISSION;
            }
        }
        List<Mission> missions = new LinkedList<>(missionStack);
        missionStack.clear();
        return ReportDetails.ok(missions);
    }

    private static MissionDetail parseMissionVersion(final String[] columns) {
        return new MissionDetail(
                TIME_PARSER.parse(columns[4]),
                TIME_PARSER.parse(columns[6]),
                Integer.parseInt(columns[7]),
                columns[8],
                columns[9]
        );
    }

    private static Mission parseMission(final String[] columns) {
        return new Mission(
                columns[0],
                columns[1],
                columns[2],
                Boolean.parseBoolean(columns[3]),
                TIME_PARSER.parse(columns[4]),
                TIME_PARSER.parse(columns[5]),
                TIME_PARSER.parse(columns[6])
        );
    }

    @Override protected Logger getLogger() {
        return LOGGER;
    }

    private enum LineType {
        MISSION,
        VERSION,
        WAYPOINT,
        ;
    }

}
