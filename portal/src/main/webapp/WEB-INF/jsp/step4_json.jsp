<%--
 - Copyright (c) 2015 Memorial Sloan-Kettering Cancer Center.
 -
 - This library is distributed in the hope that it will be useful, but WITHOUT
 - ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS
 - FOR A PARTICULAR PURPOSE. The software and documentation provided hereunder
 - is on an "as is" basis, and Memorial Sloan-Kettering Cancer Center has no
 - obligations to provide maintenance, support, updates, enhancements or
 - modifications. In no event shall Memorial Sloan-Kettering Cancer Center be
 - liable to any party for direct, indirect, special, incidental or
 - consequential damages, including lost profits, arising out of the use of this
 - software and its documentation, even if Memorial Sloan-Kettering Cancer
 - Center has been advised of the possibility of such damage.
 --%>

<%--
 - This file is part of cBioPortal.
 -
 - cBioPortal is free software: you can redistribute it and/or modify
 - it under the terms of the GNU Affero General Public License as
 - published by the Free Software Foundation, either version 3 of the
 - License.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 - GNU Affero General Public License for more details.
 -
 - You should have received a copy of the GNU Affero General Public License
 - along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>

<%@ page import="org.mskcc.cbio.portal.servlet.QueryBuilder" %>
<%
    String step4ErrorMsg = (String) request.getAttribute(QueryBuilder.STEP4_ERROR_MSG);
%>

<div class="query_step_section">
    <span class="step_header">Select Genes:</span>

    <script language="javascript" type="text/javascript">

    function popitup(url) {
        newwindow=window.open(url,'OncoSpecLangInstructions','height=1000,width=1000,left=400,top=0,scrollbars=yes');
        if (window.focus) {newwindow.focus()}
        return false;
    }
    </script>

    <% if (localTabIndex.equals(QueryBuilder.TAB_VISUALIZE)) { %>
        <% out.println("<span style='font-size:120%; color:black'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='onco_query_lang_desc.jsp' onclick='return popitup('onco_query_lang_desc.jsp')'>Advanced: Onco Query Language (OQL)</a></span>"); %>
    <% } %>
    
    <%
	// Output step 4 form validation error
	if (step4ErrorMsg != null) {
	    out.println("<div class='ui-state-error ui-corner-all' style='margin-top:4px; padding:5px;'>"
	            + "<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span>"
	            + "<strong>" + step4ErrorMsg + "</strong>");
	    customCaseListStyle = "block";
	}
	%>

    <script type="text/javascript" src="js/src/genesets.js?<%=GlobalProperties.getAppVersion()%>"></script>
    <div style='padding-top:10px;padding-bottom:5px;'>
        <select id="select_gene_set" name="<%= QueryBuilder.GENE_SET_CHOICE %>" title="Select Gene Set"></select>
    </div>
    
    <script type="text/javascript">
  	//Displays the popup for the geneset hierarchy
    function popupGenesetHierarchy() {
        "use strict";

        // open the dialog box
        $('#geneset_dialog').dialog('open');

        // show everything but loader image
        $('#geneset_dialog').children().show();
        $('#geneset_dialog #loader-img').hide();

    	// Retrieve cancer study
        var cancerStudyId = $("#main_form").find("#select_single_study").val();
        var cancer_study = window.metaDataJson.cancer_studies[cancerStudyId];
        
        var genesetGeneticProfile;
        
        // Find the genomic profile id of the gene set profile
        for (var i = 0; i < cancer_study.genomic_profiles.length; i++) {
        	if (cancer_study.genomic_profiles[i].alteration_type == "GENESET_SCORE") {
        		genesetGeneticProfile = cancer_study.genomic_profiles[i].id;
        	}
        }
        
        initializeGenesetJstree(genesetGeneticProfile, $('#geneset_dialog #loader-img'));

        return;
    };
    </script>
        
    <div style="padding-bottom:5px;margin-left:-3px;">
        <button id="toggle_mutsig_dialog" onclick="promptMutsigTable(); return false;" style="font-size: 1em;">Select from Recurrently Mutated Genes (MutSig)</button>
        <button id="toggle_gistic_dialog" onclick="Gistic.UI.open_dialog(); return false;" style="font-size: 1em; display: none;">Select Genes from Recurrent CNAs (Gistic)</button>
    </div>

    <script type="text/javascript">
        $(document).ready(function() {
            GeneSymbolValidator.initialize();
        });
    </script>

