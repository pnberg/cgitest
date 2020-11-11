# cgitest
CGI test assignment to generate random zip files, unpack them in threads and return a report containing count of unpacked files, total size of them and time estimated for unpacking.

Properties which can be set in application.properties
cgitest.zip-directory-path: folder where zip files are generated
cgitest.unzip-directory-path: folder where zip-files are unpacked
cgitest.default-file-count: count of zip files to generete if value is not given
cgitest.default-thread-count: count of concurrent threads for unpacking

Usage:
Call <host>/zipfiles/build to generate zip files. Request attribute "count" can be set to specifiy count of files generated
Call <host>/unzip to unzip files. Unzipping report is given as response.

