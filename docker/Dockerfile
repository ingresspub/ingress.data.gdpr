#
# Copyright (C) 2014-2018 SgrAlpha
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

FROM sgrio/java8-tcnative:latest
MAINTAINER SgrAlpha <admin@mail.sgr.io>

RUN mkdir -p /opt/ingress/gdpr

COPY files/libs /opt/ingress/gdpr/libs

WORKDIR /opt/ingress/gdpr

CMD java $JAVA_OPTS -classpath .:/opt/ingress/gdpr/libs/* ingress.data.gdpr.web.Application
