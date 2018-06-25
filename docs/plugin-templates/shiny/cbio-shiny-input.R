# This function generates the client-side HTML for a cbio input
cbioInput <- function(inputId, label, value = "") {
  tagList(
    # This makes web page load the JS file in the HTML head.
    # The call to singleton ensures it's only included once
    # in a page.
    shiny::singleton(
      shiny::tags$head(
        shiny::tags$script(src = "/shinyCbioCommon/cbio-shiny-input-binding.js")
      )
    ),
    shiny::tags$label(label, `for` = inputId, style="display:none"),
    shiny::tags$input(id = inputId, type = "cbio", value = value, style="display:none")
  )
}


# Send an update message to the cbio input on the client.
# This update message can change the value and/or label.
updateCbioInput <- function(session, inputId,
                           label = NULL, value = NULL) {

  message <- dropNulls(list(label = label, value = value))
  session$sendInputMessage(inputId, message)
}


# Given a vector or list, drop all the NULL items in it
dropNulls <- function(x) {
  x[!vapply(x, is.null, FUN.VALUE=logical(1))]
}
