/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mskcc.cbio.portal.model;

import java.io.Serializable;

/**
 *
 * @author dionnezaal
 */
public class DBClinicalFieldWithStats implements Serializable {
    public String attr_id;
    public String display_name;
    public String description;
    public String datatype;
    public String is_patient_attribute;
    public String priority;
    public Integer attribute_count;
}
