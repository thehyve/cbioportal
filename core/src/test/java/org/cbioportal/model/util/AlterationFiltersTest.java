package org.cbioportal.model.util;

import org.apache.commons.collections.map.HashedMap;
import org.cbioportal.model.AlterationFilter;
import org.cbioportal.model.CNA;
import org.cbioportal.model.DriverAnnotationAlterationFilter;
import org.cbioportal.model.MutationEventType;
import org.cbioportal.model.MutationStatusAlterationFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class AlterationFiltersTest {

    @Test
    public void testAlterationFilterWithEmptyConstructor() {
        AlterationFilter f = new AlterationFilter();
        Assert.assertTrue(f.getMutationTypeSelect().hasAll());
        Assert.assertTrue(f.getCnaTypeSelect().hasAll());
        Assert.assertTrue(f.getIncludeDriver());
        Assert.assertTrue(f.getIncludeVUS());
        Assert.assertTrue(f.getIncludeUnknownOncogenicity());
        Assert.assertTrue(f.getIncludeGermline());
        Assert.assertTrue(f.getIncludeGermline());
        Assert.assertTrue(f.getIncludeUnknownStatus());
        Assert.assertTrue(f.getIncludeUnknownTier());
        Assert.assertTrue(f.getSelectedTiers().hasAll());
    }

    @Test
    public void testMutationStatusFilterWithEmptyConstructor() {
        MutationStatusAlterationFilter f = new MutationStatusAlterationFilter();
        Assert.assertTrue(f.getIncludeDriver());
        Assert.assertTrue(f.getIncludeVUS());
        Assert.assertTrue(f.getIncludeUnknownOncogenicity());
        Assert.assertTrue(f.getIncludeGermline());
        Assert.assertTrue(f.getIncludeGermline());
        Assert.assertTrue(f.getIncludeUnknownStatus());
        Assert.assertTrue(f.getIncludeUnknownTier());
        Assert.assertTrue(f.getSelectedTiers().hasAll());
    }

    @Test
    public void testDriverAnnotationFilterWithEmptyConstructor() {
        DriverAnnotationAlterationFilter f = new DriverAnnotationAlterationFilter();
        Assert.assertTrue(f.getIncludeDriver());
        Assert.assertTrue(f.getIncludeVUS());
        Assert.assertTrue(f.getIncludeUnknownOncogenicity());
        Assert.assertTrue(f.getIncludeUnknownTier());
        Assert.assertTrue(f.getSelectedTiers().hasAll());
    }

    @Test
    public void setSelectAllWhenAllCnaTypesTrue() {
        AlterationFilter f = new AlterationFilter();
        Map<CNA, Boolean> types = new HashedMap();
        types.put(CNA.HOMDEL, true);
        types.put(CNA.DIPLOID, true);
        f.setCopyNumberAlterationEventTypes(types);
        Assert.assertTrue(f.getCnaTypeSelect().hasValues());
        Assert.assertTrue(f.getCnaTypeSelect().hasAll());
        Assert.assertFalse(f.getCnaTypeSelect().hasNone());
    }

    @Test
    public void setSelectAllWhenSomeCnaTypesTrue() {
        AlterationFilter f = new AlterationFilter();
        Map<CNA, Boolean> types = new HashedMap();
        types.put(CNA.HOMDEL, false);
        types.put(CNA.DIPLOID, true);
        f.setCopyNumberAlterationEventTypes(types);
        Assert.assertTrue(f.getCnaTypeSelect().hasValues());
        Assert.assertFalse(f.getCnaTypeSelect().hasAll());
        Assert.assertFalse(f.getCnaTypeSelect().hasNone());
    }

    @Test
    public void setSelectAllWhenAllCnaTypesFalse() {
        AlterationFilter f = new AlterationFilter();
        Map<CNA, Boolean> types = new HashedMap();
        types.put(CNA.HOMDEL, false);
        types.put(CNA.DIPLOID, false);
        f.setCopyNumberAlterationEventTypes(types);
        Assert.assertFalse(f.getCnaTypeSelect().hasValues());
        Assert.assertFalse(f.getCnaTypeSelect().hasAll());
        Assert.assertTrue(f.getCnaTypeSelect().hasNone());
    }

    @Test
    public void setSelectAllWhenAllMutationTypesTrue() {
        AlterationFilter f = new AlterationFilter();
        Map<MutationEventType, Boolean> types = new HashedMap();
        types.put(MutationEventType.feature_truncation, true);
        types.put(MutationEventType.missense_mutation, true);
        f.setMutationEventTypes(types);
        Assert.assertTrue(f.getMutationTypeSelect().hasValues());
        Assert.assertTrue(f.getMutationTypeSelect().hasAll());
        Assert.assertFalse(f.getMutationTypeSelect().hasNone());
    }

    @Test
    public void setSelectAllWhenSomeMutationTypesTrue() {
        AlterationFilter f = new AlterationFilter();
        Map<MutationEventType, Boolean> types = new HashedMap();
        types.put(MutationEventType.feature_truncation, false);
        types.put(MutationEventType.missense_mutation, true);
        f.setMutationEventTypes(types);
        Assert.assertTrue(f.getMutationTypeSelect().hasValues());
        Assert.assertFalse(f.getMutationTypeSelect().hasAll());
        Assert.assertFalse(f.getMutationTypeSelect().hasNone());
    }

    @Test
    public void setSelectAllWhenAllMutationTypesFalse() {
        AlterationFilter f = new AlterationFilter();
        Map<MutationEventType, Boolean> types = new HashedMap();
        types.put(MutationEventType.feature_truncation, false);
        types.put(MutationEventType.missense_mutation, false);
        f.setMutationEventTypes(types);
        Assert.assertFalse(f.getMutationTypeSelect().hasValues());
        Assert.assertFalse(f.getMutationTypeSelect().hasAll());
        Assert.assertTrue(f.getMutationTypeSelect().hasNone());
    }

    @Test
    public void setSelectAllWhenAllTiersTrue() {
        DriverAnnotationAlterationFilter f = new DriverAnnotationAlterationFilter();
        Map<String, Boolean> types = new HashedMap();
        types.put("Class 1", true);
        types.put("Class 2", true);
        f.setSelectedTiers(types);
        Assert.assertTrue(f.getSelectedTiers().hasValues());
        Assert.assertTrue(f.getSelectedTiers().hasAll());
        Assert.assertFalse(f.getSelectedTiers().hasNone());
    }

    @Test
    public void setSelectAllWhenSomeTiersTrue() {
        DriverAnnotationAlterationFilter f = new DriverAnnotationAlterationFilter();
        Map<String, Boolean> types = new HashedMap();
        types.put("Class 1", false);
        types.put("Class 2", true);
        f.setSelectedTiers(types);
        Assert.assertTrue(f.getSelectedTiers().hasValues());
        Assert.assertFalse(f.getSelectedTiers().hasAll());
        Assert.assertFalse(f.getSelectedTiers().hasNone());
    }

    @Test
    public void setSelectAllWhenAllTiersFalse() {
        DriverAnnotationAlterationFilter f = new DriverAnnotationAlterationFilter();
        Map<String, Boolean> types = new HashedMap();
        types.put("Class 1", false);
        types.put("Class 2", false);
        f.setSelectedTiers(types);
        Assert.assertFalse(f.getSelectedTiers().hasValues());
        Assert.assertFalse(f.getSelectedTiers().hasAll());
        Assert.assertTrue(f.getSelectedTiers().hasNone());
    }
    
}