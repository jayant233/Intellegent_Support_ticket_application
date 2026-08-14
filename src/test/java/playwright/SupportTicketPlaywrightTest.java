package playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Playwright UI Automation Tests for Support Ticket Management System.
 * Implemented using Playwright Java and JUnit 5.
 */
public class SupportTicketPlaywrightTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static Playwright playwright;
    private static Browser browser;
    private static Page page;

    @BeforeAll
    public static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @AfterAll
    public static void tearDown() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    // ==========================================
    // User / Submit Ticket Scenarios
    // ==========================================

    @Test
    @DisplayName("TC01 - Verify the Submit Ticket page loads successfully")
    void testSubmitTicketPageLoads() {
        // Step 1: Open http://localhost:8080/
        page.navigate(BASE_URL + "/");

        // Step 2: Verify the page title is "Support Ticket Management - Home"
        assertEquals("Support Ticket Management - Home", page.title());

        // Step 3: Verify the "Submit a New Ticket" heading is visible
        assertTrue(page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Submit a New Ticket")
        ).isVisible());
    }

    @Test
    @DisplayName("TC02 - Verify Submit Ticket form fields are displayed")
    void testSubmitTicketFormFieldsDisplayed() {
        // Step 1: Open the home page
        page.navigate(BASE_URL + "/");

        // Step 2: Verify Customer Name field is visible
        assertThat(page.getByLabel("Customer Name:")).isVisible();

        // Step 3: Verify Ticket Description field is visible
        assertThat(page.getByLabel("Ticket Description:")).isVisible();

        // Step 4: Verify Submit Ticket button is visible
        assertThat(page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        )).isVisible();
    }

    @Test
    @DisplayName("TC03 - Verify successful ticket submission")
    void testSuccessfulTicketSubmission() {
        // Step 1: Open the home page
        page.navigate(BASE_URL + "/");

        // Step 2: Enter a valid customer name
        page.getByLabel("Customer Name:").fill("John Doe");

        // Step 3: Enter a valid support-ticket description
        page.getByLabel("Ticket Description:").fill("Cannot login to my account. Password reset is not working.");

        // Step 4: Click Submit Ticket
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        ).click();

        // Step 5: Verify the success message is displayed
        Locator successAlert = page.locator(".alert.success");
        assertThat(successAlert).isVisible();

        // Step 6: Verify that a Ticket ID is generated
        Locator ticketIdElement = successAlert.locator("strong");
        assertThat(ticketIdElement).isVisible();

        // Step 7: Print the generated Ticket ID to the console
        String ticketId = ticketIdElement.textContent();
        System.out.println("Generated Ticket ID: " + ticketId);
    }

    @Test
    @DisplayName("TC04 - Verify mandatory field validation")
    void testMandatoryFieldValidation() {
        // Step 1: Open the home page
        page.navigate(BASE_URL + "/");

        // Step 2: Click Submit Ticket without entering any data
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        ).click();

        // Step 3: Verify the Customer Name field has required validation
        assertThat(page.getByLabel("Customer Name:")).hasAttribute("required", "");

        // Step 4: Verify the Ticket Description field has required validation
        assertThat(page.getByLabel("Ticket Description:")).hasAttribute("required", "");

        // Step 5: Verify that the success message is not displayed
        assertThat(page.locator(".alert.success")).not().isVisible();
    }

    // ==========================================
    // Ticket Status Scenarios
    // ==========================================

    @Test
    @DisplayName("TC05 - Verify Check Status page loads")
    void testCheckStatusPageLoads() {
        // Step 1: Open /status
        page.navigate(BASE_URL + "/status");

        // Step 2: Verify the page title is "Support Ticket Management - Check Status"
        assertEquals("Support Ticket Management - Check Status", page.title());

        // Step 3: Verify the "Check Ticket Status" heading
        assertThat(page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Check Ticket Status")
        )).isVisible();

        // Step 4: Verify the Ticket ID field
        assertThat(page.getByLabel("Enter Ticket ID:")).isVisible();

        // Step 5: Verify the View Status button
        assertThat(page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("View Status")
        )).isVisible();
    }

    @Test
    @DisplayName("TC06 - Verify ticket status can be searched")
    void testTicketStatusCanBeSearched() {
        // Step 1: Create a ticket through the UI
        page.navigate(BASE_URL + "/");
        page.getByLabel("Customer Name:").fill("Jane Smith");
        page.getByLabel("Ticket Description:").fill("Need access to the new staging environment.");
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        ).click();

        // Step 2: Capture the generated Ticket ID
        String ticketId = page.locator(".alert.success strong").textContent();

        // Step 3: Open /status
        page.navigate(BASE_URL + "/status");

        // Step 4: Enter the generated Ticket ID
        page.getByLabel("Enter Ticket ID:").fill(ticketId);

        // Step 5: Click View Status
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("View Status")
        ).click();

        // Step 6: Verify ticket details are displayed
        Locator ticketDetailsCard = page.locator(".ticket-details.card");
        assertThat(ticketDetailsCard).isVisible();

        // Step 7: Verify the generated Ticket ID is displayed
        assertThat(ticketDetailsCard).containsText(ticketId);

        // Step 8: Verify the customer name is displayed
        assertThat(ticketDetailsCard).containsText("Jane Smith");
    }

    @Test
    @DisplayName("TC07 - Verify invalid Ticket ID behavior")
    void testInvalidTicketIdBehavior() {
        // Step 1: Open /status
        page.navigate(BASE_URL + "/status");

        // Step 2: Enter an invalid Ticket ID
        page.getByLabel("Enter Ticket ID:").fill("INVALID-999");

        // Step 3: Click View Status
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("View Status")
        ).click();

        // Step 4: Verify an error message is displayed
        assertThat(page.locator(".alert.error")).isVisible();

        // Step 5: Verify ticket details are not displayed
        assertThat(page.locator(".ticket-details.card")).not().isVisible();
    }

    // ==========================================
    // Admin Dashboard Scenarios
    // ==========================================

    @Test
    @DisplayName("TC08 - Verify Admin Dashboard loads and displays tickets")
    void testAdminDashboardLoadsAndDisplaysTickets() {
        // Step 1: Open /admin
        page.navigate(BASE_URL + "/admin");

        // Step 2: Verify the "Manage Support Tickets" heading
        assertThat(page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Manage Support Tickets")
        )).isVisible();

        // Step 3: Verify the ticket table is displayed
        assertThat(page.locator("table.data-table")).isVisible();

        // Step 4: Verify important table headers
        Locator tableHeaders = page.locator("table.data-table thead th");
        assertThat(tableHeaders.nth(0)).hasText("Ticket ID");
        assertThat(tableHeaders.nth(1)).hasText("Customer");
        assertThat(tableHeaders.nth(6)).hasText("Current Status");
        assertThat(tableHeaders.nth(7)).hasText("Action");
    }

    @Test
    @DisplayName("TC09 - Verify Admin Dashboard filters tickets")
    void testAdminDashboardFiltersTickets() {
        // Step 1: Open /admin
        page.navigate(BASE_URL + "/admin");

        // Step 2: Select "Critical" from the severity dropdown
        Locator severityDropdown = page.locator("#severity");
        severityDropdown.selectOption("Critical");

        // Step 3: Click Apply Filters
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Apply Filters")
        ).click();

        // Step 4: Verify the URL contains the severity filter
        assertThat(page).hasURL(Pattern.compile("severity=Critical"));

        // Step 5: Verify the dropdown still has "Critical" selected
        assertThat(severityDropdown).hasValue("Critical");
    }

    @Test
    @DisplayName("TC10 - Verify administrator can update ticket status")
    void testAdministratorCanUpdateTicketStatus() {
        // Step 1: Create a ticket through the UI
        page.navigate(BASE_URL + "/");
        page.getByLabel("Customer Name:").fill("Admin Tester");
        page.getByLabel("Ticket Description:").fill("Testing status update.");
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit Ticket")
        ).click();

        // Step 2: Capture the generated Ticket ID
        String ticketId = page.locator(".alert.success strong").textContent();

        // Step 3: Open /admin
        page.navigate(BASE_URL + "/admin");

        // Step 4: Search for the generated Ticket ID
        page.getByLabel("Search by ID:").fill(ticketId);
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Apply Filters")
        ).click();

        // Step 5: Locate that ticket row
        Locator ticketRow = page.locator("tr").filter(new Locator.FilterOptions().setHasText(ticketId));

        // Step 6: Verify the current status is "Open"
        assertThat(ticketRow.locator("td").nth(6)).hasText("Open");

        // Step 7: Change the status to "In Progress"
        Locator statusSelect = ticketRow.locator(".status-select");
        statusSelect.selectOption("In Progress");

        // Step 8: Click Update
        ticketRow.getByRole(
                AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Update")
        ).click();

        // Step 9: Verify that the ticket status changes to "In Progress"
        Locator updatedTicketRow = page.locator("tr").filter(new Locator.FilterOptions().setHasText(ticketId));
        assertThat(updatedTicketRow.locator("td").nth(6)).hasText("In Progress");
    }
}
