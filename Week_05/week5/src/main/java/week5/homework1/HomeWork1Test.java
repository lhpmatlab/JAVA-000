package week5.homework1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HomeWork1Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        //通过xml文件配置bean
        CreateBeanByXml createBeanByXml = (CreateBeanByXml) context.getBean("createBeanByXml");
        createBeanByXml.init();

        //通过注解的方式配置bean
        CreateBeanByAnnotation createBeanByAnnotation = context.getBean(CreateBeanByAnnotation.class);
        createBeanByAnnotation.setCreateType("annotation");
        createBeanByAnnotation.init();
    }
}
