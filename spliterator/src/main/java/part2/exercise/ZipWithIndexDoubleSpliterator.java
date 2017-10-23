package part2.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {

    /**
     * Primitive spliterator
     */
    private final OfDouble inner;
    private int currentIndex;

    public ZipWithIndexDoubleSpliterator(OfDouble inner) {
        this(0, inner);
    }

    private ZipWithIndexDoubleSpliterator(int firstIndex, OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        currentIndex = firstIndex;
        this.inner = inner;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance((double element) -> action.accept(new IndexedDoublePair(currentIndex++, element)));
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((double element) -> action.accept(new IndexedDoublePair(currentIndex++, element)));
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        // Based on: https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html
        if (inner.hasCharacteristics(Spliterator.SIZED | Spliterator.SUBSIZED)) {
            // Try to get a new spliterator from inner spliterator
            OfDouble newSpliterator = inner.trySplit();
            if (newSpliterator == null) {
                return null;
            }
            int firstIndex = currentIndex;
            // set current index as remainder
            currentIndex += newSpliterator.estimateSize();
            return new ZipWithIndexDoubleSpliterator(firstIndex, newSpliterator);
        } else {
            return super.trySplit();
        }
    }

    @Override
    public long estimateSize() {
        // Return estimate size of inner spliterator
        return inner.estimateSize();
    }

    @Override
    public int characteristics() {
        // Return characteristics of inner spliterator
        return inner.characteristics();
    }
}
