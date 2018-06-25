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

<div class='section cbioportal-frontend' id='fusion_data'>
    <img src='images/ajax-loader.gif' alt='loading'/>
</div>

<script type="text/javascript">
    window.onReactAppReady(function() {
        var initialized = false;

        function initFusionData() {
            var fusionTab = $('#fusion_data');

            if (fusionTab.hasClass('cbioportal-frontend')) {
                window.renderFusionTab(fusionTab[0]);
                return true;
            }
            return false;
        }

        // if we are already on this tab, init the tab content
        if ($("div.section#fusion_data").is(":visible")) {
            initFusionData();
        }
        // otherwise delay initialization until mutations tab is clicked
        else {
            $("a.result-tab").click(function() {
                // initialize only once
                if (!initialized && $(this).attr("href") === "#fusion_data") {
                    initialized = initFusionData();
                }
            });
        }
    });
</script>
<style type="text/css">
    /* HACK: Use specific id for cbioportal-frontend overrides */
    #fusion_data {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 14px
    }
    #fusion_data .table {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 14px
    }
    #fusion_data th,
    #fusion_data td {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 14px
    }
    #fusion_data .btn-default {
        background-image: linear-gradient(to bottom, #fff 0, #eee 100%);
    }
    #fusion_data .fa-cloud-download,
    #fusion_data .fa-clipboard {
        padding-top: 3px;
        padding-bottom: 3px;
    }
    .rc-tooltip {
        opacity:1 !important;
    }
    /* END HACK */
</style>
