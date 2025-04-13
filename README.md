## Wikipedia Search Engine

### Project Objective
In this project, primary task is to build a scalable and efficient search engine on Wikipedia pages. This constitutes two stages - inverted index creation and query search mechanism, where the scope of performance in the second stage relies heavily on the quality of index built in its preceding stage. Throughout the project, efforts have been made to build a system optimized for search time, search efficiency (i.e. the quality of results), indexing time and index size. We have used Wikipedia dumps of size 80GB in XML format, which is parsed to get Wikipedia pages.

### Core Processing Stages
- **XML Parsing** – Uses a SAX parser to efficiently extract Wikipedia pages.
- **Text Preprocessing :**
  - **Tokenization** – Splitting text into meaningful units.
  - **Case Folding** – Converting text to lowercase for uniformity.
  - **Stop Words Removal** – Eliminating common words that don’t add search value.
  - **Stemming / Lemmatization** – Reducing words to their root forms for better indexing.
- **Inverted Index Creation** – Constructs a posting list to map terms to document occurrences.
- **Optimization** – Enhancing index structure for reduced storage and faster queries.

### 📂 Project Structure
``` 
text-search-engine/
│── src/
│ ├──main/java/com/project/wikipedia_search_engine/
│ │ ├── controller/ # REST controllers for indexing xml dump and searching word.
│ │ ├── service/ # Core logic for pre-processing text, creating index files and merging them. 
│ │ ├── model/ # Data models (GloalDocBean, Query Request/Response DTOs)
│ │ ├── util/ # Utility methods
│ ├── resources/
│ │ ├── application.properties # configurations data
│── build.gradle # Dependencies (Spring Boot, stanford-corenlp, etc.)
```


### ⚙️ Exposed APIs

1. `{{baseUrl}}/start-indexing` → Provide XMLFilePath as `RequestParam` in the request.
2. `{{baseUrl}}/perform-query` → Attach QueryRequestDTO as `RequestBody` in the request.

### 🏗️ Setup & Installation
### Prerequisites
- Ensure **Java+** on your system. 
- **Clone the repository**
- **Build & Run the Project** (Use `./gradlew clean build`)