package in.ankitsaahariya.WalletLedger.services;

import in.ankitsaahariya.WalletLedger.dto.ExpenseDTO;
import in.ankitsaahariya.WalletLedger.entity.ProfileEntity;
import in.ankitsaahariya.WalletLedger.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServices {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontedUrl;

    @Scheduled(cron = "0 0 22 * * *", zone = "IST")
//@Scheduled(cron = "0 * * * * *", zone = "IST")  // for every minutes
    public void sendDailyIncomeExpenseReminder()   {

        log.info("Job started : sendDailyIncomeExpenseReminder()");

        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {

            String body =
                    "<html>" +
                            "<body>" +
                            "<h3>Hi " + profile.getFullName() + ",</h3>" +
                            "<p>This is your daily reminder.</p>" +
                            "<p>Please add today’s income and expenses.</p>" +
                            "<br>" +
                            "<a href='" + frontedUrl + "'>Open WalletLedger</a>" +
                            "<br><br>" +
                            "<p>Thanks,<br>WalletLedger Team</p>" +
                            "</body>" +
                            "</html>";

            emailService.sendEmail(
                    profile.getEmail(),
                    "Daily Reminder : Add your income Expenses ",
                    body
            );
        }

        log.info("Job finished : sendDailyIncomeExpenseReminder()");
    }


//    for Daily  Expenses Summary
//@Scheduled(cron = "0 * * * * *", zone = "IST")   for every minutes
    @Scheduled(cron = "0 0 23 * * *", zone = "IST")
public void sendDailyExpenseSummary() {
    log.info("Job started : sendDailyExpenseSummary()");

    List<ProfileEntity> profiles = profileRepository.findAll();
    for (ProfileEntity profile : profiles) {

        List<ExpenseDTO> todayExpenses =
                expenseService.getExpensesForUserOnDate(
                        profile.getId(),
                        LocalDate.now()
                );

        if (todayExpenses.isEmpty()) {
            continue;
        }

        StringBuilder table = new StringBuilder();

        table.append("<html><body>");
        table.append("<h3>Hi ").append(profile.getFullName()).append(",</h3>");
        table.append("<p>Here is your expense summary for today.</p>");

        table.append("<table border='1' cellpadding='8' cellspacing='0'>");

        // Header row
        table.append("<tr>");
        table.append("<th>S.No.</th>");
        table.append("<th>Name</th>");
        table.append("<th>Category</th>");
        table.append("<th>Amount</th>");
        table.append("<th>Date</th>");
        table.append("</tr>");

        int serialNo = 1;

        // Data rows
        for (ExpenseDTO expense : todayExpenses) {
            table.append("<tr>");
            table.append("<td>").append(serialNo++).append("</td>");
            table.append("<td>").append(expense.getName()).append("</td>");
            table.append("<td>").append(expense.getCategoryName()).append("</td>");
            table.append("<td>").append(expense.getAmount()).append("</td>");
            table.append("<td>").append(expense.getDate()).append("</td>");
            table.append("</tr>");
        }

        table.append("</table>");
        table.append("<br>");
        table.append("<p>Thanks,<br>WalletLedger Team</p>");
        table.append("</body></html>");

        emailService.sendEmail(
                profile.getEmail(),
                "Today Expense Summary",
                table.toString()
        );
    }

    log.info("Job finished : sendDailyExpenseSummary()");
}

}
