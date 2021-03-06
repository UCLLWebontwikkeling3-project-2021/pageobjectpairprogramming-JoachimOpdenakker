import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class RegisterTest {
	private WebDriver driver;
	private String path = "http://localhost:8080/Controller";
	
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Joachim\\Documents\\ucll\\web3\\web3-project-JoachimOpdenakker\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get(path+"?command=Register");
	}
	
	@After
	public void clean() {
	    driver.quit();
	}

	@Test
	public void test_Register_AllFieldsFilledInCorrectly_UserIsRegistered() {
		String useridRandom = generateRandomUseridInOrderToRunTestMoreThanOnce("jakke");
		submitForm(useridRandom, "Dirk", "Janssens", "dirk.janssens@hotmail.com" , "1234");
		
		String title = driver.getTitle();
		assertEquals("Overview",title);
		
		driver.get(path+"?command=Overview");
		
		ArrayList<WebElement> listItems=(ArrayList<WebElement>) driver.findElements(By.cssSelector("table tr"));
		boolean found=false;
		for (WebElement listItem:listItems) {
				if (listItem.getText().contains("dirk.janssens@hotmail.com") &&  listItem.getText().contains(" Dirk Janssens")) {
				    found=true;
			}
		}
		assertTrue(found);
	}
	
	@Test
	public void test_Register_UseridNotFilledIn_ErrorMessageGivenForUseridAndOtherFieldsValueKept(){
		submitForm("", "Jan", "Janssens", "jan.janssens@hotmail.com", "1234");
		
		String title = driver.getTitle();
		assertEquals("Sign Up",title);
		
		WebElement errorMsg = driver.findElement(By.cssSelector("div.alert-danger ul li"));
		assertEquals("No userid given", errorMsg.getText());
		
		WebElement fieldFirstName=driver.findElement(By.id("firstName"));
		assertEquals("Jan",fieldFirstName.getAttribute("value"));
		
		WebElement fieldLastName=driver.findElement(By.id("lastName"));
		assertEquals("Janssens",fieldLastName.getAttribute("value"));
		
		WebElement fieldEmail=driver.findElement(By.id("email"));
		assertEquals("jan.janssens@hotmail.com",fieldEmail.getAttribute("value"));
	}
	
	@Test
	public void test_Register_FirstNameNotFilledIn_ErrorMessageGivenForFirstNameAndOtherFieldsValueKept(){
		submitForm("jakke", "", "Janssens", "jan.janssens@hotmail.com", "1234");
		
		String title = driver.getTitle();
		assertEquals("Sign Up",title);
		
		WebElement errorMsg = driver.findElement(By.cssSelector("div.alert-danger ul li"));
		assertEquals("No firstname given", errorMsg.getText());

		WebElement fieldFirstName=driver.findElement(By.id("firstName"));
		assertEquals("",fieldFirstName.getAttribute("value"));
		
		WebElement fieldLastName=driver.findElement(By.id("lastName"));
		assertEquals("Janssens",fieldLastName.getAttribute("value"));
		
		WebElement fieldEmail=driver.findElement(By.id("email"));
		assertEquals("jan.janssens@hotmail.com",fieldEmail.getAttribute("value"));
	}

	@Test
	public void test_Register_LastNameNotFilledIn_ErrorMessageGivenForLastNameAndOtherFieldsValueKept(){
		submitForm("jakke", "Jan", "", "jan.janssens@hotmail.com", "1234");
		
		String title = driver.getTitle();
		assertEquals("Sign Up",title);
		
		WebElement errorMsg = driver.findElement(By.cssSelector("div.alert-danger ul li"));
		assertEquals("No lastname given", errorMsg.getText());

		WebElement fieldFirstName=driver.findElement(By.id("firstName"));
		assertEquals("Jan",fieldFirstName.getAttribute("value"));
		
		WebElement fieldLastName=driver.findElement(By.id("lastName"));
		assertEquals("",fieldLastName.getAttribute("value"));
		
		WebElement fieldEmail=driver.findElement(By.id("email"));
		assertEquals("jan.janssens@hotmail.com",fieldEmail.getAttribute("value"));
	}

	@Test
	public void test_Register_EmailNotFilledIn_ErrorMessageGivenForEmailAndOtherFieldsValueKept(){
		submitForm("jakke", "Jan", "Janssens", "", "1234");
		
		String title = driver.getTitle();
		assertEquals("Sign Up",title);

		WebElement errorMsg = driver.findElement(By.cssSelector("div.alert-danger ul li"));
		assertEquals("No email given", errorMsg.getText());
		
		WebElement fieldFirstName=driver.findElement(By.id("firstName"));
		assertEquals("Jan",fieldFirstName.getAttribute("value"));
		
		WebElement fieldLastName=driver.findElement(By.id("lastName"));
		assertEquals("Janssens",fieldLastName.getAttribute("value"));
		
		WebElement fieldEmail=driver.findElement(By.id("email"));
		assertEquals("",fieldEmail.getAttribute("value"));
	}


	@Test
	public void test_Register_PasswordNotFilledIn_ErrorMessageGivenForEmailAndOtherFieldsValueKept(){
		submitForm("jakke", "Jan", "Janssens", "jan.janssens@hotmail.com", "");
		
		String title = driver.getTitle();
		assertEquals("Sign Up",title);
		
		WebElement errorMsg = driver.findElement(By.cssSelector("div.alert-danger ul li"));
		assertEquals("No password given", errorMsg.getText());

		WebElement fieldFirstName=driver.findElement(By.id("firstName"));
		assertEquals("Jan",fieldFirstName.getAttribute("value"));
		
		WebElement fieldLastName=driver.findElement(By.id("lastName"));
		assertEquals("Janssens",fieldLastName.getAttribute("value"));
		
		WebElement fieldEmail=driver.findElement(By.id("email"));
		assertEquals("jan.janssens@hotmail.com",fieldEmail.getAttribute("value"));
	}
	
	@Test
	public void test_Register_UserAlreadyExists_ErrorMessageGiven(){
		String useridRandom = generateRandomUseridInOrderToRunTestMoreThanOnce("pierke");
		submitForm(useridRandom, "Pieter", "Pieters", "pieter.pieters@hotmail.com", "1234");
		
		driver.get(path+"?command=Register");

		submitForm(useridRandom, "Pieter", "Pieters", "pieter.pieters@hotmail.com", "1234");
		
		WebElement errorMsg = driver.findElement(By.cssSelector("div.alert-danger ul li"));
		assertEquals("User already exists", errorMsg.getText());
		
		WebElement fieldFirstName=driver.findElement(By.id("firstName"));
		assertEquals("Pieter",fieldFirstName.getAttribute("value"));
		
		WebElement fieldLastName=driver.findElement(By.id("lastName"));
		assertEquals("Pieters",fieldLastName.getAttribute("value"));
		
		WebElement fieldEmail=driver.findElement(By.id("email"));
		assertEquals("pieter.pieters@hotmail.com",fieldEmail.getAttribute("value"));
	}
	
		@Test
	public void test_Register_WhenAlready_Registered_and_Loggedin(){
		String useridRandom = generateRandomUseridInOrderToRunTestMoreThanOnce("test");
		submitForm( useridRandom,"Stephanie", "Serdons", "stephanie.serdons@hotmail.com", "test123");

		driver.get(path+"?command=Register");

		WebElement fieldMsg = driver.findElement(By.cssSelector("div.msg p"));
		assertEquals("You are already logged in!", fieldMsg.getText());
	}

	@Test
	public void test_Login_WhenAlready_Registered_and_Loggedin(){
		String useridRandom = generateRandomUseridInOrderToRunTestMoreThanOnce("test");
		submitForm( useridRandom,"Gerben", "Maes", "gerben.maes@hotmail.com", "test123");

		driver.get(path+"?command=Login");

		WebElement fieldMsg = driver.findElement(By.cssSelector("div.msg p"));
		assertEquals("You are already logged in!", fieldMsg.getText());

	private String generateRandomUseridInOrderToRunTestMoreThanOnce(String component) {
		int random = (int)(Math.random() * 1000 + 1);
		return random+component;
	}

	private void fillOutField(String name,String value) {
		WebElement field=driver.findElement(By.id(name));
		field.clear();
		field.sendKeys(value);
	}

	private void submitForm(String userid, String firstName,String lastName, String email, String password) {
		fillOutField("firstName", firstName);
		fillOutField("lastName",lastName);
		fillOutField("email", email);
		fillOutField("password", password);

		WebElement button=driver.findElement(By.id("signUp"));
		button.click();
	}

}
