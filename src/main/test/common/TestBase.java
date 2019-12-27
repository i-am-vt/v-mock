package common;

import com.vmock.VMockApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * the base class for junit test
 *
 * @author vt
 * @since 2019/11/24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VMockApplication.class)
public class TestBase {

}