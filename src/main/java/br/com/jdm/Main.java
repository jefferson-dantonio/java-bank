package br.com.jdm;

import br.com.jdm.exceptions.AccountNotFoundException;
import br.com.jdm.exceptions.AccountWithInvestmentException;
import br.com.jdm.exceptions.NoFundsEnoughException;
import br.com.jdm.repository.AccountRepository;
import br.com.jdm.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;

import static java.time.temporal.ChronoUnit.SECONDS;


public class Main {

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Olá, Seja bem-vindo ao JDM Bank!");
        System.out.println("Estamos felizes em tê-lo conosco.");
        var option = 0;

        while (option != 14){
            System.out.println("Selecione a operação desejada:");
            System.out.println("1 - Criar Conta");
            System.out.println("2 - Criar uma conta de investimento");
            System.out.println("3 - Fazer um investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - Sacar da conta");
            System.out.println("6 - Transferir entre contas");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar de um investimento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar investimentos");
            System.out.println("11 - Listar carteiras de investimento");
            System.out.println("12 - Atualizar investimento");
            System.out.println("13 - Historico de conta");
            System.out.println("14 -Sair do sistema");
            option = scanner.nextInt();
            switch (option){
                case 1:
                    createAccount();
                    break;
                case 2:
                    createInvestment();
                    break;
                case 3:
                    createWalletInvestment();;
                    break;
                case 4:
                    deposit();
                    break;
                case 5:
                    withdraw();
                    break;
                case 6:
                    transferToAccount();
                    break;
                case 7:
                    incInvestment();
                    break;
                case 8:
                    rescueInvestment();
                    break;
                case 9:
                    accountRepository.list().forEach(System.out::println);
                    break;
                case 10:
                    investmentRepository.list().forEach(System.out::println);
                    break;
                case 11:
                    investmentRepository.listWallets().forEach(System.out::println);
                    break;
                case 12:
                    investmentRepository.updateAmount();
                    System.out.println("Investimentos atualizados com sucesso!");
                    break;
                case 13:
                    checkHistory();
                    break;
                case 14:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }


        }

    }

    private static void createAccount() {
        System.out.println("Infome as chaves pix (separadas por ';'):");
        var pix = Arrays.stream(scanner.next().split(";")).toList();
        System.out.println("Informe o valor inicial de deposito:");
        var amount = scanner.nextLong();
        var wallet = accountRepository.create(pix, amount);
        System.out.println("Conta criada: " + wallet);
    }


    private static void createInvestment() {
        System.out.println("Informe a taxa de investimento:");
        var tax = scanner.nextInt();
        System.out.println("Informe o valor inicial de deposito:");
        var initialFunds = scanner.nextLong();
        var investment = investmentRepository.create(tax, initialFunds);
        System.out.println("Investimento " + investment + " criado com sucesso!");
    }

    private static void withdraw() {
        System.out.println("Informe a chave pix da conta :");
        var pix = scanner.next();
        System.out.println("Informe o valor do saque:");
        var amount = scanner.nextLong();
        try {
            accountRepository.withdraw(pix, amount);
            System.out.printf("Saque de %d realizado com sucesso na conta com a chave pix %s.%n", amount, pix);
        } catch (AccountNotFoundException | NoFundsEnoughException e) {
            System.out.println("Erro ao realizar o saque: " + e.getMessage());
        }
    }


    private static void deposit(){
        System.out.println("Informe a chave pix da conta :");
        var pix = scanner.next();
        System.out.println("Informe o valor a ser depositado:");
        var amount = scanner.nextLong();
        try {
            accountRepository.deposit(pix, amount);
            System.out.printf("Depósito de %d realizado com sucesso na conta com a chave pix %s.%n", amount, pix);
        }catch (AccountNotFoundException e) {
            System.out.println("Erro ao realizar o depósito: " + e.getMessage());
        }
    }

    private static void transferToAccount(){
        System.out.println("Informe a chave pix da conta de origem :");
        var source = scanner.next();
        System.out.println("Informe a chave pix da conta de destino :");
        var target = scanner.next();
        System.out.println("Informe o valor a ser transferido:");
        var amount = scanner.nextLong();
        try {
            accountRepository.transferMoney(source,target, amount);
            System.out.printf("Transferência de %d realizada com sucesso da conta com a chave pix %s para a conta com a chave pix %s.%n", amount, source, target);
        }catch (AccountNotFoundException e) {
            System.out.println("Erro ao realizar a transferencia: " + e.getMessage());
        }
    }

    private static void createWalletInvestment() {
        System.out.println("Informe a chave pix da conta: ");
        var pix = scanner.next();
        var account = accountRepository.findByPix(pix);
        System.out.println("Informe identificador do investimento: ");
        var investmentId = scanner.nextLong();
        try {
            investmentRepository.initialInvestment(account, investmentId);
            System.out.printf("Investimento inicializado com sucesso na conta com a chave pix %s para o investimento de id %d.%n", pix, investmentId);
        } catch (AccountNotFoundException | NoFundsEnoughException | AccountWithInvestmentException e) {
            System.out.println("Erro ao inicializar o investimento: " + e.getMessage());
        }


    }

    private static void incInvestment(){
        System.out.println("Informe a chave pix da conta :");
        var pix = scanner.next();
        System.out.println("Informe o valor a ser investido:");
        var amount = scanner.nextLong();
        try {
            investmentRepository.deposit(pix, amount);
            System.out.printf("Investimento de %d realizado com sucesso ", amount);
        }catch (AccountNotFoundException e) {
            System.out.println("Erro ao realizar o investimento: " + e.getMessage());
        }
    }

    private static void rescueInvestment() {
        System.out.println("Informe a chave pix da conta :");
        var pix = scanner.next();
        System.out.println("Informe o valor do saque:");
        var amount = scanner.nextLong();
        try {
            investmentRepository.withdraw(pix, amount);
            System.out.printf("Saque de %d realizado com sucesso na conta com a chave pix %s.%n", amount, pix);
        } catch (AccountNotFoundException | NoFundsEnoughException e) {
            System.out.println("Erro ao realizar o saque: " + e.getMessage());
        }
    }

    private static void checkHistory() {
        System.out.println("Informe a chave pix da conta :");
        var pix = scanner.next();

        try {
            var account = accountRepository.findByPix(pix);
            System.out.println("Histórico de transações da conta com a chave pix " + pix + ":");
            account.getFinancialTransactions().forEach(
                    transaction -> System.out.printf("ID: %s, Serviço: %s, Descrição: %s, Data: %s%n",
                            transaction.transactionId(),
                            transaction.targetService(),
                            transaction.description(),
                            transaction.createAt().truncatedTo(SECONDS))
            );

        } catch (AccountNotFoundException e) {
            System.out.println("Erro ao buscar histórico: " + e.getMessage());
        }


    }




}