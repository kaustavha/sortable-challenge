# Readme

Java app that solves Sortable's product list merge challenge. 
Used gradle to remove any IDE or reqd deps for running from src.

## Algo overview
```
Create a product mapping of manufacturers -> products ->  List matchingListings
Stream in products.txt into productmap populating manufactuers and products we know of
Stream in listings.txt and augment productMap, for each listing
  check if we have matching manufacturer in productmap
  	if we do then iterate through the list of products for this manufacturer and try to find a matching product
  		insert the listings into the productMap[product] listings

Finally run through the product map generating output lines for any product with listings
```

# Quickstart

```
git clone <repo-url>
cd <project-name>
./gradlew run
// Generates an output.txt file based on default products & listings from challenge site

// More examples
./gradlew test

./gradlew build

java -jar build/libs/sortable-challenge-0.0.1.jar

java -jar build/libs/sortable-challenge-0.0.1.jar prods.txt lists.txt

```

Compile from source w/ global gradle:  

```
// clone the repo
git clone <repo-url>

// get gradle if it didnt come in this repo, alternatively use ./gradlew
brew install gradle

gradle init
gradle build
gradle run
gradle test

// Alternatively you can run it w/ 2 args products.txt and listings.txt, relative paths
gradle run products.txt listings.txt

```
