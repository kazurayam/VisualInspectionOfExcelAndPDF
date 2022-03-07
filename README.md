# Patrol: visual inspection of Excel and PDF

## Motivation

A few years ago, I worked for a media company. I was asked to develop a tool software, which is a sort of web scraping tool. The target page is [this](https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html) (is still available to public at MAR 2022). The page looks something like this:

![target](./docs/images/01_NISA_target_page.png)

You don’t have to understand this page in detail except the following points.

1.  The page contains several `<a href="…​">` tags to the URLs of Excel files and PDF files.

2.  The content of Excel & PDF files are updated by the publisher irregularly. The frequency of updates are not determined. You can track the record: once a month, or once per 2 months.

3.  The URL string of Excel/PDF files are fixed. The file names are fixed. You can not tell if the file has been updated by the file name.

4.  The publisher does not provide any push-style notification (like [RSS](https://en.wikipedia.org/wiki/RSS)) for this page. Those who are interested in the information of this page are asked to keep watching the page, read it and find updates somehow.

5.  The company I worked for had a serious interest in the Excel files. Some staffs were asked to visit this page everyday. Usually they found the files unchanged.

6.  The staffs hated this job. They wanted some system to automate this bullshit job.

Since then I have worked long to solve this problem. Finally I have got a solution. Let me describe it.

## Problem to solve

The Excel files in the page are irregularly updated, and the publisher does not provided any notification. Therefore my system has to

1.  visit the web page regularly and automatically, for example once a day

2.  download the current version .xlsx files, compare the content with some previous version to find if there are any updates.

3.  If any updates found, take some action. For example, put the newer .xlsx files into some organizational file server and send an E-mail to those who are concerned.

The 1st problem (running a process regularly and automatically) is easy to solve using Linux [cron](https://en.wikipedia.org/wiki/Cron), Windows [Task Scheduler](https://docs.microsoft.com/en-us/windows/win32/taskschd/about-the-task-scheduler), and some Continuous Integration servers like [Team City](https://www.jetbrains.com/teamcity/). Katalon offers [Test Ops](https://www.katalon.com/testops/) of course.

The 3rd problem (taking some actions when your script find it necessary) is easy to solve by your custom programming.

The 2nd problem is difficult. **How can my software detect that the current version of an Excel file is updated? How can my software present visually the difference between the current and previous Excel files?**

How to compare a 2 Excel files? --- this is a technical challenge. My [Visual Inspection in Katalon Studio](https://forum.katalon.com/t/visual-inspection-in-katalon-studio-reborn/57440) showed that my product is capable of comparing pairs of PNG images and pairs of text files regardles its format --- HTML, XML, JSON, CSV, CSS, JS. However, `.xlsx` and `.pdf` are binary files. My text differ module can not deal with those binary files.

## Solution

I would call my product that addresses the aforementioned problem **Patrol** for short.

I have developed and published a set of Java/Groovy library to build a Patrol for me and for you.

-   [materialstore](https://github.com/kazurayam/materialstore/)

-   [materialstore-mapper](https://github.com/kazurayam/materialstore-mapper/)

And [This project](https://github.com/kazurayam/VisualInspectionOfExcelAndPDF) shows a sample built on top of these 2 libraries.

## Description

### Target Application Under Test

I won’t use [this page](https://www.fsa.go.jp/policy/nisa2/about/tsumitate/target/index.html) as the testbed for demonstration, because

1.  this page is not updated frequent enough. It is updated only once per a month at most. I want a URL as testbed which is updated more frequently; I want once per 1 or 2 days.

2.  this page is owned by a governmental organization. I do not like bothering them.

Instead I would use the following URL as the testbed for my demonstration:

-   [Amazon.com, Inc. - Press Room News Releases](https://press.aboutamazon.com/rss/news-releases.xml)

This URL provides a RSS feed in XML format, is updated frequently enough. I believe that the publisher (Amazon.com) would not stop me accessing it using my automated software.

My software would do a series of file format conversion: RSS XML → Excel → CSV.

## Demonstration

A example RSS document

    <rss xmlns:dc="http://purl.org/dc/elements/1.1/" version="2.0" xml:base="https://press.aboutamazon.com/">
      <channel>
        <title>Amazon.com, Inc. - Press Room News Releases</title>
        <link>https://press.aboutamazon.com/</link>
        <description>Amazon.com, Inc. - Press Room News Releases</description>
        <language>en</language>
        ...
        <item>
          <title>Amazon Announces Partnerships with Universities and Colleges in Texas to Fully Fund Tuition for Local Hourly Employees</title>
          <link>https://press.aboutamazon.com/news-releases/news-release-details/amazon-announces-partnerships-universities-and-colleges-texas</link>
          <description>Amazon employees in the U.S. will benefit from new Career Choice partnerships with more than 140 Universities and Colleges including several colleges and universities in Texas as well as national non-profit online providers Southern New Hampshire University , Colorado State University – Global,</description>
          <pubDate>Thu, 03 Mar 2022 12:45:00 -0500</pubDate>
          <dc:creator>Amazon.com, Inc. - Press Room News Releases</dc:creator>
          <guid isPermaLink="false">31586</guid>
        </item>
        ...

This RSS document is internally converted into an Excel xlsx file, like this

![spreadsheet](./docs/images/02_Spreadsheet.png)

And then the Excel xlsx file is coverted into a CSV text file, like this

    publishedDate,uri,title,link,description,author
    Sat Mar 05 10:00:00 JST 2022,31591,Amazon travaille en collaboration avec des ONG et ses employés pour offrir un soutien immédiat au peuple ukrainien,https://press.aboutamazon.com/news-releases/news-release-details/amazon-travaille-en-collaboration-avec-des-ong-et-ses-employes,"Comme beaucoup d'entre vous à travers le monde, nous observons ce qui se passe en Ukraine avec horreur, inquiétude et cœur lourds. Bien que nous n’ayons pas d'activité commerciale directe en Ukraine, plusieurs de nos employés et partenaires sont originaires de ce pays ou entretiennent un lien","Amazon.com, Inc. - Press Room News Releases"
    Fri Mar 04 02:45:00 JST 2022,31586,Amazon Announces Partnerships with Universities and Colleges in Texas to Fully Fund Tuition for Local Hourly Employees,https://press.aboutamazon.com/news-releases/news-release-details/amazon-announces-partnerships-universities-and-colleges-texas,"Amazon employees in the U.S. will benefit from new Career Choice partnerships with more than 140 Universities and Colleges including several colleges and universities in Texas as well as national non-profit online providers Southern New Hampshire University , Colorado State University – Global,","Amazon.com, Inc. - Press Room News Releases"
    ...

The CSV text file is ready to diff. The materialstore library can easily compare a pair of "previous CSV" and "current CSV". The library can generate a report for human readers.

[./docs/store/AmznPress-index.html](./docs/store/AmznPress-index.html)

![03 diff of CSV files](./docs/images/03_diff_of_CSV_files.png)

The diagram illustrates the process sequence of [Test Case/main/AmznPress/Main\_Chronos](./Scripts/main/AmznPress/Main_Chronos/Script1646628040145.groovy)

![sequence](./docs/diagrams/out/sequence/sequence.png)
