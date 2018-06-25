// message listener
window.addEventListener("message", receiveMessage, false);
var cbioData = null;
var _elGlobal = null;

function receiveMessage(event)
{
  //if (event.origin !== "http://example.org:8080")
  //  return;
  console.log("received message! " + event.origin + ", data: " + event.data);
  cbioData = event.data;

  if (_elGlobal != null){
    $(_elGlobal).trigger('change');
  }

  // ...
}

console.log("posting message to parent " + document.referrer);
window.parent.postMessage("I am ready", document.referrer);

// cbio input binding
// This input binding is very similar to textInputBinding from
// shiny.js.
var cbioInputBinding = new Shiny.InputBinding();


// An input binding must implement these methods
$.extend(cbioInputBinding, {

  // This returns a jQuery object with the DOM element
  find: function(scope) {
    return $(scope).find('input[type="cbio"]');
  },

  // return the ID of the DOM element
  getId: function(el) {
    return el.id;
  },

  // Given the DOM element for the input, return the value
  getValue: function(el) {
    return {
       value: el.value,
       cbioData: cbioData
     }
  },

  // Given the DOM element for the input, set the value
  setValue: function(el, value) {
    _elGlobal = el;
    el.value = value;
  },

  // Set up the event listeners so that interactions with the
  // input will result in data being sent to server.
  // callback is a function that queues data to be sent to
  // the server.
  subscribe: function(el, callback) {
    $(el).on('keyup.cbioInputBinding input.cbioInputBinding', function(event) {
      callback(true);
      // When called with true, it will use the rate policy,
      // which in this case is to debounce at 500ms.
    });
    $(el).on('change.cbioInputBinding', function(event) {
      callback(false);
      // When called with false, it will NOT use the rate policy,
      // so changes will be sent immediately
    });
  },

  // Remove the event listeners
  unsubscribe: function(el) {
    $(el).off('.cbioInputBinding');
  },

  // Receive messages from the server.
  // Messages sent by updatecbioInput() are received by this function.
  receiveMessage: function(el, data) {
    if (data.hasOwnProperty('value'))
      this.setValue(el, data.value);

    if (data.hasOwnProperty('label'))
      $(el).parent().find('label[for="' + $escape(el.id) + '"]').text(data.label);

    $(el).trigger('change');
  },


  // This returns a full description of the input's state.
  // Note that some inputs may be too complex for a full description of the
  // state to be feasible.
  getState: function(el) {
    return {
      label: $(el).parent().find('label[for="' + $escape(el.id) + '"]').text(),
      value: el.value
    };
  },

  // The input rate limiting policy
  getRatePolicy: function() {
    return {
      // Can be 'debounce' or 'throttle'
      policy: 'debounce',
      delay: 500
    };
  }
});

Shiny.inputBindings.register(cbioInputBinding, 'shiny.cbioInput');

