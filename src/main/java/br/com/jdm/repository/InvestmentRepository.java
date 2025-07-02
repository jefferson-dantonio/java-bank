package br.com.jdm.repository;

import br.com.jdm.exceptions.AccountWithInvestmentException;
import br.com.jdm.exceptions.PixInUseException;
import br.com.jdm.exceptions.WalletNotFoundException;
import br.com.jdm.model.AccountWallet;
import br.com.jdm.model.Investment;
import br.com.jdm.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static br.com.jdm.repository.CommonRepository.checkFundsForTransaction;

public class InvestmentRepository {

    private long nextId;
    private final List<Investment> investments = new ArrayList<>();
    private  final  List<InvestmentWallet> wallets = new ArrayList<>();



    public Investment create(final long tax, final long initialFunds){
        this.nextId ++;
        var investment = new Investment(this.nextId,tax, tax, initialFunds);
        investments.add(investment);
        return investment;

    }

    public InvestmentWallet initialInvestment(final AccountWallet account, final long id){
        var accountsInUse = wallets.stream().map(InvestmentWallet::getAccount).toList();
        if (accountsInUse.contains(account)) throw new AccountWithInvestmentException("A conta " + account +  " já possui um investimento ativo.");
        var investment = findById(id);
        checkFundsForTransaction(account,investment.initialFunds());
        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet);
        return wallet;
    }

    public InvestmentWallet deposit(final String pix, final long funds){
        var wallet = findWalletByAccountPix(pix);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getService(),"Investimento");
        return wallet;
    }

    public InvestmentWallet withdraw(final String pix, final long funds){
        var wallet = findWalletByAccountPix(pix);
        checkFundsForTransaction(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds),wallet.getService(),
                "Saque de Investimentos");
        if(wallet.getFunds() == 0){
            wallets.remove(wallet);
        }
        return wallet;
    }

    public void updateAmount(final long percent){
        wallets.forEach(w -> w.updateAmount(percent));
    }

    public Investment findById(final long id) {
        return investments.stream().filter(a -> a.id() == id)
                .findFirst().orElseThrow(
                        () -> new WalletNotFoundException("Investimento não encontrado")
                );
    }
    public InvestmentWallet findWalletByAccountPix(final String pix){
        return  wallets.stream().filter(w -> w.getAccount().getPix().contains(pix))
                .findFirst().orElseThrow(
                        () -> new WalletNotFoundException("A carteira não foi encontrada")
                );
    }

    public List<InvestmentWallet> listWallets(){
        return  wallets;
    }

    public List<Investment> list() {
        return investments;
    }
}
