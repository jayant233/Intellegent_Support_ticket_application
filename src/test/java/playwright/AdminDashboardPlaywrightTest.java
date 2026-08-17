package playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminDashboardPlaywrightTest extends BasePlaywrightTest {

    @Test
    @DisplayName("TC08 - Verify Admin Dashboard loads and displays tickets")
    void testAdminDashboardLoadsAndDisplaysTickets() {
        page.navigate(BASE_URL + "/admin");
        assertThat(page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Manage Support Tickets")
        )).isVisible();
        assertThat(page.locator("table.data-table")).isVisible();
        Locator tableHeaders = page.locator("table.data-table thead th");
        assertThat(tableHeaders.nth(0)).hasText("Ticket ID");
        assertThat(tableHeaders.nth(1)).hasText("Customer");
        assertThat(tableHeaders.nth(6)).hasText("Current Status");
        assertThat(tableHeaders.nth(7)).hasText("Action");
    }

    @Test
    @DisplayName("TC09 - Verify Admin Dashboard filters tickets")
    void testAdminDashboardFiltersTickets() {
        page.navigate(BASE_URL + "/admin");
        Locator severityDropdown = page.locator("#severity");
        severityDropdown.selectOption("Critical");
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Apply Filters")
        ).click();
        assertThat(page).hasURL(Pattern.compile("severity=Critical"));
        assertThat(severityDropdown).hasValue("Critical");
    }

    @Test
    @DisplayName("TC10 - Verify administrator can update ticket status")
    void testAdministratorCanUpdateTicketStatus() {
        page.navigate(BASE_URL + "/");
        page.getByLabel("Customer Name:").fill("Admin Tester");
        page.getByLabel("Ticket Description:").fill("Testing status update.");
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        ).click();

        String ticketId = page.locator(".alert.success strong").textContent();

        page.navigate(BASE_URL + "/admin");
        page.getByLabel("Search by ID:").fill(ticketId);
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Apply Filters")
        ).click();

        Locator ticketRow = page.locator("tr").filter(new Locator.FilterOptions().setHasText(ticketId));
        assertThat(ticketRow.locator("td").nth(6)).hasText("Open");

        Locator statusSelect = ticketRow.locator(".status-select");
        statusSelect.selectOption("In Progress");

        ticketRow.getByRole(
                AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Update")
        ).click();

        Locator updatedTicketRow = page.locator("tr").filter(new Locator.FilterOptions().setHasText(ticketId));
        assertThat(updatedTicketRow.locator("td").nth(6)).hasText("In Progress");
    }
}
