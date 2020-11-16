package org.cbioportal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class GeneFilter implements Serializable {

    private Set<String> molecularProfileIds;
    private List<List<SingleGeneQuery>> geneQueries;

    private final String GENE_QUERY_PATTERN = "^(\\w+)[\\s]*?(?:\\:(?:[\\s]*(?:(AMP)|(GAIN)|(DIPLOID)|(HETLOSS)|(HOMDEL))\\b)+)?$";

    public static class SingleGeneQuery implements Serializable {
        private String hugoGeneSymbol;
        private Integer entrezGeneId;
        private List<CNA> alterations;
        private boolean excludeVUS;
        private boolean excludeGermline;
        private List<String> selectedTiers;

        public SingleGeneQuery(String hugoGeneSymbol, Integer entrezGeneId, List<CNA> alterations, boolean excludeVUS, boolean excludeGermline, List<String> selectedTiers) {
            this.hugoGeneSymbol = hugoGeneSymbol;
            this.entrezGeneId = entrezGeneId;
            this.alterations = alterations;
            this.excludeVUS = excludeVUS;
            this.excludeGermline = excludeGermline;
            this.selectedTiers = selectedTiers;
        }

        public String getHugoGeneSymbol() {
            return hugoGeneSymbol;
        }

        public void setHugoGeneSymbol(String hugoGeneSymbol) {
            this.hugoGeneSymbol = hugoGeneSymbol;
        }

        public Integer getEntrezGeneId() {
            return entrezGeneId;
        }

        public void setEntrezGeneId(int entrezGeneId) {
            this.entrezGeneId = entrezGeneId;
        }

        public List<CNA> getAlterations() {
            return alterations;
        }

        public void setAlterations(List<CNA> alterations) {
            this.alterations = alterations;
        }

        public boolean getExcludeVUS() {
            return excludeVUS;
        }

        public void setExcludeVUS(boolean excludeVUS) {
            this.excludeVUS = excludeVUS;
        }

        public boolean getExcludeGermline() {
            return excludeGermline;
        }

        public void setExcludeGermline(boolean excludeGermline) {
            this.excludeGermline = excludeGermline;
        }

        public List<String> getSelectedTiers() {
            return selectedTiers;
        }

        public void setSelectedTiers(List<String> selectedTiers) {
            this.selectedTiers = selectedTiers;
        }
    }

//    @AssertTrue
//    private boolean isValid() {
//        if (!CollectionUtils.isEmpty(geneQueries) && !CollectionUtils.isEmpty(molecularProfileIds)) {
//            return geneQueries.stream().flatMap(geneQuery -> geneQuery.stream().map(query -> {
//                Pattern pattern = Pattern.compile(GENE_QUERY_PATTERN);
//                Matcher matcher = pattern.matcher(query.trim());
//                return matcher.matches();
//            })).reduce(Boolean.TRUE, Boolean::logicalAnd);
//        }
//        return false;
//    }

    public Set<String> getMolecularProfileIds() {
        return molecularProfileIds;
    }

    public void setMolecularProfileIds(Set<String> molecularProfileIds) {
        this.molecularProfileIds = molecularProfileIds;
    }

    public List<List<SingleGeneQuery>> getGeneQueries() {
        return geneQueries;
    }

    public void setGeneQueries(List<List<SingleGeneQuery>> geneQueries) {
        this.geneQueries = geneQueries;
    }

    //    public List<List<String>> getGeneQueries() {
//        return geneQueries;
//    }

//    public void setGeneQueries(List<List<String>> geneQueries) {
//        this.geneQueries = geneQueries;
//    }

//    @JsonIgnore
//    public List<List<SingleGeneQuery>> getSingleGeneQueries() {
//
//        return geneQueries.stream().map(geneQuery -> {
//
//            List<SingleGeneQuery> singleGeneQueries = new ArrayList<SingleGeneQuery>();
//
//            geneQuery.stream().forEach(query -> {
//                Pattern pattern = Pattern.compile(GENE_QUERY_PATTERN);
//                Matcher matcher = pattern.matcher(query.trim());
//
//                if (matcher.find()) {
//
//                    String hugoGeneSymbol = matcher.group(1);
//                    Set<CNA> alterations = new HashSet<>();
//                    for (int count = 2; count <= matcher.groupCount(); count++) {
//                        if (matcher.group(count) != null) {
//                            alterations.add(CNA.valueOf(matcher.group(count)));
//                        }
//                    }
//                    SingleGeneQuery singleGeneQuery = new SingleGeneQuery();
//                    singleGeneQuery.setHugoGeneSymbol(hugoGeneSymbol);
//                    singleGeneQuery.setAlterations(new ArrayList<>(alterations));
//                    singleGeneQueries.add(singleGeneQuery);
//                }
//            });
//
//            return singleGeneQueries;
//        }).collect(Collectors.toList());
//    }

}