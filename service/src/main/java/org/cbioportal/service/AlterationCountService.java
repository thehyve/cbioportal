package org.cbioportal.service;

import org.cbioportal.model.*;
import org.cbioportal.model.util.Select;

import java.util.List;

public interface AlterationCountService {

    List<AlterationCountByGene> getSampleAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                          List<Integer> entrezGeneIds,
                                                          boolean includeFrequency,
                                                          boolean includeMissingAlterationsFromGenePanel,
                                                          Select<MutationEventType> mutationEventTypes,
                                                          Select<CopyNumberAlterationEventType> cnaEventTypes,
                                                          boolean searchFusions,
                                                          boolean excludeVUS,
                                                          List<String> selectedTiers,
                                                          boolean excludeGermline);

    List<AlterationCountByGene> getPatientAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                           List<Integer> entrezGeneIds,
                                                           boolean includeFrequency,
                                                           boolean includeMissingAlterationsFromGenePanel,
                                                           Select<MutationEventType> mutationEventTypes,
                                                           Select<CopyNumberAlterationEventType> cnaEventTypes,
                                                           boolean searchFusions,
                                                           boolean excludeVUS,
                                                           List<String> selectedTiers,
                                                           boolean excludeGermline);

    List<AlterationCountByGene> getSampleMutationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                        List<Integer> entrezGeneIds,
                                                        boolean includeFrequency,
                                                        boolean includeMissingAlterationsFromGenePanel,
                                                        Select<MutationEventType> mutationEventTypes,
                                                        boolean excludeVUS,
                                                        List<String> selectedTiers,
                                                        boolean excludeGermline);

    List<AlterationCountByGene> getPatientMutationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                         List<Integer> entrezGeneIds,
                                                         boolean includeFrequency,
                                                         boolean includeMissingAlterationsFromGenePanel,
                                                         Select<MutationEventType> mutationEventTypes,
                                                         boolean excludeVUS,
                                                         List<String> selectedTiers,
                                                         boolean excludeGermline);

    List<AlterationCountByGene> getSampleFusionCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                      List<Integer> entrezGeneIds,
                                                      boolean includeFrequency,
                                                      boolean includeMissingAlterationsFromGenePanel,
                                                      Select<MutationEventType> mutationEventTypes,
                                                      boolean excludeVUS,
                                                      List<String> selectedTiers,
                                                      boolean excludeGermline);

    List<AlterationCountByGene> getPatientFusionCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                       List<Integer> entrezGeneIds,
                                                       boolean includeFrequency,
                                                       boolean includeMissingAlterationsFromGenePanel,
                                                       Select<MutationEventType> mutationEventTypes,
                                                       boolean excludeVUS,
                                                       List<String> selectedTiers,
                                                       boolean excludeGermline);

// Should be restored when old CNA count endpoint is retired
//    List<AlterationCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
//                                                   List<Integer> entrezGeneIds,
//                                                   boolean includeFrequency,
//                                                   boolean includeMissingAlterationsFromGenePanel,
//                                                   List<CopyNumberAlterationEventType> cnaEventTypes,
//                                                   boolean excludeVUS,
//                                                   List<String> selectedTiers);
//
//    List<AlterationCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
//                                                    List<Integer> entrezGeneIds,
//                                                    boolean includeFrequency,
//                                                    boolean includeMissingAlterationsFromGenePanel,
//                                                    List<CopyNumberAlterationEventType> cnaEventTypes,
//                                                    boolean excludeVUS,
//                                                    List<String> selectedTiers);
    
// Should be removed when old CNA count endpoint is retired
    List<CopyNumberCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                   List<Integer> entrezGeneIds,
                                                   boolean includeFrequency,
                                                   boolean includeMissingAlterationsFromGenePanel,
                                                   Select<CopyNumberAlterationEventType> cnaEventTypes,
                                                   boolean excludeVUS,
                                                   List<String> selectedTiers);

    List<CopyNumberCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                    List<Integer> entrezGeneIds,
                                                    boolean includeFrequency,
                                                    boolean includeMissingAlterationsFromGenePanel,
                                                    Select<CopyNumberAlterationEventType> cnaEventTypes,
                                                    boolean excludeVUS,
                                                    List<String> selectedTiers);
    
}
