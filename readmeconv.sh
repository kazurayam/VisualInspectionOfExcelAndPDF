#!/Usr/Bin/Env Bash

# Convert All The Files With Name Ending With `*.Adoc` Into `*.Md`.
# `*.Adoc` Is An Asciidoc Document File, `*.Md` Is A Mardown Document File.
# E.G, `Readme_.Adoc` Will Be Converted Into `Readme_.Md`
# Except Ones With `_` As Prefix.
# E.G, `_Readme.Adoc` Is Not Processed By This Script, Will Be Left Unprocessed.
#
# How To Active This: In The Command Line, Just Type
# `> ./Readmeconv.Sh`
#
# Can Generate Table Of Content In The Output *.Md File By Specifying `-T` Option
# `> ./Readmeconv.Sh -T`

Requiretoc=False

Optstring="T"
While Getopts ${Optstring} Arg; Do
    Case ${Arg} In
        T)
            Requiretoc=True
            ;;
        ?)
            ;;
    Esac
Done

Find . -Iname "*.Adoc" -Type F -Maxdepth 1 -Not -Name "_*.Adoc" | While Read Fname; Do
    Target=${Fname//Adoc/Md}
    Xml=${Fname//Adoc/Xml}
    Echo "Converting $Fname Into $Target"
    # Converting A *.Adoc Into A Docbook
    Asciidoctor -B Docbook -A Leveloffset=+1 -O - "$Fname" > "$Xml"
    If [ $Requiretoc = True ]; Then
      # Generate A Markdown File With Table Of Contents
      Cat "$Xml" | Pandoc --Standalone --Toc --Markdown-Headings=Atx --Wrap=Preserve -T Markdown_Strict -F Docbook - > "$Target"
    Else
      # Without Toc
      Cat "$Xml" | Pandoc --Markdown-Headings=Atx --Wrap=Preserve -T Markdown_Strict -F Docbook - > "$Target"
    Fi
    Echo Deleting $Xml
    Rm -F "$Xml"
Done

# If We Find A Readme*.Md (Or Readme*.Md),
# We Rename All Of Them To A Single Readme.Md While Overwriting,
# Effectively The Last Wins.
# E.G, If We Have `Readme_.Md`, It Will Be Overwritten Into `Readme.Md`
Find . -Iname "Readme*.Md" -Not -Name "Readme.Md" -Type F -Maxdepth 1 | While Read Fname; Do
    Echo Renaming $Fname To Readme.Md
    Mv $Fname Readme.Md
Done