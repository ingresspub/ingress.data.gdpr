# Ingress Data Explorer

*用其他语言阅读: [English](README.md), [简体中文](README.zh-cn.md).*

Ingress Data Explorer 是一个帮你浏览 Ingress 游戏数据的可视化工具，你可以以欧盟隐私法为由向 Niantic labs 索取你的游戏数据，之后利用本工具提供的本地 web 页面来浏览各项数据。

虽然交互界面是以上传文件的形式提供，其实仅仅是上传到运行在你本地的 web 服务器，所有的数据均只存在你的电脑本地，不会以任何形式上传至任何第三方服务器。

## 如何获取数据
给 [privacy@nianticlabs.com](mailto:privacy@nianticlabs.com) 发邮件，主题任意，内容模版如下:
```
Dear Sir or Madam,
I'd like to request a dump of the raw data Niantic stores about my Ingress account @<account_name>, as regulated under GDPR.
Yours sincerely,
<your_name>
```
请将 `<account_name>` 换成你的游戏 ID，落款的名字可以随意写。
这个过程预计需要 30 天，之后你会收到两封邮件，一封包含一个下载链接，你可以下载到一个经过加密的压缩包，另一封则是解压密码。

## 安装必须的软件
1. 下载并安装 [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/index.html#JDK8)

## 如何运行
1. 克隆或者降本仓库下载到本地，如果你下载的是 zip 包，将它解压到某个目录
2. 在命令行窗口运行:
    ```$bash
    cd <克隆或者解压后的目录>
    ./mvnw clean package spring-boot:run -pl web
    ```
3. 用浏览器打开 [http://127.0.0.1:8080](http://127.0.0.1:8080) 

## 数据存在本地的什么地方？
数据被保存在一个 [H2 数据库](http://www.h2database.com) 中，位置在 ```~/.h2/ingress_gdpr```，`~`代表操作系统登录用户的主目录。

## 许可协议

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
