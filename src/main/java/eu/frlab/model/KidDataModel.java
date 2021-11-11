package eu.frlab.model;

public class KidDataModel {

    private final String kidName;
    private final String kidSurname;
    private final String kidDayOfBirth;
    private final String kidMonthOfBirth;
    private final String kidYearOfBirth;
    private final String kidAmka;

    private final String schoolRegion;
    private final String schoolDivision;
    private final String schoolMunicipality;
    private final String schoolCategory;
    private final String schoolType;
    private final String schoolName;

    public KidDataModel(String[] kid, String[] school) {
        this.kidName = kid[0];
        this.kidSurname = kid[1];
        this.kidDayOfBirth = kid[2];
        this.kidMonthOfBirth = kid[3];
        this.kidYearOfBirth = kid[4];
        this.kidAmka = kid[5];

        this.schoolRegion = school[0];
        this.schoolDivision = school[1];
        this.schoolMunicipality = school[2];
        this.schoolCategory = school[3];
        this.schoolType = school[4];
        this.schoolName = school[5];
    }

    public String getKidName() {
        return kidName;
    }

    public String getKidSurname() {
        return kidSurname;
    }

    public String getKidDayOfBirth() {
        return kidDayOfBirth;
    }

    public String getKidMonthOfBirth() {
        return kidMonthOfBirth;
    }

    public String getKidYearOfBirth() {
        return kidYearOfBirth;
    }

    public String getKidAmka() {
        return kidAmka;
    }

    public String getSchoolRegion() {
        return schoolRegion;
    }

    public String getSchoolDivision() {
        return schoolDivision;
    }

    public String getSchoolMunicipality() {
        return schoolMunicipality;
    }

    public String getSchoolCategory() {
        return schoolCategory;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getKidFullName() {
        return String.format("%s %s", kidName, kidSurname);
    }

    @Override
    public String toString() {
        String kid = String.format("%s %s, %s/%s/%s%nAMKA: %s", kidName, kidSurname, kidDayOfBirth, kidMonthOfBirth, kidYearOfBirth, kidAmka);
        String school = String.format("%s, %s, %s%n%s, %s%n%s", schoolRegion, schoolDivision, schoolMunicipality, schoolCategory, schoolType, schoolName);
        return kid + "\n" + school;
    }
}
