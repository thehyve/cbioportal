package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.AlterationCountByGene;
import org.cbioportal.model.CopyNumberCountByGene;
import org.cbioportal.model.util.Select;

import java.util.List;

public interface AlterationCountsMapper {

    /**
     * Calculate sample-level counts of mutation and discrete CNA alteration events.
     * @param molecularProfileIds  List of stable id's of molecular profiles to include in alteration counts. Forms pairs with sampleIds based on array index. 
     * @param sampleIds List of stable id's of samples to include in alteration counts. Forms pairs with sampleIds based on array index.
     * @param entrezGeneIds  List of gene ids to get counts for.
     * @param mutationTypes  Types of mutations to include in alteration counts. When 'null' all types will be included. When empty list mutations will be excluded from counts. 
     * @param cnaTypes  Types of discrete copy number alteration types to include in alteration counts. When 'null' all types will be included. When empty list mutations will be excluded from counts.
     * @param searchFusions  When 'true' mutation counts will be limited to fusion type alterations. When 'false' mutation counts will be limited to non-fusion alterations.
     * @param excludeVUS  Filter out Variants of Unknown significance. Uses annotations loaded as 'custom driver annotations'.
     * @param selectedTiers  Force alterations assigned to a tier to be interpreted as driver events. Uses tier annotations loaded as 'custom driver annotation tiers'.
     * @param excludeGermline  Exlude germline mutations from alteration counts 
     * @return Gene-level counts of (1) the total number of alterations and (2) the number of altered samples.
     */
    List<AlterationCountByGene> getSampleAlterationCounts(List<String> molecularProfileIds,
                                                          List<String> sampleIds,
                                                          List<Integer> entrezGeneIds,
                                                          Select<String> mutationTypes,
                                                          Select<Integer> cnaTypes,
                                                          boolean searchFusions,
                                                          boolean excludeVUS,
                                                          List<String> selectedTiers,
                                                          boolean excludeGermline);
    /**
     * Calculate patient-level counts of mutation and discrete CNA alteration events.
     * @param molecularProfileIds  List of stable id's of molecular profiles to include in alteration counts. Forms pairs with sampleIds based on array index. 
     * @param patientIds List of stable id's of patients to include in alteration counts. Forms pairs with sampleIds based on array index.
     * @param entrezGeneIds  List of gene ids to get counts for.
     * @param mutationTypes  Types of mutations to include in alteration counts. When 'null' all types will be included. When empty list mutations will be excluded from counts. 
     * @param cnaTypes  Types of discrete copy number alteration types to include in alteration counts. When 'null' all types will be included. When empty list mutations will be excluded from counts.
     * @param searchFusions  When 'true' mutation counts will be limited to fusion type alterations. When 'false' mutation counts will be limited to non-fusion alterations.
     * @param excludeVUS  Filter out Variants of Unknown significance. Uses annotations loaded as 'custom driver annotations'.
     * @param selectedTiers  Force alterations assigned to a tier to be interpreted as driver events. Uses tier annotations loaded as 'custom driver annotation tiers'.
     * @param excludeGermline  Exlude germline mutations from alteration counts.
     * @return Gene-level counts of (1) the total number of alterations and (2) the number of altered patients.
     */
    List<AlterationCountByGene> getPatientAlterationCounts(List<String> molecularProfileIds,
                                                           List<String> patientIds,
                                                           List<Integer> entrezGeneIds,
                                                           Select<String> mutationTypes,
                                                           Select<Integer> cnaTypes,
                                                           boolean searchFusions,
                                                           boolean excludeVUS,
                                                           List<String> selectedTiers,
                                                           boolean excludeGermline);

    // legacy method that returns CopyNumberCountByGene
    List<CopyNumberCountByGene> getSampleCnaCounts(List<String> molecularProfileIds,
                                                          List<String> sampleIds,
                                                          List<Integer> entrezGeneIds,
                                                          Select<Integer> cnaTypes,
                                                          boolean excludeVUS,
                                                          List<String> selectedTiers);

    // legacy method that returns CopyNumberCountByGene
    List<CopyNumberCountByGene> getPatientCnaCounts(List<String> molecularProfileIds,
                                                           List<String> patientIds,
                                                           List<Integer> entrezGeneIds,
                                                           Select<Integer> cnaTypes,
                                                           boolean excludeVUS,
                                                           List<String> selectedTiers);
}

