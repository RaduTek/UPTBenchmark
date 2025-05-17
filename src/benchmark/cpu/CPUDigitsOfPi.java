package benchmark.cpu;

import bench.IBenchmark;
import java.math.BigDecimal;
import java.math.MathContext;

public class CPUDigitsOfPi implements IBenchmark {
    private int digits;
    private BigDecimal pi;
    private boolean running;

    @Override
    public String getName() {
        return "CPUDigitsOfPi";
    }

    @Override
    public void initialize(Object... params) {
        if (params.length > 0 && params[0] instanceof Integer) {
            digits = (Integer) params[0];
        } else {
            digits = 1000; // default
        }
        running = true;
        pi = null;
    }

    @Override
    public void warmup() {
        computePi(100); // warmup with 100 digits
    }

    @Override
    public void run() {
        if (!running) return;
        pi = computePi(digits);
    }

    @Override
    public void run(Object... params) {
        run();
    }

    @Override
    public void clean() {
        pi = null;
    }

    @Override
    public void cancel() {
        running = false;
    }

    private BigDecimal computePi(int digits) {
        MathContext mc = new MathContext(digits + 5);
        BigDecimal pi = BigDecimal.ZERO;
        for (int k = 0; k < digits && running; k++) {
            if (k % 1000 == 0) System.out.println("k = " + k + " / " + digits);
            BigDecimal k8 = BigDecimal.valueOf(8 * k);
            BigDecimal term = BigDecimal.valueOf(1).divide(BigDecimal.valueOf(16).pow(k), mc)
                .multiply(
                    BigDecimal.valueOf(4).divide(k8.add(BigDecimal.valueOf(1)), mc)
                    .subtract(BigDecimal.valueOf(2).divide(k8.add(BigDecimal.valueOf(4)), mc))
                    .subtract(BigDecimal.valueOf(1).divide(k8.add(BigDecimal.valueOf(5)), mc))
                    .subtract(BigDecimal.valueOf(1).divide(k8.add(BigDecimal.valueOf(6)), mc))
                );
            pi = pi.add(term);
        }
        return pi.round(new MathContext(digits));
    }

    public String getPi() {
        return pi != null ? pi.toString() : "";
    }
}