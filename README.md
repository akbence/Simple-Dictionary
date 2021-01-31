# Dictionary-Java-Application
A simple java swing application to store words and meanings. This application uses Java Collections API to store words 
and their meanings.
All the basic functionalities like AddWord, DeleteWord, SearchWord are present, on the main page there is a paged table of all the words. 
Serialization is used to store and retrieve words from file.

## Dictionary element properties
A dictionary element can have can have:
* _word_
* _pronounciation_
* _meaing_
* _source_ (book:chapter)

All of them above in UTF-8.

## Prequesites
Java (this project build with JDK 11)

## How to use

1. Download the latest release: [Release v1.1](https://github.com/akbence/Simple-Dictionary/releases/download/v1.1/dictionary_v1.1.zip)
2. Extract the files into a directory
3. If you running on Windows, just run the _szotar.bat_ file
4. If you dont like bat files, or you running other distro, just run this, inside the project folder: _java -jar dictionary.jar_

*Dont bother the .db file, corrupting it means the application wont work.*

## Sidenote

This project was made in hurry, so it needs a quite big refactor. If anybody sees this, I accept all the pull requests.
It is not beautiful, or well structured, but it is a working one.
