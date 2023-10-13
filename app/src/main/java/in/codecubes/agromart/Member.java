package in.codecubes.agromart;

public class Member {
    private String variety;
    private String grade;
    private String packingType;
    private String state;
    private String district;
    private String quantity;
    private  String village;

    public Member(){

    }
    public Member(String variety, String grade,String packingType, String quantity){
       this.variety=variety;
       this.grade=grade;
       this.packingType=packingType;
       this.quantity=quantity;
    }
    public Member(String village){
        this.village=village;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSelectedVariety() {
        return variety;
    }

    public void setSelectedVariety(String selectedVariety) {
        this.variety = selectedVariety;
    }

    public String getSelectedGrade() {
        return grade;
    }

    public void setSelectedGrade(String selectedGrade) {
        this.grade = selectedGrade;
    }

    public String getSelectedPacking() {
        return packingType;
    }

    public void setSelectedPacking(String selectedPacking) {
        this.packingType = selectedPacking;
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
