package support.dealint.manager;

public class MyTimer {

    public float waktu , batasWaktu;
    private boolean start = false;
    private boolean end = false;
    private boolean paused = false;
    private int counter = 0;
    private int loop = 0;

    public MyTimer(float batasWaktu, int loop) {
        this.batasWaktu = batasWaktu;
        this.loop = loop-1;
        waktu = 0;
    }

    public MyTimer(float batasWaktu) {
        this.batasWaktu = batasWaktu;
        waktu = 0;
    }

    /**
     * mulai dari awal
     * bisa dimulai jika sudah di stop
     */
    public void start() {
        if (waktu == 0) {
            start = true;
        }
    }

    /**
     * bisa mulai ketika waktu berjalan
     * gunakan ketika ingin mengulangi timer
     */
    public void reset() {
        stop();
        start();
    }

    public void update(float delta) {
        if (start && !paused) {
            waktu+=delta;
            if (waktu >= batasWaktu) {
                start = false;
                end = true;
            }
        }
    }

    public void stop() {
        waktu = 0;
        start = false;
        end = false;
    }

    public boolean end() {
        return end;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean loop() {
        boolean b = false;
        if (end) {
            b = true;
            next();
        }
        return b;
    }

    private void next() {
        if (counter < loop) {
            reset();
            counter++;
        } else {
            stop();
            counter++;
        }
    }

    public void resetLoop(int loop) {
        this.loop = loop;
        counter = 0;
        reset();
    }

    public boolean loopEnd() {
        boolean b = false;
        if (counter == loop+1) {
            b = true;
            counter++;
        }
        return b;
    }
}
