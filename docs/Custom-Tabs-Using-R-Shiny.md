# Introduction

This page is about how to integrate a R Shiny app as an extra custom tab, as part of the [Custom Tabs Feature](Custom-Tabs-Feature.md).
To get the integration working, it is important to understand how to pass cBioPortal query **parameters** and even cBioPortal query results **data**
to the R Shiny app, so that the app can render plots based on this data. This page provides examples, with step by step documentation on how to achieve this.

# Example 1

This is a very simple example where the app just displays the study name and the list of samples.

TODO - add screenshot of the app in cBioPortal here


### Step 1

Download and use the `cbio-shiny-input-binding.js` and `cbio-shiny-input.R` files [here](plugin-templates/shiny/).
There two files should be placed on your Shiny server, in our example we have placed them in `/srv/shiny-server/shinyCbioCommon/`.

### Step 2

Add `cbio-shiny-input.R` to the top of your `ui.R` code, e.g.:

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

This is a slightly more advanced example with a density plot and two drop down (or lists) with the queried genes and the expression profiles. The density plot shows the distribution of expression values for the selected gene in the selected profile. The gene and the profile are selected in the drop down.

TODO - add screenshot of the app in cBioPortal here

### Step 1

Download and use the `cbio-shiny-input-binding.js` and `cbio-shiny-input.R` files [here](plugin-templates/shiny/).
There two files should be placed on your Shiny server, in our example we have placed them in `/srv/shiny-server/shinyCbioCommon/`.

### Step 2

Add `cbio-shiny-input.R` to the top of your `ui.R` code, e.g.:

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
  <meta name="description" content="Shiny tab2">
  <meta name="author" content="Abbvie">

</head>

<body>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
	<script>
		
		var gIframe = null;

		var receiveMessage = function(event) {
			gIframe = this.myIframe;
			//Possible validation, but not really necessary here:
			//if (event.origin !== "http://example.org:8080")
			//  return;
			console.log("received message! " + event.origin);

			var genes = [];
			var entrez = [];
			var entrez_genes = {};
			for (var i in parent.cbioStore.genes.result) {
				genes.push(parent.cbioStore.genes.result[i].hugoGeneSymbol);
				entrez.push(parent.cbioStore.genes.result[i].entrezGeneId);
				entrez_genes[parent.cbioStore.genes.result[i].entrezGeneId] = parent.cbioStore.genes.result[i].hugoGeneSymbol;
			}
			
			var shinyStudyId = null;
			if (parent.cbioStore.studies.result.length == 1) {
				shinyStudyId = parent.cbioStore.studies.result[0].studyId;
			}

			var url = 'http://localhost:8089/cbioportal/api/studies/'+shinyStudyId+'/molecular-profiles' 
			$.getJSON(url, function(result){
				var molecularProfiles = [];
				for (var j in result) {
					molecularProfileId = result[j].molecularProfileId;
					var type = molecularProfileId.replace(shinyStudyId+"_",'');
					if (type.indexOf('rna') !== -1 || type.indexOf('protein') !== -1 || type.indexOf('rppa') !== -1) {
						molecularProfiles.push(molecularProfileId);
					}
				}

				var postParameters = {
					"entrezGeneIds": entrez,
					"molecularProfileIds": molecularProfiles
				}
				$.ajax({
					contentType: 'application/json',
					data: JSON.stringify(postParameters),
					dataType: 'json',
					success: function(result2){
						var molecularData  = {};
						for (var k in result2) {
							var gene = entrez_genes[result2[k].entrezGeneId];
							var profile = result2[k].molecularProfileId;
							if (!(gene in molecularData)) {
								molecularData[gene] = {};
								molecularData[gene][profile] = [];
							}
							if (!(profile in molecularData[gene])) {
								molecularData[gene][profile] = [];
							}
							molecularData[gene][profile].push(result2[k].value);
						}
						var data = {
						"studyId": shinyStudyId,
						"queryGenes": genes,
						"molecularProfiles": molecularProfiles,
						"molecularData": molecularData
						}
						console.log(data);
						gIframe.contentWindow.postMessage(data, "http://localhost:3838");
					},
					processData: false,
					type: 'POST',
					url: 'http://localhost:8089/cbioportal/api/molecular-data/fetch'
					});
			});
		}

	window.addEventListener("message", receiveMessage, false);			

	</script>
	<iframe id="myIframe" src="http://localhost:3838/shinyApp2/" style="height:600px;width:100%;border:none"></iframe>
</body>
</html>
```

TODO - replace the localhost:3838 URLs here by an example of reading this from the custom properties configured in the json....see [Custom Tabs Feature](Custom-Tabs-Feature.md)

This custom `.html` can be added to cBioPortal, by making sure it is deployed to tomcat e.g. at `/usr/local/tomcat/webapps/cbioportal/shinyTabs/tab1/customTab1.html`.




