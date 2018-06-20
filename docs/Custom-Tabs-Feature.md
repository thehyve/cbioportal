# Introduction

This page is about how to configure extra tabs to be displayed in the query results page. This can be a great option for institutes
that want to integrate local web applications or custom web components into their local cBioPortal. The solution is a generic one, but
specific implementation examples can also be found at the end of this document.

# Overview

The solution consists of a set of configuration options in `portal.properties` which ensure that one or more custom tabs appear in the query results page.
Each configured custom tab needs to include a *custom html page* that is loaded dynamically. This *custom html page* is responsible for integrating the
external web application (e.g. via iframe), or just integrating a custom visualization (e.g. by adding a custom web component).

# Legal notice

The integration code (so the *custom html pages* mentioned above) will fall under AGPL as well, given they constitute a combined work with the main cBioPortal
app. This means that anyone publicly hosting or distributing custom tabs should make the custom tab code available under AGPL as well.

# Configuring your custom tabs

Based on the parameters configured in the json file pointed out by the `portal.properties` parameter `ui.custom.tabs.config.location`,
the frontend will include one or more tabs to the query results page. For each tab it will load the respectively
configured *custom html page* which contains the custom logic for further integration of its custom app or visualization.

So to start, add the following to `portal.properties`:

```
ui.custom.tabs.config.location=file:/<path>
```
and make a `.json` file, following the format of the example below:

```json
{ "custom_tabs":
  [
    {
        "title": "My custom tab1",
        "location": "RESULTS_PAGE",
        "url": "/cbioportal/custom/customTab1.html",
        "show_with_multiple_studies": true,
        "custom_parameters": {...}
    },
    {
        "title": "My custom tab2",
        "location": "RESULTS_PAGE",
        "url": "/cbioportal/custom/customTab2.html",
        "show_with_multiple_studies": false,
        "custom_parameters": {...}
    },
  ]
}
```

# Examples

## Integrating custom R Shiny apps

For R Shiny examples, see the page about [integrating R-Shiny apps as custom tabs](Custom-Tabs-Using-R-Shiny.md).