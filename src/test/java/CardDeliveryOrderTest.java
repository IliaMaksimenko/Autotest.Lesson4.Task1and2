import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String generateDays(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd"));
    }

    @BeforeEach
    public void setup() {

//        Configuration.headless = true;
        open("http://localhost:9999");

    }

    @Test
    public void shouldSendDeliveryOrderForm() {

        String planningDate = generateDate(7);

        $("[data-test-id=\"city\"] input").setValue("Москва");
        SelenideElement date = $("[data-test-id=\"date\"] input");
        date.sendKeys(Keys.CONTROL + "A");
        date.sendKeys(Keys.DELETE);
        date.setValue(planningDate);
        $("[data-test-id=\"name\"] input").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] input").setValue("+79111111111");
        $("[data-test-id=\"agreement\"] .checkbox__box").click();
        $(By.className("button__content")).click();

        $("[data-test-id=\"notification\"] .notification__content").shouldBe(Condition.visible, Duration.ofMillis(15000))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));

    }


    @Test
    public void shouldWorkManualSelection() {

        int planningDays = Integer.parseInt(generateDays(7));

        $("[data-test-id=\"city\"] input").setValue("Мо");
        $x("//*[@class=\"menu-item__control\"] [text()=\"Москва\"]").shouldBe(Condition.appear, Duration.ofMillis(1000)).click();
        $("[data-test-id=\"date\"] input").click();

        SelenideElement currentDay = $x("//*[@class=\"calendar__day calendar__day_state_current\"]");
        currentDay.shouldBe(Condition.appear, Duration.ofMillis(10000));
        int intCurrentDay = Integer.parseInt(currentDay.getText());

        if (planningDays < intCurrentDay) {
            $("[data-step=\"1\"]").shouldBe(Condition.appear, Duration.ofMillis(1000)).click();
        }

        $x("//*[@role=\"gridcell\"] [text()=" + planningDays + "]").shouldBe(Condition.appear, Duration.ofMillis(1000)).click();
        $("[data-test-id=\"name\"] input").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] input").setValue("+79111111111");
        $("[data-test-id=\"agreement\"] .checkbox__box").click();
        $(By.className("button__content")).click();
        $("[data-test-id=\"notification\"] .notification__title").shouldBe(Condition.visible, Duration.ofMillis(15000))
                .shouldHave(Condition.exactText("Успешно!"));
    }
}
