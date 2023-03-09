package com.tuitionmanager.project3;

/**
 * Custom Enum class that stores major, code, and school as Major.
 * The major is the name and the code and school are stored as
 * String properties in the Enum.
 *
 * @author Aryan Patel
 */
public enum Major {

    CS    ("01:198","SAS"),
    MATH  ("01:640","SAS"),
    EE    ("14:332","SOE"),
    ITI   ("04:547","SC&I"),
    BAIT  ("33:136","RBS");

    private final String code;
    private final String school;

    /**
     * This Constructs a Major Enum object with two String inputs, code and school.
     *
     * @param code String input which is the school's major code
     * @param school String input which is the school name
     */
    Major(String code, String school){
        this.code = code;
        this.school = school;
    }

    /**
     * This method gets the school name of a Major.
     *
     * @return String which contains the school name
     */
    public String getSchool(){
        return school;
    }

    /**
     * This method returns the major name, code, and school as a String
     * which will be used to display a Major on output.
     *
     * @return String representing major name, code and school of a Major
     */
    @Override
    public String toString() {
        return "(" + this.code + " " + this.name() + " " + this.school + ")";
    }
}
