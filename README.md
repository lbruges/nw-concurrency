# nw-concurrency
## Environment Configuration
JDK: 1.23

## Application Properties
All properties are optional and have a default value if not defined

### Sample Global properties:
Create a new file named <b>application.properties</b> and add it to the out/production folder (if running from IDE), or to the same path that contains the compiled jar.<br> 
Define the following properties:
```
backtracker.printer.enabled=true
backtracker.printer.output=FILE
backtracker.printer.filename=result.txt
matrix.printer.enabled=false
matrix.printer.output=FILE
matrix.printer.filename=matrix.txt
matrix.concurrency.enabled=true
# matrix.concurrency.pool-size=2
matrix.concurrency.seq-threshold=20
matrix.score.gap=-2
matrix.score.match=1
matrix.score.miss=-1
matrix.log-exec-time=true
```
<b>Note:</b> please keep in mind that  matrix.concurrency.pool-size defaults to the number of available processors

### Sample Web Request properties:
Create a new file named <b>request.properties</b> and add it to the out/production folder (if running from IDE), or to the same path that contains the compiled jar. <br>
Define the following properties:
```
req.url=https://rest.ensembl.org/sequence/id/%s?type=cdna;content-type=application/json
req.seq-a-id=ENSG00000239615
req.seq-b-id=ENSG00000239617
```
Other sample sequences could be obtained from:
```
https://rest.ensembl.org/xrefs/symbol/homo_sapiens/VEGFA?content-type=application/json
[
    {
        "type": "gene",
        "id": "ENSG00000112715"
    }
]

https://rest.ensembl.org/lookup/id/ENSG00000112715?expand=1;content-type=application/json
// Look for protein_coding biotype
```

## Compiling instructions
To compile this project using IntelliJ idea, please follow these steps:
```
Step 1: Create a New Artifact
Open your project in IntelliJ IDEA.
Go to File > Project Structure.
Select Artifacts in the left panel.
Click the + button and choose JAR > From modules with dependencies.
Choose the Main Class (if you're creating an executable JAR) and select the modules you want to include.
Make sure that the "Include in project build" checkbox is checked.
Click OK to create the artifact.
Step 2: Build the Artifact
After the artifact is set up, go to Build > Build Artifacts.
Select your artifact and choose Build.
This will create a JAR file in the out/artifacts directory.
```

To run the JAR file, place the <b>application.properties</b> and <b>request.properties</b> in the same folder and run:
```
java -jar NeedlemanOrchestrator.jar
```