<textarea rows='5' cols='80' id='gene_list' placeholder="Enter Gene Symbols or Gene Aliases" required
name='<%= QueryBuilder.GENE_LIST %>' title='Enter Gene Symbols or Gene Aliases'><%
    if (localGeneList != null && localGeneList.length() > 0) {
	    String geneListWithSemis =
			    org.mskcc.cbio.portal.oncoPrintSpecLanguage.Utilities.appendSemis(localGeneList);
	    // this is for xss security
	    geneListWithSemis = StringEscapeUtils.escapeJavaScript(geneListWithSemis);
	    // ...but we want to keep newlines, and slashes so unescape them
	    geneListWithSemis = geneListWithSemis.replaceAll("\\\\n", "\n").replaceAll("\\\\/", "/");
        out.print(geneListWithSemis);
    }
%></textarea>

<p id="genestatus"></p>
	
	<!-- // Gene set button that opens hierarchy popup -->	
	<span class="step_header" id="select_gene_sets">Select Gene Sets:</span>
	<div style="padding-bottom:5px;margin-left:-3px;">
	       <button id="toggle_geneset_dialog" onclick="popupGenesetHierarchy()" style="font-size: 1em; display: none;">Gene Sets</button>
	</div>
	
	<!-- // Pop-up to select gene sets from hierarchy -->
    <div id="geneset_dialog" title="Select Gene Sets" class='display' align="left" style="font-size: 11px; .ui-dialog {padding: 0em;};">
        <img id='loader-img' src="images/ajax-loader.gif" alt='loading'/>
    	
 		<!-- // Search box in popup -->
	    <div class="row step_header_first_line">
	        <div class="input-group input-group-sm col-5">
	            <input type="text" id="jstree_genesets_searchbox" class="form-control" placeholder="Search..." title="Search"/>
	            
	            <!-- Button to reset search (Work in progress) -->
 	            <!-- <i id="jstree_genesets_empty_search" class="fa fa-times"></i> -->
	        </div>
	    </div>
 
  		<!-- // Create hierarchical tree in popup -->
		<div id="jstree_genesets"  style="max-height:250px; overflow-y: scroll"></div>
		
		<!-- // Select and cancel buttons on bottem of popup -->		
		<div id="geneset_dialog_footer" style="float: right;">
					<button id="cancel_geneset" title="Cancel">Cancel</button>
					<button id="select_geneset" class="tabs-button" title="Use these gene sets">Select</button>
		</div>
    </div>
    
	<textarea rows='5' cols='80' id='geneset_list' placeholder="Enter Gene Sets" required
	name='<%= QueryBuilder.GENESET_LIST %>' title='Enter Gene Sets' style='display: none;'><%
	    if (localGeneSetList != null && localGeneSetList.length() > 0) {
		    String geneSetListWithSemis =
				    org.mskcc.cbio.portal.oncoPrintSpecLanguage.Utilities.appendSemis(localGeneSetList);
		    // this is for xss security
		    geneSetListWithSemis = StringEscapeUtils.escapeJavaScript(geneSetListWithSemis);
		    // ...but we want to keep newlines, and slashes so unescape them
		    geneSetListWithSemis = geneSetListWithSemis.replaceAll("\\\\n", "\n").replaceAll("\\\\/", "/");
	        out.print(geneSetListWithSemis);
	    }
	%></textarea>
	<%
	if (step4ErrorMsg != null) {
	    out.println("</div>");
	}
	%>

</div>

<script type='text/javascript'>
$('#toggle_gistic_dialog').button();
</script>
