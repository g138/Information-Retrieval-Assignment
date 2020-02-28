# Assignment 1: Information Retrieval & Web Search (CS7IS3)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

My Amazon Instance contains the following files:

  - The full source code directory named as **LuceneFinal**.
  - The **trec_eval** folder is also in the **src** folder of project folder
  - The **CranField** whihc constains **Corrected Qrels** named as  **qrelnew** is in **src** folder of project folder


## Getting Started

- Run the following commands on ubuntu terminal or an windows via cmd as adminstrator

### Login Credentials (if required in any of the following steps):

```sh
$ Username           : cs7is3 
$ Password           : cs7is3
$ Instance IP Address: ec2-35-173-216-70.compute-1.amazonaws.com

``` 

**To enter the system**

```sh
$ ssh cs7is3@ec2-35-173-216-70.compute-1.amazonaws.com
Enter password: cs7is3
```
### Building the code

```sh
$ cd LuceneFinal/
$ mvn package
```

### Running the code

```sh
$ java -jar target/LuceneFinal-0.0.1-SNAPSHOT.jar
```
### Evaluationg the results

```sh
$ cd src/trec_eval-9.0.7/
$ make
$ ./trec_eval ../cran/qrelnew ../EnglishAnalyzer_BM25Similarity.out
```
**To Evaluate more with different Analyser and Similarity run the following:**

```sh
$ ./trec_eval ../cran/qrelnew ../EnglishAnalyzer_LMDirichletSimilarity.out
$ ./trec_eval ../cran/qrelnew ../EnglishAnalyzer_ClassicSimilarity.out
$ ./trec_eval ../cran/qrelnew ../StandardAnalyzer_ClassicSimilarity.out
$ ./trec_eval ../cran/qrelnew ../StandardAnalyzer_BM25Similarity.out
$ ./trec_eval ../cran/qrelnew ../StandardAnalyzer_LMDirichletSimilarity.out
```