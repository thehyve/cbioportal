package org.cbioportal.persistence.mybatis;

import org.apache.commons.lang3.tuple.Pair;
import org.cbioportal.model.*;
import org.cbioportal.persistence.AlterationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AlterationMyBatisRepository implements AlterationRepository {

    @Autowired
    private AlterationCountsMapper alterationCountsMapper;

    @Override
    public List<AlterationCountByGene> getSampleAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                 List<Integer> entrezGeneIds,
                                                                 List<MutationEventType> mutationEventTypes,
                                                                 List<CopyNumberAlterationEventType> cnaEventTypes,
                                                                 boolean searchFusions,
                                                                 boolean excludeVUS,
                                                                 List<String> selectedTiers,
                                                                 boolean excludeGermline) {

        if ((mutationEventTypes == null || mutationEventTypes.isEmpty())
            && (cnaEventTypes == null || cnaEventTypes.isEmpty()))
            return new ArrayList<>();

        Pair<List<String>, List<String>> caseIdToProfileIdArrays = createCaseIdToProfileIdArrays(molecularProfileCaseIdentifiers);

        return alterationCountsMapper.getSampleAlterationCounts(
            caseIdToProfileIdArrays.getLeft(),
            caseIdToProfileIdArrays.getRight(),
            entrezGeneIds,
            createMutationTypeList(mutationEventTypes),
            createCnaTypeList(cnaEventTypes),
            searchFusions,
            excludeVUS,
            selectedTiers,
            excludeGermline);
    }

    @Override
    public List<AlterationCountByGene> getPatientAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                  List<Integer> entrezGeneIds,
                                                                  List<MutationEventType> mutationEventTypes,
                                                                  List<CopyNumberAlterationEventType> cnaEventTypes,
                                                                  boolean searchFusions,
                                                                  boolean excludeVUS,
                                                                  List<String> selectedTiers,
                                                                  boolean excludeGermline) {

        if ((mutationEventTypes == null || mutationEventTypes.isEmpty())
            && (cnaEventTypes == null || cnaEventTypes.isEmpty()))
            return new ArrayList<>();

        Pair<List<String>, List<String>> caseIdToProfileIdArrays = createCaseIdToProfileIdArrays(molecularProfileCaseIdentifiers);

        return alterationCountsMapper.getPatientAlterationCounts(
            caseIdToProfileIdArrays.getLeft(),
            caseIdToProfileIdArrays.getRight(),
            entrezGeneIds,
            createMutationTypeList(mutationEventTypes),
            createCnaTypeList(cnaEventTypes),
            searchFusions,
            excludeVUS,
            selectedTiers,
            excludeGermline);
    }

    @Override
    public List<CopyNumberCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                          List<Integer> entrezGeneIds,
                                                          List<CopyNumberAlterationEventType> cnaEventTypes,
                                                          boolean excludeVUS,
                                                          List<String> selectedTiers) {


        if (cnaEventTypes == null || cnaEventTypes.isEmpty()) {
            return new ArrayList<>();
        }
        
        Pair<List<String>, List<String>> caseIdToProfileIdArrays = createCaseIdToProfileIdArrays(molecularProfileCaseIdentifiers);

        return alterationCountsMapper.getSampleCnaCounts(
            caseIdToProfileIdArrays.getLeft(),
            caseIdToProfileIdArrays.getRight(),
            entrezGeneIds,
            createCnaTypeList(cnaEventTypes),
            excludeVUS,
            selectedTiers);
    }

    @Override
    public List<CopyNumberCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                           List<Integer> entrezGeneIds,
                                                           List<CopyNumberAlterationEventType> cnaEventTypes,
                                                           boolean excludeVUS,
                                                           List<String> selectedTiers) {

        if (cnaEventTypes == null || cnaEventTypes.isEmpty()) {
            return new ArrayList<>();
        }

        Pair<List<String>, List<String>> caseIdToProfileIdArrays = createCaseIdToProfileIdArrays(molecularProfileCaseIdentifiers);

        return alterationCountsMapper.getPatientCnaCounts(
            caseIdToProfileIdArrays.getLeft(),
            caseIdToProfileIdArrays.getRight(),
            entrezGeneIds,
            createCnaTypeList(cnaEventTypes),
            excludeVUS,
            selectedTiers);
    }


    /**
     * Collect profile id and sample id arrays.
     * @param  ids List of MolecularProfileCaseIdentifiers
     * @return Pair of profile id/sample id arrays where every index
     *         represents a profile id/sample id-combination
     */
    private Pair<List<String>, List<String>> createCaseIdToProfileIdArrays(List<MolecularProfileCaseIdentifier> ids) {
        List<String> caseIds = new ArrayList<>();
        List<String> profileIds = new ArrayList<>();
        ids.forEach(pair -> {
            caseIds.add(pair.getCaseId());
            profileIds.add(pair.getMolecularProfileId());
        });
        return Pair.of(profileIds, caseIds);
    }
    
    private List<Integer> createCnaTypeList(List<CopyNumberAlterationEventType> cnaEventTypes) {
        return cnaEventTypes != null ?
            cnaEventTypes.stream().map(c -> c.getAlterationType()).collect(Collectors.toList())
            : null;
    }

    private List<String> createMutationTypeList(List<MutationEventType> mutationEventTypes) {
        return mutationEventTypes != null ?
            mutationEventTypes.stream().flatMap(m -> m.getMutationTypes().stream()).collect(Collectors.toList())
            : null;
    }
    
}
