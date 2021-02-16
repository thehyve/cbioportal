package org.cbioportal.model.util;

import org.cbioportal.model.AlterationFilter;
import org.cbioportal.model.DriverAnnotationAlterationFilter;
import org.cbioportal.model.MutationStatusAlterationFilter;
import org.junit.Assert;
import org.junit.Test;

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

}