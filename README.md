## Android Library project for working with Datalogics PDF Web APIs

Android Library project that offers predefined ````AyncTask````s for interacting
with Datalogics PDF Web APIs

## Building with Android Studio

* Import the project by opening Android Studio
* Then select _Open Project_
* Browse to the directory where you cloned this repository
* Go into the subdirectory __DatalogicsPDFWebAPILibrary__ and click _Choose_
* You should see one project with two modules __PDFWebAPILibrary__ and __app__

The default build configuration should be to build the _app_.

## Building with Gradle at the command line

It is possible to build the project in individual pieces or all at once from the
command line.

### Build everything

To build everything from the command line, start in the root of the repository

* change directory to _DatalogicsPDFWebAPILibrary_
* run ````./gradlew assembleDebug```` or ````./gradlew assembleRelease````

This will build the _app_ and its required modules.

### Build just the __PDFWebAPILibrary__

To build just the __PDFWebAPILibrary__, start in the root of the repository

* change directory to the _DatalogicsPDFWebAPILibrary_
* run ````./gradlew PDFWebAPILibrary:assembleDebug```` or ````./gradlew PDFWebAPILibrary:assembleRelease````

This will build the __PDFWebAPILibrary__ as an _aar_ file that can be incorporated
into other projects that use Android Studio but Eclipse does not support the
_aar_ file format at this time (only slightly annoying to @datalogics-bhaugen).

The output of the build will be in __PDFWebAPILibrary/build/outputs/aar/__.
