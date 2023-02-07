package rostermanager;

/**
 *
 * Needs Comments
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

    Major(String code, String school){
        this.code = code;
        this.school = school;
    }

    private String getCode(){
        return code;
    }

    private String getSchool(){
        return school;
    }

    /* possibly needs to string method */

}
