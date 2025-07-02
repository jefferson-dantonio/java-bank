package br.com.jdm.model;


import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static br.com.jdm.model.BankService.INVESTMENT;

@Getter
public class InvestmentWallet extends Wallet{
    private final Investment investment;
    private final AccountWallet account;

    public InvestmentWallet(Investment investment, AccountWallet account, final long amount) {
        super(INVESTMENT);
        this.investment = investment;
        this.account = account;
        addMoney(account.reduceMoney(amount),getService(),"investimento");

    }

    public void updateAmount(final long percent){
        var amount = getFunds() * percent / 100;
        var history = new MoneyAudit(UUID.randomUUID(), getService(), "rendimento", OffsetDateTime.now());
        var money = Stream.generate(() -> new Money(history)).limit(amount).toList();
        this.money.addAll(money);
    }

    public String toString() {
        return "InvestmentWallet{" +
                ", account= pix:" + account.getPix() +
                ", money= R$" + money.size() +
                '}';
    }
}
