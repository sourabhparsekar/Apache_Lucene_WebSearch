# Apache_Lucene_WebSearch

This Project is a document Search Engine using Lucene. This Porject aims at exploring improved & efficient ways to retrieve documents based on not just index meta data but also on the actual contents of files/documents stored in content repository. Just as an analogy, one can think of it as Google for locating files available in Content Repository based on Apache Lucene and its support APIs.

Apache Lucene is a free and open-source information retrieval software library, supported by the Apache Software Foundation and is released under the Apache Software License. Lucene has also been used to implement recommendation systems. At the core of Lucene's logical architecture is the idea of a document containing fields of text. Text from PDFs, HTML, Microsoft Word, Mind Maps, and OpenDocument documents, as well as many others (except images), can all be indexed as long as their textual information can be extracted.

Lucene is an inverted full-text index. This means that it takes all the documents, splits them into words, and then builds an index for each word. The index contains word id, number of docs where the word is present, and the position of the word in those documents. Since the index is an exact string-match, unordered, it can be extremely fast. So when you give a single word query it just searches the index (O(1) time complexity).

The Lucene query language allows the user to specify which field(s) to search on, which fields to give more weight to (boosting), the ability to perform boolean queries (AND, OR, NOT) and other functionality.
