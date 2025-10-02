import cn.yshujia.ServerApplication;
import cn.yshujia.mapper.FileMapper;
import cn.yshujia.mapper.LabelMapper;
import cn.yshujia.mapper.LeaveWordMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yshujia
 * @create 2024/12/18
 * @description ServerApplicationTests
 */

@SpringBootTest(classes = ServerApplication.class)
public class ServerApplicationTests {

	@Resource
	LeaveWordMapper leaveWordMapper;

	@Resource
	FileMapper fileMapper;

	@Resource
	LabelMapper labelMapper;

	@Test
	public void test() {

	}

}
