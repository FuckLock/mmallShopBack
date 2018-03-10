import com.mmall.controller.ProductManageController;
import com.mmall.service.impl.ProductServiceImpl;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class controllerAOPTest {

    @Test
    public void testAop() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
        ctx.start();
        ProductManageController token = (ProductManageController)ctx.getBean(ProductManageController.class);
        token.test();
    }
}
