# J-Basic
### Description
A made up interpretive language inspired by an assignment from [CSE 465](https://miamioh.edu/cec/academics/departments/cse/academics/course-descriptions/cse-465-565/index.html) at Miami University.

### How it Works (TLDR)
The interpreator reads in a series of JB (J-Basic) statements from a .JB file. It determines the variables and the methods to create via that JB file. From there is executes the JB file and runs the statements real time. 

**Note:** This does not convert JB statements to Java code, rather it interpretes the JB statements real time and produces an output.

### Running J-Basic
Currently, in order to actually see the output of any of your J-Basic code, you'll need to run a .bat to get a console view. This will change in the future, but for now, the code below will run the v0.1 jar file.

**java -Xmx256M -Xms256M -jar "J-Basic Editor v0.1.jar"**

## Documentation
All documentation is for **J-Basic Version: 0.1**. Since this language is still in beta, there will be frequent changes to the core elements of the langauge. This documentation will reflect these changes as they appear.

### Variables

Currently the list of supported variables is as follows:
* Integers **Keywords:** integer and int
* Strings **Keywords:** string and str

**Note: JB does not require the use of semicolons (;)**

**Note: JB is NOT a case sensitive langauge. Meaning, varName == VARNAME**

Defining a variable can be done in one of two ways in JB. The first of which is called **declaration without initialization** and can be see here:

*int varName*

The second way to define a variable is called **declaration WITH initialization** and can be see here:

*str strName = "A brand new string"*

As with most langauges, if you were to try and use a uninitialized variable in any case, it will result in a runtime error and the JB interpreter will produce an error.

#### Integers

Integers in JB have full support of all math operations including addition, subtraction, multiplication, division and modulo.

Therefore, all of the following statements are **valid** JB statements:
* int A = (10 * 10) + 15
* int B = (25 - 15) % 3
* int C = A / B
* int D = 0
* int E = C + D

#### Strings

Strings in JB are defined exactly as described above. All of the following are **valid** JB statements:
* string A = "This is a string"
* string B = "This is " " also a string" *(See concatenation)*
* string C = A B *(See concatenation)*
* A - 7 *(See trimming)
* string E = A - 6 *(See sub string)*

##### Concatenation

Concatenation is rather simple in JB, instead of needing + or . to add multiple strings, you just write them out with a space in between. For example:

String var = "This is a string" " with another string added to it"

*var = "This is a string with another string added to it"*

##### Trimming

Trimming is the most complex string feature. In order to trim a particular set of characters off a string, you type the name of the string, minus the amount of characters to remove. For example:

String var = "This is good. REMOVE THIS!"

var - 13

*var = "This is good."

##### Sub String

Sub string works in a similar fashion to trimming, but with sub string you are setting a new string equal to the characters that you would've removed from the old string. The act of sub string does not actually remove the characters from the orginal string unless you set it to the result of the sub string. For example: 

String var = "This string"

String newVar = var - 6

*newVar = "string"*

*var = "This string"*

### Print Statements

Print statements in JB are rather straight forward and follow all the same conventions as string for formatting. Below are some examples of some **valid** print statements. Assume that all variables have been initializated:

* print var varTwo " STRINGS"
* print intOne " " stringOne
* print "This is a string" " " intOne

### Version Statement
At the top of a JB file can contain a JB version statement **(## J-BASIC VERSION x.x ##)**. This is used by the interpreter to determine if there may or may not be any compatability errors with a particular JB file. If when trying to run an older JB file on a new JB version, you may encounter warnings and or errors depending on the changes made to the language.

