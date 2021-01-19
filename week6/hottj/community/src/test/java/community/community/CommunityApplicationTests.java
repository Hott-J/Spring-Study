//package community.community;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(value = "value=test", properties = {"property.value=propertyTest"},
//		classes = {CommunityApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//
//class CommunityApplicationTests {
//
//	@Value("${value|")
//	private String value;
//
//	@Value("${property.value}")
//	private String propertyValue;
//
//	@Test
//	void contextLoads() {
//		assertThat(value,is("test"));
//		assertThat(propertyValue,is("propertyTest"));
//	}
//}
