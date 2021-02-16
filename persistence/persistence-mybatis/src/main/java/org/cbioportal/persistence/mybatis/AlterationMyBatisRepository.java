package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.AlterationCountByGene;
import org.cbioportal.model.AlterationFilter;
import org.cbioportal.model.CNA;
import org.cbioportal.model.CopyNumberCountByGene;
import org.cbioportal.model.MolecularProfileCaseIdentifier;
import org.cbioportal.model.MutationEventType;
import org.cbioportal.model.QueryElement;
import org.cbioportal.model.util.Select;
import org.cbioportal.persistence.AlterationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class AlterationMyBatisRepository implements AlterationRepository {

    @Autowired
    private AlterationCountsMapper alterationCountsMapper;

    @Override
    public List<AlterationCountByGene> getSampleAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                 Select<Integer> entrezGeneIds,
                                                                 QueryElement searchFusions,
                                                                 AlterationFilter alterationFilter) {

        if (!alterationFilter.getMutationTypeSelect().hasAll() && searchFusions != QueryElement.PASS)
            throw new IllegalArgumentException("Filtering for mutations vs. fusions and specifying mutation types" +
                "simultaneously is not permitted.");

        if ((alterationFilter.getMutationTypeSelect().hasNone() && alterationFilter.getCnaTypeSelect().hasNone())
            || (molecularProfileCaseIdentifiers == null || molecularProfileCaseIdentifiers.isEmpty())
            || (allAlterationsExcludedDriverAnnotation(alterationFilter) && allAlterationsExcludedMutationStatus(alterationFilter))) {
            return Collections.emptyList();
        }

        List<Integer> internalSampleIds = alterationCountsMapper.getSampleInternalIds(molecularProfileCaseIdentifiers);

        return alterationCountsMapper.getSampleAlterationCounts(
            internalSampleIds,
            entrezGeneIds,
            createMutationTypeList(alterationFilter),
            createCnaTypeList(alterationFilter),
            searchFusions,
            alterationFilter.getIncludeDriver(),
            alterationFilter.getIncludeVUS(),
            alterationFilter.getIncludeUnknownOncogenicity(),
            alterationFilter.getSelectedTiers(),
            alterationFilter.getIncludeUnknownTier(),
            alterationFilter.getIncludeGermline(),
            alterationFilter.getIncludeSomatic(),
            alterationFilter.getIncludeUnknownStatus());
    }

    @Override
    public List<AlterationCountByGene> getPatientAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                  Select<Integer> entrezGeneIds,
                                                                  QueryElement searchFusions,
                                                                  AlterationFilter alterationFilter) {

        if (!alterationFilter.getMutationTypeSelect().hasAll() && searchFusions != QueryElement.PASS)
            throw new IllegalArgumentException("Filtering for mutations vs. fusions and specifying mutation types" +
                "simultaneously is not permitted.");

        if ((alterationFilter.getMutationTypeSelect().hasNone() && alterationFilter.getCnaTypeSelect().hasNone())
            || (molecularProfileCaseIdentifiers == null || molecularProfileCaseIdentifiers.isEmpty())
            || (allAlterationsExcludedDriverAnnotation(alterationFilter) && allAlterationsExcludedMutationStatus(alterationFilter))) {
            return Collections.emptyList();
        }

        List<Integer> internalPatientIds = alterationCountsMapper.getPatientInternalIds(molecularProfileCaseIdentifiers);

        return alterationCountsMapper.getPatientAlterationCounts(
            internalPatientIds,
            entrezGeneIds,
            createMutationTypeList(alterationFilter),
            createCnaTypeList(alterationFilter),
            searchFusions,
            alterationFilter.getIncludeDriver(),
            alterationFilter.getIncludeVUS(),
            alterationFilter.getIncludeUnknownOncogenicity(),
            alterationFilter.getSelectedTiers(),
            alterationFilter.getIncludeUnknownTier(),
            alterationFilter.getIncludeGermline(),
            alterationFilter.getIncludeSomatic(),
            alterationFilter.getIncludeUnknownStatus());
    }

    @Override
    public List<CopyNumberCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                          Select<Integer> entrezGeneIds,
                                                          AlterationFilter alterationFilter) {
        if (alterationFilter.getCnaTypeSelect().hasNone() || molecularProfileCaseIdentifiers == null
            || molecularProfileCaseIdentifiers.isEmpty() || allAlterationsExcludedDriverAnnotation(alterationFilter)) {
            return Collections.emptyList();
        }

        List<Integer> internalSampleIds = alterationCountsMapper.getSampleInternalIds(molecularProfileCaseIdentifiers);

        return alterationCountsMapper.getSampleCnaCounts(
            internalSampleIds,
            entrezGeneIds,
            createCnaTypeList(alterationFilter),
            alterationFilter.getIncludeDriver(),
            alterationFilter.getIncludeVUS(),
            alterationFilter.getIncludeUnknownOncogenicity(),
            alterationFilter.getSelectedTiers(),
            alterationFilter.getIncludeUnknownTier());
    }

    @Override
    public List<CopyNumberCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                           Select<Integer> entrezGeneIds,
                                                           AlterationFilter alterationFilter) {

        if (alterationFilter.getCnaTypeSelect().hasNone() || molecularProfileCaseIdentifiers == null
            || molecularProfileCaseIdentifiers.isEmpty() || allAlterationsExcludedDriverAnnotation(alterationFilter)) {
            return Collections.emptyList();
        }

        List<Integer> internalPatientIds = alterationCountsMapper.getPatientInternalIds(molecularProfileCaseIdentifiers);
        
        return alterationCountsMapper.getPatientCnaCounts(
            internalPatientIds,
            entrezGeneIds,
            createCnaTypeList(alterationFilter),
            alterationFilter.getIncludeDriver(),
            alterationFilter.getIncludeVUS(),
            alterationFilter.getIncludeUnknownOncogenicity(),
            alterationFilter.getSelectedTiers(),
            alterationFilter.getIncludeUnknownTier());
    }
    
    private Select<Short> createCnaTypeList(final AlterationFilter alterationFilter) {
        if (alterationFilter.getCnaTypeSelect().hasNone())
            return Select.none();
        if (alterationFilter.getCnaTypeSelect().hasAll())
            return Select.all();
        return alterationFilter.getCnaTypeSelect().map(CNA::getCode);
    }

    private Select<String> createMutationTypeList(final AlterationFilter alterationFilter) {
        if (alterationFilter.getMutationTypeSelect().hasNone())
            return Select.none();
        if (alterationFilter.getMutationTypeSelect().hasAll())
            return Select.all();
        return alterationFilter.getMutationTypeSelect().map(MutationEventType::getMutationType);
    }

    private boolean allAlterationsExcludedMutationStatus(AlterationFilter alterationFilter) {
        return !alterationFilter.getIncludeGermline() && !alterationFilter.getIncludeSomatic() && !alterationFilter.getIncludeUnknownStatus();
    }
    
    private boolean allAlterationsExcludedDriverAnnotation(AlterationFilter alterationFilter) {
        return !alterationFilter.getIncludeDriver() && !alterationFilter.getIncludeVUS()
            && !alterationFilter.getIncludeUnknownOncogenicity() && alterationFilter.getSelectedTiers().hasNone()
            && !alterationFilter.getIncludeUnknownTier();
    }

}
