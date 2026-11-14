package ma.fluxpay.finance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ma.fluxpay.finance.repositories.*;

@SpringBootTest(properties = {
    "spring.autoconfigure.exclude=" +
        "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
        "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration," +
        "org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration"
})
class FinancialCoreApplicationTests {

    @MockitoBean
    private WalletRepository walletRepository;

    @MockitoBean
    private LedgerAccountRepository ledgerAccountRepository;

    @MockitoBean
    private LedgerTransactionRepository ledgerTransactionRepository;

    @MockitoBean
    private LedgerEntryRepository ledgerEntryRepository;

    @MockitoBean
    private FundsReservationRepository fundsReservationRepository;

    @MockitoBean
    private IdempotencyRecordRepository idempotencyRecordRepository;

    @Test
    void contextLoads() {
    }
}
