package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.GeneFilter.SingleGeneQuery;
import org.cbioportal.model.Mutation;
import org.cbioportal.model.MutationCountByPosition;
import org.cbioportal.model.meta.MutationMeta;
import org.cbioportal.persistence.MutationRepository;
import org.cbioportal.persistence.mybatis.util.OffsetCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class MutationMyBatisRepository implements MutationRepository {

    @Autowired
    private MutationMapper mutationMapper;
    @Autowired
    private OffsetCalculator offsetCalculator;

    @Override
    public List<Mutation> getMutationsInMolecularProfileBySampleListId(String molecularProfileId, String sampleListId,
                                                                       List<Integer> entrezGeneIds, Boolean snpOnly,
                                                                       String projection, Integer pageSize,
                                                                       Integer pageNumber, String sortBy,
                                                                       String direction) {

        return mutationMapper.getMutationsBySampleListId(molecularProfileId, sampleListId, entrezGeneIds, snpOnly,
            projection, pageSize, offsetCalculator.calculate(pageSize, pageNumber), sortBy, direction);
    }

    @Override
    public MutationMeta getMetaMutationsInMolecularProfileBySampleListId(String molecularProfileId, String sampleListId,
                                                                         List<Integer> entrezGeneIds) {

        return mutationMapper.getMetaMutationsBySampleListId(molecularProfileId, sampleListId, entrezGeneIds, null);
    }

    @Override
    public List<Mutation> getMutationsInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                  List<String> sampleIds, List<Integer> entrezGeneIds,
                                                                  String projection, Integer pageSize,
                                                                  Integer pageNumber, String sortBy, String direction) {
        boolean searchFusions = false;
        return mutationMapper.getMutationsInMultipleMolecularProfiles(molecularProfileIds, sampleIds, entrezGeneIds,
            null, searchFusions, projection, pageSize, offsetCalculator.calculate(pageSize, pageNumber), sortBy, direction);
    }

    @Override
    public List<Mutation> getMutationsInMultipleMolecularProfilesByGeneQueries(List<String> molecularProfileIds,
                                                                               List<String> sampleIds,
                                                                               List<SingleGeneQuery> geneQueries,
                                                                               String projection,
                                                                               Integer pageSize,
                                                                               Integer pageNumber,
                                                                               String sortBy,
                                                                               String direction) {
        boolean searchFusions = false;
        boolean anyExcludeVUS = geneQueries.stream().anyMatch(q -> q.getExcludeVUS());
        boolean anyExcludeGermline = geneQueries.stream().anyMatch(q -> q.getExcludeGermline());
        boolean anyTiers = geneQueries.stream().map(q -> q.getSelectedTiers()).flatMap(Collection::stream).count() > 0;
        return mutationMapper.getMutationsInMultipleMolecularProfilesByGeneQueries(molecularProfileIds, sampleIds, geneQueries,
            null, searchFusions, projection, pageSize, offsetCalculator.calculate(pageSize, pageNumber), sortBy, direction,
            anyExcludeVUS, anyExcludeGermline, anyTiers);
    }

    @Override
    public MutationMeta getMetaMutationsInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                    List<String> sampleIds,
                                                                    List<Integer> entrezGeneIds) {

        return mutationMapper.getMetaMutationsInMultipleMolecularProfiles(molecularProfileIds, sampleIds, entrezGeneIds,
            null);
    }

    @Override
    public List<Mutation> fetchMutationsInMolecularProfile(String molecularProfileId, List<String> sampleIds,
                                                           List<Integer> entrezGeneIds, Boolean snpOnly,
                                                           String projection, Integer pageSize, Integer pageNumber,
                                                           String sortBy, String direction) {

        return mutationMapper.getMutationsBySampleIds(molecularProfileId, sampleIds, entrezGeneIds, snpOnly, projection,
            pageSize, offsetCalculator.calculate(pageSize, pageNumber), sortBy, direction);
    }

    @Override
    public MutationMeta fetchMetaMutationsInMolecularProfile(String molecularProfileId, List<String> sampleIds,
                                                             List<Integer> entrezGeneIds) {

        return mutationMapper.getMetaMutationsBySampleIds(molecularProfileId, sampleIds, entrezGeneIds, null);
    }

    @Override
    public MutationCountByPosition getMutationCountByPosition(Integer entrezGeneId, Integer proteinPosStart,
                                                              Integer proteinPosEnd) {

        return mutationMapper.getMutationCountByPosition(entrezGeneId, proteinPosStart, proteinPosEnd);
    }

    // TODO: cleanup once fusion/structural data is fixed in database
    @Override
    public List<Mutation> getFusionsInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                List<String> sampleIds,
                                                                List<Integer> entrezGeneIds,
                                                                String projection,
                                                                Integer pageSize,
                                                                Integer pageNumber,
                                                                String sortBy,
                                                                String direction) {
        boolean searchFusion = true;
        return mutationMapper.getMutationsInMultipleMolecularProfiles(molecularProfileIds,
            sampleIds, entrezGeneIds, false, searchFusion, projection, pageSize, pageNumber, sortBy, direction);
    }
    
    @Override
    public List<Mutation> getFusionsInMultipleMolecularProfilesByGeneQueries(List<String> molecularProfileIds,
                                                                             List<String> sampleIds,
                                                                             List<SingleGeneQuery> geneQueries,
                                                                             String projection,
                                                                             Integer pageSize,
                                                                             Integer pageNumber,
                                                                             String sortBy,
                                                                             String direction) {
        boolean searchFusion = true;
        boolean anyExcludeVUS = geneQueries.stream().anyMatch(q -> q.getExcludeVUS());
        boolean anyExcludeGermline = geneQueries.stream().anyMatch(q -> q.getExcludeGermline());
        boolean anyTiers = geneQueries.stream().map(q -> q.getSelectedTiers()).flatMap(Collection::stream).count() > 0;
        return mutationMapper.getMutationsInMultipleMolecularProfilesByGeneQueries(molecularProfileIds,
            sampleIds, geneQueries, false, searchFusion, projection, pageSize, pageNumber, sortBy, direction,
            anyExcludeVUS, anyExcludeGermline, anyTiers);
    }
    // TODO: cleanup once fusion/structural data is fixed in database

}
