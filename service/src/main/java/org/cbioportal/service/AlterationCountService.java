package org.cbioportal.service;

import org.cbioportal.model.*;
import org.cbioportal.model.QueryElement;
import org.cbioportal.model.util.Select;

import java.util.List;

public interface AlterationCountService {

    List<AlterationCountByGene> getSampleAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                          Select<Integer> entrezGeneIds,
                                                          boolean includeFrequency,
                                                          boolean includeMissingAlterationsFromGenePanel,
                                                          QueryElement searchFusions,
                                                          AlterationFilter alterationFilter);

    List<AlterationCountByGene> getPatientAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                           Select<Integer> entrezGeneIds,
                                                           boolean includeFrequency,
                                                           boolean includeMissingAlterationsFromGenePanel,
                                                           QueryElement searchFusions,
                                                           AlterationFilter alterationFilter);

    List<AlterationCountByGene> getSampleMutationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                        Select<Integer> entrezGeneIds,
                                                        boolean includeFrequency,
                                                        boolean includeMissingAlterationsFromGenePanel,
                                                        AlterationFilter alterationFilter);

    List<AlterationCountByGene> getPatientMutationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                         Select<Integer> entrezGeneIds,
                                                         boolean includeFrequency,
                                                         boolean includeMissingAlterationsFromGenePanel,
                                                         AlterationFilter alterationFilter);

    List<AlterationCountByGene> getSampleFusionCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                      Select<Integer> entrezGeneIds,
                                                      boolean includeFrequency,
                                                      boolean includeMissingAlterationsFromGenePanel,
                                                      AlterationFilter alterationFilter);

    List<AlterationCountByGene> getPatientFusionCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                       Select<Integer> entrezGeneIds,
                                                       boolean includeFrequency,
                                                       boolean includeMissingAlterationsFromGenePanel,
                                                       AlterationFilter alterationFilter);

// Should be restored when old CNA count endpoint is retired
//    List<AlterationCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
//                                                   Select<Integer> entrezGeneIds,
//                                                   boolean includeFrequency,
//                                                   boolean includeMissingAlterationsFromGenePanel,
//                                                   AlterationFilter alterationFilter);
//
//    List<AlterationCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
//                                                    Select<Integer> entrezGeneIds,
//                                                    boolean includeFrequency,
//                                                    boolean includeMissingAlterationsFromGenePanel,
//                                                   AlterationFilter alterationFilter);

    // Should be removed when old CNA count endpoint is retired
    List<CopyNumberCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                   Select<Integer> entrezGeneIds,
                                                   boolean includeFrequency,
                                                   boolean includeMissingAlterationsFromGenePanel,
                                                   AlterationFilter alterationFilter);

    List<CopyNumberCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                    Select<Integer> entrezGeneIds,
                                                    boolean includeFrequency,
                                                    boolean includeMissingAlterationsFromGenePanel,
                                                    AlterationFilter alterationFilter);
    
}
