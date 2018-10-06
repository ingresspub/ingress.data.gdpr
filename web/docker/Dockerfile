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

FROM sgrio/java-oracle:server_jre_8
MAINTAINER SgrAlpha <admin@mail.sgr.io>

ENV DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8

RUN mkdir -p /opt/ingress/gdpr && \
    APR_VERSION=1.6.3 && \
    TC_NATIVE_VERSION=1.2.17 && \
    apt-get update && apt-get install \
            gcc \
            libc6-dev \
            libssl-dev \
            libfile-dircompare-perl \
            make \
            -y --no-install-recommends && \
        curl --silent --location --retry 3 \
            http://archive.apache.org/dist/apr/apr-${APR_VERSION}.tar.gz \
            | tar xz -C /tmp && \
        cd /tmp/apr-${APR_VERSION} && \
            ./configure && \
            make clean && make && make install && \
        ln -s /usr/local/apr/lib/libapr-1.so.0 /usr/lib/libapr-1.so.0 && \
        ln -s /usr/local/apr/lib/libapr-1.so /usr/lib/libapr-1.so && \
        curl --silent --location --retry 3 \
            http://archive.apache.org/dist/tomcat/tomcat-connectors/native/${TC_NATIVE_VERSION}/source/tomcat-native-${TC_NATIVE_VERSION}-src.tar.gz \
            | tar xz -C /tmp && \
        cd /tmp/tomcat-native-${TC_NATIVE_VERSION}-src/native && \
            ./configure -with-apr=/usr/local/apr/ -with-ssl=yes -with-java-home=${JAVA_HOME} && \
            make clean && make && make install && \
        ln -s /usr/local/apr/lib/libtcnative-1.so.0 /usr/lib/libtcnative-1.so.0 && \
        ln -s /usr/local/apr/lib/libtcnative-1.so /usr/lib/libtcnative-1.so && \
        apt-get remove --purge --auto-remove -y \
            gcc \
            libc6-dev \
            libssl-dev \
            libfile-dircompare-perl \
            make && \
        apt-get autoclean && apt-get --purge -y autoremove && \
        rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

COPY files/libs /opt/ingress/gdpr/libs

WORKDIR /opt/ingress/gdpr

CMD java $JAVA_OPTS -Dspring.profiles.active=prod -classpath .:/opt/ingress/gdpr/libs/* ingress.data.gdpr.web.Application
