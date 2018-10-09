# Ingress Data Explorer

*Read this in other languages: [English](README.md), [简体中文](README.zh-cn.md).*

Ingress Data Explorer is a simple toolkit allows you to view Ingress gaming data exported by Niantic labs as part of their GDPR compliance.

It provides a user friendly web UI for you to upload the encrypted zip file you get from Niantic labs, then explore all kinds of data inside.

Everything will be kept exactly on your machine, nothing will be collected and send to backend servers.

## How to get the data
Send an E-Mail to [privacy@nianticlabs.com](mailto:privacy@nianticlabs.com):
```
Dear Sir or Madam,
I'd like to request a dump of the raw data Niantic stores about my Ingress account @<account_name>, as regulated under GDPR.
Yours sincerely,
<your_name>
```
Please remember to change `<account_name>` to your codename in the game.
This will take about 30 days for Niantic to process your request. After done, you will receive two mails, one contains a link for you to download an encrypted zip file, the other is the password to decrypt the zip file.

## Prerequisites
1. Download and install [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/index.html#JDK8)
2. Download and install git
  * Windows: [https://git-scm.com/download/win](https://git-scm.com/download/win)
  * Mac: [https://git-scm.com/download/mac](https://git-scm.com/download/mac)
  * Linux: [https://git-scm.com/download/linux](https://git-scm.com/download/linux)

## How to run
1. Run in Terminal:
    ```$bash
    git clone git@github.com:ingresspub/ingress.data.gdpr.git
    cd ingress.data.gdpr
    ./mvnw clean install && ./mvnw spring-boot:run -pl web
    ```
2. Open [http://127.0.0.1:8080](http://127.0.0.1:8080) in your favorite web browser. 

## Where are the data stored on my local?
It's in a [H2 database](http://www.h2database.com) under ```~/.h2/ingress_gdpr```

## License

```
    Ingress Data Explorer is a simple toolkit allows you to view Ingress
    gaming data exported by Niantic labs as part of their GDPR compliance.

    Copyright (C) 2014-2018  SgrAlpha

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
