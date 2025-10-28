public class Conta {
    private int saldo = 1000;

    // Objetos usados como travas específicas
    private final Object lockSaque = new Object();
    private final Object lockDeposito = new Object();

    // Método sincronizado apenas no lock de saque
    public void sacar(int valor) {
        synchronized (lockSaque) {
            System.out.println(Thread.currentThread().getName() + " tentando sacar " + valor);
            if (saldo >= valor) {
                try {
                    Thread.sleep(500); // simula tempo de processamento
                } catch (InterruptedException e) {}
                saldo -= valor;
                System.out.println(Thread.currentThread().getName() + " sacou " + valor + ". Saldo atual: " + saldo);
            } else {
                System.out.println(Thread.currentThread().getName() + " tentou sacar, mas saldo insuficiente!");
            }
        }
    }

    // Método sincronizado apenas no lock de depósito
    public void depositar(int valor) {
        synchronized (lockDeposito) {
            System.out.println(Thread.currentThread().getName() + " depositando " + valor);
            try {
                Thread.sleep(500); // simula tempo de processamento
            } catch (InterruptedException e) {}
            saldo += valor;
            System.out.println(Thread.currentThread().getName() + " terminou depósito. Saldo atual: " + saldo);
        }
    }

    public int getSaldo() {
        return saldo;
    }

    // Método principal para testar
    public static void main(String[] args) throws InterruptedException {
        Conta conta = new Conta();

        // Cria múltiplas threads
        Thread t1 = new Thread(() -> conta.sacar(300), "T1");
        Thread t2 = new Thread(() -> conta.depositar(500), "T2");
        Thread t3 = new Thread(() -> conta.depositar(200), "T3");
        Thread t4 = new Thread(() -> conta.sacar(100), "T4");

        // Inicia todas
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // Espera todas terminarem
        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("\nSaldo final: " + conta.getSaldo());
    }
}