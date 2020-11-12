package org.cbioportal.persistence;

import org.cbioportal.model.GeneFilter;
import org.cbioportal.model.GeneFilter.SingleGeneQuery;
import org.cbioportal.model.Mutation;
import org.cbioportal.model.MutationCountByPosition;
import org.cbioportal.model.meta.MutationMeta;

import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface MutationRepository {

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    List<Mutation> getMutationsInMolecularProfileBySampleListId(String molecularProfileId, String sampleListId,
                                                                List<Integer> entrezGeneIds, Boolean snpOnly,
                                                                String projection, Integer pageSize, Integer pageNumber,
                                                                String sortBy, String direction);


    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    MutationMeta getMetaMutationsInMolecularProfileBySampleListId(String molecularProfileId, String sampleListId,
                                                                  List<Integer> entrezGeneIds);

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    List<Mutation> getMutationsInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                           List<String> sampleIds,
                                                           List<Integer> entrezGeneIds,
                                                           String projection,
                                                           Integer pageSize,
                                                           Integer pageNumber,
                                                           String sortBy,
                                                           String direction);

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    List<Mutation> getMutationsInMultipleMolecularProfilesByGeneQueries(List<String> molecularProfileIds,
                                                                        List<String> sampleIds,
                                                                        List<SingleGeneQuery> geneQueries,
                                                                        String projection,
                                                                        Integer pageSize,
                                                                        Integer pageNumber,
                                                                        String sortBy,
                                                                        String direction);

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    MutationMeta getMetaMutationsInMultipleMolecularProfiles(List<String> molecularProfileIds, List<String> sampleIds,
                                                             List<Integer> entrezGeneIds);

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    List<Mutation> fetchMutationsInMolecularProfile(String molecularProfileId, List<String> sampleIds,
                                                    List<Integer> entrezGeneIds, Boolean snpOnly, String projection,
                                                    Integer pageSize, Integer pageNumber, String sortBy,
                                                    String direction);

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    MutationMeta fetchMetaMutationsInMolecularProfile(String molecularProfileId, List<String> sampleIds,
                                                      List<Integer> entrezGeneIds);

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    MutationCountByPosition getMutationCountByPosition(Integer entrezGeneId, Integer proteinPosStart, 
                                                       Integer proteinPosEnd);

    // TODO: cleanup once fusion/structural data is fixed in database
    List<Mutation> getFusionsInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                         List<String> sampleIds,
                                                         List<Integer> entrezGeneIds,
                                                         String projection,
                                                         Integer pageSize,
                                                         Integer pageNumber,
                                                         String sortBy,
                                                         String direction);

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    List<Mutation> getFusionsInMultipleMolecularProfilesByGeneQueries(List<String> molecularProfileIds,
                                                                      List<String> sampleIds,
                                                                      List<SingleGeneQuery> geneQueries,
                                                                      String projection,
                                                                      Integer pageSize,
                                                                      Integer pageNumber,
                                                                      String sortBy,
                                                                      String direction);

    // TODO: cleanup once fusion/structural data is fixed in database
}
