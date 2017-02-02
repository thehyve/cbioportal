/*
 * Copyright (c) 2015 Memorial Sloan-Kettering Cancer Center.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS
 * FOR A PARTICULAR PURPOSE. The software and documentation provided hereunder
 * is on an "as is" basis, and Memorial Sloan-Kettering Cancer Center has no
 * obligations to provide maintenance, support, updates, enhancements or
 * modifications. In no event shall Memorial Sloan-Kettering Cancer Center be
 * liable to any party for direct, indirect, special, incidental or
 * consequential damages, including lost profits, arising out of the use of this
 * software and its documentation, even if Memorial Sloan-Kettering Cancer
 * Center has been advised of the possibility of such damage.
 */

/*
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


/**
 * Render the Co-expression view using dataTable Jquery Plugin,
 * along side a plot for selected row
 *
 * User: yichao
 * Date: 12/5/13
 */

var CoExpView = (function() {
	
    //Pre settings for every sub tab instance
    var Prefix = {
            divPrefix: "coexp_",
            loadingImgPrefix: "coexp_loading_img_",
            tableDivPrefix: "coexp_table_div_",
            tablePrefix: "coexp_table_",
            plotPrefix: "coexp_plot_"
        },
        dim = {
            coexp_table_width: "380px",
            coexp_plots_width: "750px"
        },
        has_mutation_data = false;
    //Containers    
    var profileList = []; //Profile Lists for all queried genes

    //Sub tabs
    var Tabs = (function() {

        function appendTabsContent() {
        	//Add a sub-tab for every queried genetic entity
        	var geneIds = [];
        	if (window.QuerySession.getQueryGenes() != null) {
        		geneIds = window.QuerySession.getQueryGenes();
        	}
        	var genesetIds = [];
        	if (window.QuerySession.getQueryGenesets() != null) {
        		genesetIds = window.QuerySession.getQueryGenesets();
        	}
            var geneEntityIds = geneIds.concat(genesetIds); 
            $.each(geneEntityIds, function(index, value) {
                $("#coexp-tabs-list").append("<li><a href='#" + Prefix.divPrefix + cbio.util.safeProperty(value) + 
                  "' class='coexp-tabs-ref'><span>" + value + "</span></a></li>");
            });
        }

        function appendLoadingImgs() {
        	var geneIds = [];
        	if (window.QuerySession.getQueryGenes() != null) {
        		geneIds = window.QuerySession.getQueryGenes();
        	}
        	var genesetIds = [];
        	if (window.QuerySession.getQueryGenesets() != null) {
        		genesetIds = window.QuerySession.getQueryGenesets();
        	}
            var geneEntityIds = geneIds.concat(genesetIds); 
            $.each(geneEntityIds, function(index, value) {
                $("#coexp-tabs-content").append("<div id='" + Prefix.divPrefix + cbio.util.safeProperty(value) + "'>" +
                    "<div id='" + Prefix.loadingImgPrefix + cbio.util.safeProperty(value) + "'>" +
                    "<table><tr><td><img style='padding:20px;' src='images/ajax-loader.gif' alt='loading' /></td>" +
                    "<td>Calculating and rendering... (this may take up to 1 minute)</td></tr></table>" +
                    "</div></div>");
            });
        }

        function generateTabs() {
            $("#coexp-tabs").tabs();
            $("#coexp-tabs").tabs('paging', {tabsPerPage: 10, follow: true, cycle: false});
            $("#coexp-tabs").tabs("option", "active", 0);
            $(window).trigger("resize");
        }

        function bindListenerToTabs() {
            $("#coexp-tabs").on("tabsactivate", function(event, ui) {
                var _genetic_entity = ui.newTab.text();
                var _genetic_entity_type = null;
                if (window.QuerySession.getQueryGenes() != null) {
	                if ((window.QuerySession.getQueryGenes()).indexOf(_genetic_entity) !== -1) {
	                	_genetic_entity_type = "GENE";
	                }
                } else if (window.QuerySession.getQueryGenesets() != null) {
	                if ((window.QuerySession.getQueryGenesets()).indexOf(_genetic_entity) !== -1) {
	                	_genetic_entity_type = "GENESET";
	                }
                }
                var coExpSubTabView = new CoExpSubTabView();
                coExpSubTabView.init(_genetic_entity, _genetic_entity_type);
            });
        }

        return {
            appendTabsContent: appendTabsContent,
            appendLoadingImgs: appendLoadingImgs,
            generateTabs: generateTabs,
            bindListenerToTabs: bindListenerToTabs
        };

    }());

    var ProfileSelector = (function() {

        function filterProfiles(_profileList) {
            $.each(_profileList, function(i, obj) {
                if (obj["GENETIC_ALTERATION_TYPE"] === "MRNA_EXPRESSION") {
                    if (obj["STABLE_ID"].toLowerCase().indexOf("zscores") !== -1) {
                        if (obj["STABLE_ID"].toLowerCase().indexOf("merged_median_zscores") !== -1) {
                            profileList.push(obj);
                        }
                    } else {
                        profileList.push(obj);
                    }
                } else if (obj["GENETIC_ALTERATION_TYPE"] === "MUTATION_EXTENDED") {
                	if (window.QuerySession.getQueryGenes() != null) {
                		has_mutation_data = true;
                	}
                }
            });
            //swap the rna seq profile to the top
            $.each(profileList, function(i, obj) {
                if (obj["STABLE_ID"].toLowerCase().indexOf("rna_seq") !== -1) {
                    cbio.util.swapElement(profileList, i, 0);
                }
            });
        }

        function drawProfileSelector() {
            $("#coexp-profile-selector-dropdown").append(
                "Gene Expression Data Set " + 
                "<select id='coexp-profile-selector'></select>");
            $.each(profileList, function(index, value) {
                $("#coexp-profile-selector").append(
                    "<option value='" + value["STABLE_ID"] + "'>" +
                    value["NAME"] + "</option>"
                );            
            });
        }

        function bindListener() {
            $("#coexp-profile-selector").change(function() {
            	var geneIds = [];
            	if (window.QuerySession.getQueryGenes() != null) {
            		geneIds = window.QuerySession.getQueryGenes();
            	}
            	var genesetIds = [];
            	if (window.QuerySession.getQueryGenesets() != null) {
            		genesetIds = window.QuerySession.getQueryGenesets();
            	}
                var geneEntityIds = geneIds.concat(genesetIds);
                
                $.each(geneEntityIds, function(index, value) {
                    //Destroy all the sub-view instances
                    var element =  document.getElementById(Prefix.tableDivPrefix + cbio.util.safeProperty(value));
                    if (typeof(element) !== 'undefined' && element !== null) { 
                        element.parentNode.removeChild(element); //destroy all the existing instances
                    }
                    element =  document.getElementById(Prefix.plotPrefix + cbio.util.safeProperty(value));
                    if (typeof(element) !== 'undefined' && element !== null) { 
                        element.parentNode.removeChild(element); //destroy all the existing instances
                    }   
                    //Empty all the sub divs
                    $("#" + Prefix.tableDivPrefix + cbio.util.safeProperty(value)).empty();
                    $("#" + Prefix.plotsPreFix + cbio.util.safeProperty(value)).empty();
                    $("#" + Prefix.loadingImgPrefix + cbio.util.safeProperty(value)).empty();
                    //Add back loading imgs
                    $("#" + Prefix.loadingImgPrefix + cbio.util.safeProperty(value)).append(
                        "<table><tr><td><img style='padding:20px;' src='images/ajax-loader.gif' alt='loading' /></td>" +
                        "<td>Calculating and rendering may take up to 1 minute.</td></tr></table>" +
                        "</div>");
                });
                //Re-draw the currently selected sub-tab view
                var curTabIndex = $("#coexp-tabs").tabs("option", "active");
                var _genetic_entity_type = null;
                if ((window.QuerySession.getQueryGenes()).indexOf(geneEntityIds[curTabIndex]) !== -1) {
                	_genetic_entity_type = "GENE";
                } else if ((window.QuerySession.getQueryGenesets()).indexOf(geneEntityIds[curTabIndex]) !== -1) {
                	_genetic_entity_type = "GENESET";
                }
                var coExpSubTabView = new CoExpSubTabView();
                coExpSubTabView.init(geneEntityIds[curTabIndex], _genetic_entity_type);
            });
        }

        return {
            init: function(_profileList) {
                filterProfiles(_profileList);
                drawProfileSelector();
                bindListener();
            }
        };

    }()); //Closing Profile Selector


    //Instance of each sub tab
    var CoExpSubTabView = function() {

        var Names = {
                divId: "", //Id for the div of the single query gene (both coexp table and plot)
                loadingImgId: "", //Id for ajax loading img
                tableId: "", //Id for the co-expression table
                tableDivId: "", //Id for the div of the co-expression table
                plotsId: "" //Id for the plots on the right
            },
            geneEntityId = "", //Genetic entity of this sub tab instance
            geneEntityType = "", //Type of genetic entity (gene or gene set)
            geneticEntityProfile = ""; //Profile of the genetic entity of the sub-tab
            coexpTableArr = [], //Data array for the datatable
            coExpTableInstance = "",
            entityProfileMap = {},
            geneArr = [], //Genes correlated with the queried genetic entity
            geneSetArr = [], //Gene sets correlated with the queried genetic entity
        	genesRetrieved = false,
        	geneSetsRetrieved = false;

        var CoExpTable = function() {

            function configTable() {
                //Draw out the markdown of the datatable
                $("#" + Names.tableId).append(
                    "<thead style='font-size:70%;' >" +
                    "<tr>" + 
                    "<th>Correlated Gene</th>" +
                    "<th>Pearson's Correlation</th>" +
                    "<th>Spearman's Correlation</th>" +
                    "</tr>" +
                    "</thead><tbody></tbody>"
                );

                //Configure the datatable with jquery
                coExpTableInstance = $("#" + Names.tableId).dataTable({
                    "sDom": '<"H"f<"coexp-table-filter-pearson">>t<"F"i<"datatable-paging"p>>',
                    "bPaginate": true,
                    "sPaginationType": "two_button",
                    "bInfo": true,
                    "bJQueryUI": true,
                    "bAutoWidth": false,
                    "aaData" : coexpTableArr,
                    "aaSorting": [[1, 'desc']],
                    "aoColumnDefs": [
                        {
                            "bSearchable": true,
                            "aTargets": [ 0 ],
                            "sWidth": "56%"
                        },
                        {
                            "sType": 'coexp-absolute-value',
                            //TODO: should be disabled; this is just a quick fix, otherwise the fnfilter would work on this column
                            //"bSearchable": false, 
                            "bSearchable": true, 
                            "aTargets": [ 1 ],
                            "sWidth": "22%"
                        },
                        {
                            "sType": 'coexp-absolute-value',
                            "bSearchable": false,
                            "aTargets": [ 2 ],
                            "sWidth": "22%"
                        }
                    ],
                    "sScrollY": "600px",
                    "bScrollCollapse": true,
                    //iDisplayLength: coexp_table_arr.length,
                    "oLanguage": {
                        "sSearch": "Search Genetic Entity"
                    },
                    "bDeferRender": true,
                    "iDisplayLength": 30,
                    "fnRowCallback": function(nRow, aData) {
                        $('td:eq(0)', nRow).css("font-weight", "bold");
                        $('td:eq(2)', nRow).css("font-weight", "bold");
                        if (aData[1] > 0) {
                            $('td:eq(2)', nRow).css("color", "#3B7C3B");
                        } else {
                            $('td:eq(2)', nRow).css("color", "#B40404");
                        }
                        if (aData[2] > 0) {
                            $('td:eq(3)', nRow).css("color", "#3B7C3B");
                        } else {
                            $('td:eq(3)', nRow).css("color", "#B40404");
                        }
                    },
                    "fnInfoCallback": function( oSettings, iStart, iEnd, iMax, iTotal, sPre ) {
                        if (iTotal === iMax) {
                            return iStart +" to "+ iEnd + " of " + iTotal;
                        } else {
                            return iStart + " to " + iEnd + " of " + iTotal + " (filtered from " + iMax + " total)";
                        }
                    }
                });  
            }

            function attachDownloadResultButton() {
                //Append download result button at the bottom of the table
                $("#" + Names.tableDivId).append("<button id='download_button' style='float:right;'>Download Results</button>");
                document.getElementById("download_button").onclick = function() {
	            	//Function that creates the document to download the table in the data
	            	var tableData=coExpTableInstance.fnGetData();
	            	//Create a string with the full results, containing also the header
	            	fullResultStr = ("Genetic Entity Symbol\tPearson Score\tSpearman Score\n");
	            	tableData.forEach(function(geneticEntity) {
	            		_row = "";
	            		geneticEntity.forEach(function(value) {
	            			_row += value + "\t";
	            		});
	            		//Replace last '\t' for '\n'
	            		_row = _row.slice(0,-1);
	            		_row += "\n";
	            		//Add the genetic entity to the final string
	            		fullResultStr += _row;
	            	});
	            	//construct file name
	            	_comparedGeneticEntities ="";
	            	if ($("#gene_checkbox"+cbio.util.safeProperty(geneEntityId)).prop('checked')) {
	            		if ($("#geneset_checkbox"+cbio.util.safeProperty(geneEntityId)).prop('checked')) {
	            			_comparedGeneticEntities = "genes_and_gene_sets";
	            		} else {
	            			_comparedGeneticEntities +="genes";
	            		}
	            	} else {
	            		if ($("#geneset_checkbox"+cbio.util.safeProperty(geneEntityId)).prop('checked')) {
	                		_comparedGeneticEntities += "gene_sets";
	                	}
	            	}
	                _fileName = "coexpression_" + geneEntityId + "_(" +
	                geneticEntityProfile.replace(/\\s+/g, "_") + ")_vs_" +
	                _comparedGeneticEntities + ".txt";
	                //Download the file
	            	cbio.download.initDownload(fullResultStr, {filename: _fileName, contentType: 'text/plain', preProcess: null});
                }
            }

            function attachPearsonFilter() { 
                //Add drop down filter for positive/negative pearson display
                $("#" + Names.tableDivId).find('.coexp-table-filter-pearson').append(
                    "<select id='coexp-table-select-" + cbio.util.safeProperty(geneEntityId) + "' style='width: 230px; margin-left: 5px;'>" +
                    "<option value='all'>Show All</option>" +
                    "<option value='positivePearson'>Show Only Positively Correlated</option>" +
                    "<option value='negativePearson'>Show Only Negatively Correlated</option>" +
                    "</select>");
                $("select#coexp-table-select-" + cbio.util.safeProperty(geneEntityId)).change(function () {
                    if ($(this).val() === "negativePearson") {
                        coExpTableInstance.fnFilter("-", 1, false);
                    } else if ($(this).val() === "positivePearson") {
                        coExpTableInstance.fnFilter('^[0-9]*\.[0-9]*$', 1, true);
                    } else if ($(this).val() === "all") {
                        coExpTableInstance.fnFilter("", 1);
                    }
                });
            }
            
            function attachGeneticEntityButtons() { 
	        	//Create buttons to select genes and gene sets
	        	$("#" + Names.tableDivId).find('.coexp-table-filter-pearson').append(
	        			"<br><input type='checkbox' id='gene_checkbox"+cbio.util.safeProperty(geneEntityId)+"' checked><label for='gene_checkbox'>Genes</label>" +
	        			"<input type='checkbox' id='geneset_checkbox"+cbio.util.safeProperty(geneEntityId)+"' ><label for='geneset_checkbox'>Gene sets</label>"
	        		);
	        	$("input#gene_checkbox"+cbio.util.safeProperty(geneEntityId)).change(function() {
		        		if ($("#gene_checkbox"+cbio.util.safeProperty(geneEntityId)).prop('checked')) {
				        		if (genesRetrieved == true){ //Add the genes into the coexpTable again if they are not there
				        			if (geneArr.length >= 1) {
			        					coExpTableInstance.fnAddData(geneArr);
			        				} else {
    		            				$("center").append("<div id='no_genes' data-notify='container' class='col-xs-11 col-sm-3 alert alert-warning geneValidationNotification animated fadeInDown' role='alert' data-notify-position='top-right' style='display: inline-block; margin: 0px auto; position: fixed; transition: all 0.5s ease-in-out; z-index: 1031; top: 20px; right: 20px; animation-iteration-count: 1;'><button type='button' style='display: none' aria-hidden='true' class='close' data-notify='dismiss'>Ã—</button><span data-notify='icon'></span> <span data-notify='title'></span> <span data-notify='message'><span id='AD' class='close' data-notify='dismiss' data-hasqtip='35'>No genes with a correlation higher than 0.3 or lower than -0.3 were found</span></span></div>");
    		            			}
		        				} else { //Genes not retrieved, so append an error message
		        					$("center").append("<div id='no_genes' data-notify='container' class='col-xs-11 col-sm-3 alert alert-warning geneValidationNotification animated fadeInDown' role='alert' data-notify-position='top-right' style='display: inline-block; margin: 0px auto; position: fixed; transition: all 0.5s ease-in-out; z-index: 1031; top: 20px; right: 20px; animation-iteration-count: 1;'><button type='button' style='display: none' aria-hidden='true' class='close' data-notify='dismiss'>Ã—</button><span data-notify='icon'></span> <span data-notify='title'></span> <span data-notify='message'><span id='AD' class='close' data-notify='dismiss' data-hasqtip='35'>No genes with a correlation higher than 0.3 or lower than -0.3 were found</span></span></div>");
    		            			
		        				}
		        			} else { //Button not checked, clear the whole table
		        				coExpTableInstance.fnClearTable();
		        				if ($("#geneset_checkbox"+cbio.util.safeProperty(geneEntityId)).prop('checked')) { //If the gene sets box is checked, keep the gene sets
		        					if (geneSetArr.length >= 1) {
			        					coExpTableInstance.fnAddData(geneSetArr);
			        				}
		        				}
		        			}
		        		});
	        	$("input#geneset_checkbox"+cbio.util.safeProperty(geneEntityId)).change(function() {
		        	    if ($("#geneset_checkbox"+cbio.util.safeProperty(geneEntityId)).prop('checked')) {
		        	    	if (geneSetsRetrieved == false) { //If it is the first time that the checkbox is checked, retrieve the data
        		            	var paramsGetCoExpData = {
        		                        cancer_study_id: window.QuerySession.getCancerStudyIds()[0],
        		                        genetic_entity: geneEntityId,
        		                        genetic_entity_profile_id: geneSetProfile,
        		                        genetic_entity_profile_id: geneticEntityProfile,
        		                        correlated_entities_to_find: "GENESET",
        		                        correlated_entities_profile_id: geneSetProfile,
        		                        genetic_entity_type: geneEntityType, 
        		                        case_set_id: window.QuerySession.getCaseSetId(),
        		                        case_ids_key: window.QuerySession.getCaseIdsKey(),
        		            	};
        		            	$.post(
        		            		"getCoExp.do", 
        		            		paramsGetCoExpData, 
        		            		function(result) {
        		            			convertData(result, "GENESET");
        		            			if (geneSetArr.length >= 1) { //Only add gene sets if we have them
        		            				coExpTableInstance.fnAddData(geneSetArr);
        		            			} else {
        		            				$("center").append("<div id='no_genesets' data-notify='container' class='col-xs-11 col-sm-3 alert alert-warning geneValidationNotification animated fadeInDown' role='alert' data-notify-position='top-right' style='display: inline-block; margin: 0px auto; position: fixed; transition: all 0.5s ease-in-out; z-index: 1031; top: 20px; right: 20px; animation-iteration-count: 1;'><button type='button' style='display: none' aria-hidden='true' class='close' data-notify='dismiss'>Ã—</button><span data-notify='icon'></span> <span data-notify='title'></span> <span data-notify='message'><span id='AD' class='close' data-notify='dismiss' data-hasqtip='35'>No gene sets with a correlation higher than 0.3 or lower than -0.3 were found</span></span></div>");
        		            			}
        		            			geneSetsRetrieved = true;
        		            		},
        		            		"json"
        		            	);
		        				} else { //Add the gene sets into the coexpTable again
		        					if (geneSetArr.length >= 1) { //Only add gene sets if we have them
    		            				coExpTableInstance.fnAddData(geneSetArr);
    		            			} else {
    		            				$("center").append("<div id='no_genesets' data-notify='container' class='col-xs-11 col-sm-3 alert alert-warning geneValidationNotification animated fadeInDown' role='alert' data-notify-position='top-right' style='display: inline-block; margin: 0px auto; position: fixed; transition: all 0.5s ease-in-out; z-index: 1031; top: 20px; right: 20px; animation-iteration-count: 1;'><button type='button' style='display: none' aria-hidden='true' class='close' data-notify='dismiss'>Ã—</button><span data-notify='icon'></span> <span data-notify='title'></span> <span data-notify='message'><span id='AD' class='close' data-notify='dismiss' data-hasqtip='35'>No gene sets with a correlation higher than 0.3 or lower than -0.3 were found</span></span></div>");
    		            			}
		        				} 
		        			} else { //Button not checked, clear the whole table
		                		coExpTableInstance.fnClearTable();
		                		if (geneSetArr.length < 1) {
		                			document.getElementById("no_genesets").remove();
		                		}
		                		if ($("#gene_checkbox"+cbio.util.safeProperty(geneEntityId)).prop('checked')) { //If the gene box is checked, keep the genes
		                			if (geneArr.length >= 1) {
			        					coExpTableInstance.fnAddData(geneArr);
			        				} 
		                		}
		        			}
		        		});
            }

            function attachRowListener() {
                $("#" + Names.tableId + " tbody tr").live('click', function (event) {
                    //Highlight selected row
                    $(coExpTableInstance.fnSettings().aoData).each(function (){
                        $(this.nTr).removeClass('row_selected');
                    });
                    $(event.target.parentNode).addClass('row_selected');
                    //Get the gene name of the selected row
                    var aData = coExpTableInstance.fnGetData(this);
                    if (null !== aData) {
                        $("#" + Names.plotId).empty();
                        $("#" + Names.plotId).append("<img style='padding:220px;' src='images/ajax-loader.gif' alt='loading' />");
                        var coexpPlots = new CoexpPlots();
                        var profile1Id = null;
                        if (geneEntityType == "GENE") {
                        	profile1Id = $("#coexp-profile-selector :selected").val();
                        } else if (geneEntityType == "GENESET") {
                        	profile1Id = geneSetProfile;
                        }
                        var entity1Id = geneEntityId;
                        var entity2Id = aData[0];
                        var profile2Id = entityProfileMap[entity2Id];
                        coexpPlots.init(Names.plotId, entity1Id, entity2Id, aData[1], aData[2], profile1Id, profile2Id);
                    }
                });
            }

            function initTable() {
                //Init with selecting the first row
                $('#' + Names.tableId + ' tbody tr:eq(0)').click();
                $('#' + Names.tableId + ' tbody tr:eq(0)').addClass("row_selected");
            }

            //Overwrite some datatable function for custom filtering
            function overWriteFilters() {
                jQuery.fn.dataTableExt.oSort['coexp-absolute-value-desc'] = function(a,b) {
                    if (Math.abs(a) > Math.abs(b)) return -1;
                    else if (Math.abs(a) < Math.abs(b)) return 1;
                    else return 0;
                };
                jQuery.fn.dataTableExt.oSort['coexp-absolute-value-asc'] = function(a,b) {
                    if (Math.abs(a) > Math.abs(b)) return 1;
                    else if (Math.abs(a) < Math.abs(b)) return -1;
                    else return 0;
                };
            }  

            function convertData(_result, correlatedEntitiesToFind) {
                //Convert the format of the callback result to fit datatable, and fill the geneArr or geneSetArr
                coexpTableArr = [];
                $.each(_result, function(i, obj) {
                    var tmp_arr = [];
                    tmp_arr.push(obj.gene);
                    tmp_arr.push(obj.pearson.toFixed(2));
                    tmp_arr.push(obj.spearman.toFixed(2));
                    entityProfileMap[obj.gene] = obj.profileId;
                    coexpTableArr.push(tmp_arr);
                    if (correlatedEntitiesToFind == "GENE") {
                    	geneArr.push(tmp_arr);
                    } else if (correlatedEntitiesToFind == "GENESET") {
                    	geneSetArr.push(tmp_arr);
                    }
                });
            }

            function getCoExpDataCallBack(_result, correlatedEntitiesToFind, geneticEntityId, _geneticEntityType) {
                //Hide the loading img
                $("#" + Names.loadingImgId).empty();
                if (_result.length === 0) { //If there are no genes available, call the gene sets
	        		var paramsGetCoExpData = {
	                        cancer_study_id: window.QuerySession.getCancerStudyIds()[0],
	                        genetic_entity: geneticEntityId,
	                        genetic_entity_profile_id: geneticEntityProfile,
	                        correlated_entities_to_find: "GENESET",
	                        correlated_entities_profile_id: geneSetProfile,
	                        genetic_entity_type: _geneticEntityType, 
	                        case_set_id: window.QuerySession.getCaseSetId(),
	                        case_ids_key: window.QuerySession.getCaseIdsKey(),
	                   };
	                   $.post(
	                       "getCoExp.do", 
	                       paramsGetCoExpData, 
	                       function(result) {
	                    	   if (result.length === 0) { 
	                    		   $("#" + Names.tableDivId).append("There are no genes or gene sets paired with the queried genetic entity with a Pearson or Spearman score > 0.3 or < -0.3.");
	                    	   } else {
	                        	   convertData(result, correlatedEntitiesToFind);
	                        	   overWriteFilters(); 
	                               configTable();
	                               attachDownloadResultButton();
	                               attachPearsonFilter();
	                               attachGeneticEntityButtons();
	                               attachRowListener();
	                               initTable();
	                    	   }
	                    	   geneSetsRetrieved = true;
	                      },
	                      "json"
	                   );
                } else {
                	convertData(_result, correlatedEntitiesToFind);
                	overWriteFilters(); 
                    configTable();
                    attachDownloadResultButton();
                    attachPearsonFilter();
                    attachGeneticEntityButtons();
                    attachRowListener();
                    initTable();
                }
            }

            return {
                init: function(_geneticEntityId, _geneticEntityType) {
                    //Getting co-exp data (for currently selected gene/profile) from servlet
                    $("#" + Names.plotId).empty();
                    //Determine the profile for the correlated entities
                    if (_geneticEntityType == "GENE") {
                    	geneticEntityProfile = $("#coexp-profile-selector :selected").val();
                    } else if (_geneticEntityType == "GENESET") {
                    	geneticEntityProfile = geneSetProfile;
                    }
                    //Make the first call only with genes (we have always genes in our query)
                    var paramsGetCoExpData = {
                         cancer_study_id: window.QuerySession.getCancerStudyIds()[0],
                         genetic_entity: _geneticEntityId,
                         genetic_entity_profile_id: geneticEntityProfile,
                         correlated_entities_to_find: "GENE",
                         correlated_entities_profile_id: $("#coexp-profile-selector :selected").val(),
                         genetic_entity_type: _geneticEntityType, 
                         case_set_id: window.QuerySession.getCaseSetId(),
                         case_ids_key: window.QuerySession.getCaseIdsKey(),
                    };
                    $.post(
                        "getCoExp.do", 
                        paramsGetCoExpData, 
                        function(result) {
                            getCoExpDataCallBack(result, "GENE", _geneticEntityId, _geneticEntityType);
                            genesRetrieved = true;
                       },
                       "json"
                    );
                }
            };          
            
        }; //Closing CoExpTable

        function assembleNames() {
            //figure out div id
            var safeGeneId = cbio.util.safeProperty(geneEntityId);
            Names.divId = Prefix.divPrefix + safeGeneId;
            Names.loadingImgId = Prefix.loadingImgPrefix + safeGeneId;
            Names.tableId = Prefix.tablePrefix + safeGeneId + jQuery.now();
            Names.tableDivId = Prefix.tableDivPrefix + safeGeneId;
            Names.plotId = Prefix.plotPrefix + safeGeneId;
        }

        function drawLayout() {
            //Configure the layout(div) of table and plots
            $("#" + Names.divId).append(
                "<table>" +
                "<tr>" +
                "<td width='" + dim.coexp_table_width + "' valign='top'>" + 
                "<div id='" + Names.tableDivId + "'></div></td>" +
                "<td width='" + dim.coexp_plots_width + "' valign='top'>" + 
                "<div id='" + Names.plotId + "'></div></td>" +
                "</tr>" +
                "</table>");
            $("#" + Names.tableDivId).addClass("coexp-table");
            $("#" + Names.tableDivId).addClass("coexp-plots");
            $("#" + Names.tableDivId).append(
                "<table id='" + Names.tableId + "' class='display coexp_datatable_" + geneEntityId + "' cellpadding='0' cellspacing='0' border='0'></table>");
        }
        
        return {
            init: function(_geneEntityId, _geneEntityType) {
                //Set the attributes of the sub-view instance
                geneEntityId = _geneEntityId;
                geneEntityType = _geneEntityType;
                //TODO: Just a quick fix for the sub-tab collapse bug
                $(window).trigger("resize");
                //Get the div id of the right sub-tab
                var element = $(".coexp_datatable_" + cbio.util.safeProperty(_geneEntityId));
                if (element.length === 0) { //Avoid duplication (see if the subtab instance already exists)
                    assembleNames();
                    drawLayout();
                    var coExpTable = new CoExpTable();
                    coExpTable.init(geneEntityId, geneEntityType);
                }
            },
        	app: function(_geneEntityId, _geneEntityType) {
                //Set the attributes of the sub-view instance
                geneEntityId = _geneEntityId;
                geneEntityType = _geneEntityType;
                //TODO: Just a quick fix for the sub-tab collapse bug
                $(window).trigger("resize");
                //Get the div id of the right sub-tab
                var element = $(".coexp_datatable_" + cbio.util.safeProperty(_geneEntityId));
                assembleNames();
                drawLayout();
                var coExpTable = new CoExpTable();
                coExpTable.init(geneEntityId, geneEntityType);
            }
        };

    };   //Closing coExpSubTabView

    function getGeneticProfileCallback(result) {
        var _genes = window.QuerySession.getQueryGenes(); //TODO: support only geneset queries (without genes)
        //Init Profile selector
        var _profile_list = {};
        _.each(_genes, function(_gene) {
            _profile_list = _.extend(_profile_list, result[_gene]);
        });
        ProfileSelector.init(_profile_list);
        if (profileList.length === 1) {
            $("#coexp-profile-selector-dropdown").hide();
        }
        var coExpSubTabView = new CoExpSubTabView();
        coExpSubTabView.init(_genes[0], "GENE");
    }
    
    function getGeneSetsProfileCallback(result) {
        var _genesets = window.QuerySession.getQueryGenesets();
        //Set gene set profile variable
    	geneSetProfile = '';
        //Retrieve the gene set profile
        var _geneSetProfile_list = {};
        _.each(_genesets, function(_geneset) {
        	_geneSetProfile_list = _.extend(_geneSetProfile_list, result[_geneset]);
        });
        //Select the profiles that are GSVA scores and discard the profiles with GSVA P-values
        var _geneSetScoresProfileList = [];
        $.each(_geneSetProfile_list, function(i, obj) {
            if (obj["DATATYPE"] === "GSVA-SCORE") {
            	_geneSetScoresProfileList.push(obj);
            }
        });
        if (_geneSetScoresProfileList.length === 1) { //Assumption: only one GSVA-score per study
        	$.each(_geneSetScoresProfileList, function(i, obj) {
        		geneSetProfile = obj["STABLE_ID"];
        	});
        } else  if (_geneSetScoresProfileList.length === 0) {
        	throw new Error("There are no profiles with GSVA-scores available for this study");
        } else {
        	throw new Error("This study contains more than one GSVA-scores profile");
        }
        if (window.QuerySession.getQueryGenes() == null) {
        	var coExpSubTabView = new CoExpSubTabView();
            coExpSubTabView.init(_genesets[0], "GENESET");
        }
    }

    return {
        init: function() {
            //Init Tabs
            Tabs.appendTabsContent();
            Tabs.appendLoadingImgs();
            Tabs.generateTabs();
            Tabs.bindListenerToTabs();
            //Get all the genetic profiles with data available 
            if (window.QuerySession.getQueryGenes() != null) {
	            var paramsGetProfiles = {
	                cancer_study_id: window.QuerySession.getCancerStudyIds()[0],
	                case_set_id: window.QuerySession.getCaseSetId(),
	                case_ids_key: window.QuerySession.getCaseIdsKey(),
	                genetic_entity_list: window.QuerySession.getQueryGenes().join(" "),
	                genetic_entity_type: "GENE"
	            };
	            $.post("getGeneticProfile.json", paramsGetProfiles, getGeneticProfileCallback, "json");
            }
            //Get the gene set profiles
            if (window.QuerySession.getQueryGenesets() != null) {
	            var paramsGetGeneSetProfiles = {
	                cancer_study_id: window.QuerySession.getCancerStudyIds()[0],
	                case_set_id: window.QuerySession.getCaseSetId(),
	                case_ids_key: window.QuerySession.getCaseIdsKey(),
	                genetic_entity_list: window.QuerySession.getQueryGenesets().join(" "),
	                genetic_entity_type: "GENESET"
	           };
	           $.post("getGeneticProfile.json", paramsGetGeneSetProfiles, getGeneSetsProfileCallback, "json");
	        }
        },
        has_mutation_data: function() {
            return has_mutation_data;
        }
    };

}());    //Closing CoExpView
