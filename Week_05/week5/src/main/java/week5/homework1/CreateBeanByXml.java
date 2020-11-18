package week5.homework1;

/**
 * 通过XML文件配置bean
 */
public class CreateBeanByXml {
    String createType;

    public void init() {
        System.out.println("createBeanByXml : " + this.createType);
    }

    public CreateBeanByXml() {
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public CreateBeanByXml(String createType) {
        this.createType = createType;
    }
}
