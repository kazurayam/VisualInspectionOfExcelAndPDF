# Visual Inspection of Excel and PDF

## Motivation

A few years ago, I worked for a media company in Tokyo. I was asked to develop a tool software, which is a sort of web scraping tool. The target page is [this](https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html), which is still available to public at MAR 2022. The page looks something like this:

![target](./docs/images/01_NISA_target_page.png)

You don’t have to understand this page in detail except the following points.

1.  The page contains several `<a href="…​">` tags to the URLs of Excel files and PDF files.

2.  The URL string of Excel/PDF files are fixed. The file names are fixed. The URLs are not variable.

3.  The content of Excel & PDF files are updated by the publisher irregularly. The frequency of updates are not determined. You can track the record: once a month, or once per 2 months.

4.  The publisher does not provide any push-style notification (like [RSS](https://en.wikipedia.org/wiki/RSS)) for this page. Those who are interested in the information of this page are asked to keep watching the page, read it and find updates.

5.  The company I worked for had a serious interest in the Excel files. Therefore some staffs are asked to visit this page everyday.

6.  The staffs hated this job. No need to tell why. They wanted some system to automate this *bullshit job*.

I have worked long to solve this problem, and finally I have got a solution.

## Problem to solve

The Excel files in the page are irregularly updated, and the publisher does not provided any notification. Therefore I have to

1.  visit the web page automatically, for example once a day

2.  download the current version .xlsx files, compare them with some previous version to find if there are any updates.

3.  If any updates found, take some action. For example, put the newer .xlsx files into some organizational file server and send E-mail to those who are concerned to tell them: "Look, that Excel is updated!".

The 1st problem (run a process regularly and automatically) will be easily solved. Linux [cron](https://en.wikipedia.org/wiki/Cron), Windows [Task Scheduler](https://docs.microsoft.com/en-us/windows/win32/taskschd/about-the-task-scheduler), some Continuous Integration servers like [Team City](https://www.jetbrains.com/teamcity/). Katalon offers [Test Ops](https://www.katalon.com/testops/) of course.

The 3rd problem (take some actions when your script find it necessary) is easy to solve by your custom programming.

The 2nd problem is the core part to solve. That is: **how can we detect if the current version of an Excel file is somehow updated?**

## Solution

## Description

## Demonstration
