### CSV Normalizer
This tool normalizes entries in a specified csv file and outputs the normalized entries to a new csv file. 

Normalization includes:
* formatting the `Timetamp` column entries to ISO-8601 format in the US/Eastern timezone.
* ensuring all `ZIP` codes have five digits (by prefixing needed 0s)
* converting `Full Name` column entries to upper case
* providing `Total Duration` for `Bar Duration` and `Foo Duration` in milliseconds
* replacing invalid UTF-8 characters with the Unicode Replacement character

The `Address` and `Notes` column values are passed through as is.


The normalizer tool is written in Java 8 and built using [Maven](https://maven.apache.org/) on Mac OS 10.15. You will need maven installed on your machine (download [here](https://maven.apache.org/download.cgi)).

To run the program:
* Clone this repo.
* Once maven is installed, from the `normalizer` directory, run:
 `mvn clean`
 `mvn install`
* These steps should create a jar with the required dependencies (`normalizer-1.0-SNAPSHOT-jar-with-dependencies.jar`) in a `target` directory.
* Copy the jar to the `app` directory; from that directory you can run:
`./normalizer <filename>`.  
* You may need to give permissions to the normalizer script:
`chmod 755 normalizer`
