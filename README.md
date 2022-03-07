# Patrol: visual inspection of Excel and PDF

## Motivation

A few years ago, I worked for a media company. I was asked to develop a tool software, which is a sort of web scraping tool. The target page is [this](https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html) (is still available to public at MAR 2022). The page looks something like this:

![target](./docs/images/01_NISA_target_page.png)

You don’t have to understand this page in detail except the following points.

1.  The page contains several `<a href="…​">` tags to the URLs of Excel files and PDF files.

2.  The content of Excel & PDF files are updated by the publisher irregularly. The frequency of updates are not determined. You can track the record: once a month, or once per 2 months.

3.  The URL string of Excel/PDF files are fixed. The file names are fixed. You can not tell if the file has been updated by the file name.

4.  The publisher does not provide any push-style notification (like [RSS](https://en.wikipedia.org/wiki/RSS)) for this page. Those who are interested in the information of this page are asked to keep watching the page, read it and find updates somehow.

5.  The company I worked for had a serious interest in the Excel files. Therefore some staffs are asked to visit this page everyday.

6.  The staffs hated this job. They wanted some system to automate this bullshit job.

Since then I have worked long to solve this problem. Finally I have got a solution. Let me describe it.

## Problem to solve

The Excel files in the page are irregularly updated, and the publisher does not provided any notification. Therefore my system has to

1.  visit the web page regularly and automatically, for example once a day

2.  download the current version .xlsx files, compare the content with some previous version to find if there are any updates.

3.  If any updates found, take some action. For example, put the newer .xlsx files into some organizational file server and send an E-mail to those who are concerned.

The 1st problem (running a process regularly and automatically) is easy to solve using Linux [cron](https://en.wikipedia.org/wiki/Cron), Windows [Task Scheduler](https://docs.microsoft.com/en-us/windows/win32/taskschd/about-the-task-scheduler), and some Continuous Integration servers like [Team City](https://www.jetbrains.com/teamcity/). Katalon offers [Test Ops](https://www.katalon.com/testops/) of course.

The 3rd problem (taking some actions when your script find it necessary) is easy to solve by your custom programming.

The 2nd problem is difficult. **How can we detect that the current version of an Excel file is updated? How can we read the difference of current Excel and previous Excel quickly?**

I will call this issue **"Patrol for web resources"** for short. The Patrol requires some amount of custom software development.

## Solution

I have developped a set of Java/Groovy library to build a Patrol for you.

-   [materialstore](https://github.com/kazurayam/materialstore/)

-   [materialstore-mapper](https://github.com/kazurayam/materialstore-mapper/)

And [This project](https://github.com/kazurayam/VisualInspectionOfExcelAndPDF) shows a sample built on top of the 2 library which can perform a Patrol for me.

I won’t use [this](https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html) as testbed for demonstration, because

1.  this page is not updated frequent enough. It is updated at most once per a month. I want a testbed updated at least once per a day.

2.  this page is published by a governmental authoritative organization. I do not like to bother them.

Instead I would use the following URL as the testbed for demonstration:

-   [Amazon.com, Inc. - Press Room News Releases](https://press.aboutamazon.com/rss/news-releases.xml)

This URL is updated frequently enough, and Amazon would not stop me accessing it using my automation software.

My softoware would do a series of file format conversion: RSS XML → Excel → CSV.

## Description

## Demonstration
