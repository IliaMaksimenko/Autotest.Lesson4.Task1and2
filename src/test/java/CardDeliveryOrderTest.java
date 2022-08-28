import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeOptions;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTest {

    @BeforeEach
    public void setup(){

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
//        Configuration.headless = true;
        open("http://localhost:9999");

    }

    @Test
    public void shouldSendDeliveryOrderForm() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date newDate = calendar.getTime();

        $("[data-test-id=\"city\"] input").setValue("Москва");
        SelenideElement date = $("[data-test-id=\"date\"] input");
        date.sendKeys(Keys.CONTROL + "A");
        date.sendKeys(Keys.DELETE);
        date.setValue(formatter.format(newDate));
        $("[data-test-id=\"name\"] input").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] input").setValue("+79111111111");
        $("[data-test-id=\"agreement\"] .checkbox__box").click();
        $(By.className("button__content")).click();

        $("[data-test-id=\"notification\"] .notification__content").shouldBe(Condition.visible, Duration.ofMillis(15000))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + formatter.format(newDate)));

    }


    @Test
    public void shouldWorkManualSelection() {

        $("[data-test-id=\"city\"] input").setValue("Мо");
        $x("//*[text()=\"Москва\"]").shouldBe(Condition.appear, Duration.ofMillis(1000)).click();
        $("[data-test-id=\"date\"] input").click();
        $("[data-step=\"1\"]").shouldBe(Condition.appear, Duration.ofMillis(1000)).click();
        $("[data-day='1662238800000']").shouldBe(Condition.appear, Duration.ofMillis(1000)).click();
        $("[data-test-id=\"name\"] input").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] input").setValue("+79111111111");
        $("[data-test-id=\"agreement\"] .checkbox__box").click();
        $(By.className("button__content")).click();
        $("[data-test-id=\"notification\"] .notification__title").shouldBe(Condition.visible, Duration.ofMillis(15000))
                .shouldHave(Condition.exactText("Успешно!"));
    }
}
