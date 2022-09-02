# FileZipper

FileZipper is a RESTFul webservice with an endpoint to receive list of files (form-data) and return a zipped file containing the input files.

## Installation

TBD

```bash
mvn spring-boot:run
```

## Usage

```
curl -X POST \ http://localhost:8080/files/zip \ -H 'cache-control: no-cache' \ -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \ -H 'postman-token: 6e8468b7-42a4-0ab8-4f08-21c42cfc696c' \ -F 'fileData=@test1.txt' \ -F 'fileData=@test2.txt'

```
