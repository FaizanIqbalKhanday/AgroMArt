package in.codecubes.agromart;

public class Member {
    private String selectedVariety;
    private String selectedGrade;
    private String selectedPacking;
    private String state;
    private String district;

    public Member(){

    }
    public Member(String selectedVariety, String selectedGrade,String selectedPacking){
        this.selectedVariety=selectedVariety;
        this.selectedGrade=selectedGrade;
        this.selectedPacking=selectedPacking;
    }

    public String getSelectedVariety() {
        return selectedVariety;
    }

    public void setSelectedVariety(String selectedVariety) {
        this.selectedVariety = selectedVariety;
    }

    public String getSelectedGrade() {
        return selectedGrade;
    }

    public void setSelectedGrade(String selectedGrade) {
        this.selectedGrade = selectedGrade;
    }

    public String getSelectedPacking() {
        return selectedPacking;
    }

    public void setSelectedPacking(String selectedPacking) {
        this.selectedPacking = selectedPacking;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
