package playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubmitTicketPlaywrightTest extends BasePlaywrightTest {

    @Test
    @DisplayName("TC01 - Verify the Submit Ticket page loads successfully")
    void testSubmitTicketPageLoads() {
        page.navigate(BASE_URL + "/");
        assertEquals("Support Ticket Management - Home", page.title());
        assertTrue(page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Submit a New Ticket")
        ).isVisible());
    }

    @Test
    @DisplayName("TC02 - Verify Submit Ticket form fields are displayed")
    void testSubmitTicketFormFieldsDisplayed() {
        page.navigate(BASE_URL + "/");
        assertThat(page.getByLabel("Customer Name:")).isVisible();
        assertThat(page.getByLabel("Ticket Description:")).isVisible();
        assertThat(page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        )).isVisible();
    }

    @Test
    @DisplayName("TC03 - Verify successful ticket submission")
    void testSuccessfulTicketSubmission() {
        page.navigate(BASE_URL + "/");
        page.getByLabel("Customer Name:").fill("John Doe");
        page.getByLabel("Ticket Description:").fill("Cannot login to my account. Password reset is not working.");
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        ).click();

        Locator successAlert = page.locator(".alert.success");
        assertThat(successAlert).isVisible();
        Locator ticketIdElement = successAlert.locator("strong");
        assertThat(ticketIdElement).isVisible();
        String ticketId = ticketIdElement.textContent();
        System.out.println("Generated Ticket ID: " + ticketId);
    }

    @Test
    @DisplayName("TC04 - Verify mandatory field validation")
    void testMandatoryFieldValidation() {
        page.navigate(BASE_URL + "/");
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        ).click();

        assertThat(page.getByLabel("Customer Name:")).hasAttribute("required", "");
        assertThat(page.getByLabel("Ticket Description:")).hasAttribute("required", "");
        assertThat(page.locator(".alert.success")).not().isVisible();
    }
}
