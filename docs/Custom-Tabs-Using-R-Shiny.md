# Introduction

This page is about how to integrate a R Shiny app as an extra custom tab, as part of the [Custom Tabs Feature](Custom-Tabs-Feature.md).
To get the integration working, it is important to understand how to pass cBioPortal query **parameters** and even cBioPortal query results **data**
to the R Shiny app, so that the app can render plots based on this data. This page provides examples, with step by step documentation on how to achieve this.

# Example 1

This is a very simple example where the app just displays the study name and the list of samples.

TODO - add screenshot of the app in cBioPortal here


### Step 1

Download and use the `cbio-shiny-input-binding.js` and `cbio-shiny-input.R` files [here](.TODO link to something like docs/plugin-templates/shiny/.).
There two files should be placed on your Shiny server, in our example we have placed them in `/srv/shiny-server/shinyCbioCommon/`.

### Step 2

Add ``cbio-shiny-input.R` to the top of your `ui.R` code, e.g.:

```R
source("../shinyCbioCommon/cbio-shiny-input.R")
library(shiny)

shinyUI(
  ...
```
Also add the cBioPortal component (which will be a hidden component) to the `ui.R` code, e.g.:
```R
...
   cbioInput("my_cbio", "cbio parameters: ", "...")
...
```

### Step 3

Make your custom `.html` page and add it to the cBioPortal configuration as explained by [Custom Tabs Feature](Custom-Tabs-Feature.md).

The custom `.html` page can look like this:

```html
<!doctype html>

<html lang="en">
<head>
  <meta charset="utf-8">

  <title>Shiny tab</title>
  <meta name="description" content="Shiny tab1">
  <meta name="author" content="Shiny Dev">
</head>

<body>
	<script>
		
		var receiveMessage = function(event) {
			//Possible validation, but not really necessary here:
			//if (event.origin !== "http://example.org:8080")
			//  return;
			console.log("received message2! " + event.origin);
		
			var shinyStudyId = parent.cbioStore.studyIds.result[0];
			var sampleIds = getSampleIds(parent.cbioStore.samples.result);
			var sampleValues = Array.from({length: sampleIds.length}, () => Math.floor(Math.random() * 40));
			var data = {
					"studyId": shinyStudyId,
					"sampleIds" : sampleIds,
					"sampleValues" : sampleValues
			}
			this.myIframe.contentWindow.postMessage(data, "http://localhost:3838");
		}

		window.addEventListener("message", receiveMessage, false);

		function getSampleIds(cbioSamples) {
			var result = [];
			for (var i = 0; i < cbioSamples.length; i++) {
				result[i] = cbioSamples[i].sampleId;
			}
			return result;
		}
		
	</script>
	<iframe id="myIframe" src="http://localhost:3838/shinyApp1/" style="height:600px;width:100%;border:none"></iframe>
</body>
</html>
```

TODO - replace the localhost:3838 URLs here by an example of reading this from the custom properties configured in the json....see [Custom Tabs Feature](Custom-Tabs-Feature.md)
TODO2 - replace code by the real Example 1 code.

This custom `.html` can be added to cBioPortal, by making sure it is deployed to tomcat e.g. at `/usr/local/tomcat/webapps/cbioportal/shinyTabs/tab1/customTab1.html`.


# Example 2

This is a slightly more advanced example with a histogram and a drop down (or list) with the queried genes. The histogram shows the distribution
of expression values for the gene selected in the drop down.

TODO - add screenshot of the app in cBioPortal here




