package week5.homework1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateBeanByAnnotation {

    String createType;

    public CreateBeanByAnnotation() {
    }

    public CreateBeanByAnnotation(String createType) {
        this.createType = createType;
    }

    public void init() {
        System.out.println("createBeanByAnnotation : " + this.createType);
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }
}
