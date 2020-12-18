package org.cbioportal.persistence.mybatis;

import org.apache.commons.lang3.tuple.Pair;
import org.cbioportal.model.*;
import org.cbioportal.model.util.Select;
import org.cbioportal.persistence.AlterationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class AlterationMyBatisRepository implements AlterationRepository {

    @Autowired
    private AlterationCountsMapper alterationCountsMapper;

    @Override
    public List<AlterationCountByGene> getSampleAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                 List<Integer> entrezGeneIds,
                                                                 final Select<MutationEventType> mutationEventTypes,
                                                                 final Select<CopyNumberAlterationEventType> cnaEventTypes,
                                                                 boolean searchFusions) {

        if ((mutationEventTypes.hasNone() && cnaEventTypes.hasNone())
            || molecularProfileCaseIdentifiers.isEmpty()) {
            return Collections.emptyList();
        }

        Pair<List<String>, List<String>> caseIdToProfileIdArrays = createCaseIdToProfileIdArrays(molecularProfileCaseIdentifiers);

        List<Integer> internalSampleIds = alterationCountsMapper.getSampleInternalIds(caseIdToProfileIdArrays.getLeft(),
            caseIdToProfileIdArrays.getRight());
        if (internalSampleIds.isEmpty()) {
            return Collections.emptyList();
        }
        return alterationCountsMapper.getSampleAlterationCounts(
            internalSampleIds,
            entrezGeneIds,
            createMutationTypeList(mutationEventTypes),
            createCnaTypeList(cnaEventTypes),
            searchFusions);
    }

    @Override
    public List<AlterationCountByGene> getPatientAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                  List<Integer> entrezGeneIds,
                                                                  Select<MutationEventType> mutationEventTypes,
                                                                  Select<CopyNumberAlterationEventType> cnaEventTypes,
                                                                  boolean searchFusions) {

        if ((mutationEventTypes.hasNone() && cnaEventTypes.hasNone())
            || molecularProfileCaseIdentifiers.isEmpty()) {
            return Collections.emptyList();
        }

        Pair<List<String>, List<String>> caseIdToProfileIdArrays = createCaseIdToProfileIdArrays(molecularProfileCaseIdentifiers);

        List<Integer> internalPatientIds = alterationCountsMapper.getPatientInternalIds(caseIdToProfileIdArrays.getLeft(),
            caseIdToProfileIdArrays.getRight());
        if (internalPatientIds.isEmpty()) {
            return Collections.emptyList();
        }

        return alterationCountsMapper.getPatientAlterationCounts(
            internalPatientIds,
            entrezGeneIds,
            createMutationTypeList(mutationEventTypes),
            createCnaTypeList(cnaEventTypes),
            searchFusions);
    }

    @Override
    public List<CopyNumberCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                          List<Integer> entrezGeneIds,
                                                          Select<CopyNumberAlterationEventType> cnaEventTypes) {

        if (cnaEventTypes.hasNone()) {
            return Collections.emptyList();
        }

        Pair<List<String>, List<String>> caseIdToProfileIdArrays = createCaseIdToProfileIdArrays(molecularProfileCaseIdentifiers);

        List<Integer> internalSampleIds = alterationCountsMapper.getSampleInternalIds(caseIdToProfileIdArrays.getLeft(),
            caseIdToProfileIdArrays.getRight());
        if (internalSampleIds.isEmpty()) {
            return Collections.emptyList();
        }
        return alterationCountsMapper.getSampleCnaCounts(
            internalSampleIds,
            entrezGeneIds,
            createCnaTypeList(cnaEventTypes));
    }

    @Override
    public List<CopyNumberCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                           List<Integer> entrezGeneIds,
                                                           Select<CopyNumberAlterationEventType> cnaEventTypes) {

        if (cnaEventTypes.hasNone()) {
            return Collections.emptyList();
        }

        Pair<List<String>, List<String>> caseIdToProfileIdArrays = createCaseIdToProfileIdArrays(molecularProfileCaseIdentifiers);

        return alterationCountsMapper.getPatientCnaCounts(
            caseIdToProfileIdArrays.getLeft(),
            caseIdToProfileIdArrays.getRight(),
            entrezGeneIds,
            createCnaTypeList(cnaEventTypes));
    }


    /**
     * Collect profile id and sample id arrays.
     *
     * @param ids List of MolecularProfileCaseIdentifiers
     * @return Pair of profile id/sample id arrays where every index
     * represents a profile id/sample id-combination
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

    private Select<Integer> createCnaTypeList(final Select<CopyNumberAlterationEventType> cnaEventTypes) {
        return cnaEventTypes.map(CopyNumberAlterationEventType::getAlterationType);
    }

    private Select<String> createMutationTypeList(final Select<MutationEventType> mutationEventTypes) {
        return mutationEventTypes.map(MutationEventType::getMutationType);
    }

}