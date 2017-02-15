/*
 * Copyright (c) 2016 The Hyve B.V.
 * This code is licensed under the GNU Affero General Public License,
 * version 3, or (at your option) any later version.
 *
 * This file is part of cBioPortal.
 *
 * cBioPortal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

// This code is based on mutsig.js written by Gideon Dresdner 
// Sander Tan, Oleguer Plantalech and Pieter Lukasse, The Hyve
// December 2016

// Save selected checkboxes
var selectedBoxes;

// initialize and bind for
// geneset toggle button and geneset dialog box
var initGenesetDialogue = function() {
	"use strict";
	
	console.log("Setting up gene set popup event");

    // initialize geneset button
    // as hidden, and with JQuery UI style
    $('#toggle_geneset_dialog').hide();
    $('#toggle_geneset_dialog').button();

    // set up popup for gene set hierarchy
    $('#geneset_dialog').dialog({autoOpen: false,
        resizable: false,
        modal: true,
        minHeight: 315,
        minWidth: 636,
        
    	// destroy the tree so that it is renewed upon next dialog pop-up
        close: function() {
        	$('#jstree_genesets').jstree('destroy');
        }
    });

    // set listener for geneset select button
    $('#select_geneset').click(function() {
    	
    	// save selected checkboxes
    	selectedBoxes = $('#jstree_genesets').jstree("get_selected", true);

        // close dialog box
        $('#geneset_dialog').dialog('close');
    });

    // set listener for geneset cancel button
    $('#cancel_geneset').click(function() {

        // close dialog box
        $('#geneset_dialog').dialog('close');
    });
    
    // Update gene sets in query box
    $('#select_geneset').click(updateGenesetList);    
};

var loadingImage;

// Inititalize gene set hierarchical tree
var initializeGenesetJstree = function (genesetGeneticProfile, loadingImageElement) {
	
	console.log("Initializing hierarchical tree for gene set popup");
	
	loadingImage = loadingImageElement;
	
	// Construct URL
	var hierarchyJSON = "api/genesets/hierarchy?geneticProfileId=" + genesetGeneticProfile;
	
	// Get genomic profile specific gene set hierarchy
	$.getJSON(hierarchyJSON, hierarchyServiceCallback);
	
	// Show loading image
	loadingImage.show();
 }	


// Callback when gene set hierarchical tree is retrieved
var hierarchyServiceCallback = function(result_data) {
	// Hide loading image
	loadingImage.hide();
	
	var data = result_data;
	
	// Loop over JSON file to make it flat to input it in jsTree
	var flatData = [];
	
	// Leafs can be non-unique, therefor a unique ID is necessary,
	// because selecting a duplicate leaf results in a visualization issue.
	var leafId = 0;

	for (var i = 0; i < data.length; i++ ) {		
		// Read the node
		nodeId = data[i].nodeName;
		nodeName = data[i].nodeName;
		nodeParent = data[i].parentNode;
		
		// Convert node information to a flat format suitable for jstree
		if (nodeParent == null) {
			nodeParent = "#";
		}
		flatData.push({
			id : nodeId.toString(),
			parent : nodeParent,
			text : nodeName,
			name : nodeName,
			geneset : false,
			state : {
				opened : true,
				selected : false,
			}
		});
		
		// Check if node has any gene sets
		if (_.has(data[i], 'genesets')) {
			
			// Read the genesets in the node
			for (var j = 0; j < data[i].genesets.length; j++ ) {
				
				// Convert gene set information to a flat format suitable for jstree
				genesetId = leafId ++;
				genesetName = data[i].genesets[j].genesetId;
				genesetDescription = data[i].genesets[j].description;
				genesetRepresentativeScore = data[i].genesets[j].representativeScore;
				genesetReflink = data[i].genesets[j].reflink;
				genesetNrGenes = data[i].genesets[j].nrGenes;
	
				var genePlurality;
				var genesetNameText;
				
				// Decide if it tree description show geneset or genesets
				if (genesetNrGenes == 1) {
					genePlurality = 'gene';
				} else if (genesetNrGenes > 1) {
					genePlurality = 'genes';
				} else {
					genePlurality = '';
					genesetNrGenes = '';
				}
				
				// Add nr of genes to leaf
				genesetNameText = genesetNrGenes + ' ' + genePlurality;
				
				// Add score to leaf
				if (genesetRepresentativeScore != null) {
					genesetNameText = genesetNameText + ', representative GSVA score = ' +  genesetRepresentativeScore;
				}
	
				// Build label and add styling
				genesetNameText = genesetName + '<span style="font-weight:normal;font-style:italic;"> ' + genesetNameText + '</span>';
				
				flatData.push({
					// Add compulsary characteristics
					id : genesetId.toString(),
					parent : nodeId,
					text: genesetNameText, 
					state : {
						selected : false
					},
				
					// Also add data which might be useful later
					name: genesetName,
					description : genesetDescription,
					representativeScore : genesetRepresentativeScore,
					reflink : genesetReflink,
					geneset : true,
					
				});
			}
		}
	}

	// Build the tree
	$('#jstree_genesets').jstree({
		"plugins": ['checkbox', 'search'],
		"search": {
			'show_only_matches': true,
			},
		"core" : {
			"data" : flatData,
			"themes": {
				"icons":false
			} 
		}		
	}).on('search.jstree before_open.jstree', function (e, data) {
	    if(data.instance.settings.search.show_only_matches) {
	        data.instance._data.search.dom.find('.jstree-node')
	            .show().filter('.jstree-last').filter(function() { return this.nextSibling; }).removeClass('jstree-last')
	            .end().end().end().find(".jstree-children").each(function () { $(this).children(".jstree-node:visible").eq(-1).addClass("jstree-last"); });
	    }
	});
	
	// Search function
	var to = false;
	$('#jstree_genesets_searchbox').keyup(function () {
	    if(to) { clearTimeout(to); }
	    to = setTimeout(function () {
	    	var v = $('#jstree_genesets_searchbox').val();
	        
	    	// This does not work:
//	        // Only add clear search button when something is in box 
//			if ($("#jstree_search_input").val() !== "") {
//				$("#jstree_genesets_empty_search").css("display", "none");
//			}

	    	$('#jstree_genesets').jstree(true).search(v);
	    }, 250);
	    
	});
	
	// This does not work yet:
	// When button is clicked, empty search
//    $('#jstree_genesets_empty_search').click(function() { 
//    	var v = "";
//        $("#jstree_genesets_searchbox").val(v);
//
//    	$('#jstree_genesets').jstree(true).search(v);
//		
//		// Hide button
//        $("#jstree_genesets_empty_search").css("display", "none");
//    });    
}	


var updateGenesetList = function() {
    "use strict";

    // push all the genes in the gene_list onto a list
    var genesetList = $('#geneset_list').val();

    // placeholder gene sets, necessary until the real selection method is implemented
    //var selectedGenesets = ["MORF_ATRX", "MORF_ATOX1"];
    
    // Create list of objects with selected gene sets
    var selectedGenesets = [];

	// After we get the selection, we can remove the tree object
	$('#jstree_genesets').jstree('destroy');
	
	// Loop over the selected checkboxes
	for (var i = 0; i < selectedBoxes.length; i++ ){
		
		// Check if selected checkbox is not a node
		if (selectedBoxes[i].original.geneset) {
			var boxName = selectedBoxes[i].original.name;
			
			// Select a geneset only once
			if (selectedGenesets.indexOf(boxName) == -1) {
				selectedGenesets.push(boxName);
			}
		}
	}
	
    // if gene_list is currently empty put all the checked geneset genes into it.
    if (genesetList === "") {
        genesetList = [];
        $.each(selectedGenesets, function(index, value) {     // don't select the Select All checkbox
            genesetList.push(value);
        });
        genesetList = genesetList.join(" ");
    }

    else {
        // look for the selected genesets in gene_list
        // if they're not there, append them
        $.each(selectedGenesets, function(index, value) {
            var checked = value;

            if ( genesetList.search(new RegExp(checked, "i")) === -1 ) {
                genesetList = $.trim(genesetList);
                checked = " " + checked;
                genesetList += checked;
            }
        });
    }
    
    // remove spaces around gene_list
    genesetList = $.trim(genesetList);
    
    // replace 2 or more spaces in a row by 1 space
    genesetList = genesetList.replace(/\s{1,}/, " ");
    
    // Update the gene set list in the query box
    $('#geneset_list').val(genesetList);
};


// todo: refactor this and put it in with other init functions in step3.json
$(document).ready( function () {
    "use strict";
    initGenesetDialogue();
});
