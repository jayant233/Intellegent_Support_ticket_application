package playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckStatusPlaywrightTest extends BasePlaywrightTest {

    @Test
    @DisplayName("TC05 - Verify Check Status page loads")
    void testCheckStatusPageLoads() {
        page.navigate(BASE_URL + "/status");
        assertEquals("Support Ticket Management - Check Status", page.title());
        assertThat(page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Check Ticket Status")
        )).isVisible();
        assertThat(page.getByLabel("Enter Ticket ID:")).isVisible();
        assertThat(page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("View Status")
        )).isVisible();
    }

    @Test
    @DisplayName("TC06 - Verify ticket status can be searched")
    void testTicketStatusCanBeSearched() {
        page.navigate(BASE_URL + "/");
        page.getByLabel("Customer Name:").fill("Jane Smith");
        page.getByLabel("Ticket Description:").fill("Need access to the new staging environment.");
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        ).click();

        String ticketId = page.locator(".alert.success strong").textContent();

        page.navigate(BASE_URL + "/status");
        page.getByLabel("Enter Ticket ID:").fill(ticketId);
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("View Status")
        ).click();

        Locator ticketDetailsCard = page.locator(".ticket-details.card");
        assertThat(ticketDetailsCard).isVisible();
        assertThat(ticketDetailsCard).containsText(ticketId);
        assertThat(ticketDetailsCard).containsText("Jane Smith");
    }

    @Test
    @DisplayName("TC07 - Verify invalid Ticket ID behavior")
    void testInvalidTicketIdBehavior() {
        page.navigate(BASE_URL + "/status");
        page.getByLabel("Enter Ticket ID:").fill("INVALID-999");
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("View Status")
        ).click();

        assertThat(page.locator(".alert.error")).isVisible();
        assertThat(page.locator(".ticket-details.card")).not().isVisible();
    }
}
