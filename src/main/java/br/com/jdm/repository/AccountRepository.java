package br.com.jdm.repository;

import br.com.jdm.exceptions.AccountNotFoundException;
import br.com.jdm.exceptions.PixInUseException;
import br.com.jdm.model.AccountWallet;
import java.util.ArrayList;
import java.util.List;
import static br.com.jdm.repository.CommonRepository.checkFundsForTransaction;


public class AccountRepository {

    private final List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final long initialFunds){
        if(!accounts.isEmpty()) {
            var pixInUse = accounts.stream().flatMap(a -> a.getPix().stream()).toList();
            pix.forEach(p -> {
                if (pixInUse.contains(p)) throw new PixInUseException("A chave pix " + p + " já está em uso.");
            });
        }
        var newAccount = new AccountWallet(initialFunds,pix);
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount){
        var target = findByPix(pix);
        target.addMoney(fundsAmount, "depósito");
    }


    public long withdraw(final String pix, final long amount){
        var source = findByPix(pix);
        checkFundsForTransaction(source, amount);
        source.reduceMoney(amount);
        return amount;

    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount){
        var source = findByPix(sourcePix);
        checkFundsForTransaction(source, amount);
        var target = findByPix(targetPix);
        var message = String.format("Pix enviado de '%s' para '%s' no valor de %s",sourcePix, targetPix, amount);
        target.addMoney(source.reduceMoney(amount), source.getService(), message);

    }

    public AccountWallet findByPix(final String pix){
        return  accounts.stream().filter(a -> a.getPix().contains(pix))
                .findFirst().orElseThrow(() -> new AccountNotFoundException("A conta com a chave pix " + pix + "Não foi encontrada."));
    };

    public List<AccountWallet> list(){
        return this.accounts;
    }


}
