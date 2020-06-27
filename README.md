# Elasticsearch-Application

## Overview
This is a project about indexing and utilising datasets, implemented using Java Swing and Elasticsearch. The project is still in development but for now contains a rather simple GUI and logic that allows a dataset to be indexed in Elasticsearch. The dataset is from [Signal Media](https://research.signal-ai.com/newsir16/signal-dataset.html) and contains one million entries, each a news article, with 6 fields. The dataset is in a JSON format and, as a result of its size, is not able to be opened in many text editors. In the project, I have included filters which allow end-users to initiate retrievals that only return a subset of what was stored. An application for this application is being able to show - on a website - news articles that the end-user has subscribed to view, perhaps in a specific order (using the Published field); these could be selectable via mime types, for example.  

## Fields
The types of field are as follows:

* Id | Type: String
* Title | Type: String
* Content | Type: String 
* Media-Type | Type: String
* Published | Type: Datetime
* Media-Type | Type: String (These remind me of the HTTP MIME types)
